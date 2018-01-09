package com.example.star.imhi.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.star.imhi.R;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent = new Intent(this,LoginActivity.class);
        Timer timer =   new Timer();
        TimerTask task =  new TimerTask(){
            public void run()
            {
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(task,1000*3);

    }
    public void sendMessage(View view)
    {

    }
}
