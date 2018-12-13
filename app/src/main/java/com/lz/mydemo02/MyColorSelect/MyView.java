package com.lz.mydemo02.MyColorSelect;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        System.out.println("event action  " + event.getAction());


//            switch (event.getAction()) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("event action down");
                break;

            case MotionEvent.ACTION_MOVE:

//                System.out.println("event action move");
                break;

            case MotionEvent.ACTION_UP:
                System.out.println("event action up");
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                System.out.println("event action 2down");
                break;
        }

        return true;
    }
}
