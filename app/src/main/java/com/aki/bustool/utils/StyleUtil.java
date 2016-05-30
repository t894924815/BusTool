package com.aki.bustool.utils;

import android.content.Context;
import android.widget.TextView;

import com.aki.bustool.R;

/**
 * Created by chunr on 2016/4/30.
 */
public class StyleUtil {


    public static Context context;


    public static void init(Context ctx){
        context = ctx;
    }


    public static void setColor(TextView textView,String status){
        switch (status){
            case "click":
                textView.setTextColor(context.getResources().getColor(R.color.click));
                break;
            case "default":
                textView.setTextColor(context.getResources().getColor(R.color.text_default));
                break;
        }

    }
}
