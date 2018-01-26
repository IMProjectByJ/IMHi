package com.example.star.imhi.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.star.imhi.R;
import com.example.star.imhi.addfriend.*;

import java.util.Map;

/**
 * Created by d c on 2018/1/12.
 */

public class tab2Fragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    private RadioGroup hRadioGroup;
    private RadioButton mRadio01;
    private RadioButton mRadio02;
    private Fragment[] mFragments;
    private LinearLayout newfriends;
    private int mIndex;
    private String applicationContext;
    Map<String,String> content;
    //好友

    home1Fragment home1Fragment;
    //群
    home2Fragment home2Fragment;
/*
    public tab2Fragment(){
    }
*/
    @SuppressLint("ValidFragment")
    public tab2Fragment(home1Fragment home1Fragment,home2Fragment home2Fragment){
        this.home1Fragment = home1Fragment;
        this.home2Fragment = home2Fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_home_page, container, false);
        hRadioGroup = (RadioGroup) view.findViewById(R.id.home_radioGroup);
        newfriends = (LinearLayout) view.findViewById(R.id.newfriend);
        newfriends.setOnClickListener(this);

        initFragment();
        initEvent1();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newfriend:
                Context context = getActivity();
                Intent intent = new Intent(context,AddFriendActivity.class);
                context.startActivity(intent);
        }
    }


    private void initFragment() {
        /*
    //好友

        home1Fragment home1Fragment =new home1Fragment();
        //群
        home2Fragment home2Fragment =new home2Fragment();
*/

    //添加到数组
    mFragments = new Fragment[]{home1Fragment,home2Fragment};

    //开启事务
    FragmentTransaction ft = getFragmentManager().beginTransaction();

    //添加首页
        ft.add(R.id.content,home1Fragment).commit();

    //默认设置为第0个
    setIndexSelected(0);
}
    private void setIndexSelected(int index) {

        if(mIndex==index){
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if(!mFragments[index].isAdded()){
            ft.add(R.id.content,mFragments[index]).show(mFragments[index]);
        }else {
            ft.show(mFragments[index]);
        }

        ft.commit();
        //再次赋值
        mIndex=index;
    }
    private void initEvent1() {
        hRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home_tab1:
                        setIndexSelected(0);
                        break;
                    case R.id.home_tab2:
                        setIndexSelected(1);
                        break;
                }
            }
        });
    }

    public String getApplicationContext() {
        return applicationContext;
    }
}
