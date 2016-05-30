package com.aki.bustool.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aki.bustool.R;
import com.aki.bustool.adapter.SearchListAdapter;
import com.aki.bustool.bean.LocationMessage;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,
        TextWatcher, PoiSearch.OnPoiSearchListener, AdapterView.OnItemClickListener {

    private ImageButton backButton;

    private EditText searchEdit;

    private ImageView clearImage;

    private ListView searchList;


    private RelativeLayout hiddenImage;

    private String searchText;
    private LocationMessage locationMessage;


    //Poi搜索
    private PoiSearch mPoiSearch;
    private int currentPage;
    private PoiSearch.Query query;
    private String city;
    private LatLonPoint lp;
    private PoiResult poiResult;
    private List<PoiItem> poiItems;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hiddenImage.setVisibility(View.GONE);
            searchEdit.setFocusable(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if(null != getIntent().getParcelableExtra("Location")){
            locationMessage = getIntent().getParcelableExtra("Location");
        }

        initView();

    }

    public void initView(){
        backButton = $(R.id.back_ico);
        searchEdit = $(R.id.search_edit);
        clearImage = $(R.id.clear_text);
        searchList = $(R.id.search_list);
        hiddenImage = $(R.id.hiddenImage);


        backButton.setTag(1);
        backButton.setOnClickListener(this);

        searchEdit.addTextChangedListener(this);


        searchList.setOnItemClickListener(this);
        searchList.setVerticalScrollBarEnabled(false);

        clearImage.setTag(2);
        clearImage.setOnClickListener(this);
    }

    private <T extends View> T $(int resId){
        return (T) super.findViewById(resId);
    }


    //点击事件接口
    @Override
    public void onClick(View view) {
        int tag = (Integer) view.getTag();
        switch (tag) {
            case 1:
                finish();
                break;
            case 2:
                searchEdit.setText("");
                break;
        }
    }


    public String subSpace(String s) {
        StringBuilder sb = new StringBuilder();
        for (int count = 0; count < s.length(); count++) {
            if (s.charAt(count) != ' ') {
                sb.append(s.charAt(count));
            }
        }
        return sb.toString();
    }



    //edittext 文本变化监听接口
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i("beforeTextChanged", charSequence.toString() + "   i:" + i + "   i1:" + i1 + "  i2:" + i2);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i("onTextChanged", charSequence.toString() + "   i:" + i + "   i1:" + i1 + "  i2:" + i2);
    }


    @Override
    public void afterTextChanged(Editable editable) {
        Log.i("afterTextChanged", editable.toString());
        searchText = subSpace(editable.toString());
        if (!searchText.isEmpty()) {
            clearImage.setVisibility(View.VISIBLE);
            searchList.setVisibility(View.VISIBLE);
            if(null != locationMessage){
                doSearchQuery();
            }
        } else {
            clearImage.setVisibility(View.GONE);
            searchList.setVisibility(View.GONE);
        }
    }


    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        currentPage = 0;
//        city = locationMessage.getCity();
        city = locationMessage.getCity();
//        lp = new LatLonPoint(locationMessage.getLatitude(),locationMessage.getLongitude());
//        lp = new LatLonPoint(39.993167, 116.473274);
        query = new PoiSearch.Query(searchText, "", city);
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
//        if (lp != null) {
        mPoiSearch = new PoiSearch(this, query);
        mPoiSearch.setOnPoiSearchListener(this);
//        mPoiSearch.setBound(new PoiSearchService.SearchBound(lp, 1000, true));//
        // 设置搜索区域为以lp点为圆心，其周围5000米范围
        mPoiSearch.searchPOIAsyn();// 异步搜索
//        }
    }

    private SearchListAdapter adapter = null;
    private boolean isNotSearched = true;
    private List<PoiItem> list = null;

    //poi搜索结果返回接口
    @Override
    public void onPoiSearched(PoiResult result, int rcode) {

        if (rcode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    Log.i("Aki2345667", poiItems.get(0).getBusinessArea());

                    if (!isNotSearched) {
                        Log.i("Aki932085", "刷新了!");
                        list.clear();
                        list.addAll(poiItems);
                        adapter.notifyDataSetChanged();
                    }

                    if (isNotSearched && (null != poiItems)) {
                        Log.i("Aki89893489", "进来了！");
                        list = (poiItems);
                        adapter = new SearchListAdapter(this, list);
                        searchList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        isNotSearched = false;
                        Log.i("Aki", "走完了");
                    }

                }
            } else {

            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    //listview item点击监听接口
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this,"" + i,Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onStart() {

        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.locationReceiver");
        registerReceiver(locationReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(locationReceiver);
    }

    private boolean countVisible = true;
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getExtras().getBoolean("Fail")) {
                locationMessage = intent.getParcelableExtra("Location");
//                Log.i("Aki23565",locationMessage.getCity()
//                        + "        "
//                        + locationMessage.getLatitude()
//                        + "        "
//                        + locationMessage.getLongitude());
                if(countVisible){
                    mHandler.sendEmptyMessage(0);
                    countVisible = false;
                }
            } else {
                //尚未实现!!!
            }

        }
    };


}

