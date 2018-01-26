package com.example.star.imhi.mina;

import android.util.Log;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

/**
 * Created by 11599 on 2018/1/9.
 */

public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {
    private static  int HEARTBEATREQUEST = 0x11 ;
    private  static int HEARTBEATRESPONSE = 0x12 ;
    String TAG = "KeepAliveMessageFactoryImpl";
    @Override
    //客户端主动发送心跳
    //判断是否是请求信息
    public boolean isRequest(IoSession ioSession, Object o) {

        if(o.toString().equals(String.valueOf(HEARTBEATREQUEST)))
        {
            Log.e(TAG,"是请求包");
            return true;
        }
        System.out.println("不是请求包");

        return false;
    }

    @Override
    //判断是否是响应信息
    public boolean isResponse(IoSession ioSession, Object o) {
        if(o.toString().equals(String.valueOf(HEARTBEATRESPONSE)))
        {
            System.out.println("是响应包");
            return true;
        }
        System.out.println("不是响应包");
        return false;
    }

    @Override
    //准备发送的信息
    public Object getRequest(IoSession ioSession) {
        System.out.println("请求预设信息"+HEARTBEATREQUEST );
        return HEARTBEATREQUEST ;
    }

    @Override
    public Object getResponse(IoSession ioSession, Object o) {
        return null;
    }
}
