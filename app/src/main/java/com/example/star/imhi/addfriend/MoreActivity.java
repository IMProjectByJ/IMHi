package com.example.star.imhi.addfriend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.google.gson.Gson;

public class MoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        Button btn_back = (Button) findViewById(R.id.back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setMessage();
    }

    private void setMessage(){
        Intent intent = getIntent();
        Gson gson = new Gson();
        User user = gson.fromJson(intent.getStringExtra("user"), User.class);

        if (user == null) {
            Log.e("MOREUSER:", "is NULL" );
        } else {
            Log.e("MOREUSER:", user.toString());
        }
        TextView t_age = (TextView)findViewById(R.id.age);
        TextView t_birthday = (TextView)findViewById(R.id.birthday);
        TextView t_personal_message = (TextView)findViewById(R.id.personal_message);

        if (user.getAge() == 0 || user.getAge() == null) {
            t_age.setText("0");
        } else {
            t_age.setText(user.getAge().toString());
        }

        if (!user.getBirth().equals("")) {
            Log.e("birthday:", user.getBirth() );
            t_birthday.setText(user.getBirth());
        }

        if (!user.getMotto().equals("")) {
            t_personal_message.setText(user.getMotto());
        }
    }
}
