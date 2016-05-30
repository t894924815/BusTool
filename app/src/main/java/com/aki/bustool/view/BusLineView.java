package com.aki.bustool.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.util.*;

import com.aki.bustool.Interfaces.OnBusLineNameClick;
import com.aki.bustool.Interfaces.OnBusStationClickListener;
import com.aki.bustool.utils.Initialize;
import com.amap.api.services.busline.BusStationItem;

/**
 * Created by chunr on 2016/5/17.
 */
public class BusLineView extends View implements OnBusStationClickListener {

    private Paint mLinePaint;
    private Paint mPointPaint;
    private float lineWidth;
    private float lineHeight;
    private float defaultPoint;
    private Paint alphaPaint;
    private List<BusStationItem> busStation;
    private float pointRadius;
    private float distance = 0;
    private Scroller mScroller;
    private GestureDetector mDetector;
    private Paint textPaint;
    private Paint clickPaint;
    private float alphaWidth;
    private float linewidth;
    private float textWidth;
    private float alphaRadius;
    private Paint redPaint;
    private boolean canClick = true;

    public BusLineView(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BusLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public BusLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BusLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setBusStation(List<BusStationItem> message){
        busStation = message;
        invalidate();
    }

    public void setOnClickEnable(boolean canClick){
        this.canClick = canClick;
    }

    public void init(){
        pointRadius = (float) (Initialize.SCREEN_WIDTH/128.0);
        lineWidth = (float) (Initialize.SCREEN_WIDTH/6.0);
        lineHeight = (float) (Initialize.SCREEN_HEIGHT/11.0);
        defaultPoint = (float) (Initialize.SCREEN_WIDTH/7.7);
        alphaWidth = (float) (Initialize.SCREEN_WIDTH/128.0);
        linewidth = (float) (Initialize.SCREEN_WIDTH/64.0);
        alphaRadius = alphaWidth/2 + pointRadius;
        textWidth = 3*alphaRadius;

        redPaint = new Paint();
        clickPaint = new Paint();
        textPaint = new Paint();
        mLinePaint = new Paint();
        mPointPaint = new Paint();
        alphaPaint = new Paint();

        redPaint.setColor(Initialize.POINT_RED);
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setAntiAlias(true);

        clickPaint.setColor(Color.RED);
        clickPaint.setTextSize(textWidth + 2);
        clickPaint.setAntiAlias(true);

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textWidth);
        textPaint.setAntiAlias(true);

        mLinePaint.setColor(Initialize.LINE_GREEN);
        mLinePaint.setStrokeWidth(linewidth);
        mLinePaint.setAntiAlias(true);

        mPointPaint.setColor(Initialize.POINT_BACK);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setAntiAlias(true);

        alphaPaint.setColor(Initialize.DRAW_BACK);
        alphaPaint.setStyle(Paint.Style.STROKE);
        alphaPaint.setStrokeWidth(alphaWidth);
        alphaPaint.setAntiAlias(true);

        mDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                distance += distanceX;
                if(distance < 0){
                    distanceX = -1 * (distance - distanceX);
                    distance = 0;
                }else if(distance > defaultPoint + lineWidth * (busStation.size() - 4)){
                    distanceX = (defaultPoint + lineWidth * (busStation.size() - 4)) - (distance - distanceX);
                    distance = defaultPoint + lineWidth * (busStation.size() - 4);
                }

                scrollBy((int) distanceX,0);

                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });

        this.setOnStationClickListener(this,"this");

        mScroller = new Scroller(getContext());
    }


    private boolean itemClick = true;
    private int witchItem = -1;

    @Override
    public void OnClickItem(int position) {
            witchItem = position;
//        Log.i("clickTest",position+ "***************");
            invalidate();
    }

    private float clickX;
    private float clickY;
    private float endX;
    private float endY;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(busStation != null){
            mDetector.onTouchEvent(event);
            Log.i("ClickAll",getScrollX() + "**************");
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    clickX = event.getX() + getScrollX();
                    clickY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    if(canClick){
                        endX = event.getX() + getScrollX();
                        endY = event.getY();
                        if(clickX == endX && clickY == endY){
                            float judeX = (clickX-defaultPoint+pointRadius+alphaWidth+20)%(lineWidth);
                            if(judeX >= 0 && judeX <= alphaRadius*2+40){
                                int num = (int) ((clickX-defaultPoint+pointRadius+linewidth)/(lineWidth));
                                if(clickY >= (lineHeight + lineWidth/2 +textWidth)
                                        &&
                                        clickY <= lineHeight + lineWidth/2+ 10 + (busStation.get(num).getBusStationName().length() + 2) * textWidth ){
                                    if(null != mListener){
                                        mListener.OnClickItem(num);
                                        if(null != elseListener){
                                            elseListener.OnClickItem(num);
                                        }
//                                    Log.i("ClickTest",event.getX() + "getX*****");
                                    }
                                }
                            }
                        }
                    }

                    break;
            }
        }
        return true;
    }


    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            int currentX = mScroller.getCurrX();
            scrollTo(currentX,0);
            invalidate();

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("drawTest","start");

        canvas.drawColor(Initialize.DRAW_BACK);

        if(busStation != null){
            for (int count = 0;count < busStation.size() - 1 ;count ++){
                canvas.drawLine(defaultPoint + count * lineWidth,
                        lineHeight,
                        defaultPoint + (count + 1) * lineWidth,
                        lineHeight,
                        mLinePaint);
            }

            for (int count = 0;count < busStation.size();count ++){
                canvas.drawCircle(defaultPoint + count * lineWidth,
                        lineHeight,
                        pointRadius,
                        mPointPaint);
                canvas.drawCircle(defaultPoint + count * lineWidth,
                        lineHeight,
                        alphaRadius,
                        ((itemClick)&&(count==witchItem))?redPaint:alphaPaint);

                canvas.drawText((count + 1) + "",
                        defaultPoint + count * lineWidth - (textWidth/2),
                        lineHeight + lineWidth/2 +textWidth ,
                        ((itemClick)&&(count==witchItem))?clickPaint:textPaint);
                for (int i = 0; i < busStation.get(count).getBusStationName().length(); i++) {
                    if(i == 0){
                        canvas.drawText(String.valueOf(busStation.get(count).getBusStationName().charAt(i)),
                                defaultPoint + count * lineWidth - (textWidth/2),
                                lineHeight + lineWidth/2+ 30 + (i + 2) * textWidth,
                                ((itemClick)&&(count==witchItem))?clickPaint:textPaint);
                    }else{
                        canvas.drawText(String.valueOf(busStation.get(count).getBusStationName().charAt(i)),
                                defaultPoint + count * lineWidth - (textWidth/2),
                                lineHeight + lineWidth/2+ 30 + (i + 2) * textWidth,
                                ((itemClick)&&(count==witchItem))?clickPaint:textPaint);
                    }
                }
            }
        }
        Log.i("drawTest","finish");
    }

    private OnBusStationClickListener mListener;
    private OnBusStationClickListener elseListener;
    public void setOnStationClickListener(OnBusStationClickListener onStationClickListener,String location){
        if("this".equals(location)){
            this.mListener = onStationClickListener;
        }else {
            elseListener = onStationClickListener;
        }
    }
}
