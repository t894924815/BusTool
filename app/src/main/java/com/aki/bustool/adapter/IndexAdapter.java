package com.aki.bustool.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aki.bustool.R;
import com.aki.bustool.utils.Initialize;

import java.util.List;
import java.util.Map;

/**
 * Created by chunr on 2016/4/30.
 */
public class IndexAdapter extends BaseAdapter {

    private List<Map<String ,String>> data;
    private Context context;

    public IndexAdapter(List<Map<String,String>> data, Context context) {
        this.data = data;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.index_listview, null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(Initialize.SCREEN_WIDTH,Initialize.SCREEN_HEIGHT/12));
        }
        TextView clientName = (TextView) convertView.findViewById(R.id.client_name);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);

        clientName.setText(data.get(position).get("clientName"));
        distance.setText(data.get(position).get("distance"));


        return convertView;
    }
}
