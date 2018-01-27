package com.example.star.imhi.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.star.imhi.DAO.pojo.Notice_entity;
import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.adapter.NoticeAdapter;
import com.example.star.imhi.database.MyDatabaseHelper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Notice extends AppCompatActivity implements View.OnClickListener {
    private List<Notice_entity> noticeList = new ArrayList<>();
    NoticeAdapter adapter;
    private MyDatabaseHelper dbHelper;
    String nikname;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        LinearLayout noticeback = (LinearLayout) findViewById(R.id.noticeback);
        noticeback.setOnClickListener(Notice.this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.noticerecylerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String user_id = preferences.getString("userId", "0");
        adapter = new NoticeAdapter(noticeList,user_id);

        dbHelper = new MyDatabaseHelper(this, "FriendsStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;



        cursor = db.query("history_message", new String[]{"*"},
                "to_id=?  and message_type=? ",
                new String[]{user_id,"8"}, null, null, null);


        if (cursor!=null && cursor.moveToFirst() ) {
            do{
                String fromid = cursor.getString(cursor.getColumnIndex("user_from_id"));
                String fromwho = FindNikname(fromid);
                Notice_entity user1 = new Notice_entity(fromwho,fromid);
                noticeList.add(user1);
            }while (cursor .moveToNext());
        }


        // Intent intent=getIntent();
       // String fromwho=intent.getStringExtra("from");
        //Notice_entity user1 = new Notice_entity();
        //noticeList.add(user1);
        recyclerView.setAdapter(adapter);

    }

    public String FindNikname(final String user_id) {
        //  dbHelper = new MyDatabaseHelper(this, "FriendsStore.db", null, 1);
        SQLiteDatabase db;
        db = dbHelper.getReadableDatabase();
        Cursor cursor;
        cursor = db.query("Friends", new String[]{"nikname"},
                "user_id=?", new String[]{String.valueOf(user_id)},
                null, null, null);
        Log.e("findnikname", String.valueOf(cursor.getCount()));
        cursor.moveToFirst();

        if(cursor.getCount() == 0){
            userid =user_id;
         Thread thread =  new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    Request request;
                        request = new Request.Builder().url(getString(R.string.postUrl) +"api/user/add_search_by_uid/" + user_id).build();
                    try {
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        if (jsonObject.optString("err") == "") {
                            Gson gson  = new Gson();
                            User user = gson.fromJson(jsonObject.getString("add_search"),User.class);
                            nikname = user.getNikname();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else{
            nikname = cursor.getString(cursor.getColumnIndex("nikname"));
        }
        return nikname;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.noticeback:
                finish();
                break;
        }

    }
}
