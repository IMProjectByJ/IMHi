package com.example.star.imhi.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.Friends;
import com.example.star.imhi.R;

import java.util.List;

/**
 * Created by d c on 2018/1/9.
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {
    private List<Friends> mfriends;
    static class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView friendimage;
        TextView friendname;

        public ViewHolder (View itemView) {
            super(itemView);
            friendimage = (ImageView) itemView.findViewById(R.id.touxiang);
            friendname = (TextView) itemView.findViewById(R.id.friendname);
        }
    }
    public  FriendsListAdapter(List<Friends> friendlist){
        mfriends = friendlist;
    }
    @Override
    public FriendsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendslist,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendsListAdapter.ViewHolder holder, int position) {
        Friends mfriend= mfriends.get(position);
      //  holder.friendimage.setImageResource(mfriend.getImageid());
        holder.friendname.setText(mfriend.getName());

    }
    @Override
    public int getItemCount()
    {
        return mfriends.size();
    }
}
