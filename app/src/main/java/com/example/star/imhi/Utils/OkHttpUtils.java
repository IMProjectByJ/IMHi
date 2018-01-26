package com.example.star.imhi.Utils;

import android.widget.ProgressBar;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.OkHttpClient;

/**
 * Created by star on 18-1-16.
 */

public class OkHttpUtils {
    private static OkHttpClient client;
    private static  OkHttpClient progClient;
    /**
     * 创建一个OkHttpClient的对象的单例,不带progre监听
     * create by : liuyunxing
     * @return
     */
    public synchronized  static  OkHttpClient getOkHttpClientInstance() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)//设置连接超时
                    .build();
        }
        return client;
    }

    /**
     * 创建一个OkHttpClient的对象的单例,带prog监听
     * create by : liuyunxing
     * @return
     */
    public synchronized  static  OkHttpClient getProgressOkHttpClientInstance() {
        if (progClient == null) {
            progClient = ProgressManager.getInstance().with(
                    new OkHttpClient.Builder()
                            .connectTimeout(120, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS)
                            .writeTimeout(120, TimeUnit.SECONDS)//设置连接超时
            ).build();

            //  client = builder.build();
        }
        return progClient;
    }

    /**
     * 获取文件MimeType
     *
     * @param filename 文件名
     * @return
     */
    public static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }

    private ProgressListener FileProgressListener(final ProgressBar bar, final Handler handler){

        return  new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                int progress  = progressInfo.getPercent();
                bar.setProgress(progress);
            }

            @Override
            public void onError(long id, Exception e) {
                bar.setProgress(0);
            }
        };

    }
}
