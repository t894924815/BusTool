package com.aki.bustool.utils;

import android.content.Context;

import com.aki.bustool.Interfaces.PoiSearchedListener;
import com.aki.bustool.bean.ErrorStatus;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;

/**
 * Created by chunr on 2016/5/25.
 */
public class PoiAddressUtil implements PoiSearch.OnPoiSearchListener {

    private Context mContext;
    private LatLonPoint mLatLng;
    private String mCity;
    private String searchType = Initialize.POI_TYPE_ADDRESS + "|"
            + Initialize.POI_TYPE_SCIENCE + "|"
            + Initialize.POI_TYPE_GOV + "|"
            + Initialize.POI_TYPE_TRANSPORT;
    private boolean keyWordType;
    private String keyWord;
    private int searchBound = 1000;

    public PoiAddressUtil(Context context,LatLonPoint latLn,String city,boolean keyWordType) {
        this.mContext = context;
        this.mLatLng = latLn;
        this.mCity = city;
        this.keyWordType = keyWordType;
    }

    public PoiAddressUtil(Context context,LatLonPoint latLn,String city,boolean keyWordType,String keyWord){
        this.mContext = context;
        this.mLatLng = latLn;
        this.mCity = city;
        this.keyWordType = keyWordType;
        this.keyWord = keyWord;
    }

    public PoiAddressUtil setSearchType(String type){
        this.searchType = type;
        return this;
    }

    public PoiAddressUtil setSearchBound(int bound){
        this.searchBound = bound;
        return this;
    }

    public PoiAddressUtil setmLatLng(LatLonPoint mLatLng) {
        this.mLatLng = mLatLng;
        return this;
    }

    public PoiAddressUtil setKeyWordType(boolean keyWordType) {
        this.keyWordType = keyWordType;
        return this;
    }

    public PoiAddressUtil setmCity(String mCity) {
        this.mCity = mCity;
        return this;
    }

    public PoiAddressUtil setKeyWord(String keyWord) {
        this.keyWord = keyWord;
        return this;
    }

    public void startPoiSearch(){
        init();
    }


    private void init(){
        doSearchQuery();
    }

    //Poi搜索
    private PoiSearch mPoiSearch;
    private int currentPage;
    private PoiSearch.Query query;
    private PoiResult poiResult;
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        currentPage = 0;
        if(!keyWordType){
            query = new PoiSearch.Query("",searchType, mCity);
        }else{
            query = new PoiSearch.Query(keyWord,"", mCity);
        }
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        if (mLatLng != null) {
            mPoiSearch = new PoiSearch(mContext, query);
            mPoiSearch.setOnPoiSearchListener(this);
            mPoiSearch.setBound(new PoiSearch.SearchBound(mLatLng, searchBound, true));//
            // 设置搜索区域为以lp点为圆心，其周围1000米范围
            mPoiSearch.searchPOIAsyn();// 异步搜索
        }
    }




    private List<PoiItem> poiItems;
    private ErrorStatus errorStatus;
    private String title;
    private LatLonPoint point;
    private boolean isFalse = false;
    //poi搜索结果返回接口
    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        errorStatus = new ErrorStatus();
        errorStatus.setReturnCode(rcode);
        if (rcode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();
                    if (poiItems.size() > 0) {
                        errorStatus.setIsError(false);
                    }else{
                        isFalse = true;
                    }
                } else {
                    isFalse = true;
                }
            } else {
                isFalse = true;
            }
        }else{
            isFalse = true;
        }
        title = (isFalse?Initialize.ERROR_ADDRESS:poiItems.get(0).getTitle());
        point = (isFalse?Initialize.ERROR_POINT:poiItems.get(0).getLatLonPoint());
        errorStatus.setReturnCode((isFalse?Initialize.NO_RESULT_CODE:1000));
        errorStatus.setIsError((isFalse?true:false));
        mPoiSearchedListener.onGetPoiMessage(poiItems,title,point,errorStatus);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private PoiSearchedListener mPoiSearchedListener;
    public void setPoiSearchedListener(PoiSearchedListener poiSearchedListener){
        mPoiSearchedListener = poiSearchedListener;
    }
}
