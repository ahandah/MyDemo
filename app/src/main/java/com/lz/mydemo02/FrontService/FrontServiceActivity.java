package com.lz.mydemo02.FrontService;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lz.mydemo02.FloatBall.FloatBallColorActivity;
import com.lz.mydemo02.FloatBall.MyService2;
import com.lz.mydemo02.R;

public class FrontServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_service);

        startService(new Intent(this, MyService2.class));
    }
}
