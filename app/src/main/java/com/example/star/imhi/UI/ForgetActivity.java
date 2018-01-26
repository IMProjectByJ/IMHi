package com.example.star.imhi.UI;

import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ForgetActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "forgetActivity";
    // 界面控件
    private CleanEditText phoneEdit;
    private CleanEditText passwordEdit;
    private CleanEditText verifyCodeEdit;
    private Button getVerifiCodeButton;
    private CleanEditText et_nickname;
    private VerifyCodeManager codeManager;

    // 弹出框
    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_forget);
        initViews();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
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
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);


    }


    public void commit() {
        final String phone = phoneEdit.getText().toString().trim();
        final String password = passwordEdit.getText().toString().trim();
        final String repassword = et_nickname.getText().toString().trim();

        if (!repassword.equals(password)) {
            et_nickname.setError("两次输入不一致");
            return;
        }

        if (!RegexUtil.checkPassword(password)) {
            passwordEdit.setError("密码为6-20为数字字母");
            return;
        }

        String code = verifyCodeEdit.getText().toString().trim();

        OkHttpClient okHttpClient = OkHttpUtils.getOkHttpClientInstance();
        Request request = new Request.Builder()
                .url(getString(R.string.requestAddr) + ":" + getString(R.string.port) +
                        "/api/user/ForgetPassword/" + phone + "/" + MD5Util.encrypt(password) + "/" + code)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(" in forget", "faling");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SweetAlertDialog(ForgetActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("失败")
                                .setContentText("网络请求错误")
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(" in forget", "not faling");
                String respStr = response.body().string();
                Gson gson = new Gson();

                HashMap<String, String> hash = gson.fromJson(respStr, HashMap.class);
                String message = hash.get("message");
                if (message == "" || message == null) {
                    message = "获取返回码失败";
                }
                final String finalMessage = message;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SweetAlertDialog(ForgetActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("信息")
                                .setContentText(finalMessage)
                                .show();
                    }
                });
            }
        });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_send_verifi_code:
                codeManager.getVerifyCode(VerifyCodeManager.RESET_PWD);
                break;
            case R.id.btn_create_account:
                commit();
                break;
            default:
                break;

        }

    }
}
