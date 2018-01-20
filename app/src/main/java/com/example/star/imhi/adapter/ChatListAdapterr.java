package com.example.star.imhi.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.ChatList;
import com.example.star.imhi.R;
import com.example.star.imhi.database.MyDatabaseHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by d c on 2018/1/15.
 */

public class ChatListAdapterr extends RecyclerView.Adapter<ChatListAdapterr.ViewHolder> {
    private List<ChatList> mchatlist;
    int position;
    static class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView chatlistimage;
        TextView chatlistwho;
        TextView chatlistwhat;
        public ViewHolder (View itemView) {
            super(itemView);
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
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatList nchatList= mchatlist.get(position);
       // holder.chatlistimage.setImageResource(nchatList.getImageid());
        holder.chatlistwho.setText(nchatList.getFromwho());
        holder.chatlistwhat.setText(nchatList.getWhatcontext());

    }
    @Override
    public int getItemCount()
    {
        return mchatlist.size();
    }
    public void addItem(String fromwho,int type,String whatcontext,Integer old_id,String nikname) {
        Log.e("chatlistAdapterr","引用成功");
        ChatList list = new ChatList(fromwho,type,whatcontext,old_id,nikname);
        mchatlist.add(position,list);

        position++;
        notifyItemInserted(position);
    }

}
