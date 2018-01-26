package com.example.star.imhi.Utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.example.star.imhi.R;

/**
 * Created by star on 18-1-4.
 */

public class CountDownTimerUtils extends CountDownTimer {

    private TextView  setView;
    String  info;
    String  toChangInfo;
    public CountDownTimerUtils(View view ,long millisInFuture, long countDownInterval,String info,String toChangInfo) {
        super(millisInFuture, countDownInterval);
        this.setView = (TextView)view;
        this.info = info;
        this.toChangInfo = toChangInfo;
    }

    @Override
    public void onTick(long l) {

        setView.setClickable(false);
        if (info == null)
            setView.setText(l/ 1000 +"s");
        else
            setView.setText(info);
        // 设置按钮背景颜色
       // setView.setBackgroundColor(Color.GRAY);

        SpannableString spannableString = new SpannableString(setView.getText().toString());  //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        setView.setText(spannableString);


    }


    @Override
    public void onFinish() {

        setView.setText(toChangInfo);
        setView.setClickable(true);//重新获得点击
        //mTextView.setBackgroundResource(R.drawable.bg_identify_code_normal);  //还原背景色

    }
}
