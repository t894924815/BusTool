package com.aki.bustool.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.aki.bustool.R;
import com.aki.bustool.adapter.RouteSearchListAdapter;
import com.aki.bustool.bean.LocationMessage;
import com.aki.bustool.utils.Initialize;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.*;

public class RouteInputActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, AdapterView.OnItemClickListener, PoiSearch.OnPoiSearchListener {


    private Intent resultIntent;
    private String placeWhere;

    private EditText searchEdit;
    private Button backButton;
    private Button myLocation;
    private Button mapSelect;
    private ImageView clearImage;
    private ListView searchList;

    private String searchText;

    private LocationMessage locationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_input);


    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    public void initView(){


        searchEdit = $(R.id.search_edit);
        backButton = $(R.id.back);
        myLocation = $(R.id.my_location);
        mapSelect = $(R.id.map_select);
        searchList = $(R.id.search_list);
        clearImage = $(R.id.clear_text);

        searchEdit.addTextChangedListener(this);
        searchEdit.setFocusable(true);
        backButton.setOnClickListener(this);
        myLocation.setOnClickListener(this);
        mapSelect.setOnClickListener(this);
        searchList.setOnItemClickListener(this);
        clearImage.setOnClickListener(this);

        searchEdit.setHint("输入" + (((getIntent().getStringExtra(Initialize.PLACE)).equals(Initialize.UP))?"起点":"终点"));



        if(null != getIntent().getParcelableExtra(Initialize.LOCATION)){
            locationMessage = getIntent().getParcelableExtra(Initialize.LOCATION);
        }
        if(null != getIntent().getStringExtra(Initialize.PLACE)){
            placeWhere = getIntent().getStringExtra(Initialize.PLACE);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends View> T $(int resId){
        return (T) super.findViewById(resId);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        searchText = subSpace(editable.toString());
        if (!searchText.isEmpty()) {
            clearImage.setVisibility(View.VISIBLE);
            searchList.setVisibility(View.VISIBLE);
            doSearchQuery();
        } else {
            clearImage.setVisibility(View.GONE);
            searchList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.clear_text:
                searchEdit.setText("");
                searchEdit.setHint("输入" + (((getIntent().getStringExtra(Initialize.PLACE)).equals(Initialize.UP))?"起点":"终点"));
//                searchEdit.setFocusable(false);
                break;
            case R.id.my_location:
                finishByMyLocation();
                break;
            case R.id.map_select:
               getPointFromMap();
//                Toast.makeText(this,"尚未实现!",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this,i + "---" + list.get(i),Toast.LENGTH_SHORT).show();
        resultIntent = new Intent();
        resultIntent.putExtra(Initialize.PLACE_NAME,poiItems.get(i).getTitle());
        resultIntent.putExtra(Initialize.PLACE_POINT,poiItems.get(i).getLatLonPoint());
        handMessage();
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
    public void finishByMyLocation(){
        resultIntent = new Intent();
        resultIntent.putExtra(Initialize.PLACE_NAME,"我的位置");
        resultIntent.putExtra(Initialize.PLACE_POINT,new LatLonPoint(locationMessage.getLatitude(),locationMessage.getLongitude()));
        handMessage();
    }

    public void handMessage(){
        setResult((placeWhere.equals(Initialize.UP)?Initialize.RESULT_CODE_UP:Initialize.RESULT_CODE_BELOW),resultIntent);
        finish();
    }


    private Intent getPointResultIntent;
    public void getPointFromMap(){
        getPointResultIntent = new Intent(this,SelectMapPointActivity.class);
        if(null != locationMessage){
            getPointResultIntent.putExtra(Initialize.PLACE,getIntent().getStringExtra(Initialize.PLACE));
            getPointResultIntent.putExtra(Initialize.LOCATION,locationMessage);
            startActivityForResult(getPointResultIntent,Initialize.GET_POINT);
        }else {
            Toast.makeText(this,"尚未定位完成，请稍后！",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开始进行poi搜索
     */
    private int currentPage;
    private String city;
    private PoiSearch.Query query;
    private PoiSearch mPoiSearch;
    public void doSearchQuery(){
        currentPage = 0;
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

    private RouteSearchListAdapter adapter = null;
    private boolean isNotSearched = true;
    private List<String> list = new ArrayList<String>();
    private PoiResult poiResult;
    private List<PoiItem> poiItems;

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
                        for(PoiItem item:poiItems){
                            list.add(item.getTitle());
                        }
                        adapter.notifyDataSetChanged();
                    }

                    if (isNotSearched && (null != poiItems)) {
                        Log.i("Aki89893489", "进来了！");
                        for(PoiItem item:poiItems){
                            Log.i("newLocation","-=-=" + item.getTitle());
                            list.add(item.getTitle());
                        }
                        adapter = new RouteSearchListAdapter(this,list);
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


    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Initialize.GET_POINT == requestCode && Initialize.SEND_POINT == resultCode){
            if(null != data.getStringExtra(Initialize.PLACE_NAME) && null != data.getParcelableExtra(Initialize.PLACE_POINT)){
                resultIntent = new Intent();
                resultIntent.putExtra(Initialize.PLACE_NAME,data.getStringExtra(Initialize.PLACE_NAME));
                resultIntent.putExtra(Initialize.PLACE_POINT,data.getParcelableExtra(Initialize.PLACE_POINT));
                handMessage();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
