package com.example.star.imhi.addfriend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity {

    //    private TextView t_name;
//    private TextView t_id_number;
//    private TextView t_tel_number;
//      private TextView t_more;
//    private ImageView sex;
    private User user;

    Intent intent_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setMessage();
        TextView t_more = (TextView) findViewById(R.id.more);
        TextView t_set_notes = (TextView) findViewById(R.id.set_notes);
        Button btn_back = (Button) findViewById(R.id.back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        t_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, MoreActivity.class);
                Gson gson = new Gson();
                User user = gson.fromJson(intent_message.getStringExtra("user"), User.class);
                intent.putExtra("user", gson.toJson(user));
                startActivity(intent);
            }
        });

        t_set_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, RemarksActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setButtonAction() {
        Button btn_send = (Button) findViewById(R.id.btn_send);
        Button btn_del = (Button) findViewById(R.id.btn_del);

    }

    private void setMessage() {

        //setHeadImage("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg");

        intent_message = getIntent();
        Gson gson = new Gson();
        user = gson.fromJson(intent_message.getStringExtra("user"), User.class);
        TextView t_name = (TextView) findViewById(R.id.name);
        TextView t_id_number = (TextView) findViewById(R.id.id_number);
        TextView t_tel_number = (TextView) findViewById(R.id.tel_number);
        ImageView sex = (ImageView) findViewById(R.id.sex);

        t_name.setText(user.getNikname());
        t_id_number.setText(user.getUserId().toString());
        t_tel_number.setText(user.getPhoneNum());
        if (user.getGender() != null)
            if (user.getGender().equals("男")) {
                sex.setImageResource(R.drawable.man);
            } else {
                sex.setImageResource(R.drawable.woman);
            }
    }

    private void setHeadImage(final String imgUrl) {
        final ImageView imageView = (ImageView) findViewById(R.id.head_img);

        new Thread(new Runnable() {
            @Override
            public void run() {

                URL url = null;
                HttpURLConnection connection = null;
                try {
                    url = new URL(imgUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setDoInput(true);
                    File parent = Environment.getExternalStorageDirectory();
                    File file = new File(parent, String.valueOf(System.currentTimeMillis()));
                    FileOutputStream fos = new FileOutputStream(file);
                    InputStream in = connection.getInputStream();
                    Log.e("file path:", file.getAbsolutePath());

                    byte ch[] = new byte[1024 * 2];
                    int len;
                    if (fos != null) {
                        while ((len = in.read(ch)) != -1) {
                            fos.write(ch, 0, len);
                        }
                        in.close();
                        fos.close();
                    }

                    final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap != null) {
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
