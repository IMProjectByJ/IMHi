package com.example.star.imhi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.star.imhi.BaseActivity;
import com.example.star.imhi.R;

import java.io.FileOutputStream;

public class ShowPicture extends BaseActivity {
    private ImageView imageView;
    private Button commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        imageView=(ImageView) findViewById(R.id.showpicture);
        commit=(Button) findViewById(R.id.commit);
        FileOutputStream b = null;
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
         Bitmap bitmap = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(bitmap);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.smile);
            }
        });
   }
}
