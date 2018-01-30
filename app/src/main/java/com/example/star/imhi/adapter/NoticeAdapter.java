package com.example.star.imhi.adapter;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.imhi.DAO.pojo.Notice_entity;
import com.example.star.imhi.R;
import com.example.star.imhi.database.MyDatabaseHelper;
import com.example.star.imhi.mina.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by d c on 2018/1/19.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private List<Notice_entity> maddfriends;
    private int position;
    private String user_id;
    private String touseid, toname;
    private MyDatabaseHelper dbHelper;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mwho, madd;
        Button agree, refuse;
        View noticeview;

        public ViewHolder(View itemView) {
            super(itemView);
            noticeview = itemView;
            mwho = (TextView) itemView.findViewById(R.id.fromwho);
            agree = (Button) itemView.findViewById(R.id.agree);
            refuse = (Button) itemView.findViewById(R.id.refuse);
            madd = (TextView) itemView.findViewById(R.id.addfriends);
        }
    }

    public NoticeAdapter(List<Notice_entity> addfriends, String user_id) {
        maddfriends = addfriends;
        position = addfriends.size();
        this.user_id = user_id;
    }

    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_child, parent, false);
        final NoticeAdapter.ViewHolder holder = new NoticeAdapter.ViewHolder(view);
        final Button agree = (Button) view.findViewById(R.id.agree);
        final Button refuse = (Button) view.findViewById(R.id.refuse);
        dbHelper = new MyDatabaseHelper(view.getContext(), "FriendsStore.db", null, 1);
        //长按删除
        holder.noticeview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                maddfriends.remove(holder.getLayoutPosition());
                notifyItemRemoved(holder.getLayoutPosition());
                return false;
            }
        });
        //点击同意按钮后的操作
        holder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agree.setText("已同意");
                agree.setBackgroundResource(R.drawable.clickchange);
                agree.setTextColor(Color.WHITE);
                refuse.setBackgroundResource(R.drawable.clickchange);
                agree.setEnabled(false);
                refuse.setEnabled(false);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("message_type", 9);

                db.update("history_message", values, "user_from_id=?", new String[]{touseid});
                //广播发送数据
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(view.getContext());
                Intent intent = new Intent("com.agree.LOCAL_BROADCAST");
                intent.putExtra("touseid", touseid);
                intent.putExtra("toname", toname);
                Log.e("agree1", touseid);
                Log.e("agree2", user_id);
                localBroadcastManager.sendBroadcast(intent);
                Toast.makeText(v.getContext(), "你同意了好友请求", Toast.LENGTH_SHORT).show();
                int weizhi = holder.getAdapterPosition();
                Notice_entity notice_entity = maddfriends.get(weizhi);

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("to", notice_entity.getWhoid());

                    if (notice_entity.getAddfriend().equals("请求添加您为好友")) {
                        jsonObject.put("from", user_id);
                        jsonObject.put("message_type", "9");
                    } else {
                        Log.e("noticeAdapter",notice_entity.getAddfriend());
                        String[] infor=notice_entity.getAddfriend().split("\\s+");
                        jsonObject.put("from", infor[1]);
                        jsonObject.put("message_type", "14");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("NoticeAdapter", jsonObject.toString());
                SessionManager.getInstance().writeMag(jsonObject);

                //   Intent intent = new Intent("com.bs.myMsg");
                //  intent.putExtra("message_type", "20");
                //    LocalBroadcastManager.getInstance().sendBroadcast(intent);

            }
        });
        //点击拒绝按钮
        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refuse.setText("已拒绝");
                refuse.setBackgroundResource(R.drawable.clickchange);
                refuse.setTextColor(Color.WHITE);
                agree.setBackgroundResource(R.drawable.clickchange);
                Toast.makeText(v.getContext(), "你拒绝了好友请求", Toast.LENGTH_SHORT).show();
                refuse.setEnabled(false);
                agree.setEnabled(false);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notice_entity mfriend = maddfriends.get(position);
        holder.mwho.setText(mfriend.getWho());
        touseid = mfriend.getWhoid();
        toname = mfriend.getWho();
        holder.madd.setText(mfriend.getAddfriend());

    }

    @Override
    public int getItemCount() {
        return maddfriends.size();
    }

    public void addItem(String fromwho, String fromid) {
        Log.e("chatlistAdapterr", "引用成功");
        Notice_entity notice_entity = new Notice_entity(fromwho, fromid);
        maddfriends.add(position, notice_entity);
        position++;
        notifyItemInserted(position);
    }
}
