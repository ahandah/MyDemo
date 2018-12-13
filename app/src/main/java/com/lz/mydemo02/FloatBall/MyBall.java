package com.lz.mydemo02.FloatBall;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lz.mydemo02.FloatBall.MyService;
import com.lz.mydemo02.R;

import java.util.Collections;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by Administrator on 2018/9/26.
 */

public class MyBall extends View {
//        extends android.support.v7.widget.AppCompatTextView {
    public MyBall(Context context) {
        super(context);
        init(context);
    }

    public MyBall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyBall(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(targetColor);
//        canvas.drawCircle(250, 250, 250, paint);
//        if (bitmap != null) {
//            canvas.drawBitmap(bitmap, 0, 0, paint);
//        }
        path.addCircle(250, 250, 250, Path.Direction.CCW);
        canvas.clipPath(path);

        if (cropBitmap != null) {

            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale, cropBitmap.getWidth() / 2, cropBitmap.getHeight() / 2);
            canvas.drawBitmap(cropBitmap, matrix, paint);
        }


        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(40);
        canvas.drawCircle(250, 250, 230, paint);

        paint.setStrokeWidth(1);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(250, 250, 210, paint);


        paint.setColor(Color.rgb(
                255 - Color.red(targetColor),
                255 -Color.green(targetColor),
                255 - Color.blue(targetColor)
        ));

//        paint.setStrokeWidth(10);
//        canvas.drawPoint(250, 250, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        paint.setTextSize(40);

        canvas.drawRect(250, 250, 260, 260, paint);

        /**
         * 上半弧
         * 起始-70逆时针,然后都是顺时针,一段内容间隔30,数字间隔7,字母间隔8
         *
         * */
//        canvas.rotate(-80, lp.width / 2, lp.height / 2);
//        String s = "BLUE",
//                s2 = "GREEN",
//                s3 = "900";
//        for (String c : s.split("")) {
//            if (c.equals("")) continue;
//            canvas.drawText(c, 251, 40, paint);
//            canvas.rotate(8, lp.width / 2, lp.height / 2);
//        }
//        canvas.rotate(5, lp.width / 2, lp.height / 2);
//        for (String c : s2.split("")) {
//            if (c.equals("")) continue;
//            canvas.drawText(c, 251, 40, paint);
//            canvas.rotate(8, lp.width / 2, lp.height / 2);
//        }
//        canvas.rotate(5, lp.width / 2, lp.height / 2);
//        for (String c : s3.split("")) {
//            if (c.equals("")) continue;
//            canvas.drawText(c, 251, 40, paint);
//            canvas.rotate(7, lp.width / 2, lp.height / 2);
//        }
//
//        String s4 = "#263238";
//        canvas.rotate(5, lp.width / 2, lp.height / 2);
//        for (String c : s4.split("")) {
//            if (c.equals("")) continue;
//            canvas.drawText(c, 251, 40, paint);
//            canvas.rotate(7, lp.width / 2, lp.height / 2);
//        }

        /**
         * 下半弧
         *
         * */

        String RGBtoHex = "#" + Integer.toHexString(Color.red(targetColor)) + Integer.toHexString(Color.green(targetColor)) + Integer.toHexString(Color.blue(targetColor));
        canvas.rotate(70, lp.width / 2, lp.height / 2);
        for (String c : RGBtoHex.split("")) {
            if (c.equals("")) continue;
            canvas.drawText(c, 250, 494, paint);
            canvas.rotate(-7, lp.width / 2, lp.height / 2);
        }

        canvas.rotate(-50, lp.width / 2, lp.height / 2);

        String s2 = (lp.x + lp.width / 2 ) + "," + (lp.y + lp.height / 2) ;
        for (String c : s2.split("")) {
            if (c.equals("")) continue;
            canvas.drawText(c, 250, 494, paint);
            canvas.rotate(-5, lp.width / 2, lp.height / 2);
        }



    }

    private int sWidth, sHeight;
    private ImageView imageView;
    private String RGBtoHex;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();

    private void init(Context context) {
        this.context = context;
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        sWidth = dm.widthPixels;
        sHeight = dm.heightPixels;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        lp.width = 500;
        lp.height = 500;
        lp.x = 0;
        lp.y = 0;
//        lp.x = width / 2;
//        lp.y = height / 2;
        lp.gravity = Gravity.LEFT | Gravity.TOP ;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.format = PixelFormat.RGBA_8888 | PixelFormat.TRANSLUCENT;

        setWindowManager(windowManager);
        setLayoutParams(lp);

        windowManager.addView(this, lp);
        //        setBackgroundColor(Color.BLACK);

    }

    float dx = 0, dy = 0, mx = 0, my = 0;
    WindowManager windowManager;
    WindowManager.LayoutParams lp;
    float size = 0;
    private Bitmap screenBitmap, bitmap, cropBitmap;

    private Context context;

    private int targetColor = Color.WHITE;

    private boolean doubleP = false;
    private float scale = 1f;

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

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                doubleP = false;

                if (context instanceof MyService) {
                    ((MyService) context).startScreenShot();
                }

                dx = event.getRawX();
                dy = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:

                if (doubleP) {

                    float distanceX = event.getX(0) - event.getX(1);
                    float distanceY = event.getY(0) - event.getY(1);
                    float newDistance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                    scale += (newDistance - startDistance) / 100;
                    startDistance = newDistance;

                    if (scale < 1) {
                        scale = 1;
                    }

                    if (scale > 15) {
                        scale = 15;
                    }


                } else {

                    int speed = 1;

                    if (scale > 3) {
                        speed = (int) (scale - 2);
                    }

                    mx = (int) (event.getRawX() - dx) / speed;
                    my = (int) (event.getRawY() - dy) / speed;

                    if (mx > size || mx < -size) {
                        lp.x += mx;
                        dx = event.getRawX();
                    }
                    if (my > size || my < -size) {
                        lp.y += my;
                        dy = event.getRawY();
                    }

                }



                break;

            case MotionEvent.ACTION_UP:

                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                float distanceX = event.getX(0) - event.getX(1);
                float distanceY = event.getY(0) - event.getY(1);
                startDistance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                if (doubleP == false) {
                    doubleP = true;
                }

                break;

            case MotionEvent.ACTION_POINTER_UP:

                if (doubleP == true) {
                    doubleP = false;
                }
                break;
        }

        if (screenBitmap != null) {

            if (lp.x < -lp.width / 2) {lp.x = -lp.width / 2;}
            if (lp.x > screenBitmap.getWidth() - lp.width * 3 / 2) {lp.x = screenBitmap.getWidth() - lp.width * 3 / 2;}
            if (lp.y < -lp.height / 2) {lp.y = -lp.height / 2;}
            if (lp.y > screenBitmap.getHeight() - lp.height * 3 / 2) {lp.y = screenBitmap.getHeight() - lp.height * 3 / 2;}

//                        matrix.setScale(1, 1);

            cropBitmap = Bitmap.createBitmap(screenBitmap, lp.x + lp.width / 2, lp.y + lp.height / 2,
                    lp.width, lp.height);

            targetColor = cropBitmap.getPixel(lp.width / 2, lp.height / 2);

            invalidate();
        }

        windowManager.updateViewLayout(this,lp);

        return false;
    }

    private float startDistance;

    private void getBitmapCircle() {
        bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        path.addCircle(250, 250, 200, Path.Direction.CCW);
        canvas.clipPath(path);
        canvas.drawBitmap(cropBitmap, 0, 0, paint);
    }
}
