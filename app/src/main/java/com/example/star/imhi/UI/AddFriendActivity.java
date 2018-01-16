package com.example.star.imhi.UI;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddFriendActivity extends AppCompatActivity {
    User user = null;
    EditText editText;
    ImageView imageView;
    Button btn_details;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        editText = (EditText) findViewById(R.id.search_msg);
        final String aimPhoneNumber = editText.getText().toString();

        imageView = (ImageView) findViewById(R.id.search_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestServert();
            }
        });

        btn_details = (Button) findViewById(R.id.details);
        btn_details.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (user != null) {
                    Intent intent = new Intent(AddFriendActivity.this, DetailsActivity.class);
                    intent.putExtra("user", new Gson().toJson(user));
                    startActivity(intent);
                }
            }
        });

        List<User> list = new ArrayList<>();
        Friend_list(10000, list);

    }

    public void Friend_list(final int userId, final List<User> list_user){
        //好友列表功能：测试
        Button btn_friend = (Button) findViewById(R.id.friend);
        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url("http://172.20.10.10:8080/api/user/friend_list/" + userId).build();
                        try {
                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            Log.e("responseData:", responseData);
                            JSONObject jsonObject = new JSONObject(responseData);

                            Gson gson = new Gson();
                            Type type = new TypeToken<List<User>>() {
                            }.getType();
                            List<User> list = gson.fromJson(jsonObject.optString("friend_list"), type);

                            for (User u: list) {
                                Log.e("user:", u.toString());
                                list_user.add(u);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    public void setSearchMessage(){
    }

    public void requestServert(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OkHttpClient client = new OkHttpClient().newBuilder().build();
                String Mobile = editText.getText().toString();
                Request request;

                try {
                    if (Mobile.length() == 11)
                         request = new Request.Builder().get().url("http://172.20.10.10:8080/api/user/add_search_by_number/" + editText.getText().toString()).build();
                    else
                         request = new Request.Builder().get().url("http://172.20.10.10:8080/api/user/add_search_by_uid/" + editText.getText().toString()).build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    Log.e("responseData:", responseData);
                    JSONObject jsonObject = new JSONObject(responseData);

                    if (jsonObject.optString("err") == "") {
                        Gson gson  = new Gson();
                        user = gson.fromJson(jsonObject.getString("add_search"),User.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView = (TextView) findViewById(R.id.search_person);
                                textView.setText(user.getNikname());
                            }
                        });
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
