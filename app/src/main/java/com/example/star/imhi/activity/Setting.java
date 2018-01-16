package com.example.star.imhi.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.star.imhi.BaseActivity;
import com.example.star.imhi.R;

public class Setting extends BaseActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private TextView textView;
    private LinearLayout chat,friends,setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        chat =(LinearLayout) findViewById(R.id.llChat);
//        friends =(LinearLayout) findViewById(R.id.llFriends);
//        setting =(LinearLayout) findViewById(R.id.llContacts);
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//
//
//        textView = (TextView) findViewById(R.id.tage);
//        textView.setOnClickListener(this);
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
//                Intent intent = new Intent(Setting.this,Message.class);
//                startActivity(intent);
//                Setting.this.overridePendingTransition(0, 0);
//                finish();
//                break;
//            case R.id.llFriends:
//                Intent intent2 = new Intent(Setting.this,HomePage.class);
//                startActivity(intent2);
//                Setting.this.overridePendingTransition(0, 0);
//                finish();
//                break;
//            case R.id.llContacts:
//                break;
//            case R.id.tage:
//                textView.setText("hello");
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
//                break;
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                break;
//            default:
//        }
//        return true;
//    }
}
