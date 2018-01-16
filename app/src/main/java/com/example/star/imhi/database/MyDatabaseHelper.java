package com.example.star.imhi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by d c on 2018/1/10.
 */

public class MyDatabaseHelper  extends SQLiteOpenHelper {
    public  static  final  String CREATE_FRIENDS = "create table Friends ("
            + "user_id integer primary key, "
            + "phone_num text,"
            + "nikname text,"
            + "head_url integer,"
            + "age integer,"
            + "gender text,"
            + "birth text,"
            + "motto text)";
    public  static  final  String CREATE_History_message = "create table history_message ("
            + "user_id integer, "
            + "to_id integer,"
            + "text_type integer,"
            + "message_id integer  primary key,"
            + "message_type integer,"
            + "text_content text,"
            + "date datetime)";
    private  Context mcontext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FRIENDS);
        db.execSQL(CREATE_History_message);
        Toast.makeText(mcontext,"Create success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Friends");
        Toast.makeText(mcontext,"drop success",Toast.LENGTH_SHORT).show();
        onCreate(db);

    }
}
