package com.example.star.imhi.UI;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.star.imhi.R;
import com.example.star.imhi.Utils.AccountValidatorUtil;
import com.example.star.imhi.Utils.CountDownTimerUtils;
import com.example.star.imhi.Utils.SendVerifyUtils;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {


   private Button btnSenMsm;
   private TextView tPhone;
   private TextView tPasswd;
   private TextView tRepeat;
   private TextView tCode;
   private  Button  btnReg;
   private  int verifyCode;
   private long  sendTime;  // 发送时间

    String  strPhone=null ; //  表示为获取验证码时的PhoneNum
    String  strPasswd ;
    String  strRepeat ;
    String  strCode ;


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
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(view,50000,1000);
                countDownTimerUtils.start();
                String  ret;
                strPhone = tPhone.getText().toString();
                if (isMobile(strPhone))
                {
                    new Thread(sendSms).start();
                }
                else
                {
                    tPhone.setError("请输入正确的手机号格式");
                }

            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptReg();
            }
        });

    }

    private void attemptReg() {

        Boolean cancel = false;

         strPasswd = tPasswd.getText().toString();
         strRepeat = tRepeat.getText().toString();
         strCode =  tCode.getText().toString();

        tPhone.setError(null);
        tPasswd.setError(null);
        tRepeat.setError(null);
        tCode.setError(null);


        if (isRigetCode(strCode))
        {
            cancel = true;
            tCode.setError(getString(R.string.error_sms_error));
        }
        if (!isMobile(strPhone))
        {
            cancel = true;
            tPhone.setError("手机号码不合法");
        }
        else {
            if (!tPhone.getText().toString().trim().equals(strPhone))
            {
                tPhone.setError("您还没有获取验证码或者与注册手机号不匹配");
            }
        }
        if (!isPasswd(strPasswd))
        {
            cancel = true;
            tPasswd.setError(getString(R.string.error_invalid_password));
        }
        if (!isRepeat(strPasswd,strRepeat))
        {
            cancel = true;
            tRepeat.setError("两次输入不一致");
        }

        if (!cancel)
        {


        }



    }

    boolean isMobile(String phone)
    {
        return !TextUtils.isEmpty(phone)&&AccountValidatorUtil.isMobile(phone);
    }

    boolean isPasswd(String passwd)
    {
        return !TextUtils.isEmpty(passwd)&&AccountValidatorUtil.isPassword(passwd);
    }
    boolean isRepeat(String passwd, String repeat)
    {
        return !TextUtils.isEmpty(passwd)&&passwd.trim().equals(repeat.trim());
    }
    //  暂时没有实现
    boolean isRigetCode(String code)
    {
        if (!strCode.trim().equals(""+verifyCode) && (System.currentTimeMillis() - sendTime)/60000 < 20)// 在20分钟内完成认证
        return true;
        return  false;
    }

    // 向phone 发送验证码

    Runnable sendSms = new Runnable() {
        @Override
        public void run() {

            verifyCode = (int) ((Math.random()+1)* 100000);
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

}
