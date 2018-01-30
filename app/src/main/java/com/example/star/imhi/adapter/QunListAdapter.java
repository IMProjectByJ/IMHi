package com.example.star.imhi.adapter;

import android.app.DownloadManager;
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
import com.example.star.imhi.DAO.pojo.GroupChat;
import com.example.star.imhi.R;
import com.example.star.imhi.Utils.OkHttpUtils;
import com.example.star.imhi.addfriend.GroupChatActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by d c on 2018/1/21.
 */

public class QunListAdapter extends RecyclerView.Adapter<QunListAdapter.ViewHolder>{
    private List<Friends> mqun;
    int position;
    private String qunid;
    static class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView qunimage;
        TextView qunname;
        View noticeview;
        public ViewHolder (View itemView) {
            super(itemView);
            noticeview=itemView;
            qunimage = (ImageView) itemView.findViewById(R.id.touxiang);
            qunname = (TextView) itemView.findViewById(R.id.friendname);
        }
    }
    public  QunListAdapter(List<Friends> friendlist){
        mqun = friendlist;
        position = mqun.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendslist,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.noticeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Friends mfriend1= mqun.get( holder.getLayoutPosition());
                qunid = mfriend1.getUser_id();
                Toast.makeText(v.getContext(), qunid, Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request request;
                        request = new Request.Builder().url(v.getContext().getString(R.string.postUrl)+"api/groupchat/findGroup/id/" + qunid).build();

                        try {
                            Response response = okHttpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            Log.e("responseData", responseData);
                            JSONObject jsonObject = new JSONObject(responseData);

                            GroupChat groupChat = new Gson().fromJson(jsonObject.optString("result"), GroupChat.class);
                            if (groupChat != null) {
                                Intent intent = new Intent(v.getContext(), GroupChatActivity.class);
                                intent.putExtra("result", jsonObject.optString("result"));
                                v.getContext().startActivity(intent);
                            }

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friends qun= mqun.get(position);
        holder.qunimage.setImageBitmap(qun.getImageid());
        holder.qunname.setText(qun.getName());
        //   holder.friendname.setText(mfriend.getUser_id());

    }
    @Override
    public int getItemCount()
    {
        return mqun.size();
    }
}
