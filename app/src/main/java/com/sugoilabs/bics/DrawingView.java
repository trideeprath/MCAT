package com.sugoilabs.bics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by trath on 2/20/2015.
 */

public class DrawingView extends View implements View.OnClickListener {

    protected Paint mPaint;
    protected Bitmap mBitmap;
    protected Canvas mCanvas;
    float mx;
    float my;
    boolean isDrawing;
    float mStartX;
    float mStartY;
    ImageButton rectangle;
    ImageButton line;
    ImageButton undo;
    int selectedShape = 1;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        rectangle = (ImageButton) findViewById(R.id.rectangle);
        line = (ImageButton) findViewById(R.id.line);
        undo = (ImageButton) findViewById(R.id.undo);

        rectangle.setOnClickListener(this);
        line.setOnClickListener(this);
        undo.setOnClickListener(this);
    }

    protected void init() {
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //mPaint.setStrokeWidth(TOUCH_STROKE_WIDTH);
        mPaint.setStrokeWidth(2);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mx = event.getX();
        my = event.getY();

        if (selectedShape == 1) {
            onTouchEventRectangle(event);
        }
        if (selectedShape == 2) {
            onTouchEventStraightLine(event);
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);


        if (selectedShape == 1) {
            onDrawRectangle(canvas);
        } else if (selectedShape == 2) {
            onDrawStraightLine(canvas);
        }

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
                drawRectangle(mCanvas, mPaint);
                invalidate();
                break;
        }
    }

    private void onDrawRectangle(Canvas canvas) {
        drawRectangle(canvas, mPaint);
    }

    private void drawRectangle(Canvas canvas, Paint paint) {
        float right = mStartX > mx ? mStartX : mx;
        float left = mStartX > mx ? mx : mStartX;
        float bottom = mStartY > my ? mStartY : my;
        float top = mStartY > my ? my : mStartY;
        canvas.drawRect(left, top, right, bottom, paint);
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
                drawStraightLine(mCanvas, mPaint);
                invalidate();
                break;
        }
    }

    private void onDrawStraightLine(Canvas canvas) {
        drawStraightLine(canvas, mPaint);
    }

    private void drawStraightLine(Canvas canvas, Paint paint) {
        float right = mStartX > mx ? mStartX : mx;
        float left = mStartX > mx ? mx : mStartX;
        float bottom = mStartY > my ? mStartY : my;
        float top = mStartY > my ? my : mStartY;
        //canvas.drawRect(left, top , right, bottom, paint);
        canvas.drawLine(left, top, right, bottom, paint);
    }

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



}
