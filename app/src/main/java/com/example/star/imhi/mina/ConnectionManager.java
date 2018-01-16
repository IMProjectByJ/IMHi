package com.example.star.imhi.mina;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

/**
 * Created by 11599 on 2018/1/9.
 */

public class ConnectionManager extends AppCompatActivity {
    private ConnectionConfig config;
    private WeakReference<Context> mContext;
    private IoConnector mConnector;
    private SocketAddress mAddress;
    private static final int heartbeatrate = 5;
    public ConnectionManager(ConnectionConfig config){
        this.config = config;
        mContext = new WeakReference<Context>(config.getContext());
        init();
    }

    private void init() {
        mConnector = new NioSocketConnector();

        mConnector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(
                        new TextLineCodecFactory(
                                Charset.forName("UTF-8"),
                                LineDelimiter.WINDOWS.getValue(),
                                LineDelimiter.WINDOWS.getValue()
                        )
                )
        );
        /*
        KeepAliveMessageFactory heartbeatFactory = new  KeepAliveMessageFactoryImpl();
        //用不到判断超时
        //    KeepAliveRequestTimeoutHandler heartBeatHandler = new  KeepAliveRequestTimeoutHandlerImpl();

        KeepAliveFilter heartBeat = new KeepAliveFilter(
                heartbeatFactory, IdleStatus.BOTH_IDLE
        );
        heartBeat.setRequestInterval(heartbeatrate);
        mConnector.getFilterChain().addLast("heartbeat",heartBeat);
        */
        mConnector.setHandler(new MyHandler(mContext.get()));

        mConnector.getSessionConfig().setReadBufferSize(2048);
        mAddress = new InetSocketAddress(config.getIp(),config.getPort());
        mConnector.setDefaultRemoteAddress(mAddress);
    }
    public boolean connect(){

        try{

            Log.e("连接时","再来一次");
            ConnectFuture connectFuture = mConnector.connect();

            connectFuture.awaitUninterruptibly();

            IoSession mSession = connectFuture.getSession();

            SessionManager.getInstance().setSession(mSession);

            return true;
        } catch (Exception e){
            Log.e("ceshi",e.toString());
            return false;
        }

    }

    public void disConnect() {
        mConnector.dispose(true);
    }
}
