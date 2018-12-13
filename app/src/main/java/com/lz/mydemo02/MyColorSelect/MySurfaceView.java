package com.lz.mydemo02.MyColorSelect;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.lz.mydemo02.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/9/25.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback2, Runnable{

    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private boolean mIsDrawing;

    private int x = 0, y = 0;
    private Paint mPaint;
    private Path mPath;

    private Bitmap bitmap, sourceBitmap;

    private Context context;

    public MySurfaceView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;

        new Thread(this).start();

        sourceBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);

//        System.out.println("this width " + this.getWidth() + " width " + sourceBitmap.getWidth() + "   " +  + (this.getWidth() * 1.0 / sourceBitmap.getWidth()));
//        System.out.println("this height " + this.getHeight() + " height " + sourceBitmap.getHeight() + "  " + (this.getHeight() / sourceBitmap.getHeight()));



        float scaleW = (float) (this.getWidth() * 1.0 / sourceBitmap.getWidth());
        float scaleH = (float) (this.getHeight() * 1.0 / sourceBitmap.getHeight());
        Matrix matrix = new Matrix();
        matrix.postScale(scaleW, scaleH);

//        bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, this.getWidth(), this.getHeight(), matrix, true);
        bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            try {
                mCanvas = mSurfaceHolder.lockCanvas();
//                mCanvas.drawColor(Color.WHITE);
//                mCanvas.drawPath(mPath, mPaint);
//                mCanvas.bit
//                System.out.println("" + bitmap==null);

                mCanvas.drawBitmap(bitmap, 0, 0, mPaint);
            } catch (Exception e) {

            } finally {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }

//            x ++;
//            y = (int) (100 * Math.sin(2 * x * Math.PI / 100) + 400);
//
//            mPath.lineTo(x, y);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                mPath.moveTo(x, y);

                if (bitmap != null) {
//                    System.out.println("bitmap  color  R " + Integer.toHexString(Color.red(bitmap.getPixel(x, y)))
//                            + " G " + Integer.toHexString(Color.green(bitmap.getPixel(x, y)))
//                            + " B " + Integer.toHexString(Color.blue(bitmap.getPixel(x, y))));

                    ArrayList<Integer> colors = new ArrayList<>();
                    for (int i = 0; i < bitmap.getWidth(); i ++) {
                        for (int j = 0; j < bitmap.getHeight(); j ++) {
                            int item = bitmap.getPixel(i, j);
                            colors.add(item);
                        }
                    }

                    System.out.println("bitmap  color  R " + Integer.toHexString(Color.red(colors.get(1000)))
                            + " G " + Integer.toHexString(Color.green(colors.get(1000)))
                            + " B " + Integer.toHexString(Color.blue(colors.get(1000))));

                }
                break;

            case MotionEvent.ACTION_MOVE:
//                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void initView() {
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);

        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(5);

        mPath = new Path();
        mPath.moveTo(0, 100);

    }


}
