package com.example.star.imhi.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.star.imhi.R;


/*
  只是为了响应
 */
public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Intent intent = getIntent();
        TextView tv = new TextView(this);
        tv.setText(intent.getStringExtra("loginUser"));
        setContentView(tv);
    }
}
