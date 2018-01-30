package com.example.star.imhi.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.star.imhi.DAO.pojo.Friends;
import com.example.star.imhi.R;
import com.example.star.imhi.adapter.QunListAdapter;
import com.example.star.imhi.database.MyDatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by d c on 2018/1/13.
 */


@SuppressLint("ValidFragment")
public class home2Fragment extends android.support.v4.app.Fragment {
    private String grouplist;
    private MyDatabaseHelper dbHelper;
    RecyclerView  recyclerView;
    QunListAdapter adapter;
    List<Friends> group_List = new ArrayList<>();
    @SuppressLint("ValidFragment")
    public home2Fragment(String grouplist){
        this.grouplist = grouplist;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.qun, container, false);
        dbHelper = new MyDatabaseHelper(getActivity(),"FriendsStore.db",null,1);
        dbHelper.getWritableDatabase();


        JSONObject jsonObject = new JSONObject();
        Friends user ;
        try {
            jsonObject = new JSONObject(grouplist);
            System.out.println(grouplist);
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
                group_List.add(user);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) view .findViewById(R.id.qunrecyfriendslist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new QunListAdapter(group_List);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
