package com.example.star.imhi.addfriend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.DefaultUser;
import com.example.star.imhi.DAO.pojo.GroupChat;
import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.Utils.ThisTime;
import com.example.star.imhi.database.MyDatabaseHelper;
import com.example.star.imhi.message.MessageListActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class GroupChatActivity extends AppCompatActivity {

    private TextView groupName;
    private TextView groupNum;
    private TextView groupDate;
    private TextView groupUser;
    private MyDatabaseHelper dbHelper;
    Button btn_send;
    Button btn_del;
    Intent intent;
    GroupChat groupChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        dbHelper = new MyDatabaseHelper(this, "FriendsStore.db", null, 1);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(GroupChatActivity.this);

        setMessage();
    }

    public void setMessage(){
        groupName = (TextView) findViewById(R.id.group_name);
        groupNum = (TextView) findViewById(R.id.group_id_number);
        groupDate = (TextView) findViewById(R.id.group_date);
        groupUser = (TextView) findViewById(R.id.group_user_name);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.e("details","测试是否调用send");
                    Intent intent = new Intent("com.bs.myMsg");
                    intent.putExtra("message_type", "26");
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("type", "2");
                        jsonObject.put("fromwho", groupChat.getGroupId());
                        Cursor cursor;
                        String to = String.valueOf(groupChat.getGroupId());
                        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        String from = preferences.getString("userId", "0");

                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        cursor = db.query("history_message", new String[]{"max(message_id)"}, "(user_from_id=? and to_id=?) or (user_from_id=? and to_id=?) and message_type=? ", new String[]{to, from, from, to, "3"}, null, null, null);
                        cursor.moveToFirst();
                        jsonObject.put("new_id", cursor.getString(
                                cursor.getColumnIndex("max(message_id)")));
                        intent.putExtra("textcontent", jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LocalBroadcastManager.getInstance(GroupChatActivity.this).sendBroadcast(intent);
                    intent = new Intent(v.getContext(), MessageListActivity.class);
                    DefaultUser defaultUser = new DefaultUser(String.valueOf(groupChat.getGroupId()), groupChat.getGroupName()
                            , groupChat.getHeadUrl(), 2);
                    intent.putExtra("user1", defaultUser);
                intent.putExtra("message_type","3");
                    v.getContext().startActivity(intent);
            }
        });
         btn_del = findViewById(R.id.btn_del);
         btn_del.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });
       intent = getIntent();
         groupChat = new Gson().fromJson(intent.getStringExtra("result"), GroupChat.class);
        if (groupChat != null) {
            groupName.setText(groupChat.getGroupName());
            groupNum.setText(groupChat.getGroupId());



            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            String dateTime = df.format(Long.valueOf(groupChat.getCreateDate()));
            groupDate.setText(dateTime);
            groupUser.setText(groupChat.getUserId());
        } else {
            Log.e("TEST", "setMessage: " );
        }

    }
}
