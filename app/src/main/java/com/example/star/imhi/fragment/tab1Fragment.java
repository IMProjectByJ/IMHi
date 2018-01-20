package com.example.star.imhi.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.star.imhi.DAO.pojo.ChatList;
import com.example.star.imhi.R;
import com.example.star.imhi.adapter.ChatListAdapterr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d c on 2018/1/12.
 */


@SuppressLint("ValidFragment")
public class tab1Fragment  extends android.support.v4.app.Fragment implements View.OnClickListener{
    RecyclerView recyclerView;
    ChatListAdapterr adapter ;
   // List<ChatList> chat_List = new ArrayList<>();
   List<ChatList> chat_List;
    String fromwhoh,what;
    LinearLayoutManager layoutManager;
    @SuppressLint("ValidFragment")
    public tab1Fragment(List<ChatList> chat_List){
            this.chat_List = chat_List;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message, container, false);

        recyclerView = (RecyclerView) view .findViewById(R.id.messagerecylerView);
         layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatListAdapterr(chat_List);
//        ChatList user = new ChatList("张三","hello");
//        chat_List.add(user);
//        recyclerView.setAdapter(adapter);
     //   addlist(fromwhoh,what);
        recyclerView.setAdapter(adapter);

        return view;
    }
    /*
    public void addlist(String fromwhoh,String what){
        ChatList user = new ChatList(fromwhoh,what);
        chat_List.add(user);
        recyclerView.setAdapter(adapter);

    }
    */
    @Override
    public void onClick(View v) {
    }
    public ChatListAdapterr getAdapter(){
        return  this.adapter;
    }

}
