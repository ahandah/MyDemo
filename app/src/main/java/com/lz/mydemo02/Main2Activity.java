package com.lz.mydemo02;

import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        读取系统正在运行的App信息
//        获取安装应用信息
        List<PackageInfo> list = getPackageManager().getInstalledPackages(0);
        for (PackageInfo info : list) {
            Log.d("PackageInfo ---  ", info.packageName + info.applicationInfo + info.sharedUserId);
        }
//        获取当前内存进程信息
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfos) {
            Debug.MemoryInfo[] processMemoryInfo = manager.getProcessMemoryInfo(new int[]{info.pid});
            Log.d("RunningAppInfo----", info.processName + "  " + processMemoryInfo[0]);
        }
    }
}
