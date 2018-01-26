package com.example.star.imhi.adapter;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.imhi.DAO.pojo.Friends;
import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.addfriend.DetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by d c on 2018/1/9.
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {
    private List<Friends> mfriends;
    int position;
    private String friendid;
    static class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView friendimage;
        TextView friendname;
        View noticeview;

        public ViewHolder (View itemView) {
            super(itemView);
            noticeview=itemView;
            friendimage = (ImageView) itemView.findViewById(R.id.touxiang);
            friendname = (TextView) itemView.findViewById(R.id.friendname);
        }
    }
    public  FriendsListAdapter(List<Friends> friendlist){
        mfriends = friendlist;
        position = mfriends.size();
    }
    @Override
    public FriendsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendslist,parent,false);
        ViewHolder holder = new ViewHolder(view);

        /*----------------------------------好友列表点击事件-------------------------------*/

        holder.noticeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(v.getContext(), friendid, Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request;
                        request = new Request.Builder().get().url(v.getContext().getString(R.string.postUrl) + "api/user/details/" + friendid).build();
                        User user1;
                        try {
                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();

                            Log.e("responseData:", responseData);
                            JSONObject jsonObject = new JSONObject(responseData);

                            if (jsonObject.optString("err") == "") {
//                                Gson gson  = new Gson();
//                                user1 = gson.fromJson(responseData,User.class);
//                                Log.e("details:",user1.toString() );
                                Intent intent1 = new Intent(v.getContext(), DetailsActivity.class);
                                intent1.putExtra("person_user",responseData);
                                v.getContext().startActivity(intent1);
                            } else {
                                Log.e("responseData:", "error" );
                            }

//                            Intent intent1 = new Intent(v.getContext(), DetailsActivity.class);
//                            Gson gson = new Gson();
//                            intent1.putExtra("person_user",gson.toJson(user1) );
//                            Log.e("intent1:", gson.toJson(user1).toString() );
//                            v.getContext().startActivity(intent1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendsListAdapter.ViewHolder holder, int position) {
        Friends mfriend= mfriends.get(position);
        holder.friendimage.setImageBitmap(mfriend.getImageid());
        holder.friendname.setText(mfriend.getName());
        friendid=mfriend.getName();

    }
    @Override
    public int getItemCount()
    {
        return mfriends.size();
    }
    //yuyisummer
    public void addItem(String user_id,String name){
        Log.e("FriendsListAdapterr","引用成功");
        Friends user  = new Friends();
        user.setUser_id(user_id);
        user.setName(name);
        mfriends.add(position,user);
        position++;
        notifyItemInserted(position);
    }


}
