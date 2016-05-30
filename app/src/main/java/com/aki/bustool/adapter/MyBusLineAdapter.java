package com.aki.bustool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aki.bustool.R;
import com.amap.api.services.busline.BusLineItem;

import java.util.List;
import java.util.Map;

public class MyBusLineAdapter extends BaseAdapter {
	private List<Map<String,BusLineItem>> busLineItems;
	private LayoutInflater layoutInflater;

	public MyBusLineAdapter(Context context, List<Map<String,BusLineItem>> busLineItems) {
		this.busLineItems = busLineItems;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return busLineItems.size();
	}

	@Override
	public Object getItem(int position) {
		return busLineItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.busline_item, null);
			holder = new ViewHolder();
			holder.busName = (TextView) convertView.findViewById(R.id.busname);
			holder.busId = (TextView) convertView.findViewById(R.id.busid);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.busName.setText("公交名 :"
				+ busLineItems.get(position).get("GoBusLineMessage").getBusLineName());
		holder.busId.setText("公交ID:"
				+ busLineItems.get(position).get("GoBusLineMessage").getBusLineId());
		return convertView;
	}

	class ViewHolder {
		public TextView busName;
		public TextView busId;
	}

}
