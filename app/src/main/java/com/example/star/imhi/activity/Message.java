package com.example.star.imhi.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.example.star.imhi.BaseActivity;
import com.example.star.imhi.R;

public class Message extends BaseActivity implements View.OnClickListener{
    private LinearLayout chat,friends,setting;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        chat =(LinearLayout) findViewById(R.id.llChat);
//        friends =(LinearLayout) findViewById(R.id.llFriends);
//        setting =(LinearLayout) findViewById(R.id.llContacts);
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        chat.setOnClickListener(this);
//        friends.setOnClickListener(this);
//        setting.setOnClickListener(this);
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.daohang);
//        }
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.llChat:
//                break;
//            case R.id.llFriends:
//                Intent intent = new Intent(Message.this,HomePage.class);
//                startActivity(intent);
//                Message.this.overridePendingTransition(0, 0);
//                finish();
//                break;
//            case R.id.llContacts:
//                Intent intent2 = new Intent(Message.this,Setting.class);
//                startActivity(intent2);
//                Message.this.overridePendingTransition(0, 0);
//                finish();
//                break;
//        }
    }
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.toolbar,menu);
//        return true;
//    }
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.add:
//                Toast.makeText(Message.this,"you click here",Toast.LENGTH_SHORT).show();
//                break;
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                break;
//            default:
//        }
//        return true;
//    }
}
