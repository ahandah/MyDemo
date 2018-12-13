package com.lz.mydemo02;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lz.mydemo02.FloatBall.MyService;
import com.lz.mydemo02.FloatBall.MyService2;
import com.lz.mydemo02.MyColorSelect.MyView;

import java.util.Arrays;

public class TestActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private ImageReader imageReader;
    private Handler childHandler;
    private Handler mainHandler;
    private CameraCaptureSession cameraCaptureSession;

    private ImageView myImageView;
    private Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        startService(new Intent(this, MyService2.class));

//        surfaceView = findViewById(R.id.surfaceView);
//        surfaceHolder = surfaceView.getHolder();
//        setSurface();

//        myImageView = findViewById(R.id.miv);


//        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
//        myImageView.setImageBitmap(bitmap);


//        Resources resources = this.getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        Bitmap ivBitmap = ((BitmapDrawable) myImageView.getDrawable()).getBitmap();
//        Matrix matrix = new Matrix();
//        matrix.setScale(width * 1.0f / ivBitmap.getWidth(), width * 1.0f / ivBitmap.getWidth());
//
//        bitmap = Bitmap.createBitmap(ivBitmap, 0, 0, ivBitmap.getWidth(), ivBitmap.getHeight(), matrix, true);
//
//        System.out.println(bitmap.getWidth() + "     " + bitmap.getHeight() + "   image view  " + myImageView.getWidth() + "  " + myImageView.getHeight());


//        MyView tv = findViewById(R.id.tv);


//        tv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                System.out.println("event action  " + motionEvent.getAction());
//
//
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        System.out.println("event action down");
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//
//                        System.out.println("event action move");
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        System.out.println("event action up");
//                        break;
//
//                    case MotionEvent.ACTION_POINTER_DOWN:
//                        System.out.println("event action 2down");
//                        break;
//                }
//
//                return false;
//            }
//        });

    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        int x = (int) event.getX();
//        int y = (int) event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//
//                if (bitmap != null) {
//                    System.out.println("R  " + Integer.toHexString(Color.red(bitmap.getPixel(x, y))) +
//                            "G  " + Integer.toHexString(Color.green(bitmap.getPixel(x, y))) +
//                            "B  " + Integer.toHexString(Color.blue(bitmap.getPixel(x, y)))
//                    );
//                }
//
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
//
//        return true;
//    }

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;

            takePreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            if (null != cameraDevice) {
                cameraDevice.close();
                TestActivity.this.cameraManager = null;
            }
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Toast.makeText(TestActivity.this, "摄像头开启失败", Toast.LENGTH_SHORT).show();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void takePreview() {

        try{
            final CaptureRequest.Builder previewRequestBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            previewRequestBuilder.addTarget(surfaceHolder.getSurface());

            cameraDevice.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(), imageReader.getSurface()),
            new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (null == cameraDevice) return;
                    cameraCaptureSession = session;
                    try {
                        // 自动对焦
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // 打开闪光灯
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        // 显示预览
                        CaptureRequest previewRequest = previewRequestBuilder.build();
                        cameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(TestActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
                }
            }, childHandler);


        } catch (CameraAccessException e) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setCamera2() {
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        String mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        imageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG,1);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {

            }
        }, mainHandler);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraManager.openCamera(mCameraID, stateCallback, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void setSurface() {

        surfaceHolder.addCallback(new SurfaceHolder.Callback2() {
            @Override
            public void surfaceRedrawNeeded(SurfaceHolder holder) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                setCamera2();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }
}
