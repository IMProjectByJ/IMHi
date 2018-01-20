package com.example.star.imhi.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by d c on 2018/1/13.
 */

@SuppressLint("ValidFragment")
public class home1Fragment extends android.support.v4.app.Fragment {
    private MyDatabaseHelper dbHelper;
    RecyclerView  recyclerView;
    FriendsListAdapter adapter;
    Map<String,Object> content;
    String friendlist;

/*
    public home1Fragment(){}
*/

    @SuppressLint("ValidFragment")
    public home1Fragment(String friendlist){
        this.friendlist = friendlist;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //yuyisummer
        List<Friends> friends_List = new ArrayList<>();

        dbHelper = new MyDatabaseHelper(getActivity(),"FriendsStore.db",null,1);
        dbHelper.getWritableDatabase();


        View view=inflater.inflate(R.layout.haoyou, container, false);
       JSONObject jsonObject = new JSONObject();
        Friends user ;
        try {
            jsonObject = new JSONObject(friendlist);
            System.out.println(friendlist);
            Iterator<String> keyIter= jsonObject.keys();
            String key;
            Object value ;
           // Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
            //先注释掉，后面可能还是需要
                //    valueMap.put(key, value);
                 user = new Friends();
                 Log.e("home1Fragment",key + "      "+value);
                user.setUser_id(key);
                user.setName(String.valueOf(value));
                friends_List.add(user);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }





/*
        for(Map.Entry entry : content.entrySet()) {
            Friends user = new Friends();
            String user_id = String.valueOf(entry.getKey());
            user.setUser_id(user_id);
            user.setName(content.get(user_id).toString());
            //这里需要去服务器取数据，有多少条信息没读
            //user.setOffline_msg();
            friends_List.add(user);
        }
*/
        recyclerView = (RecyclerView) view .findViewById(R.id.haoyourecyfriendslist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FriendsListAdapter(friends_List);
       // Fri
        // ends user = new Friends("张三",R.drawable.smile);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        /*
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
        */
      //  Friends user = new Friends("10001","小小小小郁",0);
       // friends_List.add(user);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
