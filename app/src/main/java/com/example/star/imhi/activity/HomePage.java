package com.example.star.imhi.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.star.imhi.BaseActivity;
import com.example.star.imhi.DAO.pojo.Friends;
import com.example.star.imhi.R;
import com.example.star.imhi.adapter.FriendsListAdapter;
import com.example.star.imhi.database.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends BaseActivity implements View.OnClickListener{
    private LinearLayout chat,friends,setting;
    private DrawerLayout drawerLayout;
    List<Friends> friends_List = new ArrayList<>();
    private MyDatabaseHelper dbHelper;
    RecyclerView  recyclerView;
    FriendsListAdapter adapter ,adapter1;
    SQLiteDatabase db;
    //RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
//     //   List<Friends> friends_List = new ArrayList<>();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        recyclerView = (RecyclerView) findViewById(R.id.recyfriendslist);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new FriendsListAdapter(friends_List);
//    //创建数据库
//        dbHelper = new MyDatabaseHelper(this,"FriendsStore.db",null,2);
//        db = dbHelper.getWritableDatabase();
//   //     db.execSQL("drop table Friends");
////        ContentValues values = new ContentValues();
////        values.clear();
////        values.put("id",222);
////        values.put("dname","李四");
////        values.put("dtouxiang",R.drawable.left2);
////        db.insert("Friends",null,values);
////        values.clear();
//
//       //查询表中所有的数据
//         Cursor cursor = db.query("Friends",null,null,null,null,null,null);
//        if (cursor.moveToFirst()){
//            do {
//                //遍历Cursor对象，取出数据
//                int id = cursor.getInt(cursor.getColumnIndex("id"));
//                String dname = cursor.getString(cursor.getColumnIndex("dname"));
//                int dtouxiang = cursor.getInt(cursor.getColumnIndex("dtouxiang"));
//
//                Friends user = new Friends(dname,dtouxiang);
//                friends_List.add(user);
//                recyclerView.setAdapter(adapter);
//            }while (cursor.moveToNext());
//        }
//        cursor.close();
//        chat =(LinearLayout) findViewById(R.id.llChat);
//        friends =(LinearLayout) findViewById(R.id.llFriends);
//        setting =(LinearLayout) findViewById(R.id.llContacts);
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        chat.setOnClickListener(this);
//        friends.setOnClickListener(this);
//        setting.setOnClickListener(this);
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.daohang);
//        }

    //    navview.setCheckedItem(R.id.);
    }
//    private void initFriend(){
//        Friends user = new Friends("张三",R.drawable.smile);
//        friends_List.add(user);
//    }
    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.llChat:
//                Intent intent = new Intent(HomePage.this,Message.class);
//                startActivity(intent);
//                HomePage.this.overridePendingTransition(0, 0);
//                finish();
//                break;
//            case R.id.llFriends:
//                break;
//            case R.id.llContacts:
//                Intent intent2 = new Intent(HomePage.this,Setting.class);
//                startActivity(intent2);
//                HomePage.this.overridePendingTransition(0, 0);
//                finish();
//                break;
//        }
    }
//    @SuppressLint("RestrictedApi")
//    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
//        if (menu != null) {
//            if (menu.getClass() == MenuBuilder.class) {
//                try {
//                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
//                    m.setAccessible(true);
//                    m.invoke(menu, true);
//                } catch (Exception e) {
//                }
//            }
//        }
//        return super.onPrepareOptionsPanel(view, menu);
//    }
//
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.toolbar,menu);
//        return true;
//    }
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.add:
////                List<Friends> friends_List2 = new ArrayList<>();
////                adapter = new FriendsListAdapter(friends_List2);
//                ContentValues values = new ContentValues();
//                values.clear();
//                values.put("user_id",333);
//                values.put("phone_num","666666666");
//                values.put("nikname","张三");
//                values.put("head_url",R.drawable.left1);
//                values.put("age",20);
//                values.put("gender","男");
//                values.put("birth","2000-1-1");
//                values.put("motto","helloworld");
//
//
//                db.insert("Friends",null,values);
//                forlist();
//                break;
//            case  R.id.del:
//                db.delete("Friends",null,null);
//                forlist();
//                Intent intent = new Intent(HomePage.this,HomePage.class);
//                startActivity(intent);
//                HomePage.this.overridePendingTransition(0, 0);
//                finish();
//                break;
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                break;
//            default:
//        }
//        return true;
//    }
//    public void forlist()
//    {
//        List<Friends> friends_List1 = new ArrayList<>();
//        adapter = new FriendsListAdapter(friends_List1);
//        Cursor cursor = db.query("Friends",null,null,null,null,null,null);
//        if (cursor.moveToFirst()){
//            do {
//                //遍历Cursor对象，取出数据
//                int id = cursor.getInt(cursor.getColumnIndex("user_id"));
//                String dname = cursor.getString(cursor.getColumnIndex("nikname"));
//                int dtouxiang = cursor.getInt(cursor.getColumnIndex("head_url"));
//                Friends user = new Friends(dname,dtouxiang);
//                friends_List1.add(user);
//                recyclerView.setAdapter(adapter);
//            }while (cursor.moveToNext());
//        }
//        cursor.close();
//    }
}
