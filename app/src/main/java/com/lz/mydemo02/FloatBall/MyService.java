package com.lz.mydemo02.FloatBall;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.View;

import com.lz.mydemo02.R;

import java.nio.ByteBuffer;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

//    WindowManager.LayoutParams lp;
//    WindowManager windowManager;
    private MyBall myBall;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private ImageReader imageReader;
    private int sWidth, sHeight, density;
    private static Intent mResultData;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeAll();
        stopForeground(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        sWidth = dm.widthPixels;
        sHeight = dm.heightPixels;
        density = dm.densityDpi;

        imageReader = ImageReader.newInstance(sWidth, sHeight, PixelFormat.RGBA_8888, 1);

        myBall = new MyBall(this);
        myBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScreenShot();
            }
        });

        //设置Notification
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 为该通知设置一个id
        int notifyID = 1;
        // 通知渠道的id
        String CHANNEL_ID = "my_channel_01";

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("New Message")
                .setContentText("You've received new messages.")
                .setSmallIcon(R.mipmap.ic_launcher);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //构建通知渠道
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("description  -");
            mNotificationManager.createNotificationChannel(channel);

            builder.setChannelId(CHANNEL_ID);
        }

        Notification notification = builder.build();
        // 发布通知
        mNotificationManager.notify(notifyID, notification);

        startForeground(notifyID, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startVirtual() {
        if (mediaProjection != null) {
            setVirtualDisplay();
        } else {
            setUpMediaProjection();
            setVirtualDisplay();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setUpMediaProjection() {
        if (mResultData == null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(intent);
        } else {
            mediaProjection = getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK, mResultData);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setVirtualDisplay() {
        virtualDisplay = mediaProjection.createVirtualDisplay(
                "screen-mirror",
                sWidth, sHeight, density,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.getSurface(),
                null, null);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private MediaProjectionManager getMediaProjectionManager() {

        return (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    public static void setResultData(Intent data) {
        mResultData = data;
    }

    public void startScreenShot() {

        myBall.setVisibility(View.INVISIBLE);

        Handler handler1 = new Handler();

        startVirtual();

        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                startCapture();
            }
        }, 200);

    }

    private void startCapture() {


        Image image = imageReader.acquireLatestImage();

        if (image == null) {
            startScreenShot();
        } else {
            Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            //每个像素的间距
            int pixelStride = planes[0].getPixelStride();
            //总的间距
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * sWidth;
            Bitmap screenbitmap = Bitmap.createBitmap(sWidth + rowPadding / pixelStride, sHeight, Bitmap.Config.ARGB_8888);
            screenbitmap.copyPixelsFromBuffer(buffer);

//            screenbitmap = Bitmap.createBitmap(screenbitmap, )

            screenbitmap = cropBitmap(screenbitmap, image);

            Bitmap bitmap2 = Bitmap.createBitmap(screenbitmap.getWidth() + myBall.getWidth(), screenbitmap.getHeight() + myBall.getHeight(), Bitmap.Config.ARGB_8888);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(screenbitmap, myBall.getWidth() / 2, myBall.getHeight() / 2, paint);

            myBall.setBitmap(bitmap2);
            myBall.setVisibility(View.VISIBLE);

            image.close();
            virtualDisplay.release();
        }
    }

    private void closeAll() {
        virtualDisplay.release();
        imageReader.close();
    }

    private Bitmap cropBitmap(Bitmap bitmap, Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixel=new int[width];
        bitmap.getPixels(pixel,0,width ,0,0,width,1);
        int leftPadding=0;
        int rightPadding=width;
        for (int i=0;i<pixel.length;i++){
            if (pixel[i]!=0){
                leftPadding=i;
                break;
            }
        }
        for (int i=pixel.length-1;i>=0;i--){
            if (pixel[i]!=0){
                rightPadding=i;
                break;
            }
        }
        bitmap=Bitmap.createBitmap(bitmap,leftPadding, 0, rightPadding-leftPadding, height);
        return bitmap;
    }

}
