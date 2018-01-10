package com.example.star.imhi.UI;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Trace;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.Utils.AccountValidatorUtil;
import com.example.star.imhi.Utils.CountDownTimerUtils;
import com.example.star.imhi.Utils.MD5Util;
import com.example.star.imhi.Utils.SendVerifyUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class RegisterActivity extends AppCompatActivity {


    private Button btnSenMsm;
    private TextView tPhone;
    private TextView tPasswd;
    private TextView tRepeat;
    private TextView tCode;
    private Button btnReg;
    private int verifyCode;
    private long sendTime;  // 发送时间

    String strPhone = null; //  表示为获取验证码时的PhoneNum
    String strPasswd;
    String strRepeat;
    String strCode;
    WaitDialog waitDialog;
    int delayTime = 5;// 延迟5秒关闭
    Handler handler;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnSenMsm = findViewById(R.id.getCode);
        tPhone = findViewById(R.id.reg_mobile);
        tPasswd = findViewById(R.id.reg_password);
        tRepeat = findViewById(R.id.repi_password);
        tCode = findViewById(R.id.verify_code);
        btnReg = findViewById(R.id.mobile_reg_button);

        btnSenMsm.setOnClickListener(new View.OnClickListener() { // 监听发送验证码按钮
            @Override
            public void onClick(View view) {

                strPhone = tPhone.getText().toString();
                if (isMobile(strPhone)) {
                    new Thread(sendSms).start();
                    CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(view, 50000, 1000, null, "点击重新获取验证码");
                    countDownTimerUtils.start();
                } else {
                    tPhone.setError("请输入正确的手机号格式");
                }

            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptReg(view);
            }
        });

    }

    private void attemptReg(View view) {

        Boolean cancel = false;

        strPasswd = tPasswd.getText().toString();
        strRepeat = tRepeat.getText().toString();
        strCode = tCode.getText().toString();

        tPhone.setError(null);
        tPasswd.setError(null);
        tRepeat.setError(null);
        tCode.setError(null);


        if (isRigetCode(strCode)) {
            cancel = true;
            tCode.setError(getString(R.string.error_sms_error));
        }
        if (!isMobile(strPhone)) {
            cancel = true;
            tPhone.setError("手机号码不合法");
        } else {
            if (!tPhone.getText().toString().trim().equals(strPhone)) {
                tPhone.setError("您还没有获取验证码或者与注册手机号不匹配");
            }
        }
        if (!isPasswd(strPasswd)) {
            cancel = true;
            tPasswd.setError(getString(R.string.error_invalid_password));
        }
        if (!isRepeat(strPasswd, strRepeat)) {
            cancel = true;
            tRepeat.setError("两次输入不一致");
        }

        if (!cancel) {
            // http请求
            User user = new User();
            user.setPhoneNum(strPhone);
            user.setUserPassword(MD5Util.encrypt(strPasswd));
            sendPost(getString(R.string.postUrl) + "api/user/register/", user);
            Log.e("sss", new Gson().toJson(user).toString());
            CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(view, 5000, 1000, "正在注册ing", "注册");
            countDownTimerUtils.start();
            // showwaiting Dailog
            showWaitDialog("正在注册，请稍后");



        }


    }

    private void sendPost(String url, User user) {
        Log.e("in sendPost", "sendPost");
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, new Gson().toJson(user));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Log.e("passwd", user.getUserPassword());
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("注册")
                                .setMessage("网络请求错误")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    }
                });
            }

            boolean Success = false;
            boolean isFail = false;
            User user2;

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String strRes = response.body().string();
                    if (strRes != null) {
                        try {
                            JSONObject jsonObject = null;
                            jsonObject = new JSONObject(strRes);
                            if (jsonObject.optString("err") != "" && jsonObject.get("err").equals("用户已存在")) {
                                Log.e("err", "用户已存在");
                            } else {

                                user2 = new Gson().fromJson(strRes, User.class);
                                Log.e("success", user2.getUserId() + "");
                                Success = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else
                    isFail = true;


                // ui操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 弹出对话框，并返回登录界面
                        if (Success) {
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("注册")
                                    .setMessage("注册成功，您的imHi聊账号为" + user2.getUserId() + "是否返回登录界面?")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                        } else if (isFail == false) {
                            tPhone.requestFocus();
                            tPhone.setError("用户名重复");
                        } else {
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("注册")
                                    .setMessage("未知错误")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                        }

                    }
                });
            }

        });

    }

    boolean isMobile(String phone) {
        return !TextUtils.isEmpty(phone) && AccountValidatorUtil.isMobile(phone);
    }

    boolean isPasswd(String passwd) {
        return !TextUtils.isEmpty(passwd) && AccountValidatorUtil.isPassword(passwd);
    }

    boolean isRepeat(String passwd, String repeat) {
        return !TextUtils.isEmpty(passwd) && passwd.trim().equals(repeat.trim());
    }

    //  暂时没有实现
    boolean isRigetCode(String code) {
        if (!strCode.trim().equals("" + verifyCode) && (System.currentTimeMillis() - sendTime) / 60000 < 10)// 在20分钟内完成认证
            return true;
        return false;
    }

    // 向phone 发送验证码

    Runnable sendSms = new Runnable() {
        @Override
        public void run() {

            verifyCode = (int) ((Math.random() + 1) * 100000);
            String text = getString(R.string.imHi) + verifyCode;
            Log.e("10100", verifyCode + " ");
            Log.e("view", "倒计时");
            try {
                //  SendVerifyUtils.sendSms(getString(R.string.key_value), text, strPhone);
                sendTime = System.currentTimeMillis();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 实现方法2
     *
     * @param waitMsg
     */
    private void showWaitDialog(String waitMsg) {
        if (waitDialog == null) {
            waitDialog = new WaitDialog(RegisterActivity.this, waitMsg);
        }
        Log.e("sss","login---------------");
        waitDialog.setDismissDelay(delayTime);
        waitDialog.show();


    }
}
