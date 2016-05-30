package com.aki.bustool.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.aki.bustool.R;

import java.io.InputStream;

/**
 * Created by chunr on 2016/4/29.
 */
public class IcoUtil {

    private static Bitmap mMine;
    private static Bitmap mMineC;
    private static Bitmap mBus;
    private static Bitmap mBusC;
    private static Bitmap mRoute;
    private static Bitmap mRouteC;
    private static Bitmap mIndex;
    private static Bitmap mIndexC;


    private static Context ctx;



    public static void init(Context context){

        ctx = context;

        mBus = scaleBottomImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.bus));
        mBusC = scaleBottomImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.busc));
        mRoute = scaleBottomImage(BitmapFactory.decodeResource(context.getResources(),R.drawable.route));
        mRouteC = scaleBottomImage(BitmapFactory.decodeResource(context.getResources(),R.drawable.routec));
        mIndex = scaleBottomImage(BitmapFactory.decodeResource(context.getResources(),R.drawable.index));
        mIndexC = scaleBottomImage(BitmapFactory.decodeResource(context.getResources(),R.drawable.indexc));
        mMine = scaleBottomImage(BitmapFactory.decodeResource(context.getResources(),R.drawable.mine));
        mMineC = scaleBottomImage(BitmapFactory.decodeResource(context.getResources(),R.drawable.minec));
    }

    //缩放下方图标
    public static Bitmap scaleBottomImage(Bitmap oringinBitmap){
        Matrix matrix = new Matrix();
        float width = Initialize.SCREEN_WIDTH/12;
        float height = Initialize.SCREEN_HEIGHT/11;
        float widthRatio = width/oringinBitmap.getWidth();
        float heightRatio = height/oringinBitmap.getHeight();
        matrix.postScale(widthRatio,heightRatio);
        return Bitmap.createBitmap(oringinBitmap,0,0,oringinBitmap.getWidth(),oringinBitmap.getHeight(),matrix,false);
    }

    //缩放方法
    public static Bitmap scaleImage(int resource,float widthRatio,float heightRatio){
        Log.i("Aki345688",widthRatio + "---" + heightRatio);
        Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(),resource);
        Matrix matrix = new Matrix();
        matrix.postScale(widthRatio,heightRatio);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
    }
    //缩放重载
    public static Bitmap scaleImage(InputStream is,float widthRatio,float heightRatio){
        Log.i("Aki345688",widthRatio + "---" + heightRatio);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        Matrix matrix = new Matrix();
        matrix.postScale(widthRatio,heightRatio);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
    }

    public static Bitmap getBitmapFromResource(int resId){
        return BitmapFactory.decodeResource(ctx.getResources(),resId);
    }

    public static Bitmap getmBus() {
        return mBus;
    }

    public static Bitmap getmRoute() {
        return mRoute;
    }

    public static Bitmap getmIndex() {
        return mIndex;
    }

    public static Bitmap getmMine() {
        return mMine;
    }

    public static Bitmap getmIndexC() {
        return mIndexC;
    }

    public static Bitmap getmRouteC() {
        return mRouteC;
    }

    public static Bitmap getmBusC() {
        return mBusC;
    }

    public static Bitmap getmMineC() {
        return mMineC;
    }
}
