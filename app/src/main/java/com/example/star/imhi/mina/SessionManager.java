package com.example.star.imhi.mina;

import android.util.Log;

import org.apache.mina.core.session.IoSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 11599 on 2018/1/9.
 */

public class SessionManager {
    private  static SessionManager instance = new SessionManager();
    private  SessionManager(){}
    public static SessionManager getInstance() {
        return instance;
    }
    private IoSession session;
    public IoSession getSession(){
        return session;
    }
    public void setSession(IoSession session) {
        this.session = session;
    }
    public void writeMag(JSONObject info)  {
        Log.e("SessionManager","准备发送");
        if(session !=null){
           // JSONArray jsonArray = new JSONArray();
            session.write(info);
           // session.write(jsonArray);
            //测试添加好友
            /*
            JSONObject info1 = new JSONObject();
            try {
                info1.put("from","10001");
                info1.put("to","10002");
                info1.put("type","8");
                info1.put("texttype","1");
                info1.put("textcontent","你好");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            session.write(info1);
            */
        } else
            Log.e("SessionManager","session is null");
    }


    public void closeSession(){
        if(session !=null){
            session.close(true);
            session = null;
        }
    }
}
