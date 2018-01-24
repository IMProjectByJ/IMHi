package com.example.star.imhi.adapter;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.ChatList;
import com.example.star.imhi.R;
import com.example.star.imhi.activity.Notice;
import com.example.star.imhi.database.MyDatabaseHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by d c on 2018/1/15.
 */



public class ChatListAdapterr extends RecyclerView.Adapter<ChatListAdapterr.ViewHolder> {
    private List<ChatList> mchatlist;
    Map<String,Integer>map = new HashMap<>();
    int position;
    static class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView chatlistimage;
        TextView chatlistwho;
        TextView chatlistwhat;
        View clickview;

        public ViewHolder (View itemView) {
            super(itemView);
            clickview = itemView;
           // chatlistimage = (ImageView) itemView.findViewById(R.id.touxiang);
            chatlistwho = (TextView) itemView.findViewById(R.id.fromwho);
            chatlistwhat = (TextView) itemView.findViewById(R.id.whatcontext);
        }
    }
    public  ChatListAdapterr(List<ChatList> chatList){
        mchatlist = chatList;
        position = mchatlist.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.clickview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ChatList chatList = mchatlist.get(position);
                if (chatList.getType() == 3){
                    Intent intent =new Intent(v.getContext(), Notice.class);
                    v.getContext().startActivity(intent);
                }
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatList nchatList= mchatlist.get(position);
       // holder.chatlistimage.setImageResource(nchatList.getImageid());
        holder.chatlistwho.setText(nchatList.getFromwho());
        holder.chatlistwhat.setText(nchatList.getWhatcontext()+"条信息未处理");

    }
    @Override
    public int getItemCount()
    {
        return mchatlist.size();
    }
    public void addItem(String fromwho,int type,String whatcontext,String nikname) {
        Log.e("chatlistAdapterr","引用成功");
        String key = fromwho+"|"+type;
        ChatList list = new ChatList(fromwho,type,whatcontext,nikname);
        Integer weizhi = map.get(key);
        if(weizhi == null) {
            mchatlist.add(position, list);
            map.put(key,position);
            position++;
            notifyItemInserted(position);
        } else {
            int n = Integer.parseInt(mchatlist.get(weizhi).getWhatcontext());
            n = n+Integer.valueOf(whatcontext);
            mchatlist.get(weizhi).setWhatcontext(String.valueOf(n));
            notifyItemChanged(weizhi);
        }
    }
}
