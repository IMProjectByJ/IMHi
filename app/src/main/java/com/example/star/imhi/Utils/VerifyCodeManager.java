package com.example.star.imhi.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.star.imhi.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @creation_time: 2017/4/11
 * @author: Vegen
 * @e-mail: vegenhu@163.com
 * @describe:
 */

public class VerifyCodeManager {
    public final static int REGISTER = 1;
    public final static int RESET_PWD = 2;
    public final static int BIND_PHONE = 3;

    private Context mContext;
    private Activity activity;
    private int recLen = 60;
    private Timer timer = new Timer();
    private Handler mHandler = new Handler();
    private String phone;

    private EditText phoneEdit;
    private Button getVerifiCodeButton;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public VerifyCodeManager(Context context, EditText editText, Button btn, Activity activity) {
        this.mContext = context;
        this.phoneEdit = editText;
        this.getVerifiCodeButton = btn;
        this.activity = activity;
    }

    public boolean getVerifyCode(int type) {
        // 获取验证码之前先判断手机号

        phone = phoneEdit.getText().toString().trim(); // 获取手机号码

        if (TextUtils.isEmpty(phone)) {

            phoneEdit.setError(mContext.getString(R.string.tip_please_input_phone));

        } else if (phone.length() < 11) {

            phoneEdit.setError(mContext.getString(R.string.tip_phone_regex_not_right));

        } else if (!RegexUtil.checkMobile(phone)) {

            phoneEdit.setError(mContext.getString(R.string.tip_phone_regex_not_right));

        }else {
            /*
             发送验证码操作
             liuyunxing
             */
            sendMsM(phone, type);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setButtonStatusOff();
                            if (recLen < 1) {
                                setButtonStatusOn();
                            }
                        }
                    });
                }
            };

            timer = new Timer();
            timer.schedule(task, 0, 1000);

        }
        return false;
    }


    private void setButtonStatusOff() {
        getVerifiCodeButton.setText(String.format(
                mContext.getResources().getString(R.string.count_down),""+recLen--));
        getVerifiCodeButton.setClickable(false);
        getVerifiCodeButton.setTextColor(Color.parseColor("#f3f4f8"));
        getVerifiCodeButton.setBackgroundColor(Color.parseColor("#b1b1b3"));
    }

    private void setButtonStatusOn() {
        timer.cancel();
        getVerifiCodeButton.setText("重新发送");
        getVerifiCodeButton.setTextColor(Color.parseColor("#b1b1b3"));
        getVerifiCodeButton.setBackgroundColor(Color.parseColor("#f3f4f8"));
        recLen = 60;
        getVerifiCodeButton.setClickable(true);
    }

    private void sendMsM(String phone, int type) {

        OkHttpClient okHttpClient = OkHttpUtils.getOkHttpClientInstance();


        Log.e(" in send","sending ");
        final Request request = new Request.Builder()
                .url(mContext.getString(R.string.requestAddr)+":"+mContext.getString(R.string.port)+"/api/SMS/sendSMS/"+phone+"/"+type)
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("网络错误")
                                .setContentText("请检查您的网络")
                                .show();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){

                    String getStr = response.body().string();
                    Log.e("in verify",getStr);
                    Gson gson = new Gson();
                   HashMap<String,String> hashMap = gson.fromJson(getStr, HashMap.class);
                   String messasge = hashMap.get("message");
                   if (messasge.equals("成功")) {
                        Log.e("in success","----------------");
                   } else {
                      // Log.e("in response  fail",jsonObject.optString("message"));
                       final String finalMessage = messasge;
                       activity.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {

                               new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                       .setTitleText("发生错误")
                                       .setContentText(finalMessage)
                                       .show();
                           }
                       });

                   }
                }
            }
        });
    }


}
