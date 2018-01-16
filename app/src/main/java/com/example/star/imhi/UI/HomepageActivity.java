package com.example.star.imhi.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v4.content.LocalBroadcastManager;

import com.example.star.imhi.R;
import com.example.star.imhi.mina.MyService;
import com.example.star.imhi.mina.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

/**
 * Created by 11599 on 2018/1/10.
 */

public class HomepageActivity extends AppCompatActivity {
    String loginUser  ;
    Date login_date;
/*
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e("HomepageActivity","handlemessage has use");
            super.handleMessage(msg);
            Log.e("HomepageActivity",msg.toString());
        }
    };
*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MsgReceiver msgReceiver = new MsgReceiver();
//        IntentFilter filter = new IntentFilter("com.bs.myMsg");
//        LocalBroadcastManager.getInstance(HomepageActivity.this).registerReceiver(msgReceiver,filter);


       Intent in = new Intent(HomepageActivity.this, MyService.class);
        Log.e("LoginActivity", "进行MyService");
        startService(in);


    //    setContentView(R.layout.activity_home_page);
       // Intent intent = getIntent();

      //  setContentView(tv);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return super.registerReceiver(receiver, filter);
    }

    class  MsgReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("ceshi", "获得到了数据");

            String type = intent.getStringExtra("type");
            Log.e("HomepageAcitivty",type);
            switch (type){
                case "6":
                    denglu();
                    break;
                case "7":
                    Log.e("HomepageAcitivy","7");
                    break;
                case "8":
                    Log.e("HomepageActivity","8");

            }
        }

    }

    public void denglu(){

        loginUser = "10001";
        JSONObject json = new JSONObject();
        try {
            json.put("from",loginUser);
            json.put("type","6");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("HomepageActivity","登录信息准备完毕");
        SessionManager.getInstance().writeMag(json);
        Log.e("HomepageActivity","登录信息测试");

    }
    
}
