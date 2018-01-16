package com.example.star.imhi.mina;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 11599 on 2018/1/9.
 */

public class ConnectionConfig extends AppCompatActivity {
    private Context context;
    private String ip;
    private int readBufferSize;
    private long connectionTimeout;
    private int port;

    public Context getContext() {
        return context;
    }

    public String getIp() {
        return ip;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getPort() {
        return port;
    }

    public static class Builder {
        private Context context;
        private String ip;
        private int port;
        private int readBufferSize;
        private long connectionTimeout;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setReadBufferSize(int readBufferSize) {
            this.readBufferSize = readBufferSize;
            return this;
        }

        public Builder setConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public ConnectionConfig builder() {

            ConnectionConfig config = new ConnectionConfig();
            config.context = this.context;
            config.ip = this.ip;
            config.port = this.port;
            config.readBufferSize = this.readBufferSize;
            config.connectionTimeout = this.connectionTimeout;
            return config;
        }
    }
}
