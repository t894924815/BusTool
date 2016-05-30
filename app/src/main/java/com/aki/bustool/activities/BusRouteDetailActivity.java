package com.aki.bustool.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aki.bustool.R;
import com.aki.bustool.adapter.BusSegmentListAdapter;
import com.aki.bustool.utils.AMapUtil;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;

public class BusRouteDetailActivity extends Activity implements View.OnClickListener {

	private BusPath mBuspath;
	private BusRouteResult mBusRouteResult;
	private TextView  mTitleBusRoute, mDesBusRoute;
	private ListView mBusSegmentList;
	private BusSegmentListAdapter mBusSegmentListAdapter;
	private RelativeLayout showInMap;
	private Button backButton;
	private ViewPager furtherDetailPath;
	private ViewPager roughDetailPath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_detail);
		getIntentData();
		init();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent != null) {
			mBuspath = intent.getParcelableExtra("bus_path");
			mBusRouteResult = intent.getParcelableExtra("bus_result");
		}
	}

	private void init() {
		showInMap = $(R.id.show_in_map);
		mTitleBusRoute = $(R.id.firstline);
		mDesBusRoute = $(R.id.secondline);
		backButton = $(R.id.back);

		backButton.setOnClickListener(this);
		String dur = AMapUtil.getFriendlyTime((int) mBuspath.getDuration());
		String dis = AMapUtil.getFriendlyTime((int) mBuspath.getDistance());
		mTitleBusRoute.setText(dur + "(" + dis + ")");
		int taxiCost = (int) mBusRouteResult.getTaxiCost();
		mDesBusRoute.setText("打车约"+taxiCost+"元");
		mDesBusRoute.setVisibility(View.VISIBLE);
		configureListView();
	}

	private void configureListView() {
		mBusSegmentList = (ListView) findViewById(R.id.bus_segment_list);
		mBusSegmentListAdapter = new BusSegmentListAdapter(
				this.getApplicationContext(), mBuspath.getSteps());
		mBusSegmentList.setAdapter(mBusSegmentListAdapter);
		
	}

	private <T extends View> T $(int resId){
		return (T) super.findViewById(resId);
	}
	
	public void onBackClick(View view) {
		this.finish();
	}


	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.back:
				this.finish();
				break;
		}
	}
}
