package com.example.star.imhi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.star.imhi.BaseActivity;
import com.example.star.imhi.DAO.pojo.ChatList;
import com.example.star.imhi.DAO.pojo.Friends;
import com.example.star.imhi.DAO.pojo.HistoryMessage;
import com.example.star.imhi.DAO.pojo.Numinfo;
import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.Utils.FileOperateService;
import com.example.star.imhi.Utils.FileUtils;
import com.example.star.imhi.Utils.OkHttpUtils;
import com.example.star.imhi.adapter.TabAdapter;
import com.example.star.imhi.database.MyDatabaseHelper;
import com.example.star.imhi.fragment.home1Fragment;
import com.example.star.imhi.fragment.home2Fragment;
import com.example.star.imhi.fragment.tab1Fragment;
import com.example.star.imhi.fragment.tab2Fragment;
import com.example.star.imhi.fragment.tab3Fragment;
import com.example.star.imhi.mina.MyService;
import com.example.star.imhi.mina.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StartActivity extends BaseActivity {
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    private DrawerLayout drawerLayout;
    private RadioGroup mRadioGroup;
    private RadioButton mRadio01;
    private RadioButton mRadio02;
    private RadioButton mRadio03;
    private MyDatabaseHelper dbHelper;

    private ImageView mTabLine;// 指导线
    private int screenWidth;// 屏幕的宽度

    private ViewPager mViewPager;
    private TabAdapter mAdapter;
    private int position = 0;
    private TextView title_name;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private tab1Fragment tab1Fragment;
    private tab2Fragment tab2Fragment;
    private home1Fragment home1Fragment;
    private home2Fragment home2Fragment;
    //yuyisummer
    String loginUser;
    Map<String, Numinfo> localofflist = new HashMap<>();

    private TextView tAddress;
    private TextView tName;
    private TextView tPhone;
    private User user;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private static final String PREFERENCE_NAME = "userInfo";
    private ImageView headImg;
    private String  token;
    private TextView tNi;
    private TextView tAge;
    private TextView tBirth;

    private Handler hander = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            JSONObject jsonObject = null;
            String type = null;
            try {
                jsonObject = new JSONObject((String) msg.obj);
                type = jsonObject.getString("friend_type");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (type) {
                case "1":
                case "3":
                    try {
                        tab1Fragment.getAdapter().addItem(jsonObject.getString("friend_id"),
                                jsonObject.getInt("friend_type"),
                                jsonObject.getString("message_num"),
                                jsonObject.getInt("old_id"),
                                "暂时没有"
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        setContentView(R.layout.activity_start);

        tName = (TextView) findViewById(R.id.nicheng);
        tAddress = (TextView) findViewById(R.id.taddress);
        tPhone = (TextView) findViewById(R.id.tTelphone);
        sp = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        editor = sp.edit();
        token = sp.getString("token", "");
        headImg = (ImageView)findViewById(R.id.head_image);
        tNi = (TextView) findViewById(R.id.nicheng);
        tAge = (TextView)findViewById(R.id.tAge);
        tBirth = (TextView)findViewById(R.id.tBirth);

        Log.e("token",token);
        Log.e("-------token:", token+"is null?");
//yuyisummer
        dbHelper = new MyDatabaseHelper(this, "FriendsStore.db", null, 1);
        dbHelper.getWritableDatabase();
        Intent intent = getIntent();
        loginUser = intent.getStringExtra("loginUser");
        MsgReceiver msgReceiver = new MsgReceiver();
        IntentFilter filter = new IntentFilter("com.bs.myMsg");
        LocalBroadcastManager.getInstance(StartActivity.this).registerReceiver(msgReceiver, filter);
        Intent in = new Intent(StartActivity.this, MyService.class);
        Log.e("LoginActivity", "进行MyService");
        startService(in);


        personMsg(loginUser);


//        Log.e("image_url:", sp.getString("image_url", "TEST!!!!!!!!!!!!!!!"));
//        Bitmap bm = BitmapFactory.decodeFile(sp.getString(loginUser,""));
//
//        if (bm != null) {
//            Log.e("-----------Bitmap:", "bitmap is not null" );
//            headImg.setImageBitmap(bm);
//            //headImg.setImageResource(R.drawable.man);
//        } else {
//            Log.e("-----------Bitmap:", "bitmap is null" );
//
//            String responseUrl = getString(R.string.postUrl)+"/api/imageOperate/imageDownload";
//            responseServert(responseUrl);
//        }
    }


    //标题栏菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                break;
            case R.id.del:
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    private void initViews() {
        mRadioGroup = (RadioGroup) findViewById(R.id.id_radioGroup);
        mRadio01 = (RadioButton) findViewById(R.id.tab1);
        mRadio02 = (RadioButton) findViewById(R.id.tab2);
        mRadio03 = (RadioButton) findViewById(R.id.tab3);

        mTabLine = (ImageView) findViewById(R.id.id_tab_line);
        //获取屏幕的宽度
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;

        //设置mTabLine宽度//获取控件的(注意：一定要用父控件的LayoutParams写LinearLayout.LayoutParams)
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLine.getLayoutParams();//获取控件的布局参数对象
        lp.width = screenWidth / 3;
        mTabLine.setLayoutParams(lp); //设置该控件的layoutParams参数
        //这里需要往布局页面里加数据
        mFragments.clear();
        //yuyisummer 加载第一个界面
        mFragments.add(tab1Fragment);

        Log.e("StartActivity", "      " + 3);
        //yuyisummer 加载第二个界面

        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String friendlist = preferences.getString("friendlist", null);
        Log.e("start+friendlist", friendlist);

        home1Fragment = new home1Fragment(friendlist);
        home2Fragment = new home2Fragment();
        tab2Fragment = new tab2Fragment(home1Fragment, home2Fragment);
        mFragments.add(tab2Fragment);
        mFragments.add(new tab3Fragment());

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mAdapter = new TabAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1);
        mRadio02.setChecked(true);

    }

    public void personMsg(final String userId){
        personMsgRequest(userId);

    }

    public void responseServert(final String Url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils fileUtils = new FileUtils();
                OkHttpClient okHttpClient = OkHttpUtils.getOkHttpClientInstance();
                Log.e("-------OkHttpClient:", "run1: ");
                Request request = FileOperateService.DownloadFileRequest(101, Url, token);
                Log.e("-----OkHttpClient:", "run2: ");
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    File file = fileUtils.createFileInSDCard(user.getPhoneNum(), user.getUserId().toString());
                    File file1 = FileOperateService.saveFile(response, user.getPhoneNum()+".jpg", file.getAbsolutePath());

                    if (file1 == null) {
                        Log.e("file1?", "file1 is null" );
                        //Log.e("-------file1:", file1.getAbsolutePath());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                headImg.setImageResource(R.drawable.man);
                            }
                        });
                    } else {
                        Log.e("-------file1:", file1.getAbsolutePath());
                        final Bitmap bm1 = BitmapFactory.decodeFile(file1.getAbsolutePath());//                    Log.e("--------ResponseBody:", file );

                        if (bm1 == null) {
                            Log.e("-----------Bitmap1111:?", "Bitmap is null" );
                            //Log.e("-----------Bitmap1111:", bm1.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    headImg.setImageResource(R.drawable.man);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("bm1-----:", bm1.toString() );
                                    headImg.setImageBitmap(bm1);
                                    //headImg.setImageResource(R.drawable.woman);
                                }
                            });
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void personMsgRequest(final String userId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request;
                request = new Request.Builder().get().url(getString(R.string.postUrl) + "api/user/details/" + userId).build();

                try{
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();

                    Log.e("responseData:", responseData);
                    JSONObject jsonObject = new JSONObject(responseData);

                    if (jsonObject.optString("err") == "") {
                        Gson gson  = new Gson();
                        user = gson.fromJson(responseData,User.class);
                        Log.e("details:",user.toString() );
                    }

                    //头像
                    Log.e("image_url:", sp.getString("image_url", "TEST!!!!!!!!!!!!!!!"));
                    final Bitmap bm = BitmapFactory.decodeFile(sp.getString(loginUser,""));

                    if (bm != null) {
                        Log.e("-----------Bitmap:", "bitmap is not null" );
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                headImg.setImageBitmap(bm);
                            }
                        });

                        //headImg.setImageResource(R.drawable.man);
                    } else {
                        Log.e("-----------Bitmap:", "bitmap is null" );

                        String responseUrl = getString(R.string.postUrl)+"/api/imageOperate/imageDownload";
                        responseServert(responseUrl);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tName.setText(user.getNikname());
                            tPhone.setText(user.getPhoneNum());
                        }
                    });

                } catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLine.getLayoutParams();
            //获取组件距离左侧组件的距离
            lp.leftMargin = (int) ((positionOffset + position) * screenWidth / 3);
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
    class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SQLiteDatabase db;
            db = dbHelper.getWritableDatabase();
            Log.e("ceshi", "获得到了数据");
            String message_type = intent.getStringExtra("message_type");
            final Gson gson = new Gson();
            Log.e("HomepageAcitivty", message_type);
            Cursor cursor = null;
            Cursor oldIdCursor = null;
            switch (message_type) {
                case "6":
                    //gb  setContentView(R.layout.activity_home_page);
                    denglu();
                    break;
                case "7":
                    Log.e("HomepageAcitivy", "7");
                    String textcontent = intent.getStringExtra("textcontent");
                    Map<String, Numinfo> content = new HashMap<String, Numinfo>();
                    List<ChatList> chat_List = new ArrayList<>();

                    final JSONObject jsonObject;

                    try {
                        jsonObject = new JSONObject(textcontent);
                        Iterator<String> keyIter = jsonObject.keys();
                        String key;
                        Numinfo numinfo;

                        int message_num = 0;
                        while (keyIter.hasNext()) {
                            key = keyIter.next();
                            numinfo = gson.fromJson(jsonObject.get(key).toString(), Numinfo.class);
                            content.put(key, numinfo);

                            String[] fromwho = key.split("\\|");

                            if (fromwho[1].equals("1")) {
                                oldIdCursor = db.query("history_message", new String[]{"max(message_id)"}, "message_type=?", new String[]{"2"}, null, null, null);
                            } else if (fromwho[1].equals("2")) {
                                oldIdCursor = db.query("history_message", new String[]{"max(message_id)"}, "message_type=?", new String[]{"2"}, null, null, null);
                            } else if (fromwho[1].equals("3")) {
                                oldIdCursor = db.query("history_message", new String[]{"max(message_id)"}, "message_type=? or message_type=? or message_type=?", new String[]{"8", "9", "10"}, null, null, null);
                            }

                            int old_id;
                            oldIdCursor.moveToFirst();
                            if (oldIdCursor.getCount() == 0) {
                                System.out.println("oldcursor    0");
                                old_id = 0;
                            } else {
                                System.out.println("oldcursor    " + oldIdCursor.getCount());
                                old_id = oldIdCursor.getColumnIndex("max(message_id)");
                                System.out.println("oldcursor    不为0 为：" + oldIdCursor.getCount());
                            }
                      //      jsonObject.put("oldId", old_id);
                            ContentValues values = new ContentValues();
                            values.put("user_id", numinfo.getUserId());
                            values.put("friend_type", numinfo.getFriendType());
                            values.put("friend_id", numinfo.getFriendId());
                            values.put("new_id", numinfo.getNewId());
                            values.put("old_id", old_id);
                                cursor = db.query("NumInfo", new String[]{"friend_id"},
                                        "user_id=? and friend_id=? and friend_type=?",
                                        new String[]{String.valueOf(numinfo.getUserId()), String.valueOf(numinfo.getFriendId())
                                                , numinfo.getFriendType()}, null, null, null);
                                if (cursor.getCount() == 0) {
                                long retval = db.insert("NumInfo", null, values);

                                if (retval == -1)
                                    Log.e("StartActivity", "failed");
                                else
                                    Log.e("StartActivity", "success " + retval);
                            } else {
                                int update = db.update("NumInfo", values, "user_id=? and friend_id=? and friend_type=?",
                                        new String[]{String.valueOf(numinfo.getUserId()), String.valueOf(numinfo.getFriendId())
                                                , numinfo.getFriendType()});
                                Log.e("LoginAcitivity", "update " + update);
                            }
                        }
                        localofflist = content;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tab1Fragment = new tab1Fragment(chat_List);

                    init();

                    //开始一个一个增加
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            OkHttpClient client = new OkHttpClient();
                            dbHelper.getReadableDatabase();
                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            Cursor numinfo_cursor = null;
                            JSONObject jsonObject = new JSONObject();
                            SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            String user_id = preferences.getString("userId", "0");
                            System.out.println("userId" + user_id)
                            ;
                            numinfo_cursor = db.query("NumInfo", new String[]{"user_id", "friend_type","friend_id","new_id","old_id"},
                                    "user_id=?",
                                    new String[]{user_id}, null, null, null);
                            numinfo_cursor.moveToFirst();

                            if (numinfo_cursor.getCount() != 0  ) {
                                numinfo_cursor.moveToFirst();
                                Log.e("cursor", "is not null");
                                do {
                                    try {

                                        jsonObject.put("friend_type", numinfo_cursor.getString(numinfo_cursor.getColumnIndex("friend_type")));
                                        jsonObject.put("friend_id", numinfo_cursor.getString(numinfo_cursor.getColumnIndex("friend_id")));
                                        jsonObject.put("new_id", numinfo_cursor.getString(numinfo_cursor.getColumnIndex("new_id")));
                                        jsonObject.put("old_id", numinfo_cursor.getString(numinfo_cursor.getColumnIndex("old_id")));
                                        jsonObject.put("user_id", numinfo_cursor.getString(numinfo_cursor.getColumnIndex("user_id")));
                                        Request request;
                                        String str = jsonObject.toString();
                                        request = new Request.Builder().url(getString(R.string.postUrl)
                                                + "api/numinfo/register/" + str).get().build();
                                        try {
                                            Response response = client.newCall(request).execute();
                                            String responseData = response.body().string();

                                            Gson gson = new Gson();
                                            Log.e("startActivity response",responseData);
                                            List<HistoryMessage> list = gson.fromJson(responseData, new TypeToken<List<HistoryMessage>>() {
                                            }.getType());

                                            Cursor cursor1;
                                            for (i = 0; i < list.size(); i++) {
                                                HistoryMessage historyMessage = list.get(i);
                                                ContentValues values = new ContentValues();
                                                values.put("user_from_id", historyMessage.getUserFromId());
                                                values.put("to_id", historyMessage.getToId());
                                                values.put("text_type", historyMessage.getTextType());
                                                values.put("message_id", historyMessage.getMessageId());
                                                values.put("message_type", historyMessage.getMessageType());
                                                values.put("text_content", historyMessage.getTextContent());
                                                values.put("date", String.valueOf(historyMessage.getDate()));
                                                cursor1 = db.query("history_message", new String[]{"message_id"},
                                                        "message_id=?",
                                                        new String[]{String.valueOf(historyMessage.getMessageId())}, null, null, null);
                                                if (cursor1.getCount() == 0) {
                                                    long retval = db.insert("history_message", null, values);

                                                    if (retval == -1)
                                                        Log.e("history_message", "failed");
                                                    else
                                                        Log.e("history_message", "success " + retval);
                                                    //  Log.e("history_message已存在", String.valueOf(historyMessage.getMessageId()));
                                                }

                                            }

                                            int num = list.size();
                                            if (num != 0) {
                                                Numinfo numinfo1 = new Numinfo();
                                                Message msg = hander.obtainMessage();
                                                jsonObject.put("message_num", num);
                                                msg.obj = jsonObject.toString();
                                                hander.sendMessage(msg);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } while (numinfo_cursor .moveToNext());
                            }
                        }
                    }).start();

                    break;
                case "8":
                    Log.e("HomepageActivity", "8");

                    String str = intent.getStringExtra("textcontent");
                    System.out.println("type8需要的str" + str);
                    Numinfo numinfo = gson.fromJson(str, Numinfo.class);
                    tab1Fragment.getAdapter().addItem(String.valueOf(numinfo.getFriendId())
                            , Integer.valueOf(numinfo.getFriendType()),
                            "1", numinfo.getOldId(), "通知");
                    break;
            }
        }

    }

    int i = 0;

    public void init() {
        i++;
        Log.e("StartActivity", "      " + String.valueOf(i));

        List<Friends> friends_List = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        title_name = (TextView) findViewById(R.id.title_name);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.daohang);
        }
        Log.e("StartActivity", "      " + 2);
        initViews();
        initEvent();

    }

    public void denglu() {

        //  loginUser = "10001";
        JSONObject json = new JSONObject();
        try {
            json.put("from", loginUser);
            json.put("message_type", "6");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("StartActivity", "登录信息准备完毕");
        SessionManager.getInstance().writeMag(json);
        Log.e("StartActivity", "登录信息测试");
    }

    public void receiveadd() {
        //tab1Fragment tab1Fragment = new tab1Fragment();
        // tab1Fragment.addlist("群通知","有人加你");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(StartActivity.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.imagePath.LOCAL_BROADCAST");
        LocalReceiver localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);

    }
    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String a = intent.getStringExtra("imgData");
            String name = intent.getStringExtra("userName");
            String age = intent.getStringExtra("userAge");
            String birth = intent.getStringExtra("userBirth");
            Log.e("receive","#################"+a);

            Log.e("userAge:", age + "age" );

            if (a != null && !a.equals("")) {
                File file = new File(a);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                headImg.setImageBitmap(bitmap);
            }

            if (name != null && !name.equals("") && !name.equals(tName.getText().toString())) {
                tNi.setText(name);
            }

            if (age != null && !age.equals("") && !age.equals(tAge.getText().toString())) {
                tAge.setText(age);
            }

            if (birth != null && !birth.equals("") && !birth.equals(tBirth.getText().toString())) {
                tBirth.setText(birth);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        System.exit(0);
    }




    //------------------------------------home键--------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HOME){
            Log.e("home键","*********");
            Intent intenthome = new Intent(StartActivity.this,StartActivity.class);
            PendingIntent pi =PendingIntent.getActivity(this,0,intenthome,0);
            // 创建一个NotificationManager的引用
            NotificationManager notificationManager = (NotificationManager)getSystemService(android.content.Context.NOTIFICATION_SERVICE);
            // 定义Notification的各种属性
            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.app)
                    .setTicker("This is ticker text")
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("Hi")
                    .setContentText("hi正在后台运行")
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();
            notificationManager.notify(0, notification);
            return true;
        }
        //------------------------------back键------------------------------------------
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Log.e("back键","#D########");
            AlertDialog.Builder dialog = new AlertDialog.Builder(StartActivity.this);
            dialog.setTitle("Hi");
            dialog.setMessage("确定退出？");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

}
