package com.aki.bustool.utils;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chunr on 2016/5/31.
 */
public class UserOpenHelper extends SQLiteOpenHelper {

    private Activity activity;

    public UserOpenHelper(Context context) {
        super(context, "user.db", null, 1);
        this.activity = (Activity) context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table user (id integer primary key autoincrement," +
                "user_name txt," +
                "password text)");
        sqLiteDatabase.execSQL("create table user_data (id integer primary key autoincrement," +
                "user_name text," +
                "nick_name text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
