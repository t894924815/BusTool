package com.aki.bustool.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aki.bustool.R;
import com.aki.bustool.bean.ErrorStatus;
import com.aki.bustool.bean.LocationMessage;
import com.aki.bustool.fragment.IndexFragment;
import com.aki.bustool.fragment.MineFragment;
import com.aki.bustool.service.LocationService;
import com.aki.bustool.test.Main2Activity;
import com.aki.bustool.utils.IcoUtil;
import com.aki.bustool.utils.Initialize;
import com.aki.bustool.utils.StyleUtil;
import com.aki.bustool.utils.UserDao;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.core.PoiItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener/*implements View.OnTouchListener*/ {


    private LinearLayout loginPage;
    private LinearLayout noUser;
    private Button submit;
    private EditText userName;
    private EditText password;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private ArrayAdapter arrayAdapter;
    private ImageView login;
    private Button regist;

    private LinearLayout mIndex;
    private ImageView mIndexImage;
    private TextView mIndexText;

    private LinearLayout mRoute;
    private ImageView mRouteImage;
    private TextView mRouteText;

    private LinearLayout mMine;
    private ImageView mMineImage;
    private TextView mMineText;

    private LinearLayout mAboard;
    private ImageView mAboardImage;
    private TextView mAboardText;


    private FragmentManager mFragmentManager;

    private static int FRAGMENT_MAIN;

    private IndexFragment mIndexFragment;
    private MineFragment mMineFragment;
    private Intent startLocationServiceIntent;

    private int currentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Initialize.init(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        initLeft();
    }

    public void initLeft() {
        toolbar.setTitle(Initialize.INDEX_PAGE);//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setToolbarTitle();
                toolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toolbar.setVisibility(View.GONE);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);
    }

    public void initView() {
        userName = $(R.id.user_name);
        password = $(R.id.password);
        submit = $(R.id.submit);
        noUser = $(R.id.no_user);
        loginPage = $(R.id.login_page);
        regist = $(R.id.regist);
        submit.setOnClickListener(this);
        regist.setOnClickListener(this);

        mDrawerLayout = $(R.id.drawer_layout);
        toolbar = $(R.id.tl_custom);
        toolbar.setVisibility(View.GONE);
        mDrawerLayout = $(R.id.drawer_layout);
        lvLeftMenu = $(R.id.lv_left_menu);
        login = $(R.id.login);
        login.setOnClickListener(this);
        loginPage.setOnClickListener(this);

        //下方组件初始化
        mIndex = $(R.id.index);
        mIndex.setOnClickListener(this);
        mIndexImage = $(R.id.index_image);
        mIndexText = $(R.id.index_text);

        mRoute = $(R.id.route);
        mRoute.setOnClickListener(this);
        mRouteImage = $(R.id.route_image);
        mRouteText = $(R.id.route_text);

        mMine = $(R.id.mine);
        mMine.setOnClickListener(this);
        mMineImage = $(R.id.mine_image);
        mMineText = $(R.id.mine_text);

        mAboard = $(R.id.aboard);
        mAboard.setOnClickListener(this);
        mAboardImage = $(R.id.aboard_image);
        mAboardText = $(R.id.aboard_text);

        //fragment初始化
        FRAGMENT_MAIN = R.id.main_fragment;
        mFragmentManager = getFragmentManager();

        userName.addTextChangedListener(new OnGetUser(true));
        password.addTextChangedListener(new OnGetUser(false));
        submit.setEnabled(false);
        mIndex.performClick();
    }

    @SuppressWarnings("unchecked")
    private <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.index:
                setTabSelection(0);
                break;
            case R.id.route:
//                setTabSelection(1);
                startRouteCalculate();
                break;
            case R.id.mine:
                setTabSelection(2);
                break;
            case R.id.aboard:
                aboard();
                break;
            case R.id.login:
                login();
                break;
            case R.id.submit:
                submit();
                break;
            case R.id.regist:
                regist();
                break;
        }
    }

    private void submit() {
        UserDao check = new UserDao(this,userId,pass,null);
        int result = check.validate();
        switch(result){
            case Initialize.ERROR_USER:
                Toast.makeText(this,"用户名不存在!",Toast.LENGTH_LONG).show();
                break;
            case Initialize.ERROR_PASSWORD:
                Toast.makeText(this,"密码错误!",Toast.LENGTH_LONG).show();
                break;
            case Initialize.LOGIN_SUCCESS:
                showUserPage(userId);
                break;
        }
    }

    private void showUserPage(String username) {

    }


    private boolean userFull;
    private boolean passFull;

    private void login() {
        userFull = false;
        passFull = false;

        loginPage.setVisibility(View.VISIBLE);
        noUser.setVisibility(View.GONE);
    }

    private void regist() {
        Intent intent = new Intent(this,RegistPageActivity.class);
        startActivityForResult(intent,Initialize.REGIST_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Initialize.REGIST_REQUEST == requestCode && Initialize.REGIST_RESULT == resultCode){
            showUserPage(data.getStringExtra("UserId"));
        }
    }

    public void setTabSelection(int index) {
        currentPage = index;
        // 开启一个Fragment事务
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
                // 当点击了首页时，改变控件的图片和文字颜色
                switchIco(0);
                if (mIndexFragment == null) {
//                    Log.i("AOISora","mIndexFragment Create");
                    // 如果mIndexFragment为空，则创建一个并添加到界面上
                    mIndexFragment = new IndexFragment();
                    fragmentTransaction.add(FRAGMENT_MAIN, mIndexFragment);
                } else {
                    // 如果mIndexFragment不为空，则直接将它显示出来
//                    Log.i("AOISora","mIndexFragment Show");
                    fragmentTransaction.show(mIndexFragment);
                }
                break;
            /*case 1:
                // 当点击了路线时，改变控件的图片和文字颜色
                switchIco(1);
                if (mRouteFragment == null) {
//                    Log.i("AOISora","mRouteFragment Create");
                    // 如果mRouteFragment为空，则创建一个并添加到界面上
                    mRouteFragment = new RouteFragment();
                    fragmentTransaction.add(FRAGMENT_MAIN, mRouteFragment);
                } else {
//                    Log.i("AOISora","mRouteFragment Show");
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    fragmentTransaction.show(mRouteFragment);
                }
                break;*/
            case 2:
                // 当点击了个人中心时，改变控件的图片和文字颜色
                switchIco(3);
                if (mMineFragment == null) {
//                    Log.i("AOISora","mMineFragment Create");
                    // 如果mMineFragment为空，则创建一个并添加到界面上
                    mMineFragment = new MineFragment();
                    fragmentTransaction.add(FRAGMENT_MAIN, mMineFragment);
                } else {
//                    Log.i("AOISora","mMineFragment Show");
                    // 如果NewsFragment不为空，则直接将它显示出来
                    fragmentTransaction.show(mMineFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    public void hideFragments(FragmentTransaction transaction) {
        switchIco(22);
        if (mIndexFragment != null) {
            transaction.hide(mIndexFragment);
            switchIco(99);
        }
        /*if (mRouteFragment != null) {
            transaction.hide(mRouteFragment);
            switchIco(11);
        }*/
        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
            switchIco(33);
        }
    }


    //切换底层图标
    public void switchIco(int count) {
        switch (count) {
            case 0:
                mIndexImage.setImageBitmap(IcoUtil.getBitmapFromResource(R.drawable.indexc));
                StyleUtil.setColor(mIndexText, "click");
                break;
            /*case 1:
                mRouteImage.setImageBitmap(IcoUtil.getBitmapFromResource(R.drawable.routec));
                StyleUtil.setColor(mRouteText, "click");
                break;*/
            case 2:
                mAboardImage.setImageBitmap(IcoUtil.getBitmapFromResource(R.drawable.busc));
                StyleUtil.setColor(mAboardText, "click");
                break;
            case 3:
                mMineImage.setImageBitmap(IcoUtil.getBitmapFromResource(R.drawable.minec));
                StyleUtil.setColor(mMineText, "click");
                break;
            case 99:
                mIndexImage.setImageBitmap(IcoUtil.getBitmapFromResource(R.drawable.index));
                StyleUtil.setColor(mIndexText, "default");
                break;
            /*case 11:
                mRouteImage.setImageBitmap(IcoUtil.getBitmapFromResource(R.drawable.route));
                StyleUtil.setColor(mRouteText, "default");
                break;*/
            case 22:
                mAboardImage.setImageBitmap(IcoUtil.getBitmapFromResource(R.drawable.bus));
                StyleUtil.setColor(mAboardText, "default");
                break;
            case 33:
                mMineImage.setImageBitmap(IcoUtil.getBitmapFromResource(R.drawable.mine));
                StyleUtil.setColor(mMineText, "default");
                break;
        }
    }

    public void aboard() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    public void indexMap(View view) {
        Intent intent = new Intent(this, IndexMapActivity.class);
        startActivity(intent);
    }

    public void indexCollection(View view) {
    }

    public void startRouteCalculate() {
        Intent routeCalculate = new Intent(this, RouteActivity.class);
        if (null != locationMessage) {
            routeCalculate.putExtra("LocationMessage", locationMessage);
        }
        startActivity(routeCalculate);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(null != startLocationServiceIntent){
            stopService(startLocationServiceIntent);
        }*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 每次重新回到界面的时候注册广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.locationReceiver");
        registerReceiver(mainReceiver, filter);

        IntentFilter poiFilter = new IntentFilter();
        poiFilter.addAction("com.PoiBroadcast");
        registerReceiver(poiSearchReceiver, poiFilter);

        IntentFilter busLineFilter = new IntentFilter();
        busLineFilter.addAction("com.BusLineBroadcast");
        registerReceiver(busLineReceiver, busLineFilter);

        //先注册触发器再开启服务发送广播
        startLocationServiceIntent = new Intent(this,
                LocationService.class);
        startService(startLocationServiceIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mainReceiver) {
            unregisterReceiver(mainReceiver);
        }
        if (null != poiSearchReceiver && poiFlag) {
            unregisterReceiver(poiSearchReceiver);
        }
        if (null != busLineReceiver) {
            unregisterReceiver(busLineReceiver);
        }
    }


    private boolean poiFlag = false;

    private LocationMessage locationMessage;
    private ErrorStatus locationErrorStatus;

    private BroadcastReceiver mainReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            locationErrorStatus = intent.getParcelableExtra("ErrorStatus");
            if (locationErrorStatus.getIsError()) {

            } else {
                locationMessage = intent.getExtras().getParcelable("Location");
                if (null == mGetLocationMessage) {
                    mGetLocationMessage.OnReceiveMessage(locationMessage, locationErrorStatus);
                }
                poiFlag = true;
            }
        }
    };


    private ArrayList<PoiItem> mPoiMessage;
    private ErrorStatus poiErrorStatus;

    private BroadcastReceiver poiSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            poiErrorStatus = intent.getParcelableExtra("ErrorStatus");
            if (poiErrorStatus.getIsError()) {

            } else {

                mPoiMessage = intent.getParcelableArrayListExtra("PoiMessage");
                if (null != mGetPoiMessage) {
                    mGetPoiMessage.OnReceivePoiMessage(mPoiMessage, poiErrorStatus);
                }


//                Log.i("Aki0909090","poiMessage");
            }
        }
    };


    private ArrayList<Map<String, BusLineItem>> busLineItems;
    private ErrorStatus busLineErrorStatus;

    private BroadcastReceiver busLineReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            busLineErrorStatus = intent.getParcelableExtra("ErrorStatus");
            if (busLineErrorStatus.getIsError()) {
                Log.i("ERRORR", "busline服务获取错误");
            } else {
                String jsonData = intent.getStringExtra("BusLine");
                busLineItems = new Gson()
                        .fromJson(jsonData, new TypeToken<ArrayList<Map<String, BusLineItem>>>() {
                        }.getType());
//                Log.i("testAki93937557","广播到达!");

//                String test = intent.getExtras().getString("test");

//                busLineItems = intent.getExtras().getParcelableArrayList("BusLine");
//                ParcelableMap _map = busLineItems.get(0);
//                String b = busLineItems.get(0).get("GoBusLineMessage").getClass().getCanonicalName();
                /**
                 *busLineItems类型：List<Map<String,BusLineItem>>，map里有来去程的busline信息
                 *
                 * */

                /*for(Map<String,BusLineItem> map : busLineItems){
                    BusLineItem Gobus = (BusLineItem) map.get("GoBusLineMessage");
                    BusLineItem Backbus = (BusLineItem) map.get("BackBusLineMessage");
                    Log.i("testAki048585",Gobus.getBusLineType()+ "56");
                    Log.i("resultAki",Backbus.getBusLineName() + "/");
                }*/
                if (null != mGetBusLineMessage) {
                    mGetBusLineMessage.OnReceiveBusLineMessage(busLineItems, busLineErrorStatus);
                    Initialize.RECEIVED = true;
                }

            }
        }
    };


    public void search_index(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        if (null != locationMessage) {
            intent.putExtra("Location", locationMessage);
        }
        startActivity(intent);
    }

    public void setToolbarTitle() {
        switch (currentPage) {
            case 0:
                toolbar.setTitle(Initialize.INDEX_PAGE);
                break;
            case 1:
                toolbar.setTitle(Initialize.SECOND_PAGE);
                break;
            case 2:
                toolbar.setTitle(Initialize.THIRD_PAGE);
                break;
        }
    }


    public void go(View view) {
//        Intent intent = new Intent(this,);

    }

    private String userId;
    private String pass;

    class OnGetUser implements TextWatcher{

        private boolean isUser = false;

        public OnGetUser(boolean isUser) {
            this.isUser = isUser;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(isSpace(editable.toString())){
                if(isUser){
                    userFull = false;
                }else{
                    passFull = false;
                }
            }else{
                if(isUser){
                    userFull = true;
                    userId = editable.toString();
                }else{
                    passFull = true;
                    pass = editable.toString();
                }
            }
            if(userFull && passFull){
                submit.setEnabled(true);
            }else{
                submit.setEnabled(false);
            }
        }
    }

    public boolean isSpace(String text){
        boolean i = false;
        for(char a:text.toCharArray()){
            if(' ' == a){
                i = true;
            }
        }
        if(i){
            return true;
        }else{
            return false;
        }
    }

    private OnGetLocationMessage mGetLocationMessage;

    public void setOnGetLocationMessage(OnGetLocationMessage onGetLocationMessage) {
        mGetLocationMessage = onGetLocationMessage;
    }

    private OnGetPoiMessage mGetPoiMessage;

    public void setOnGetPoiMessage(OnGetPoiMessage onGetPoiMessage) {
        mGetPoiMessage = onGetPoiMessage;
    }

    private OnGetBusLineMessage mGetBusLineMessage;

    public void setOnGetBusLineMessage(OnGetBusLineMessage onGetBusLineMessage) {
        mGetBusLineMessage = onGetBusLineMessage;
    }

    //接口便于其他fragment即使获取locationMessage
    public interface OnGetLocationMessage {
        public void OnReceiveMessage(LocationMessage locationMessage, ErrorStatus errorStatus);
    }

    //poi获取接口
    public interface OnGetPoiMessage {
        public void OnReceivePoiMessage(ArrayList<PoiItem> poiMessage, ErrorStatus errorStatus);
    }

    //busline获取接口
    public interface OnGetBusLineMessage {
        public void OnReceiveBusLineMessage(ArrayList<Map<String, BusLineItem>> busLineItems, ErrorStatus errorStatus);
    }


}
