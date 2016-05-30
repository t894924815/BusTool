package com.aki.bustool.utils;

import android.content.Context;

import com.aki.bustool.Interfaces.BusLineSearchedListener;
import com.aki.bustool.bean.ErrorStatus;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineResult;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.busline.BusStationQuery;
import com.amap.api.services.busline.BusStationResult;

import java.util.*;

/**
 * Created by chunr on 2016/5/26.
 */
public class BusLineUtil implements BusLineSearch.OnBusLineSearchListener {

    private Context mContext;
    private String name;
    private String cityCode;
    private List<String> lineList;
    private boolean normalMode = true;
    private int searchProgress;


    public BusLineUtil(Context context,String name,String cityCode) {
        this.mContext = context;
        this.name = name;
        this.cityCode = cityCode;
    }

    public BusLineUtil(Context context,List<String> lineNameList,String cityCode) {
        this.mContext = context;
        this.lineList = lineNameList;
        this.cityCode = cityCode;
        this.normalMode = false;
        this.searchProgress = lineNameList.size();
    }

    public void startSearch(){
        if(normalMode){
            initName();
        }else{
            initList();
        }
    };


    private void initName(){
        searchLine(name);
    }

    private void initList(){
        searchLineList();
    }

    private int currentpage = 0;// 公交搜索当前页，第一页从0开始
    private BusLineResult busLineResult;// 公交线路搜索返回的结果
    private List<BusLineItem> lineItems = null;// 公交线路搜索返回的busline
    private BusLineQuery busLineQuery;// 公交线路查询的查询类

    private BusStationResult busStationResult;// 公交站点搜索返回的结果
    private List<BusStationItem> stationItems;// 公交站点搜索返回的busStation
    private BusStationQuery busStationQuery;// 公交站点查询的查询类

    private BusLineSearch busLineSearch;// 公交线路列表查询


    /**
     * 公交线路搜索
     */

    private void searchLine(String lineNumber) {
        currentpage = 0;// 第一页默认从0开始
        busLineQuery = new BusLineQuery(lineNumber, BusLineQuery.SearchType.BY_LINE_NAME,
                cityCode);// 第一个参数表示公交线路名，第二个参数表示公交线路查询，第三个参数表示所在城市名或者城市区号
        busLineQuery.setPageSize(10);// 设置每页返回多少条数据
        busLineQuery.setPageNumber(currentpage);// 设置查询第几页，第一页从0开始算起
        busLineSearch = new BusLineSearch(mContext, busLineQuery);// 设置条件
        busLineSearch.setOnBusLineSearchListener(this);// 设置查询结果的监听
        busLineSearch.searchBusLineAsyn();// 异步查询公交线路名称
    }

    private int a = 0;
    private void searchLineList(){
        if(searchProgress == 0){
            a ++;
            int b = a++;
        }
        if(searchProgress != 0){
            searchLine(lineList.get(lineList.size() - searchProgress));
            searchProgress --;
        }
    }

    private List<List<BusLineItem>> resultList = new ArrayList<>();

    private void searchManager(List<BusLineItem> items,ErrorStatus errorStatus){
        if(!errorStatus.getIsError()){
            resultList.add(items);
            if(0 == searchProgress){
                if(null != mBusLineSearchedListener){
                    mBusLineSearchedListener.getManyBusLineMessage(resultList);
                }
            }else{
                searchLineList();
            }
        }
    }

    private ErrorStatus mErrorStatus;

    /**
     * 公交线路查询结果回调
     */
    @Override
    public void onBusLineSearched(BusLineResult result, int rCode) {
        mErrorStatus = new ErrorStatus();
        mErrorStatus.setReturnCode(rCode);
        if (rCode == 1000) {
//            Log.i("testAki999","busLineSearch*******" + result.getBusLines().get(0).getTotalPrice());
            if (result != null && result.getQuery() != null
                    && result.getQuery().equals(busLineQuery)) {
                if (result.getQuery().getCategory() == BusLineQuery.SearchType.BY_LINE_NAME) {
                    if (result.getPageCount() > 0
                            && result.getBusLines() != null
                            && result.getBusLines().size() > 0) {
                        mErrorStatus.setIsError(false);
                        busLineResult = result;
                        lineItems = result.getBusLines();
                    }
                } else if (result.getQuery().getCategory() == BusLineQuery.SearchType.BY_LINE_ID) {
                    mErrorStatus.setIsError(false);
                    busLineResult = result;
                    lineItems = busLineResult.getBusLines();
                }
            } else {
                mErrorStatus.setIsError(true);
                mErrorStatus.setReturnCode(Initialize.NO_RESULT_CODE);
            }
        } else {
            mErrorStatus.setIsError(true);
            mErrorStatus.setReturnCode(Initialize.NO_RESULT_CODE);
        }
        if(!normalMode){
            searchManager(lineItems,mErrorStatus);
        }
//        mBusLineSearchedListener.getBusLineMessage(lineItems,mErrorStatus);
    }


    private BusLineSearchedListener mBusLineSearchedListener;
    public void setOnGetBusLineMessageListener(BusLineSearchedListener busLineMessageListener){
        this.mBusLineSearchedListener = busLineMessageListener;
    }
}
