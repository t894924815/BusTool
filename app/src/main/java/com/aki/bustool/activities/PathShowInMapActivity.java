package com.aki.bustool.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aki.bustool.R;
import com.aki.bustool.utils.AMapUtil;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.RouteSearch;

public class PathShowInMapActivity extends AppCompatActivity implements View.OnClickListener {

    private BusRouteResult mBusRouteResult;
    private BusPath mBusPath;
    private int position;
    private TextView title;
    private Button backButton;
    private AMap aMap;
    private MapView mapView;
    private LatLonPoint mStartPoint,mEndPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_show_in_map);

        getIntentData();
        initView(savedInstanceState);
        initMap();
    }

    public void getIntentData(){
        Intent intent = getIntent();
        if(null != intent){
            mBusRouteResult = intent.getParcelableExtra("BusResult");
            position = intent.getIntExtra("Position",0);
            mBusPath = mBusRouteResult.getPaths().get(position);
            mStartPoint = mBusRouteResult.getStartPos();
            mEndPoint = mBusRouteResult.getTargetPos();
        }
    }


    public void initView(Bundle bundle){
        mapView = $(R.id.map_view);
        title = $(R.id.title);
        backButton = $(R.id.back);

        mapView.onCreate(bundle);
        backButton.setOnClickListener(this);
        title.setText("方案" + (position+1));
    }


    public void initMap(){
        /**
         * 初始化AMap对象
         */
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        setfromandtoMarker();

        BusRouteOverlay busRouteOverlay =
                new BusRouteOverlay(this,aMap,mBusPath,mBusRouteResult.getStartPos(),mBusRouteResult.getTargetPos());
        busRouteOverlay.addToMap();
        busRouteOverlay.zoomToSpan();

    }

    private <T extends View> T $(int resId){
        return (T) super.findViewById(resId);
    }

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
