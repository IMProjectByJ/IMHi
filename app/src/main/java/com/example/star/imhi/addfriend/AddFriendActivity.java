package com.example.star.imhi.addfriend;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.GroupChat;
import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.Utils.FileOperateService;
import com.example.star.imhi.database.MyDatabaseHelper;
import com.example.star.imhi.mina.SessionManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddFriendActivity extends AppCompatActivity {
    User user = null;
    EditText editText;
    ImageView imageView;
    Button btn_addfriend;
    TextView textView;
    private GroupChat groupChat;
    private ImageView headImg;
    private LinearLayout search;
    private SharedPreferences sp;
    Bitmap bitmapHead;
    private MyDatabaseHelper dbHelper;

    private static final String PREFERENCE_NAME = "userInfo";
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private Handler handler = new Handler();
    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            headImg.setImageBitmap((Bitmap)msg.obj);
        }
    };

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            requestServert();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        headImg = (ImageView) findViewById(R.id.search_person_head_img);

        editText = (EditText) findViewById(R.id.search_msg);
        search = (LinearLayout) findViewById(R.id.search);
        sp = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        final String aimPhoneNumber = editText.getText().toString();

        imageView = (ImageView) findViewById(R.id.search_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestServert();
            }
        });

        btn_addfriend = (Button) findViewById(R.id.addfriend);
        btn_addfriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //yuyisummer 且这里不能等于自己，
                SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String user_id = preferences.getString("userId", "1");
                JSONObject json = new JSONObject();
                if (user != null ) {
                    //   Intent intent = new Intent(AddFriendActivity.this, DetailsActivity.class);
                    // intent.putExtra("user", new Gson().toJson(user));
                    // startActivity(intent);

                    try {

                        json.put("from", user_id);
                        json.put("to", user.getUserId());
                        json.put("message_type", "8");
                        SessionManager.getInstance().writeMag(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else  if(groupChat != null){
                    try {
                        json.put("from", user_id);
                        json.put("to",groupChat.getGroupId());
                        json.put("message_type", "11");
                        Date date = new Date();
                        json.put("date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date));
                        SessionManager.getInstance().writeMag(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                    user = null;
                    groupChat = null;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (delayRun != null) {
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                String editString = s.toString();

                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(delayRun, 800);
            }
        });
    }

    public void setSearchMessage(){
    }

    public void requestServert(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String Mobile = editText.getText().toString();
                Request request;
                if(Mobile.length() == 11)
                    request = new Request.Builder().url(getString(R.string.postUrl) +"api/user/add_search_by_number/" + editText.getText().toString()).build();
                else if (Mobile.length() == 4) {
                    request = new Request.Builder().url(getString(R.string.postUrl) +"api/groupchat/findGroup/id/" + editText.getText().toString()).build();
                } else
                    request = new Request.Builder().url(getString(R.string.postUrl) +"api/user/add_search_by_uid/" + editText.getText().toString()).build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e("responseData:", responseData);
                    JSONObject jsonObject = new JSONObject(responseData);

                    if (jsonObject.optString("add_search") != null && !jsonObject.optString("add_search").equals("")) {
                        Gson gson  = new Gson();
                        user = gson.fromJson(jsonObject.getString("add_search"),User.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView = (TextView) findViewById(R.id.search_person);
                                textView.setText(user.getNikname());
                            }
                        });
                    } else if (jsonObject.getString("message") != null && jsonObject.optString("message").equals("查询成功")) {
                        Gson gson = new Gson();
                        Log.e("result:", jsonObject.optString("result"));
                        groupChat = gson.fromJson(jsonObject.optString("result"), GroupChat.class);
                        if (groupChat != null) {
                            Log.e("message:", jsonObject.optString("message"));
                            Log.e("groupChat", groupChat.toString());
                        } else {
                            //Log.e("message:", groupChat.getGroup_id());
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView = (TextView) findViewById(R.id.search_person);
                                textView.setText(groupChat.getGroupName());
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                search.setVisibility(View.GONE);
                            }
                        });
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    search.setVisibility(View.GONE);
                                }
                            });
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        search.setVisibility(View.VISIBLE);
                    }
                });

            }
        }).start();
    }

    public void InsertOwnInfo(User user){
        dbHelper = new MyDatabaseHelper(this, "FriendsStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String userid = "";
        userid = String.valueOf(user.getUserId());
        values.put("user_id",userid );
        values.put("phone_num", user.getPhoneNum());
        values.put("nikname", user.getNikname());
        Cursor cursor;
        cursor = db.query("Friends", new String[]{"user_id"}, "user_id=?", new String[]{String.valueOf(userid)}, null, null, null);
        Log.e("LoginAcitvity", "cursor num" + cursor.getCount());
        if (cursor.getCount() == 0) {
            values.put("user_id", userid);
            long retval = db.insert("Friends", null, values);

            if (retval == -1)
                Log.e("LoginActivity", "failed");
            else
                Log.e("LoginActivity", "success " + retval);
        } else {

            int update = db.update("Friends", values, "user_id=?", new String[]{String.valueOf(userid)});
            Log.e("LoginAcitivity", "ceshi " + update);
        }
    }
}
