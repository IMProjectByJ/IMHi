package com.example.star.imhi.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.UI.LoginActivity;
import com.example.star.imhi.activity.StartActivity;
import com.example.star.imhi.personally.Personal_information;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by d c on 2018/1/12.
 */

public class tab3Fragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private LinearLayout set_1,set_2,set_3;
    private User user;
    private Button exit;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_setting, container, false);
        set_1 = (LinearLayout) view.findViewById(R.id.set_1);
        set_2 = (LinearLayout) view.findViewById(R.id.set_2);
        set_3 = (LinearLayout) view.findViewById(R.id.set_3);
        exit = (Button) view.findViewById(R.id.exit);
        set_1.setOnClickListener(this);
        set_2.setOnClickListener(this);
        set_3.setOnClickListener(this);
        exit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_1:
                test();
                break;
            case R.id.set_2:
                break;
            case R.id.set_3:
                break;
            case R.id.exit:
                exitlogin();
                break;
            default:
                break;

        }
    }
    public void exitlogin(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Hi");
        dialog.setMessage("确定退出？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StartActivity startActivity = (StartActivity)getActivity();
                Intent intent = new Intent(startActivity, LoginActivity.class);
                intent.putExtra("auto", false);
                startActivity(intent);
//                System.exit(0);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void test(){
        StartActivity startActivity = (StartActivity)getActivity();
        Intent intent = startActivity.getIntent();
        String uid = intent.getStringExtra("loginUser");
        Log.e("TEST----:", uid+"aaaaaaaaaaaaaaaaaaaaaaaaa");
        requestServert(uid);
    }

    public void requestServert(final String uid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request;
                request = new Request.Builder().get().url(getString(R.string.postUrl) + "api/user/details/" + uid).build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    Log.e("responseData:", responseData);
                    JSONObject jsonObject = new JSONObject(responseData);

                    if (jsonObject.optString("err") == "") {
                        Gson gson  = new Gson();
                        user = gson.fromJson(responseData,User.class);
                        Log.e("details:",user.toString() );
                    }

                    Intent intent1 = new Intent(getActivity(), Personal_information.class);
                    Gson gson = new Gson();
                    intent1.putExtra("person_user",gson.toJson(user) );
                    Log.e("intent1:", gson.toJson(user).toString() );
                    startActivity(intent1);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
