package com.aki.bustool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aki.bustool.R;
import com.aki.bustool.utils.Initialize;

import java.util.*;

/**
 * Created by chunr on 2016/5/22.
 */
public class RouteSearchListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> message;

    public RouteSearchListAdapter(Context context,List<String> list) {
        mContext = context;
        message = list;
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

        if(null == view){

            viewHolder = new ViewHolder();

            view = LayoutInflater.from(mContext).inflate(R.layout.search_item,null);

            viewHolder.relativeLayout = (RelativeLayout) view;

            viewHolder.title = (TextView) view.findViewById(R.id.result_item);

            view.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolder) view.getTag();

        }
        if(null != message.get(i)){
            viewHolder.relativeLayout.setLayoutParams(new ListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    Initialize.SCREEN_HEIGHT/12));
            viewHolder.title.setText(message.get(i));

        }


        return view;
    }

    private static class ViewHolder{
        TextView title;
        RelativeLayout relativeLayout;
    }
}
