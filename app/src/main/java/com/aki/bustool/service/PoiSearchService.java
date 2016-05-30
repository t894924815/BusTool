package com.aki.bustool.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.aki.bustool.bean.ErrorStatus;
import com.aki.bustool.bean.LocationMessage;
import com.aki.bustool.bean.PoiMessage;
import com.aki.bustool.utils.Initialize;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.*;

public class PoiSearchService extends Service implements PoiSearch.OnPoiSearchListener{
    private LocationMessage locationMessage;
    private ErrorStatus errorStatus;

    public PoiSearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

//    private int COUNT = 0;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(startId == 1){
            errorStatus = intent.getParcelableExtra("ErrorStatus");

            if(null == intent.getExtras()){

            }
            if(errorStatus.getIsError()){
//            Toast.makeText(PoiSearchService.this,"获取定位信息失败!!!---PoiSearchService",Toast.LENGTH_SHORT).show();
            }else{
                locationMessage = intent.getParcelableExtra("Location");
                doSearchQuery();
            }
        }

        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != startBusLineSearch){
            stopService(startBusLineSearch);
        }
    }

    //Poi搜索
    private PoiSearch mPoiSearch;
    private int currentPage;
    private PoiSearch.Query query;
    private String city;
    private LatLonPoint lp;
    private PoiResult poiResult;
    private ArrayList<PoiItem> poiItems;
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        currentPage = 0;
        city = locationMessage.getCity();
        lp = new LatLonPoint(locationMessage.getLatitude(),locationMessage.getLongitude());
//        lp = new LatLonPoint(39.993167, 116.473274);
        query = new PoiSearch.Query("公交", "", city);
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        if (lp != null) {
            mPoiSearch = new PoiSearch(this, query);
            mPoiSearch.setOnPoiSearchListener(this);
            mPoiSearch.setBound(new PoiSearch.SearchBound(lp, 10000, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            mPoiSearch.searchPOIAsyn();// 异步搜索
        }
    }




    private Intent sendPoi;
    private PoiMessage poiMessage;
    private Intent startBusLineSearch;
    private ErrorStatus thisErrorStatus;

    //poi搜索结果返回接口
    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        thisErrorStatus = new ErrorStatus();
        thisErrorStatus.setReturnCode(rcode);

        sendPoi = new Intent("com.PoiBroadcast");
        startBusLineSearch = new Intent(this,BusLineService.class);

        if (rcode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    thisErrorStatus.setReturnCode(rcode).setIsError(false);

                    poiResult = result;
                    poiItems = poiResult.getPois();
//                    List<SuggestionCity> suggestionCities = poiResult
//                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
//                    Log.i("Aki2345667", poiItems.get(0).getBusinessArea());
                    if(null != poiItems.get(0).getTitle() && null != poiItems.get(0).getSnippet()){
                        poiMessage = new PoiMessage();


                        /*
                        * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                        * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                        * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓炸弹↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                        * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                        * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                        * */

                        poiMessage.setTitle(poiItems.get(0).getTitle())
                                .setSnippet(poiItems.get(0).getSnippet())
                                .setCityCode(poiItems.get(0).getCityCode())
                                .setLatLonPoint(poiItems.get(0).getLatLonPoint());

                        /*
                        * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                        * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                        * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑炸弹↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                        * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                        * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                        * */
                        sendPoi.putParcelableArrayListExtra("PoiMessage",poiItems);
                        startBusLineSearch.putExtra("PoiMessage",poiMessage);

                    }else{
                        thisErrorStatus.setIsError(true).setReturnCode(Initialize.NO_RESULT_CODE);
                    }

                }else{
                    thisErrorStatus.setIsError(true).setReturnCode(Initialize.NO_RESULT_CODE);
                }
            } else {
                //待处理
                thisErrorStatus.setIsError(true).setReturnCode(Initialize.NO_RESULT_CODE);
            }
        }else{
            thisErrorStatus.setIsError(true).setReturnCode(Initialize.NO_RESULT_CODE);
        }

        sendPoi.putExtra("ErrorStatus",thisErrorStatus);

        startBusLineSearch.putExtra("ErrorStatus",thisErrorStatus);


        sendBroadcast(sendPoi);

        startService(startBusLineSearch);

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
