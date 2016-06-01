package com.aki.bustool.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aki.bustool.R;
import com.aki.bustool.utils.Initialize;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;

import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by chunr on 2016/5/30.
 */
public class RoughDetailPagerAdapter extends PagerAdapter {

    private Context context;
    private List<BusPath> pathList;
    private List<View> views;
    private LayoutInflater layoutInflater;
    private View view;
    private LinearLayout busPaths;
    private List<LinearLayout> busLine;
    private TextView time;
    private TextView distance;
    private TextView walkDistance;
    private TextView price;

    public RoughDetailPagerAdapter(Context context, List<BusPath> data) {
        this.context = context;
        this.pathList = data;
        this.layoutInflater = LayoutInflater.from(context);
        initViews();
    }


    private void initViews(){
        if(null != pathList) {
            views = new ArrayList<>();
            for (int count = 0; count < pathList.size(); count++) {
                List<String> busLineName = null;
                busLineName = calculateLine(pathList.get(count).getSteps());

                view = layoutInflater.inflate(R.layout.route_path_item, null);

                busPaths = $(view, R.id.bus_paths);

                LinearLayout itemsL = null;

                busLine = new ArrayList<>();

                for(int i = 0;i < busLineName.size();i ++){
                    itemsL = (LinearLayout) layoutInflater.inflate(R.layout.line_item,null);
//                itemsL.setId(View.generateViewId());
                    busLine.add(itemsL);
                    busPaths.addView(itemsL);
                }

                time = $(view, R.id.time);
                distance = $(view, R.id.distance);
                walkDistance = $(view, R.id.walk_distance);
                price = $(view, R.id.price);

                TextView lineName = null;
                for (int num = 0; num < busLine.size(); num++) {
                    lineName = $(busLine.get(num), R.id.line_name);

                    lineName.setText(busLineName.get(num));

                    if (num == busLine.size() - 1) {
                        ImageView arrow = $(busLine.get(num), R.id.arrow);
                        arrow.setVisibility(View.GONE);
                    }

                }
                time.setText(FormatTime(pathList.get(count).getDuration()));
                distance.setText(FormatBusDistance(pathList.get(count).getBusDistance()));
                walkDistance.setText(String.valueOf(pathList.get(count).getWalkDistance()) + "米");
                price.setText(String.valueOf(pathList.get(count).getCost()) + "元");

                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        Initialize.SCREEN_HEIGHT/11));
                views.add(view);
            }
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return views.size();
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view   Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position  The page position to be removed.
     * @param object    The same object that was returned by
     *                  {@link #instantiateItem(View, int)}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View in which the page will be shown.
     * @param position  The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
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
}
