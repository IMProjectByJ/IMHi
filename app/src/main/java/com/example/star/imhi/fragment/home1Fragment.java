package com.example.star.imhi.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.star.imhi.DAO.pojo.Friends;
import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.Utils.FileOperateService;
import com.example.star.imhi.activity.StartActivity;
import com.example.star.imhi.adapter.FriendsListAdapter;
import com.example.star.imhi.database.MyDatabaseHelper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by d c on 2018/1/13.
 */

@SuppressLint("ValidFragment")
public class home1Fragment extends android.support.v4.app.Fragment {
    private MyDatabaseHelper dbHelper;
    RecyclerView  recyclerView;
    FriendsListAdapter adapter;
    Map<String,Object> content;
    String friendlist;
    List<Friends> friends_List = new ArrayList<>();
    private String a,b;
    private User user;
    private ImageView headImg;
    private Bitmap bitmap;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
/*
    public home1Fragment(){}
*/

    @SuppressLint("ValidFragment")
    public home1Fragment(String friendlist){
        this.friendlist = friendlist;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //yuyisummer

        dbHelper = new MyDatabaseHelper(getActivity(),"FriendsStore.db",null,1);
        dbHelper.getWritableDatabase();


        View view=inflater.inflate(R.layout.haoyou, container, false);
       JSONObject jsonObject = new JSONObject();
        Friends user ;
        try {
            jsonObject = new JSONObject(friendlist);
            System.out.println(friendlist);
            Iterator<String> keyIter= jsonObject.keys();
            String key;
            Object value ;
           // Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
            //先注释掉，后面可能还是需要
                //    valueMap.put(key, value);
                 user = new Friends();
                 Log.e("home1Fragment",key + "      "+value);
                user.setUser_id(key);
                user.setName(String.valueOf(value));
                friends_List.add(user);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) view .findViewById(R.id.haoyourecyfriendslist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FriendsListAdapter(friends_List);
        recyclerView.setAdapter(adapter);
        headImg = (ImageView)view.findViewById(R.id.touxiang);

        //接收广播---同意了好友请求
        LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(view.getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.agree.LOCAL_BROADCAST");
        LocalReceiver localReceiver =new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
        return view;
    }
    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            a = intent.getStringExtra("touseid");
            b = intent.getStringExtra("toname");
            Log.e("receive","#################"+a);
            Log.e("receive","#################"+b);
            getHeadImg();

        }
    }
    public FriendsListAdapter getAdapter(){
        return this.adapter;
    }
    public void getHeadImg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request;
                request = new Request.Builder().url(getString(R.string.postUrl) +"api/user/add_search_by_uid/" + a).build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e("responseData:", responseData);
                    JSONObject jsonObject = new JSONObject(responseData);

                    if (jsonObject.optString("add_search") != null && !jsonObject.optString("add_search").equals("")) {
                        Gson gson  = new Gson();
                        user = gson.fromJson(jsonObject.getString("add_search"),User.class);
                        Log.e("TEST_USER:", user.toString() );

                    } else {
                        return;
                    }


                    //搜索好友头像
                    RequestBody requestBody = RequestBody.create(JSON, jsonObject.getString("add_search"));
                    Request request1 = new Request.Builder()
                            .url(getString(R.string.postUrl) + "api/imageOperate/headDownload")
                            .post(requestBody)
                            .build();
                    //发送请求获取响应
                    try {
                        Response response1 = client.newCall(request1).execute();
                        //判断请求是否成功
                        if(response1.isSuccessful()) {
                            //打印服务端返回结果
                            Log.e("Success", "success!!!!!!!!!");
                            bitmap = FileOperateService.imgdecode(response1);
                        } else {
                            return;
                        }

                        StartActivity startActivity = (StartActivity)getActivity();
                        startActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("TEST", "run1: " );
                                Friends user = new Friends(a,b,bitmap);

                                if (user != null) {
                                    Log.e("FriendUser:", "111111111111");
                                } else {
                                    Log.e("USER-----", "user is null");
                                }
                                friends_List.add(user);
                                Log.e("TEST", "run2: " );
                                adapter = new FriendsListAdapter(friends_List);
                                Log.e("TEST", "run3: " );
                                recyclerView.setAdapter(adapter);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }).start();
    }
}
