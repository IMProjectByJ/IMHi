package com.example.star.imhi.addfriend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.Utils.FileOperateService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {

    //    private TextView t_name;
//    private TextView t_id_number;
//    private TextView t_tel_number;
//      private TextView t_more;
//    private ImageView sex;
    private User user;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private ImageView headImg;


    Intent intent_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setMessage();
        TextView t_more = (TextView) findViewById(R.id.more);
        TextView t_set_notes = (TextView) findViewById(R.id.set_notes);
        Button btn_back = (Button) findViewById(R.id.back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        t_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, MoreActivity.class);
                Gson gson = new Gson();
                User user = gson.fromJson(intent_message.getStringExtra("user"), User.class);
                intent.putExtra("user", gson.toJson(user));
                startActivity(intent);
            }
        });

        t_set_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, RemarksActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setButtonAction() {
        Button btn_send = (Button) findViewById(R.id.btn_send);
        Button btn_del = (Button) findViewById(R.id.btn_del);

    }

    private void setMessage() {

        //setHeadImage("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg");

        intent_message = getIntent();
        Gson gson = new Gson();
        user = gson.fromJson(intent_message.getStringExtra("person_user"), User.class);
        TextView t_name = (TextView) findViewById(R.id.name);
        TextView t_id_number = (TextView) findViewById(R.id.id_number);
        TextView t_tel_number = (TextView) findViewById(R.id.tel_number);
        ImageView sex = (ImageView) findViewById(R.id.sex);
        headImg = (ImageView)findViewById(R.id.head_img);

        t_name.setText(user.getNikname());
        t_id_number.setText(user.getUserId().toString());
        t_tel_number.setText(user.getPhoneNum());
        if (user.getGender() != null) {
            if (user.getGender().equals("男")) {
                sex.setImageResource(R.drawable.man);
            } else {
                sex.setImageResource(R.drawable.woman);
            }
        }
        getHeadImg(user.getUserId().toString());
    }

    public void getHeadImg(final String uid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request;
                request = new Request.Builder().url(getString(R.string.postUrl) +"api/user/add_search_by_uid/" + uid).build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e("responseData:", responseData);
                    JSONObject jsonObject = new JSONObject(responseData);

                    if (jsonObject.optString("add_search") != null && !jsonObject.optString("add_search").equals("")) {
                        Gson gson  = new Gson();
                        user = gson.fromJson(jsonObject.getString("add_search"),User.class);

                    } else {
                        return;
                    }


                    //搜索好友头像
                    RequestBody requestBody = RequestBody.create(JSON, jsonObject.getString("add_search"));
                    Request request1 = new Request.Builder()
                            .url(getString(R.string.postUrl) + "api/imageOperate/headDownload")
                            .post(requestBody)
                            .build();
                    //发送请求获取响应
                    try {
                        Response response1 = client.newCall(request1).execute();
                        //判断请求是否成功
                        if(response1.isSuccessful()) {
                            //打印服务端返回结果
                            Log.e("Success", "success!!!!!!!!!");
                            final Bitmap bitmap = FileOperateService.imgdecode(response1);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    headImg.setImageBitmap(bitmap);
                                }
                            });


                        } else {
                            return;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }).start();
    }
}
