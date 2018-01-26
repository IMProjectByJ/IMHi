package com.example.star.imhi.mina;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.star.imhi.R;

import java.io.IOException;

/**
 * Created by 11599 on 2018/1/9.
 */

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private  ConnectionManager mManager;
    private  ConnectionThread mConnectThread;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Myservice","开始创建");
        mConnectThread = new ConnectionThread("mina",getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mConnectThread = new ConnectionThread("mina",getApplicationContext());
        mConnectThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConnectThread.disConnect();
        mConnectThread = null;
    }

    class ConnectionThread extends HandlerThread {
        public ConnectionThread(String name,Context context) {
            super(name);
//            ConnectionConfig config = new ConnectionConfig.Builder(context)
//                    .setIp(getString(R.string.socketUrl) )
//                    .setPort(R.string.socketPort)
//                    .setReadBufferSize(2048)
//                    .setConnectionTimeout(10)
//                    .builder();
            ConnectionConfig config = new ConnectionConfig.Builder(context)
                    .setIp("192.168.252.1" )
                    .setPort(8888)
                    .setReadBufferSize(2048)
                    .setConnectionTimeout(10)
                    .builder();
            mManager = new ConnectionManager(config);
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            Log.e("MyService", "开始连接");
            for(int i=  0;i<1;i++){
                if(mManager.connect()){
                    Log.e("MyService","连接成功");
                   // Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_LONG).show();
                    break;
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void disConnect(){
            mManager.disConnect();
        }
    }
}
