package com.insuranceline.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.insuranceline.controller.AlarmIntentService;

import timber.log.Timber;

/**
 * Created by zeki on 21/02/2016.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    public static final int ALARM_FOR_RESTART = 101;
    public static final int ALARM_FOR_BOOST = 102;
    public static final int ALARM_FOR_REMINDER = 103;

    public static final String RECEIVER_ACTION_TYPE = "RECEIVER_ACTION_TYPE";
    @Override
    public void onReceive(Context context, Intent intent) {

        Timber.d("onReceive()");

        Intent service = new Intent(context, AlarmIntentService.class);

        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Timber.d("Action: ALARM_FOR_RESTART");
            service.putExtra(RECEIVER_ACTION_TYPE, ALARM_FOR_RESTART);
            startWakefulService(context, service);
        }else {
            int code = intent.getIntExtra("requestCode", 0);

            if (code == ALARM_FOR_BOOST){
                Timber.d("Action: ALARM_FOR_BOOST");
                service.putExtra(RECEIVER_ACTION_TYPE, ALARM_FOR_BOOST);
                startWakefulService(context, service);
            }else if (code == ALARM_FOR_REMINDER){
                Timber.d("Action: ALARM_FOR_REMINDER");
                service.putExtra(RECEIVER_ACTION_TYPE, ALARM_FOR_REMINDER);
                startWakefulService(context, service);
            }else {
                Timber.e("No Action");
            }
        }

    }
}
