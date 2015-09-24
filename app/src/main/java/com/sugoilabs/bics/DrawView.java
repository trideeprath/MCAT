package com.sugoilabs.bics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by trath on 2/24/2015.
 */
public class DrawView extends View  {

    protected Paint mPaint;
    protected Paint mPaintFinal;
    protected Bitmap mBitmap;
    protected Canvas mCanvas;
    float mx;
    float my;
    float mStartX;
    float mStartY;
    float mStopX;
    float mStopY;
    int mCurrentShape=1;
    boolean isDrawing=true;
    int selectedShape=1;
    ImageButton rectangle;
    ImageButton line;
    ImageButton undo;
    float[][] straightLineArray = new float[100][4];
    float[][] rectangleArray = new float[100][4];
    int rectangleCount =0;
    int straightLineCount =0;

    public DrawView(Context context, AttributeSet attrs){
        super(context,attrs);
        setupDrawing();

    }

    private void setupDrawing() {
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(4);
        mPaintFinal = mPaint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        isDrawing=true;
        mCurrentShape =1;
        int RECTANGLE =1;

        if (SelectedShape.selectedShape == 1) {
            onDrawRectangle(canvas);
        } else if (SelectedShape.selectedShape == 2) {
            onDrawStraightLine(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mx = event.getX();
        my = event.getY();
        mCurrentShape =1;
        if (SelectedShape.selectedShape == 1) {
            onTouchEventRectangle(event);
        }
        if (SelectedShape.selectedShape == 2) {
            onTouchEventStraightLine(event);
        }
        return true;
    }

    private void onTouchEventRectangle(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mStopX=mx;
                mStopY=my;
                saveRectangle(mStartX,mStartY, mStopX, mStopY);
                drawRectangle(mCanvas,mPaintFinal);
                invalidate();
                break;
        }
    }




    private void onDrawRectangle(Canvas canvas) {
        drawRectangle(canvas,mPaint);
    }

    private void drawRectangle(Canvas canvas,Paint paint){
        float right = mStartX > mx ? mStartX : mx;
        float left = mStartX > mx ? mx : mStartX;
        float bottom = mStartY > my ? mStartY : my;
        float top = mStartY > my ? my : mStartY;
        canvas.drawRect(left, top , right, bottom, paint);

    }

    private void onTouchEventStraightLine(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mStopX=mx;
                mStopY=my;
                saveStraightLine(mStartX,mStartY, mStopX,mStopY);
                drawStraightLine(mCanvas, mPaint);
                invalidate();
                break;
        }
    }



    private void onDrawStraightLine(Canvas canvas) {
        drawStraightLine(canvas, mPaint);
    }

    private void drawStraightLine(Canvas canvas, Paint paint) {
        canvas.drawLine(mStartX,mStartY, mx , my,paint);
    }

    private void saveStraightLine(float mStartX, float mStartY, float mStopX, float mStopY) {

        int length = straightLineArray.length;
        straightLineArray[straightLineCount][0] = mStartX;
        straightLineArray[straightLineCount][1] = mStartY;
        straightLineArray[straightLineCount][2] = mStopX;
        straightLineArray[straightLineCount][3] = mStartY;

        Log.d("straightLines",Arrays.deepToString(straightLineArray));
        SelectedShape.straightLineArray = straightLineArray;
                straightLineCount++;

    }

    private void saveRectangle(float mStartX, float mStartY, float mStopX, float mStopY) {
        int length = rectangleArray.length;
        rectangleArray[rectangleCount][0] = mStartX;
        rectangleArray[rectangleCount][1] = mStartY;
        rectangleArray[rectangleCount][2] = mStopX;
        rectangleArray[rectangleCount][3] = mStopY;

        Log.d("rectangles",Arrays.deepToString(rectangleArray));
        SelectedShape.rectangleArray= rectangleArray;
        rectangleCount++;
    }


    /*
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rectangle) {
            selectedShape = 1;
        }
        if (v.getId() == R.id.line) {
            selectedShape = 2;
        }
        if (v.getId() == R.id.undo) {

        }
        Log.d("shape", String.valueOf(selectedShape));
    }
    */



}
