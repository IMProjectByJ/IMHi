package com.example.star.imhi.activity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.star.imhi.BaseActivity;
import com.example.star.imhi.DAO.pojo.Friends;
import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.UI.HomepageActivity;
import com.example.star.imhi.adapter.ChatListAdapterr;
import com.example.star.imhi.adapter.FriendsListAdapter;
import com.example.star.imhi.adapter.TabAdapter;
import com.example.star.imhi.database.MyDatabaseHelper;
import com.example.star.imhi.fragment.tab1Fragment;
import com.example.star.imhi.fragment.tab2Fragment;
import com.example.star.imhi.fragment.tab3Fragment;
import com.example.star.imhi.mina.MyService;
import com.example.star.imhi.mina.Protocol;
import com.example.star.imhi.mina.SessionManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StartActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private RadioGroup mRadioGroup;
    private RadioButton mRadio01;
    private RadioButton mRadio02;
    private RadioButton mRadio03;
    String friend_str;
    RecyclerView recyclerView;
    FriendsListAdapter adapter ,adapter1;
    private MyDatabaseHelper dbHelper;

    private ImageView mTabLine;// 指导线
    private int screenWidth;// 屏幕的宽度

    private ViewPager mViewPager;
    private TabAdapter mAdapter;
    private  int position = 0;
    private TextView title_name;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private  tab1Fragment tab1Fragment =new tab1Fragment();
   // int initFlag = 1;
    //yuyisummer
    String loginUser  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

//yuyisummer
        dbHelper = new MyDatabaseHelper(this,"FriendsStore.db",null,1);
        dbHelper.getWritableDatabase();


        Intent intent = getIntent();
        loginUser = intent.getStringExtra("loginUser");
        MsgReceiver msgReceiver = new MsgReceiver();
        IntentFilter filter = new IntentFilter("com.bs.myMsg");
        LocalBroadcastManager.getInstance(StartActivity.this).registerReceiver(msgReceiver,filter);





        Intent in = new Intent(StartActivity.this, MyService.class);
        Log.e("LoginActivity", "进行MyService");
        startService(in);




   }
    //标题栏菜单
        public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add:
                break;
            case  R.id.del:
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }


    private void initViews(Map<String,Integer> content) {
        mRadioGroup = (RadioGroup) findViewById(R.id.id_radioGroup);
        mRadio01 = (RadioButton) findViewById(R.id.tab1);
        mRadio02 = (RadioButton) findViewById(R.id.tab2);
        mRadio03 = (RadioButton) findViewById(R.id.tab3);

        mTabLine = (ImageView) findViewById(R.id.id_tab_line);
        //获取屏幕的宽度
        DisplayMetrics outMetrics=new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth=outMetrics.widthPixels;

        //设置mTabLine宽度//获取控件的(注意：一定要用父控件的LayoutParams写LinearLayout.LayoutParams)
        LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams) mTabLine.getLayoutParams();//获取控件的布局参数对象
        lp.width=screenWidth/3;
        mTabLine.setLayoutParams(lp); //设置该控件的layoutParams参数
    //这里需要往布局页面里加数据
        mFragments.clear();
        mFragments.add(tab1Fragment);
        mFragments.add(new tab2Fragment(content));
        mFragments.add(new tab3Fragment());

        mViewPager=(ViewPager) findViewById(R.id.id_viewpager);
        mAdapter=new TabAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1);
        mRadio02.setChecked(true);




    }

    private void initEvent() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab1:
                        mViewPager.setCurrentItem(0);// 选择某一页
                        title_name.setText("消息");
                        break;
                    case R.id.tab2:
                        mViewPager.setCurrentItem(1);
                        title_name.setText("联系人");
                        break;
                    case R.id.tab3:
                        mViewPager.setCurrentItem(2);
                        title_name.setText("设置");
                        break;
                }
            }
        });

        mViewPager.setOnPageChangeListener(new TabOnPageChangeListener());
    }


    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当滑动状态改变时调用
         * state=0的时候表示什么都没做，就是停在那
         * state=1的时候表示正在滑动
         * state==2的时候表示滑动完毕了
         */
        public void onPageScrollStateChanged(int state) {

        }

        /**
         * 当前页面被滑动时调用
         * position:当前页面
         * positionOffset:当前页面偏移的百分比
         * positionOffsetPixels:当前页面偏移的像素位置
         */
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams) mTabLine.getLayoutParams();
            //获取组件距离左侧组件的距离
            lp.leftMargin=(int) ((positionOffset+position)*screenWidth/3);
            mTabLine.setLayoutParams(lp);
        }

        //当新的页面被选中时调用
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mRadio01.setChecked(true);
                    break;
                case 1:
                    mRadio02.setChecked(true);
                    break;
                case 2:
                    mRadio03.setChecked(true);
                    break;
            }
        }
    }

    //yuyisummer
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return super.registerReceiver(receiver, filter);
    }



    //yuyisummer
    class  MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("ceshi", "获得到了数据");
            String message_type = intent.getStringExtra("message_type");

            Log.e("HomepageAcitivty",message_type);
            switch (message_type){
                case "6":
                    //gb  setContentView(R.layout.activity_home_page);
                    denglu();
                    break;
                case "7":
                    Log.e("HomepageAcitivy","7");
                    Gson gson = new Gson();
                    Protocol protocol = gson.fromJson(intent.getStringExtra("friendstr"), Protocol.class);
                    Map<String,Integer> content = (Map<String, Integer>) protocol.getTextcontent();
                    //这里要传数据
                    /*
                    if(initFlag == 1) {
                        init(content);
                        initFlag++;
                    }*/

                    init(content);

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    for(Map.Entry entry : content.entrySet()) {
                        String user_id = String.valueOf(entry.getKey());
                        Cursor cursor;
                        cursor = db.query("Friends",null,"user_id=?", new String[]{user_id},null,null,null);
                        cursor.moveToFirst();
                        cursor.getString(cursor.getColumnIndex("nikname"));
                        String nikname = cursor.getString(cursor.getColumnIndex("nikname"));
                        String xiaoxi = content.get(user_id)+"条消息未读";
                        tab1Fragment.getAdapter().addItem(position,nikname,xiaoxi);
                        position++;
                    }

                    break;
                case "8":
                    Log.e("HomepageActivity","8");
               //  receiveadd();
                    String str = intent.getStringExtra("friendstr");
                    JSONObject jsonObject = null;

                    try {
                       jsonObject  = new JSONObject(str);
                       tab1Fragment.getAdapter().addItem(position,jsonObject.getString("from"),"获取新消息");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;

            }
        }

    }
    int i =0;
    public void init(Map<String,Integer> content)
    {
        i++;
        Log.e("StartActivity","      "+String.valueOf(i));

        List<Friends> friends_List = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        title_name = (TextView) findViewById(R.id.title_name);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.daohang);
        }
        initViews(content);
        initEvent();

    }

    public void denglu(){

      //  loginUser = "10001";
        JSONObject json = new JSONObject();
        try {
            json.put("from",loginUser);
            json.put("message_type","6");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("StartActivity","登录信息准备完毕");
        SessionManager.getInstance().writeMag(json);
        Log.e("StartActivity","登录信息测试");
    }
    public void receiveadd(){
        tab1Fragment tab1Fragment = new tab1Fragment();
       // tab1Fragment.addlist("群通知","有人加你");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
