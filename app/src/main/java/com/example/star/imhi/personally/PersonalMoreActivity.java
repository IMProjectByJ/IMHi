package com.example.star.imhi.personally;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonalMoreActivity extends AppCompatActivity {

    private String[] years = new String[500];
    private Button btn_back;
    private Button btn_finsh;
    private EditText person_age;
    private User user;
    private LinearLayout current_touch;
    private Button btn_data;
    private TextView birthday;
    int mYear, mMonth, mDay;
    final int DATE_DIALOG = 1;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_more);
        init();

        current_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                person_age.setCursorVisible(false);
            }
        });

        clickAge();
        clickData();
        titleButton();


        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    private void init(){
        int j = 0;
        btn_back = (Button) findViewById(R.id.back);
        btn_finsh = (Button) findViewById(R.id.finsh);
        person_age = (EditText) findViewById(R.id.personal_age);
        current_touch = (LinearLayout) findViewById(R.id.current_touch);
        btn_data = (Button) findViewById(R.id.btn_data);
        birthday = (TextView) findViewById(R.id.set_birthday);
        Intent intent = getIntent();

        user = new Gson().fromJson(intent.getStringExtra("person_user"), User.class);

        if (user.getAge() == null) {
            Log.e("intent:", "null");
            person_age.setText("0");
        } else {
            person_age.setText(user.getAge().toString());
        }
        birthday.setText(user.getBirth());

    }

    public void clickData(){
        btn_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        birthday.setText(new StringBuffer().append(mYear + 1).append("-").append(mMonth).append("-").append(mDay).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    public void clickAge(){
        person_age.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE ||
                        (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode())) {
                    person_age.setCursorVisible(false);
                }

                return false;
                //return keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            }
        });

        person_age.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                person_age.setCursorVisible(true);
                return false;
            }
        });
    }

    public void titleButton(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("destory:", "destory");
                Log.e("birthday:", user.getBirth());
                Log.e("user:", user.toString() );

                if (isNumeric(person_age.getText().toString())) {
                    user.setAge(Integer.parseInt(person_age.getText().toString()));
                    user.setBirth(birthday.getText().toString());
                } else {
                    Toast.makeText(PersonalMoreActivity.this, "年龄不是正确的格式", Toast.LENGTH_SHORT).show();
                    return;
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                            String jsonObject = new Gson().toJson(user);
//                            Request request;
//                            Response response;
//
//                            request = new Request.Builder().url("http://192.168.252.1:8080/api/user/information/" + jsonObject).build();
//                            //request = new Request.Builder().url("http://192.168.253.1:8080/api/user/add_search_by_uid/" + editText.getText().toString()).build();
//                            response = okHttpClient.newCall(request).execute();
//                            String responseData = response.body().string();


                            //Log.e("personInformation:", responseData);
                            OkHttpClient okHttpClient = new OkHttpClient();
                            //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                            //json为String类型的json数据
                            RequestBody requestBody = RequestBody.create(JSON, jsonObject);
                            //创建一个请求对象
                            Request request = new Request.Builder()
                                    .url(getString(R.string.postUrl) + "api/user/information_post")
                                    .post(requestBody)
                                    .build();
                            //发送请求获取响应
                            try {
                                Response response=okHttpClient.newCall(request).execute();
                                //判断请求是否成功
                                if(response.isSuccessful()){
                                    //打印服务端返回结果
                                    Log.e("Success", "success!!!!!!!!!" );

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent();
                            intent.putExtra("data_return", new Gson().toJson(user));
                            setResult(RESULT_OK, intent);

                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(PersonalMoreActivity.this);
                            Intent intent1 = new Intent("com.imagePath.LOCAL_BROADCAST");
                            intent1.putExtra("userAge",user.getAge().toString());
                            intent1.putExtra("userBirth",user.getBirth());
                            localBroadcastManager.sendBroadcast(intent1);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });
    }

    public boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            Log.e("string:", String.valueOf(str.charAt(i)));
            if (!Character.isDigit(str.charAt(i))) {
                Log.e("num:", "false" );
                return false;
            }
        }
        return true;
    }

}
