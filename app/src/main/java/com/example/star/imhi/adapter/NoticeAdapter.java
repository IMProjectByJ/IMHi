package com.example.star.imhi.adapter;

import android.graphics.Color;
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
import com.example.star.imhi.mina.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by d c on 2018/1/19.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private List<Notice_entity> maddfriends;
    int position;
    String user_id;

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
//            madd = (TextView) itemView.findViewById(R.id.addfriends);
        }
    }

    public NoticeAdapter(List<Notice_entity> addfriends, String user_id) {
        maddfriends = addfriends;
        position = addfriends.size();
        this.user_id = user_id;
    }

    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_child, parent, false);
        final NoticeAdapter.ViewHolder holder = new NoticeAdapter.ViewHolder(view);
        holder.noticeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //点击同意按钮后的操作
        holder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button agree = (Button) v.findViewById(R.id.agree);
                agree.setText("已同意");
                agree.setBackgroundColor(Color.GRAY);
                Toast.makeText(v.getContext(), "你同意了好友请求", Toast.LENGTH_SHORT).show();
                int weizhi = holder.getAdapterPosition();
                Notice_entity notice_entity = maddfriends.get(weizhi);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("from",user_id );
                    jsonObject.put("message_type", "9");
                    jsonObject.put("to",notice_entity.getWhoid());

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
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notice_entity mfriend = maddfriends.get(position);
        holder.mwho.setText(mfriend.getWho());
//        holder.madd.setText(mfriend.getAddfriend());

    }

    @Override
    public int getItemCount() {
        return maddfriends.size();
    }

    public void addItem(String fromwho,String fromid) {
        Log.e("chatlistAdapterr", "引用成功");
        Notice_entity notice_entity = new Notice_entity(fromwho,fromid);
        maddfriends.add(position, notice_entity);
        position++;
        notifyItemInserted(position);

    }
}
