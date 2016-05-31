package com.aki.bustool.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import com.aki.bustool.Interfaces.OnGetAllViews;
import com.aki.bustool.R;
import com.aki.bustool.utils.Initialize;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;

/**
 * Created by chunr on 2016/5/22.
 */
public class PathListAdapter extends BaseAdapter{

    private Context context;
    private List<BusPath> pathList;
    private LayoutInflater layoutInflater;

    public PathListAdapter(Context context, List<BusPath> paths) {
        this.context = context;
        this.pathList = paths;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    @Override
    public Object getItem(int i) {
        return pathList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private int listen = 0;

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        listen ++;

        List<String> busLineName = null;

        View view = null;

        ViewHolder viewHolder = null;
        if(null == convertView){

            busLineName = calculateLine(pathList.get(i).getSteps());
            viewHolder = new ViewHolder();

            view = layoutInflater.inflate(R.layout.route_path_item,null);

            viewHolder.busPaths = $(view,R.id.bus_paths);

            viewHolder.busLineName = busLineName;

            LinearLayout itemsL = null;

            viewHolder.busLine = new ArrayList<>();

            for(int count = 0;count < viewHolder.busLineName.size();count ++){
                itemsL = (LinearLayout) layoutInflater.inflate(R.layout.line_item,null);
//                itemsL.setId(View.generateViewId());
                viewHolder.busLine.add(itemsL);
                viewHolder.busPaths.addView(itemsL);
            }

            viewHolder.time = $(view,R.id.time);
            viewHolder.distance = $(view,R.id.distance);
            viewHolder.walkDistance = $(view,R.id.walk_distance);
            viewHolder.price = $(view,R.id.price);

            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
            busLineName = viewHolder.busLineName;
        }

        TextView lineName = null;
        for(int num = 0 ; num < viewHolder.busLine.size() ; num ++){

//            Log.i("TextAki","第" + listen + "次调用getView"
//                    + "*****num:"
//                    + num
//                    + "当前busLineName.size()："
//                    + busLineName.size());

            lineName = $(viewHolder.busLine.get(num),R.id.line_name);

            lineName.setText(viewHolder.busLineName.get(num));

            if(num == viewHolder.busLine.size()-1){
                ImageView arrow = $(viewHolder.busLine.get(num),R.id.arrow);
                arrow.setVisibility(View.GONE);
            }
        }

        viewHolder.time.setText(FormatTime(pathList.get(i).getDuration()));
        viewHolder.distance.setText(FormatBusDistance(pathList.get(i).getBusDistance()));
        viewHolder.walkDistance.setText(String.valueOf(pathList.get(i).getWalkDistance()) + "米");
        viewHolder.price.setText(String.valueOf(pathList.get(i).getCost()) + "元");

        view.setLayoutParams(new ListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                Initialize.SCREEN_HEIGHT/7));
        return view;
    }

    private <T extends  View> T $(View view,int resId){
        return (T) view.findViewById(resId);
    }

    public String FormatTime(long time){
        long hour = time / 3600;
        long minute = (time % 3600)/60;
        return String.valueOf(hour) + "小时" + String.valueOf(minute) + "分钟";
    }

    public String FormatBusDistance(float distance){
        String number =  new DecimalFormat("0").format(distance/1000);
        return number + "km";
    }

    public List<String> calculateLine(List<BusStep> busStep){
        List<String> lineName = new ArrayList<>();
        String name;
        for(int count = 0;count < busStep.size();count ++){
            if(0 != busStep.get(count).getBusLines().size()){
                name = busStep.get(count).getBusLines().get(0).getBusLineName();
                lineName.add(name.substring(0,name.indexOf("路") + 1));
            }
        }
        return lineName;
    }

    public static class ViewHolder{
        public LinearLayout busPaths;
        public List<LinearLayout> busLine;
        public TextView time;
        public TextView distance;
        public TextView walkDistance;
        public TextView price;
        public  List<String> busLineName;
    }

}
