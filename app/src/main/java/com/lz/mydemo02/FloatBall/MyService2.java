package com.lz.mydemo02.FloatBall;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import com.lz.mydemo02.FrontService.FrontServiceActivity;
import com.lz.mydemo02.MainActivity;
import com.lz.mydemo02.R;
import com.lz.mydemo02.TestActivity;

import static android.app.PendingIntent.getActivity;

public class MyService2 extends Service {
    public MyService2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("执行了onCreat()");
//        stopForeground(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 为该通知设置一个id
        int notifyID = 1;
        // 通知渠道的id
        String CHANNEL_ID = "my_channel_01";

        //        添加下列代码将后台Service变成前台Service
        //        构建"点击通知后打开MainActivity"的Intent对象
        Intent notificationIntent = new Intent(this,FrontServiceActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        //构建通知渠道
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channel 1", NotificationManager.IMPORTANCE_LOW);
        channel.setDescription("description  -");
        mNotificationManager.createNotificationChannel(channel);

        // Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(this)
                .setContentTitle("New Message")
                .setContentText("You've received new messages.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId(CHANNEL_ID)
                .build();
        // 发布通知
        mNotificationManager.notify(notifyID, notification);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
