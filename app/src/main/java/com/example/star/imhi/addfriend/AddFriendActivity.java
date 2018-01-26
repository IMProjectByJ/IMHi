package com.example.star.imhi.addfriend;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.database.MyDatabaseHelper;
import com.example.star.imhi.mina.SessionManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddFriendActivity extends AppCompatActivity {
    User user = null;
    EditText editText;
    ImageView imageView;
    Button btn_addfriend;
    TextView textView;
    private MyDatabaseHelper dbHelper;

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

        btn_addfriend = (Button) findViewById(R.id.addfriend);
        btn_addfriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //yuyisummer 且这里不能等于自己，
                if (user != null ) {
                 //   Intent intent = new Intent(AddFriendActivity.this, DetailsActivity.class);
                   // intent.putExtra("user", new Gson().toJson(user));
                   // startActivity(intent);
                    JSONObject json = new JSONObject();
                    try {
                        SharedPreferences preferences=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        String user_id=preferences.getString("userId","1");
                        json.put("from",user_id);
                        json.put("to",user.getUserId());
                        json.put("message_type","8");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SessionManager.getInstance().writeMag(json);
                }
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
                else
                    request = new Request.Builder().url(getString(R.string.postUrl) +"api/user/add_search_by_uid/" + editText.getText().toString()).build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("luyue", editText.getText().toString());
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
