package com.example.star.imhi.Utils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class FileOperateService {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static Request uploadFileRequest(String fileName, String filePath,Integer toId, Integer messageType,String url, String token)
    {
        File file = new File(filePath);
        RequestBody fileBody = RequestBody.create(MediaType.parse(OkHttpUtils.getMimeType(file.getName())), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("file",fileName,fileBody)
                .setType(MultipartBody.FORM)
                .addFormDataPart("toId",toId+"")
                .addFormDataPart("messageType", messageType+"")
                .build();
        Request request =  new Request.Builder()
             //   .header("token",token)// 获得token
                .header("token",token)
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }

    public static Request DownloadFileRequest( Integer Message_id, String url, String token)
    {
        RequestBody fileBody  = RequestBody.create(JSON, Message_id+"");
        Request request = new Request.Builder()
                .post(fileBody)
                .header("token",token)
                .url(url)
                .build();
        return  request;
    }

    public static File saveFile(Response response,String fileName, String filePath){

        ResponseBody body = response.body();
        BufferedSource source = body.source();
        File file = new FileUtils()
                .createFileInSDCard(filePath,fileName);// 创建了文件
        try {
            /*
              第一次登录这里会出现问题
             */
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(source);
            sink.flush();
            return  file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

}
