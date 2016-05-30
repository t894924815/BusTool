package com.aki.bustool.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aki.bustool.Interfaces.BusLineSearchedListener;
import com.aki.bustool.Interfaces.PoiSearchedListener;
import com.aki.bustool.R;
import com.aki.bustool.adapter.BusLineAdapter;
import com.aki.bustool.bean.ErrorStatus;
import com.aki.bustool.bean.LocationMessage;
import com.aki.bustool.utils.BusLineUtil;
import com.aki.bustool.utils.Initialize;
import com.aki.bustool.utils.PoiAddressUtil;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import java.util.*;

public class SameStationActivity extends AppCompatActivity implements
        BusLineSearchedListener, View.OnClickListener, AdapterView.OnItemClickListener {


    private LinearLayout includeLayout;
    private RelativeLayout toInput;
    private RelativeLayout hideBridge;
    private LinearLayout showLine;
    private Button backButton;

    private ListView lineName;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_same_station);
        initView();
        initData();
    }

    public void initView(){
        includeLayout = $(R.id.include_layout);
        lineName = $(R.id.line_name);
        title = $(R.id.title);
        toInput = $(R.id.go_input);
        hideBridge = $(R.id.hide_bridge);
        showLine = $(R.id.show_line);
        backButton = $(R.id.back);

        toInput.setOnClickListener(this);
        backButton.setOnClickListener(this);
        lineName.setOnItemClickListener(this);

        includeLayout.setLayoutParams(new RelativeLayout.LayoutParams(new LinearLayout.LayoutParams(Initialize.SCREEN_WIDTH/3,LinearLayout.LayoutParams.MATCH_PARENT)));
    }

    private String stationName;
    private LocationMessage locationMessage;
    private BusLineUtil busLineUtil;
    private String name;
    private List<String> snippet;
    private LatLonPoint point;
    public void initData(){
        if(null != getIntent().getParcelableExtra("Point") && null != getIntent().getStringExtra("StationName") && null != getIntent().getStringExtra("Snippet")){
            stationName = getIntent().getStringExtra("StationName");
            snippet = getSnippetList(getIntent().getStringExtra("Snippet"));
            point = getIntent().getParcelableExtra("Point");
            if(-1 != stationName.indexOf("(")){
                name = stationName.substring(0,stationName.indexOf("("));
            }else{
                name = stationName;
            }
            title.setText(name);
        }
        if(null != Initialize.LOCAL_MESSAGE){
            locationMessage = Initialize.LOCAL_MESSAGE;
        }
        if(null != stationName && null != locationMessage){
            busLineUtil = new BusLineUtil(this,snippet,locationMessage.getCityCode());
            busLineUtil.setOnGetBusLineMessageListener(this);
            busLineUtil.startSearch();
        }
    }


    private <T extends View> T $(int resId){
        return (T) super.findViewById(resId);
    }

    private List<List<BusLineItem>> mBusLineItem;
    private BusLineAdapter busLineAdapte;
    @Override
    public void getBusLineMessage(List<BusLineItem> busLineItems, ErrorStatus errorStatus) {

    }

    @Override
    public void getManyBusLineMessage(List<List<BusLineItem>> busLineItemsList) {
        if(null != busLineItemsList && busLineItemsList.size() > 0){
            mBusLineItem = busLineItemsList;
            busLineAdapte = new BusLineAdapter(this,mBusLineItem,title.getText().toString());
            lineName.setAdapter(busLineAdapte);

            showLine.setVisibility(View.VISIBLE);
            hideBridge.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.go_input:
                getTarget();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent showLine = new Intent(this,BusLineShowActivity.class);
        showLine.putExtra("BusName",mBusLineItem.get(i).get(0).getBusLineName());
        showLine.putParcelableArrayListExtra("GoLine", (ArrayList<? extends Parcelable>) mBusLineItem
                .get(i)
                .get(0)
                .getBusStations());
        showLine.putExtra("FirstBus",mBusLineItem.get(i).get(0).getFirstBusTime());
        showLine.putExtra("LastBus",mBusLineItem.get(i).get(0).getLastBusTime());
        startActivity(showLine);
    }

    public void getTarget(){
        Intent getTarget = new Intent(this,RouteInputActivity.class);
        getTarget.putExtra(Initialize.PLACE, Initialize.BELOW);
        if(null != locationMessage){
            getTarget.putExtra(Initialize.LOCATION,locationMessage);
            startActivityForResult(getTarget, Initialize.REQUEST_CODE_BELOW);
        }
    }


    private Intent pathShowIntent;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Initialize.REQUEST_CODE_BELOW == requestCode && Initialize.RESULT_CODE_BELOW == resultCode){
            pathShowIntent = new Intent(this,ShowPathActivity.class);

            if(null != locationMessage && null != point){
                pathShowIntent.putExtra(Initialize.LOCATION,locationMessage);
                pathShowIntent.putExtra("FromName",title.getText().toString());
                pathShowIntent.putExtra("ToName",data.getStringExtra(Initialize.PLACE_NAME));
                pathShowIntent.putExtra("From",point);
                pathShowIntent.putExtra("To",data.getParcelableExtra(Initialize.PLACE_POINT));
                startActivity(pathShowIntent);
            }
        }
    }

    public List<String> getSnippetList(String txt){
        List<String> list = new ArrayList<>();
        String [] a = txt.split(";");
        for(int count = 0;count <a.length;count ++){
            list.add(a[count]);
        }
        return list;
    }

}
