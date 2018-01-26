package com.example.star.imhi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.personally.Personal_information;
import com.example.star.imhi.activity.StartActivity;
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
                test();
                break;
            case R.id.set_2:
                break;
            case R.id.set_3:
                break;
            default:
                break;

        }
    }

    public void test(){
        StartActivity startActivity = (StartActivity)getActivity();
        Intent intent = startActivity.getIntent();
        String uid = intent.getStringExtra("loginUser");
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
