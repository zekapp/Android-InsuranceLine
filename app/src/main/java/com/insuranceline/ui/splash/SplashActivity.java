package com.insuranceline.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.insuranceline.R;
import com.insuranceline.ui.DispatchActivity;
import com.insuranceline.utils.Utils;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 29,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME = 2500;

    private Thread splashTread;
    private boolean interrupted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        //print KeyHash
        Utils.printHashKey(this);
        startWaitThread();
    }

    private void startWaitThread() {
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized(this){
                        wait(SPLASH_TIME);
                    }
                } catch(InterruptedException e) {
                    Timber.e(e.getMessage());
                } finally {
                    if(!interrupted){
                        closeSplash();
                        interrupt();
                    }
                }
            }
        };

        splashTread.start();
    }


    private void closeSplash(){
        Intent intent = new Intent();
        intent.setClass(this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
