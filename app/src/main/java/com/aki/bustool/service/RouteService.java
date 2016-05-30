package com.aki.bustool.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

/**
 * Created by chunr on 2016/5/20.
 */
public class RouteService extends Service implements RouteSearch.OnRouteSearchListener {

    private int TripType;

    private RouteSearch routeSearch;
    private RouteSearch.FromAndTo fromAndTo;

    private WalkRouteResult mWalkRouteResult;
    private BusRouteResult mBusRouteResult;
    private DriveRouteResult mDriveRouteResult;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        init(intent);

        return super.onStartCommand(intent, flags, startId);
    }

    public void init(Intent intent){
        fromAndTo = new RouteSearch.FromAndTo(
                (LatLonPoint) intent.getParcelableExtra("FromPoint"),
                (LatLonPoint) intent.getParcelableExtra("TargetPoint"));
        TripType = intent.getIntExtra("TripType",0);
        searchRoute(TripType,TripType>1?RouteSearch.DrivingDefault:(TripType<1?RouteSearch.WalkDefault:RouteSearch.BusDefault));
    }



    public void searchRoute(int routeType, int mode){
        routeSearch = new RouteSearch(this);//初始化routeSearch 对象
        routeSearch.setRouteSearchListener(this);//设置数据回调监听器



        if(routeType == 0){
            //初始化query对象，fromAndTo是包含起终点信息，walkMode是不行路径规划的模式
            RouteSearch.WalkRouteQuery query
                    = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            routeSearch.calculateWalkRouteAsyn(query);//开始算路
        }else if(routeType == 1){
            RouteSearch.BusRouteQuery query
                    = new RouteSearch.BusRouteQuery();
            routeSearch.calculateBusRouteAsyn(query);//开始算路
        }else if(routeType == 2){
            RouteSearch.DriveRouteQuery query
                    = new RouteSearch.DriveRouteQuery();
            routeSearch.calculateDriveRouteAsyn(query);//开始算路
        }

    }

    public void sendRouteMessage(Parcelable message){
        Intent routeMessage = new Intent("com.RouteBroadcast");
        routeMessage.putExtra("RouteMessage",message);
        sendBroadcast(routeMessage);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int errorCode) {
        if (errorCode == 1000) {
            if (busRouteResult != null && busRouteResult.getPaths() != null) {
                if (busRouteResult.getPaths().size() > 0) {
                    mBusRouteResult = busRouteResult;
//                    BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
//                    mBusResultList.setAdapter(mBusResultListAdapter);
                } else if (busRouteResult != null && busRouteResult.getPaths() == null) {
//                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
//                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
        if (errorCode == 1000) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    mDriveRouteResult = driveRouteResult;
                } else if (driveRouteResult != null && driveRouteResult.getPaths() == null) {
//                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
//                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int errorCode) {
        if (errorCode == 1000) {
            if (walkRouteResult != null && walkRouteResult.getPaths() != null) {
                if (walkRouteResult.getPaths().size() > 0) {
                    mWalkRouteResult = walkRouteResult;
                } else if (walkRouteResult != null && walkRouteResult.getPaths() == null) {
//                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
//                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }
}
