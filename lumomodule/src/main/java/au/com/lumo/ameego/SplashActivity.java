package au.com.lumo.ameego;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import au.com.lumo.ameego.utils.WarningUtilsMD;


public class SplashActivity extends AppCompatActivity {

    public static final String  TAG         = SplashActivity.class.getSimpleName();
    private static final int    SPLASH_TIME = 2500;

    private Thread splashTread;
    private boolean interrupted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startWaitThread();
    }

    private void startWaitThread() {
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized(this){wait(SPLASH_TIME);}
                } catch(InterruptedException e) {
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
        WarningUtilsMD.stopProgress();

        overridePendingTransition(0, 0);

        Intent intent = new Intent();
        intent.setClass(this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
