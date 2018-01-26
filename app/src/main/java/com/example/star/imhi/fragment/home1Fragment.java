package com.example.star.imhi.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.star.imhi.DAO.pojo.Friends;
import com.example.star.imhi.R;
import com.example.star.imhi.adapter.FriendsListAdapter;
import com.example.star.imhi.database.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by d c on 2018/1/13.
 */

public class home1Fragment extends android.support.v4.app.Fragment {
    private MyDatabaseHelper dbHelper;
    RecyclerView  recyclerView;
    FriendsListAdapter adapter;
    Map<String,Integer> content;


    public home1Fragment(){}
    @SuppressLint("ValidFragment")
    public home1Fragment(Map<String,Integer> content){
        this.content = content;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //yuyisummer


        dbHelper = new MyDatabaseHelper(getActivity(),"FriendsStore.db",null,1);
        dbHelper.getWritableDatabase();


        View view=inflater.inflate(R.layout.haoyou, container, false);
        List<Friends> friends_List = new ArrayList<>();
        recyclerView = (RecyclerView) view .findViewById(R.id.haoyourecyfriendslist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FriendsListAdapter(friends_List);
       // Fri
        // ends user = new Friends("张三",R.drawable.smile);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(Map.Entry entry : content.entrySet()){
            Friends user = new Friends();
            String user_id = String.valueOf(entry.getKey());
            user.setUser_id(user_id);
            Cursor cursor;
            cursor = db.query("Friends",null,"user_id=?", new String[]{user_id},null,null,null);
            cursor.moveToFirst();
            cursor.getString(cursor.getColumnIndex("nikname"));
            user.setName(cursor.getString(cursor.getColumnIndex("nikname")));


            friends_List.add(user);
            Log.e("输出key",String.valueOf(entry.getKey()));
        }
      //  Friends user = new Friends("10001","小小小小郁",0);
       // friends_List.add(user);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
