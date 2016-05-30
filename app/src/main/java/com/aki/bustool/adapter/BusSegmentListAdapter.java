package com.aki.bustool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aki.bustool.R;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.route.BusStep;

import java.util.ArrayList;
import java.util.List;

public class BusSegmentListAdapter extends BaseAdapter {
	private Context mContext;
	private List<SchemeBusStep> mBusStepList = new ArrayList<SchemeBusStep>();

	public BusSegmentListAdapter(Context context, List<BusStep> list) {
		this.mContext = context;
		SchemeBusStep start = new SchemeBusStep(null);
		start.setStart(true);
		mBusStepList.add(start);
		for (BusStep busStep : list) {
			if (busStep.getWalk() != null) {
				SchemeBusStep walk = new SchemeBusStep(busStep);
				walk.setWalk(true);
				mBusStepList.add(walk);
			}
			if (busStep.getBusLine() != null) {
				SchemeBusStep bus = new SchemeBusStep(busStep);
				bus.setBus(true);
				mBusStepList.add(bus);
			}
		}
		SchemeBusStep end = new SchemeBusStep(null);
		end.setEnd(true);
		mBusStepList.add(end);
	}
	@Override
	public int getCount() {
		return mBusStepList.size();
	}

	@Override
	public Object getItem(int position) {
		return mBusStepList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_bus_segment, null);
			holder.parent = (RelativeLayout) convertView
					.findViewById(R.id.bus_item);
			holder.busLineName = (TextView) convertView
					.findViewById(R.id.bus_line_name);
			holder.busDirIcon = (ImageView) convertView
					.findViewById(R.id.bus_dir_icon);
			holder.busStationNum = (TextView) convertView
					.findViewById(R.id.bus_station_num);
			holder.busExpandImage = (ImageView) convertView
					.findViewById(R.id.bus_expand_image);
			holder.busDirUp = (ImageView) convertView
					.findViewById(R.id.bus_dir_icon_up);
			holder.busDirDown = (ImageView) convertView
					.findViewById(R.id.bus_dir_icon_down);
			holder.splitLine = (ImageView) convertView
					.findViewById(R.id.bus_seg_split_line);
			holder.expandContent = (LinearLayout) convertView
					.findViewById(R.id.expand_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final SchemeBusStep item = mBusStepList.get(position);
		if (position == 0) {
			holder.busDirIcon.setImageResource(R.drawable.dir_start);
			holder.busLineName.setText("出发");
			holder.busDirUp.setVisibility(View.INVISIBLE);
			holder.busDirDown.setVisibility(View.VISIBLE);
			holder.splitLine.setVisibility(View.GONE);
			holder.busStationNum.setVisibility(View.GONE);
			holder.busExpandImage.setVisibility(View.GONE);
			return convertView;
		} else if (position == mBusStepList.size() - 1) {
			holder.busDirIcon.setImageResource(R.drawable.dir_end);
			holder.busLineName.setText("到达终点");
			holder.busDirUp.setVisibility(View.VISIBLE);
			holder.busDirDown.setVisibility(View.INVISIBLE);
			holder.busStationNum.setVisibility(View.INVISIBLE);
			holder.busExpandImage.setVisibility(View.INVISIBLE);
			return convertView;
		} else {
			if (item.isWalk() && item.getWalk() != null) {
				holder.busDirIcon.setImageResource(R.drawable.walk_blue);
				holder.busDirUp.setVisibility(View.VISIBLE);
				holder.busDirDown.setVisibility(View.VISIBLE);
				holder.busLineName.setText("步行"
						+ (int) item.getWalk().getDistance() + "米");
				holder.busStationNum.setVisibility(View.GONE);
				holder.busExpandImage.setVisibility(View.GONE);
				return convertView;
			}else if (item.isBus() && item.getBusLines().size() > 0) {
				holder.busDirIcon.setImageResource(R.drawable.dir14);
				holder.busDirUp.setBackgroundColor(mContext.getResources().getColor(R.color.green));
				holder.busDirUp.setVisibility(View.VISIBLE);
				holder.busDirDown.setBackgroundColor(mContext.getResources().getColor(R.color.green));
				holder.busDirDown.setVisibility(View.VISIBLE);
				holder.busLineName.setText(item.getBusLines().get(0).getBusLineName());
				holder.busStationNum.setVisibility(View.VISIBLE);
				holder.busStationNum
						.setText((item.getBusLines().get(0).getPassStationNum() + 1) + "站");
				holder.busExpandImage.setVisibility(View.VISIBLE);
				ArrowClick arrowClick = new ArrowClick(holder, item);
				holder.parent.setTag(position);
				holder.parent.setOnClickListener(arrowClick);
				return convertView;
			}
		}
		return convertView;
	}

	private class ViewHolder {
		public RelativeLayout parent;
		TextView busLineName;
		ImageView busDirIcon;
		TextView busStationNum;
		ImageView busExpandImage;
		ImageView busDirUp;
		ImageView busDirDown;
		ImageView splitLine;
		LinearLayout expandContent;
		boolean arrowExpend = false;
	}
	
	
	private class ArrowClick implements OnClickListener {
		private ViewHolder mHolder;
		private BusStep mItem;

		public ArrowClick(final ViewHolder holder, final BusStep item) {
			mHolder = holder;
			mItem = item;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = Integer.parseInt(String.valueOf(v.getTag()));
			int count = 0;
			mItem = mBusStepList.get(position);
			if (mHolder.arrowExpend == false) {
				mHolder.arrowExpend = true;
				mHolder.busExpandImage
						.setImageResource(R.drawable.up);
				addBusStation(mItem.getBusLine().getDepartureBusStation(),count);
				count ++;
				for (BusStationItem station : mItem.getBusLine()
						.getPassStations()) {
					addBusStation(station,count);
					count ++;
				}
				addBusStation(mItem.getBusLine().getArrivalBusStation(),count);
				count ++;
			} else {
				mHolder.arrowExpend = false;
				mHolder.busExpandImage
						.setImageResource(R.drawable.down);
				mHolder.expandContent.removeAllViews();
			}

		}

		private void addBusStation(BusStationItem station,int count) {
			LinearLayout ll = (LinearLayout) View.inflate(mContext,
					R.layout.item_bus_segment_ex, null);
			TextView tv = (TextView) ll
					.findViewById(R.id.bus_line_station_name);
			tv.setText(station.getBusStationName());
			if(0 == count){
				tv.setTextColor(Color.GREEN);
			} else if((mItem.getBusLine()
					.getPassStations().size() + 2) == (count +1)){
				tv.setTextColor(Color.RED);
			}
			mHolder.expandContent.addView(ll);
		}
	}
}
