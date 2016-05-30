package com.aki.bustool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aki.bustool.R;

import java.util.*;

/**
 * Created by chunr on 2016/5/8.
 */
public class LocationBusMessageAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String,String>> message;

    public LocationBusMessageAdapter(Context context,List<Map<String ,String>> message) {
        this.message = message;
        this.context = context;
    }

    @Override
    public int getCount() {
        return message.size();
    }

    @Override
    public Object getItem(int i) {
        return message.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.mylocation,null);
            viewHolder.myAddress = (TextView) view.findViewById(R.id.myAddress);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        if(null != message.get(i)){
            viewHolder.myAddress.setText("");
        }
        return view;
    }

    public static class ViewHolder{
        private TextView myAddress;
    }
}
