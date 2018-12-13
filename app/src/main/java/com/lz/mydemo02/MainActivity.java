package com.lz.mydemo02;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
//    private PageIndicatorView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        } else {
            //xxxx为你原来给低版本设置的Type
//            mParams.type = WindowManager.LayoutParams.xxxx;
        }

        //权限判断
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(getApplicationContext())) {
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,10);
                return;
            } else {
                //执行6.0以上绘制代码
            }
        } else {
            //执行6.0以下绘制代码
        }


        viewPager = findViewById(R.id.viewPage);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

    }

    public void click(View view) {



        PendingIntent mainPendingIntent = PendingIntent.getActivity(
                this,
                111,
                new Intent(this, Main2Activity.class),
                0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("1", "channelName",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);

        }

//        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "1")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("title")
//                .setContentText("content text");
//                .setContentIntent(mainPendingIntent);

        NotificationCompat.Builder notification2 = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title22")
                .setContentText("content text22")
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVisibility(Notification.VISIBILITY_PUBLIC);
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setPriority(Notification.PRIORITY_MAX);
//                .setFullScreenIntent(mainPendingIntent, false);

//        notificationManager.notify(1239020, notification.build());
//        notificationManager.notify(1239020, notification2.build());
        notificationManager.notify(1239020, notification2.build());


//        String id = "my_channel_01";
//        String name="我是渠道名字";
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification notification = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
//            Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
//            Log.i("TAG", mChannel.toString());
//            notificationManager.createNotificationChannel(mChannel);
//            notification = new Notification.Builder(this)
//                    .setChannelId(id)
//                    .setContentTitle("5 new messages")
//                    .setContentText("hahaha")
//                    .setSmallIcon(R.mipmap.ic_launcher).build();
//        } else {
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setContentTitle("5 new messages")
//                    .setContentText("hahaha")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setOngoing(true);
////                    .setChannel(id);//无效
//            notification = notificationBuilder.build();
//        }
//        notificationManager.notify(111123, notification);

        System.out.println("click");

    }


}

class MyAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> list = new ArrayList<>();

    public MyAdapter(FragmentManager fm) {
        super(fm);

        list.add(new BlankFragment1());
        list.add(new BlankFragment2());
        list.add(new BlankFragment3());
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
