package com.example.star.imhi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.star.imhi.R;

/**
 * Created by d c on 2018/1/12.
 */

public class tab3Fragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private LinearLayout set_1,set_2,set_3;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_setting, container, false);
        set_1 = (LinearLayout) view.findViewById(R.id.set_1);
        set_2 = (LinearLayout) view.findViewById(R.id.set_2);
        set_3 = (LinearLayout) view.findViewById(R.id.set_3);
        set_1.setOnClickListener(this);
        set_2.setOnClickListener(this);
        set_3.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_1:
                break;
            case R.id.set_2:
                break;
            case R.id.set_3:
                break;
            default:
                break;

        }
    }
}
