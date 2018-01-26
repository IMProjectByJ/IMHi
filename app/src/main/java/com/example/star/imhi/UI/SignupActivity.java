package com.example.star.imhi.UI;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.Utils.MD5Util;
import com.example.star.imhi.Utils.OkHttpUtils;
import com.example.star.imhi.Utils.RegexUtil;
import com.example.star.imhi.Utils.VerifyCodeManager;
import com.example.star.imhi.base.BaseActivity;
import com.example.star.imhi.view.CleanEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupActivity extends BaseActivity implements View.OnClickListener  {


    private static final String TAG = "SignupActivity";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // 界面控件
    private CleanEditText phoneEdit;
    private CleanEditText passwordEdit;
    private CleanEditText repasswordEdit;
    private CleanEditText verifyCodeEdit;
    private Button getVerifiCodeButton;
    private CleanEditText et_nickname;
    private VerifyCodeManager codeManager;

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_signup);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initViews();
        codeManager = new VerifyCodeManager(this, phoneEdit, getVerifiCodeButton, this.getActivity());

    }

    private void initViews() {
        et_nickname = getView(R.id.et_nickname);
        getVerifiCodeButton = getView(R.id.btn_send_verifi_code);

        getVerifiCodeButton.setOnClickListener(this); // 设置clicklistenner事件

        phoneEdit = getView(R.id.et_phone);
        phoneEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 设置键盘下一步
        verifyCodeEdit = getView(R.id.et_verifiCode);
        verifyCodeEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 设置键盘下一步
        passwordEdit = getView(R.id.et_password);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_nickname = getView(R.id.et_nickname);
        et_nickname.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_nickname.setImeOptions(EditorInfo.IME_ACTION_GO);
        repasswordEdit = getView(R.id.et_repassword);
        repasswordEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
    }


    public void commit() {
        final String phone = phoneEdit.getText().toString().trim();
        final String password = passwordEdit.getText().toString().trim();
        final String nikename = et_nickname.getText().toString().trim();
        final String repassword = repasswordEdit.getText().toString().trim();


        if (!repassword.equals(password)) {
            repasswordEdit.setError("两次输入不一致");
            return;
        }

        if (!RegexUtil.checkPassword(password)) {
            passwordEdit.setError("密码为6-20为数字字母");
            return;
        }

        if (nikename.length() == 0){
            et_nickname.setError("昵称不能为空");
            return;
        }

        String code = verifyCodeEdit.getText().toString().trim();
        // 过程
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        // loading .......

        OkHttpClient okHttpClient = OkHttpUtils.getOkHttpClientInstance();
        User user = new User();
        user.setPhoneNum(phone);
        user.setUserPassword(MD5Util.encrypt(password));
        user.setNikname(nikename);
        RequestBody requestBody = RequestBody.create(JSON, new Gson().toJson(user));
        Request request = new Request.Builder()
                .url(getString(R.string.requestAddr) + ":" + getString(R.string.port) +
                        "/api/user/register/"  + code)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(" in regesiter", "faling");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // qv xiao
                        pDialog.dismiss();
                        new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("网络错误")
                                .setContentText("网络错误或者请求出错！")
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(" in regiser", "not faling");
                pDialog.dismiss();
                String respStr = response.body().string();
                Log.e("reponse in reg",respStr);
                Gson gson = new Gson();
                HashMap<String, String> hash = gson.fromJson(respStr, HashMap.class);
                String message = hash.get("message");
                String messageInfo ;
                if (message == "" || message == null) {
                    message = "获取返回码失败,未知错误";
                }
                if (message.equals("注册成功")){
                    messageInfo = "您的IMHi账号是 "+hash.get("userId");
                    final String finalMessage = messageInfo;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // dialog  for success
                            new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("注册成功")
                                    .setContentText(finalMessage)
                                    .show();
                        }
                    });
                } else {
                    final String finalMessage = message;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // dialog  for success
                            new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("注册失败")
                                    .setContentText(finalMessage)
                                    .show();
                        }
                    });
                }


            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_verifi_code:
                codeManager.getVerifyCode(VerifyCodeManager.REGISTER);
                break;
            case R.id.btn_create_account:
                commit();
                break;
            default:
                break;

        }
    }
}
