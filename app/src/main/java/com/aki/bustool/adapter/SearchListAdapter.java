package com.aki.bustool.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.aki.bustool.R;
import com.amap.api.services.core.PoiItem;

/**
 * Created by chunr on 2016/5/5.
 */
public class SearchListAdapter extends BaseAdapter {

    private Context context;
    private List<PoiItem> poiItems;

    public SearchListAdapter(Context context,List<PoiItem> list) {
        this.context = context;
        poiItems = list;
    }

    @Override
    public int getCount() {
        Log.i("try",poiItems.size() + "");
        return poiItems.size();
    }

    @Override
    public Object getItem(int i) {
        return poiItems.get(i);
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

            view = LayoutInflater.from(context).inflate(R.layout.search_list,null);


            viewHolder.title = (TextView) view.findViewById(R.id.poi_title);
            viewHolder.snippet = (TextView) view.findViewById(R.id.poi_snippet);


            view.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolder) view.getTag();

        }
        if(null != poiItems.get(i)){
            viewHolder.title.setText(poiItems.get(i).getTitle());
            viewHolder.snippet.setText(poiItems.get(i).getSnippet());

        }

        return view;
    }

    private static class ViewHolder{
        TextView title;
        TextView snippet;
    }
}
