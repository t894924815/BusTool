package com.aki.bustool.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aki.bustool.Interfaces.PoiSearchedListener;
import com.aki.bustool.R;
import com.aki.bustool.bean.ErrorStatus;
import com.aki.bustool.bean.LocationMessage;
import com.aki.bustool.bean.SimplePlaceMessage;
import com.aki.bustool.utils.Initialize;
import com.aki.bustool.utils.PoiAddressUtil;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import java.util.*;

public class SelectMapPointActivity extends AppCompatActivity implements
        View.OnClickListener,
        AMap.OnCameraChangeListener, View.OnTouchListener, PoiSearchedListener {


    private RelativeLayout hideView;
    private MapView mapView;
    private TextView title;
    private TextView locationName;
    private Button confirmButton;
    private Button backButton;
    private RelativeLayout targetLayout;



    private CameraUpdate mCameraUpdate;

    private LocationMessage locationMessage;
    private LatLng centerPoint;
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_map_point);
        initView(savedInstanceState);
    }


    public void initView(Bundle savedInstanceState){
        mapView = $(R.id.map);
        hideView = $(R.id.hide_view);
        title = $(R.id.title);
        locationName = $(R.id.location_name);
        confirmButton = $(R.id.confirm_location);
        backButton = $(R.id.back);
        targetLayout = $(R.id.target_image);


        mapView.onCreate(savedInstanceState);
        mapView.setOnTouchListener(this);
        confirmButton.setOnClickListener(this);
        confirmButton.setEnabled(false);
        backButton.setOnClickListener(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Initialize.SCREEN_WIDTH/12,Initialize.SCREEN_HEIGHT/12);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ABOVE,R.id.flag_center);
        targetLayout.setLayoutParams(layoutParams);
        if(null != getIntent().getParcelableExtra(Initialize.LOCATION) && null != getIntent().getStringExtra(Initialize.PLACE)){
            title.setText((getIntent().getStringExtra(Initialize.PLACE).equals(Initialize.UP))?Initialize.ORIGIN_POINT:Initialize.TARGET_POINT);
            locationMessage = getIntent().getParcelableExtra(Initialize.LOCATION);
            centerPoint = new LatLng(locationMessage.getLatitude(),locationMessage.getLongitude());
            initMap();
            hideView();
            showView();
            locationName.setText(locationMessage.getAddress());
        }else{
            Toast.makeText(this,Initialize.CANT_RESOLVE,Toast.LENGTH_SHORT).show();
        }
    }

    private <T extends View> T $(int resId){
        return (T) super.findViewById(resId);
    }

    public void initMap(){
        if(null == aMap){
            aMap = mapView.getMap();
            aMap.setOnCameraChangeListener(this);
            mCameraUpdate = CameraUpdateFactory.newLatLngZoom(centerPoint,15);
            aMap.moveCamera(mCameraUpdate);
        }
    }

    public void hideView(){
        hideView.setVisibility(View.GONE);
    }

    public void showView(){
        mapView.setVisibility(View.VISIBLE);
    }

    private Intent returnResultIntent;
    public void sendMessage(){
        if(null != addressData){
            returnResultIntent = new Intent();
            returnResultIntent.putExtra("Address",addressData);
            returnResultIntent.putExtra(Initialize.PLACE_NAME,addressData.getAddress());
            returnResultIntent.putExtra(Initialize.PLACE_POINT,addressData.getmLatLon());
            setResult(Initialize.SEND_POINT,returnResultIntent);
            finish();
        }else{
            Toast.makeText(this,"获取失败！！！",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    private PoiAddressUtil getAddressUtil;
    private LatLonPoint nowPoint;

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        nowPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
        getAddressUtil = new PoiAddressUtil(this,
                nowPoint,
                locationMessage.getCity(),false);
        getAddressUtil.setPoiSearchedListener(this);
        getAddressUtil.startPoiSearch();
        confirmButton.setEnabled(false);
        locationName.setText(getResources().getString(R.string.locating));
    }

    private SimplePlaceMessage addressData;

    @Override
    public void onGetPoiMessage(List<PoiItem> poiItems, String poiTitle, LatLonPoint addressPoint, ErrorStatus status) {
        if(null != status && !status.getIsError()){
            if(status.getReturnCode() != Initialize.NO_RESULT_CODE){
                addressData = new SimplePlaceMessage();
                addressData.setAddress(poiTitle)
                        .setmLatLon(nowPoint);
                showResult();
            }else{
                noResult();
            }
        }else{
            noResult();
        }
    }

    public void showResult(){
        confirmButton.setEnabled(true);
        locationName.setText(addressData.getAddress());
    }

    public void noResult(){
        confirmButton.setEnabled(false);
        locationName.setText(getResources().getString(R.string.cant_getaddress));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.confirm_location:
                sendMessage();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null != aMap){
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null != aMap){
            mapView.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != aMap){
            mapView.onDestroy();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_MOVE:
                confirmButton.setEnabled(false);
        }
        return false;
    }
}






