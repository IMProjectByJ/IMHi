package com.example.star.imhi.mina;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

/**
 * Created by 11599 on 2018/1/9.
 */

public class MyHandler extends IoHandlerAdapter {
    private Context mContext;

    MyHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }

    //接收信息
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Log.e("MyHandler", message.toString());

        String textcontent = "";

        JSONObject jsonObject = new JSONObject(message.toString());
        String type = jsonObject.getString("message_type");
        Log.e("测试", "测试能否进行直接转换");
        Log.e("测试", jsonObject.toString());

        Intent intent = new Intent("com.bs.myMsg");
        switch (type) {
            case "6":
                break;
            case "2":
                Log.e("MyHandler","已经接收到了信息");
                textcontent = jsonObject.getString("textcontent");
                intent.putExtra("textcontent", textcontent);
                break;
            case "3":
            case "7":
            case "8":
            case "9":
                Log.e("测试消息type8", message.toString());
                textcontent = jsonObject.getString("textcontent");
                intent.putExtra("textcontent", textcontent);
                break;

        }
        if(type.equals("8")){
            intent.putExtra("nikname",jsonObject.getString("nikname"));
        }
        if (mContext != null) {
            intent.putExtra("message_type", type);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

    }

    @Override
    public void messageSent(IoSession session, Object message) {
        try {
            super.messageSent(session, message);
        } catch (Exception e) {
            Log.e("MyHandler", "发送失败");
            e.printStackTrace();
        }
        Log.e("ceshi", "发送成功");
    }
}
