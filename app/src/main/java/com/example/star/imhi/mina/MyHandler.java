package com.example.star.imhi.mina;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.star.imhi.Utils.ObjectToMap;
import com.google.gson.Gson;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by 11599 on 2018/1/9.
 */

public class MyHandler extends IoHandlerAdapter {
    private Context mContext;

    MyHandler(Context context){
        this.mContext = context;
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }
//接收信息
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Log.e("MyHandler",message.toString());
        Intent intent = new Intent("com.bs.myMsg");
        String textcontent = "";

        JSONObject  jsonObject = new JSONObject(message.toString());
        String type =  jsonObject.getString("message_type");
        Log.e("测试","测试能否进行直接转换");
        Log.e("测试",jsonObject.toString());
        switch (type) {
            case  "7":
                textcontent = jsonObject.getString("textcontent");
                intent.putExtra("textcontent",textcontent);

                break;
            case "8":
                intent.putExtra("textcontent",message.toString());
                break;

        }


//yuyisummer 进行准备阶段

//        if(type.equals("7")) {
//            Gson gson = new Gson();
//            Protocol protocol = gson.fromJson(message.toString(), Protocol.class);
//            Map<String,Integer> content = (Map<String, Integer>) protocol.getTextcontent();
//            Object object = protocol.getTextcontent();
//            Map<String,String> map = new ObjectToMap().objecttomap(object);
//            Log.e("MyHandler", content.toString());
//            for(Map.Entry entry : content.entrySet()){
//                Log.e("输出key",String.valueOf(entry.getKey()));
//            }
//        }
      //  Log.e("MyHandler",jsonObject.toString());

        if(mContext != null){
            intent.putExtra("message_type", type);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

    }

    @Override
    public void messageSent(IoSession session, Object message)  {
        try {
            super.messageSent(session, message);
        } catch (Exception e) {
            Log.e("MyHandler","发送失败");
            e.printStackTrace();
        }
        Log.e("ceshi","发送成功");
    }
}
