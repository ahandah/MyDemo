package com.lz.mydemo02.FloatBall;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lz.mydemo02.R;

import java.util.List;

public class FloatBallColorActivity extends AppCompatActivity {

    private final static int
            REQUEST_OVERLAYS_CODE = 1,
            REQUEST_CAPTURE_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_ball_color);


        requestOverlaysPermission();
        requestCapturePermission();
//        startService(new Intent(FloatBallColorActivity.this, MyService2.class));

    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void requestCapturePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图

            return;
        }

        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_CAPTURE_CODE);
    }

    public void requestOverlaysPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, REQUEST_OVERLAYS_CODE);
            } else {
                //TODO do something you need
//                Intent it = new Intent(this, MyService.class);
//                startService(it);
//                this.finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CAPTURE_CODE:

                if (resultCode == RESULT_OK && data != null) {
                    MyService.setResultData(data);
                    startService(new Intent(getApplicationContext(), MyService.class));
                    finish();
                }
                break;
        }

    }

}
