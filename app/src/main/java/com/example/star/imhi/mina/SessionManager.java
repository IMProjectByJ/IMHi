package com.example.star.imhi.mina;

import android.content.Context;
import android.content.SharedPreferences;
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
            session.write(info);
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
