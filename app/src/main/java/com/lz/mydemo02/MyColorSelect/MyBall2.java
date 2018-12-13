package com.lz.mydemo02.MyColorSelect;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.lz.mydemo02.FloatBall.MyService;

/**
 * Created by Administrator on 2018/9/26.
 */

public class MyBall2 extends android.support.v7.widget.AppCompatImageView {
//        extends android.support.v7.widget.AppCompatTextView {
    public MyBall2(Context context) {
        super(context);
        init(context);
    }

    public MyBall2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyBall2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private int width, height;

    private void init(Context context) {
        this.context = context;
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
        setBackgroundColor(Color.parseColor("#87CEFA"));
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(200, 200);
//        lp.setMargins(200, 200, 0, 0);
//        setLayoutParams(lp);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        int x = (int) event.getX(),
//            y = (int) event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                System.out.println("down   down   ");
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                System.out.println("move    move  " + x + "   " + y);
//
////                lp.horizontalMargin = x;
////                lp.horizontalMargin = y;
//
////                this.setTranslationX(x);
//
////                measure(x + getWidth(), y + getHeight());
////                layout(x, y, x + getWidth(), y + getHeight());
//
//
////              ---------------------悬浮窗口--------------------------------------------
//
////                WindowManager windowManager = (WindowManager) getContext().getApplicationContext().getSystemService(WINDOW_SERVICE);
////                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
////
////                lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
////                lp.width = 300;
////                lp.height = 300;
////                lp.gravity = Gravity.LEFT | Gravity.TOP ;
////                lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
////                lp.format = PixelFormat.RGBA_8888 | PixelFormat.TRANSLUCENT;
////
////                lp.x = (int) event.getRawX() - 150;
////                lp.y = (int) event.getRawY() - 150;
////                windowManager.updateViewLayout(this,lp);
//
//                break;
//        }
//
//        return true;
//    }

    float dx = 0, dy = 0, mx = 0, my = 0;
    WindowManager windowManager;
    WindowManager.LayoutParams lp;
    float size = 0;
    private Bitmap screenBitmap;

    private Context context;

    public void setWindowManager(WindowManager manager) {
        windowManager = manager;
    }

    public void setLayoutParams(WindowManager.LayoutParams lp) {
        this.lp = lp;
    }

    public void setBitmap(Bitmap screenBitmap) {
        this.screenBitmap = screenBitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                System.out.println("is MyService   " + (context instanceof MyService));
                if (context instanceof MyService) {
                    ((MyService) context).startScreenShot();
                }

                dx = event.getRawX();
                dy = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:

                mx = (int) (event.getRawX() - dx);
                my = (int) (event.getRawY() - dy);

                if (mx > size || mx < -size) {
                    lp.x += mx;
                    dx = event.getRawX();
                }
                if (my > size || my < -size) {
                    lp.y += my;
                    dy = event.getRawY();
                }

                if (screenBitmap != null) {

                    if (lp.x < 0) {lp.x = 0;}
                    if (lp.x > screenBitmap.getWidth() - lp.width) {lp.x = screenBitmap.getWidth() - lp.width;}
                    if (lp.y < 0) {lp.y = 0;}
                    if (lp.y > screenBitmap.getHeight() - lp.height) {lp.y = screenBitmap.getHeight() - lp.height;}

                    Matrix matrix = new Matrix();

                    Bitmap bitmap = Bitmap.createBitmap(screenBitmap, lp.x, lp.y,
                            lp.width, lp.height, matrix, false);

                    setImageBitmap(bitmap);
                }

                windowManager.updateViewLayout(this,lp);

                break;

            case MotionEvent.ACTION_UP:

                break;
        }

        return false;
    }
}
