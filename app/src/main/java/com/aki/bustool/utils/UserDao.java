package com.aki.bustool.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by chunr on 2016/5/31.
 */
public class UserDao {

    private String userName;
    private String passWord;
    private String nickName;
    private SQLiteDatabase db;
    private Cursor cursor;

    public UserDao(Context context, String userName, String passWord,String nickname) {
        this.userName = userName;
        this.passWord = passWord;
        if(null != nickname){
            this.nickName = nickname;
        }
        db = new UserOpenHelper(context).getReadableDatabase();
    }


    /*
    *
    * 账号数据拉取
    *
    * */
    public String getNickName(){
        boolean isNull = false;
        cursor = db.rawQuery("select nick_name from user_data where user_name = ?",new String[]{userName});
        if(0 != cursor.getExtras().size()){
            isNull = true;
        }else{
            cursor.moveToNext();
        }
        return isNull?Initialize.ERROR:cursor.getString(cursor.getColumnIndex("nick_name"));
    }






    /*
    *
    * 账号验证注册模块
    * */
    public void registUser(){
        ContentValues values = new ContentValues();
        values.put("user_name",userName);
        values.put("password",passWord);
        db.beginTransaction();
        db.insert("user",null,values);
        db.setTransactionSuccessful();
        db.endTransaction();

        values.clear();
        if(null != nickName){
            values.put("user_name",userName);
            values.put("nick_name",nickName);
            db.beginTransaction();
            db.insert("user_data",null,values);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public int validate(){
        cursor = db.rawQuery("select * from user where user_name=?",new String[]{userName});
        if(0 == cursor.getExtras().size()){
            return Initialize.ERROR_USER;
        }else{
            cursor = db.rawQuery("select * from user where user_name=? and password = ?",new String[]{userName,passWord});
            if(0 == cursor.getExtras().size()){
                return Initialize.ERROR_PASSWORD;
            }else{
                return Initialize.LOGIN_SUCCESS;
            }
        }
    }
}
