package com.aki.bustool.utils;

import android.content.Context;

import com.aki.bustool.Interfaces.GecoderTransfirmListener;
import com.aki.bustool.bean.ErrorStatus;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * Created by chunr on 2016/5/15.
 */
public class GeocoderUtil implements GeocodeSearch.OnGeocodeSearchListener{

    private Context context;
    private LatLonPoint latLonPoint;
    private String address;
    private GeocodeSearch geocoderSearch;

    public GeocoderUtil(Context ctx, String address) {
        this.context = ctx;
        this.address = address;
        init();
    }

    public GeocoderUtil(Context ctx,LatLonPoint latLonPoint) {
        this.context = ctx;
        this.latLonPoint = latLonPoint;
        init();
    }

    public void init(){
        geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(this);
        int flag = (null != latLonPoint ? 1 : 0) ;
        switch(flag){
            case 1 :
                getAddr(latLonPoint);
                break;
            case 0 :
                getLatLon(address);
                break;
        }
    }



    /**
     * 响应逆地理编码
     */
    public void getLatLon(final String name) {
        GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }


    /**
     * 地理编码查询回调
     */
    public void getAddr(final LatLonPoint latLon) {
        RegeocodeQuery query = new RegeocodeQuery(latLon, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }



    private ErrorStatus mErrorStatus;
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        mErrorStatus = new ErrorStatus();
        mErrorStatus.setReturnCode(rCode);
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                mErrorStatus.setIsError(false);
                this.address = result.getRegeocodeAddress().getFormatAddress();
                mGecoderTransferTransfirmListener.onGetAddr(address,mErrorStatus);
//                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                        AMapUtil.convertToLatLng(latLonPoint), 15));
//                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
//                ToastUtil.show(GeocoderActivity.this, addressName);
            } else {
//                ToastUtil.show(GeocoderActivity.this, R.string.no_result);
                mErrorStatus.setIsError(true);
                mErrorStatus.setReturnCode(Initialize.NO_RESULT_CODE);
                mGecoderTransferTransfirmListener.onGetAddr(Initialize.ERROR_ADDRESS,mErrorStatus);
            }
        } else {
            mErrorStatus.setIsError(true);
            mGecoderTransferTransfirmListener.onGetAddr(Initialize.ERROR_ADDRESS,mErrorStatus);
//            ToastUtil.showerror(GeocoderActivity.this, rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        mErrorStatus = new ErrorStatus();
        mErrorStatus.setReturnCode(rCode);
        if (rCode == 1000) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                mErrorStatus.setIsError(false);
                this.latLonPoint = result.getGeocodeAddressList().get(0).getLatLonPoint();
                mGecoderTransferTransfirmListener.onGetLat(latLonPoint,mErrorStatus);
//                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
//                        + address.getFormatAddress();
//                ToastUtil.show(GeocoderActivity.this, addressName);
            } else {
                mErrorStatus.setIsError(true);
                mErrorStatus.setReturnCode(Initialize.NO_RESULT_CODE);
                mGecoderTransferTransfirmListener.onGetLat(Initialize.ERROR_POINT,mErrorStatus);
//                ToastUtil.show(GeocoderActivity.this, R.string.no_result);
            }

        } else {
            mErrorStatus.setIsError(true);
            mGecoderTransferTransfirmListener.onGetLat(Initialize.ERROR_POINT,mErrorStatus);
//            ToastUtil.showerror(GeocoderActivity.this, rCode);
        }
    }

    private GecoderTransfirmListener mGecoderTransferTransfirmListener;

    public void setOnGetGeocoder(GecoderTransfirmListener gecoderTransfirmListener){

        this.mGecoderTransferTransfirmListener = gecoderTransfirmListener;

    }

}
