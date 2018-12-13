package com.lz.mydemo02.MyColorSelect;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lz.mydemo02.FloatBall.MyBall;
import com.lz.mydemo02.R;

/**
 * Created by Administrator on 2018/9/26.
 */

public class MyImageView extends RelativeLayout {

    private ImageView myImageView;
    private Bitmap ivBitmap, bitmap;
    private int sizeH, width, height;

    public MyImageView(Context context) {
        super(context);
        init();
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    TextView tv;
    MyBall myBall;

    private void init() {


        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_my_image,this, true);

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;

        myImageView = view.findViewById(R.id.miv);

        ivBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
        Matrix matrix = new Matrix();
        matrix.setScale(width * 1.0f / ivBitmap.getWidth(), width * 1.0f / ivBitmap.getWidth());

        bitmap = Bitmap.createBitmap(ivBitmap, 0, 0, ivBitmap.getWidth(), ivBitmap.getHeight(), matrix, true);

        myImageView.setImageBitmap(bitmap);



        System.out.println("height  " + height + " bitmap  h  " + bitmap.getHeight() );
        tv = new TextView(getContext());
        addView(tv);

        myBall = new MyBall(getContext());
        addView(myBall);

        myBall.setBackgroundColor(Color.parseColor("#87CEFA"));
        LayoutParams lp = new LayoutParams(200, 200);
        lp.setMargins(200, 200, 0, 0);
        myBall.setLayoutParams(lp);
//        myTv.layout(200, 200, 0, 0);

        sizeH = (int) ((myImageView.getHeight() - bitmap.getHeight()) / 2);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        sizeH = (int) ((myImageView.getHeight() - bitmap.getHeight()) / 2);
        System.out.println("size  h  " + sizeH);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

//        System.out.println("height   " + myImageView.getHeight());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                System.out.println("x   " + myBall.getX() + "  y   " + myBall.getY());

                break;

            case MotionEvent.ACTION_MOVE:
                System.out.println("move   h " + y);
                int px = x, py = y - sizeH;

                if (py < 0) {py = 1;}
                if (py > bitmap.getHeight()) {py = bitmap.getHeight() - 1;}

                if (bitmap != null && px > 0 && px < bitmap.getWidth() && py > 0 && py < bitmap.getHeight()) {

                    int pixel = bitmap.getPixel(px, py);

                    int red = Color.red(pixel),
                            green = Color.green(pixel),
                            blue = Color.blue(pixel);

                    System.out.println("R  " + Integer.toHexString(red) +
                            "G  " + Integer.toHexString(green) +
                            "B  " + Integer.toHexString(blue));

                    myBall.setBackgroundColor(Color.rgb(red, green, blue));

                    LayoutParams lp = new LayoutParams(200, 200);

                    int lpx = x, lpy = y;
                    if (lpx < 0) {lpx = 0;}
                    if (lpx > bitmap.getWidth()) {lpx = bitmap.getWidth();}
                    if (lpy < sizeH) {lpy = sizeH;}
                    if (lpy > (bitmap.getHeight() + sizeH)) {lpy = (bitmap.getHeight() + sizeH);}
                    lp.setMargins(lpx, lpy, 0, 0);
                    myBall.setLayoutParams(lp);

//                    myTv.layout(lpx, lpy, 0 ,0);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

}
