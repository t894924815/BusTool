package com.aki.bustool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.List;
import java.util.ArrayList;

import com.aki.bustool.R;
import com.amap.api.services.route.BusPath;

/**
 * Created by chunr on 2016/5/30.
 */
public class MainPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<BusPath> data;
    private List<View> views;
    private BusSegmentListAdapter mBusSegmentListAdapter;

    public MainPagerAdapter(Context context,List<BusPath> items) {
        this.mContext = context;
        this.data = items;
        initView();
    }



    private void initView(){
        views = new ArrayList<>();
        ListView eachView = null;
        AbsListView.LayoutParams params =
                new AbsListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,ListView.LayoutParams.WRAP_CONTENT);
        for(BusPath path : data){
            eachView = new ListView(mContext);
            views.add(setListView(eachView,params,path));
        }
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
        ((ViewPager)container).removeView((View) object);
    }

    /**
     * Return the number of views available.
     */



    private ListView setListView(ListView view, AbsListView.LayoutParams params,BusPath path){
        view.setLayoutParams(params);
        view.setFooterDividersEnabled(false);
        view.setBackgroundColor(Color.WHITE);
        view.setDivider(new ColorDrawable(mContext.getResources().getColor(R.color.transparent)));
        view.setCacheColorHint(mContext.getResources().getColor(R.color.transparent));

        mBusSegmentListAdapter = new BusSegmentListAdapter(
				mContext, path.getSteps());
        view.setAdapter(mBusSegmentListAdapter);

        return view;
    }




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
        return view == ((View)object);
    }
}
