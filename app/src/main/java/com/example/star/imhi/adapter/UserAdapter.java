package com.example.star.imhi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Luyue on 2018/1/15.
 */

public class UserAdapter extends ArrayAdapter {
    private int  resourceId;

    public UserAdapter(Context context, int textViewResourceId, List<User> objects){
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = (User)getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.adapter_account_item_delete);
        TextView textView = (TextView) view.findViewById(R.id.adapter_account_item_iphone);
        imageView.setImageResource(R.drawable.close);
        textView.setText(user.getPhoneNum());
        return view;
    }
}
