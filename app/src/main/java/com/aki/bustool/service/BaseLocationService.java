package com.aki.bustool.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.aki.bustool.bean.LocationMessage;
import com.aki.bustool.utils.Initialize;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class BaseLocationService extends Service implements AMapLocationListener{
    /*
        *
        * 第一步，初始化定位客户端，设置监听
        * 注：请在主线程中声明AMapLocationClient类对象，
        * 需要传Context类型的参数。
        * 推荐用getApplicationConext()方法获取全进程有效的context。
        *
        * */
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;



    /*
         *
        * 第二步，配置定位参数，启动定位设置定位参数包括：
        * 定位模式（高精度定位模式，低功耗定位模式和仅设备定位模式），
        * 是否返回地址信息等。
        *
        * */
    //声明mLocationOption对象
    private  AMapLocationClientOption mLocationOption = null;


    //define the variable of location while the action of location has finished.



    @Override
    public void onCreate() {
        super.onCreate();
        getLocationInit();

    }






    private void getLocationInit(){
        Log.i("newLocation","***********");
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);


        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }







    private int COUNT = 0;
    //当位置发生变化的时候调用这个方法。
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        COUNT++;
        Intent intent = new Intent("com.BaseLocationReceiver");
        LocationMessage locationMessage = null;
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                /*aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码
                aMapLocation.getAoiName();//获取当前定位点的AOI信息*/

                locationMessage = new LocationMessage()
                        .setLocationType(aMapLocation.getLocationType())
                        .setAccuracy(aMapLocation.getAccuracy())
                        .setAdCode(aMapLocation.getAdCode())
                        .setAddress(aMapLocation.getAddress())
                        .setAoiName(aMapLocation.getAoiName())
                        .setCity(aMapLocation.getCity())
                        .setCityCode(aMapLocation.getCityCode())
                        .setCountry(aMapLocation.getCountry())
                        .setDate(aMapLocation.getTime())
                        .setDistrict(aMapLocation.getDistrict())
                        .setLatitude(aMapLocation.getLatitude())
                        .setLongitude(aMapLocation.getLongitude())
                        .setProvince(aMapLocation.getProvince())
                        .setStreetNum(aMapLocation.getStreetNum())
                        .setStreet(aMapLocation.getStreet());

                        intent.putExtra("Location", locationMessage);
                        intent.putExtra("Fail",false);

            } else {
                intent.putExtra("Fail",true);
                intent.putExtra("ErrorCode",aMapLocation.getErrorCode());
                intent.putExtra("ErrorMessage",aMapLocation.getErrorInfo());
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }else{
            intent.putExtra("Fail",true);
        }
        Log.i("newLocationAki","-------------");
        sendBroadcast(intent);

    }




    public BaseLocationService() {
    }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
