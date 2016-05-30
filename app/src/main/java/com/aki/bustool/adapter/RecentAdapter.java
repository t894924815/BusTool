package com.aki.bustool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.*;

import com.aki.bustool.R;
import com.aki.bustool.utils.Initialize;
import com.amap.api.services.core.PoiItem;

/**
 * Created by chunr on 2016/5/27.
 */
public class RecentAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PoiItem> data;

    public RecentAdapter(Context context, ArrayList<PoiItem> stationItems) {
        this.data = stationItems;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        String stationName = data.get(i).getTitle();
        stationName = stationName.substring(0,stationName.lastIndexOf("("));
        ViewHolder viewHolder = null;
        if(null == view){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.recent_stations,null);
            viewHolder.stationName = $(view,R.id.recent_stations);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.stationName.setText(stationName);

        view.setLayoutParams(new ListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                Initialize.SCREEN_HEIGHT/15));


        return view;
    }

    private <T extends View> T $(View view,int resId){
        return (T) view.findViewById(resId);
    }

    public static class ViewHolder{
        private TextView stationName;
    }
}
