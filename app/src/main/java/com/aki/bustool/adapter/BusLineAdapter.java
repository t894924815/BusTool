package com.aki.bustool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aki.bustool.R;
import com.aki.bustool.utils.Initialize;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;

import java.util.*;

public class BusLineAdapter extends BaseAdapter {
	private List<List<BusLineItem>> data;
	private LayoutInflater layoutInflater;
	private String title;

	public BusLineAdapter(Context context, List<List<BusLineItem>> busLineItems,String title) {
		this.data = busLineItems;
		layoutInflater = LayoutInflater.from(context);
		this.title = title;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.line_items_layout, null);
			holder = new ViewHolder();
			holder.lineName = (TextView) convertView.findViewById(R.id.recent_line);
			holder.nextStation = (TextView) convertView.findViewById(R.id.next_station);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			holder.lineName.setText(data.get(position).get(0).getBusLineName());

			holder.nextStation.setText(getNextStation(data.get(position).get(0).getBusStations(),title));

		convertView.setLayoutParams(new ListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				Initialize.SCREEN_HEIGHT/8));

		return convertView;
	}



	private boolean isLast = false;
	public String getNextStation(List<BusStationItem> stationItems,String name){
		int correctLocation = -1;
		if(null != name){
			int count = 0;
			for (BusStationItem item : stationItems) {
				if (item.getBusStationName().equals(name)) {
					correctLocation = count;
					break;
				}
				count ++;
			}
		}
		isLast = (correctLocation == (stationItems.size()-1))?true:false;
		return (correctLocation == -1)?
				Initialize.ERROR:(isLast?
				"终点站:" + (stationItems.get(correctLocation).getBusStationName()):"下一站:" + (stationItems.get(correctLocation+1).getBusStationName()));
	}



	class ViewHolder {
		public TextView lineName;
		public TextView nextStation;
	}


}
