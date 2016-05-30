package com.aki.bustool.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aki.bustool.R;
import com.aki.bustool.bean.LocationMessage;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

public class IndexMapActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout hideBridge;

    private ImageButton backButton;

    private TextView textView;

    private MapView mapView;

    private AMap aMap;

    private LatLng centerPoint;

    private CameraUpdate mCameraUpdate;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            mListener.onLocationChanged();
//            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude())));

            init();
            mapView.setVisibility(View.VISIBLE);
            hideBridge.setVisibility(View.GONE);
//            CameraUpdateFactory.newLatLngZoom(new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude()), 4);

            //绘制marker
            Marker marker = aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.location_marker)))
                    .draggable(true));
        }
    };
    private LocationMessage locationMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_map);

        hideBridge = (RelativeLayout) findViewById(R.id.hide_bridge);

        backButton = (ImageButton) findViewById(R.id.back_ico);


        textView = (TextView) findViewById(R.id.back_head);
        textView.setText("附近站点");

        mapView = (MapView) findViewById(R.id.index_map);

        mapView.onCreate(savedInstanceState);


        backButton.setTag(1);
        backButton.setOnClickListener(this);

//        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (null == aMap) {
            aMap = mapView.getMap();
            aMap.moveCamera(mCameraUpdate);
//            setUpMap();
        }
    }

//    private void setUpMap() {
//        // 自定义系统定位小蓝点
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
//        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
//        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
//        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        // aMap.setMyLocationType()
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.locationReceiver");
        registerReceiver(mapReceiver, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != mapReceiver){
            unregisterReceiver(mapReceiver);
        }
    }

    private BroadcastReceiver mapReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras().getBoolean("Fail")){
                Log.i("Aki1dasdasdd234","获取错误，错误码:" +
                        intent.getExtras().getInt("ErrorCode") +
                        "错误信息:" +
                        intent.getExtras().getString("ErrorMessage"));
            }else{
                locationMessage = intent.getExtras().getParcelable("Location");

                mHandler.sendEmptyMessage(0);

                centerPoint = new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude());

                mCameraUpdate = CameraUpdateFactory.newLatLngZoom(centerPoint, 15);
//                unregisterReceiver(mapReceiver);


//                Log.i("Aki1dasdasd234", locationMessage.getCountry() +
//                        locationMessage.getProvince() +
//                        locationMessage.getCity() +
//                        locationMessage.getStreet());
//                Message msg = new Message();
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("Location",mapLocation);
//                mHandler.sendMessage(msg);


            }
        }
    };


    @Override
    public void onClick(View view) {
        int tag = (Integer)view.getTag();
        switch (tag){
            case 1:
                finish();
                break;
        }
    }
}
