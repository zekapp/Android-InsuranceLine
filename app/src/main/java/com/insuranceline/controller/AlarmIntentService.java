package com.insuranceline.controller;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.insuranceline.App;
import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.receiver.AlarmReceiver;
import com.insuranceline.ui.splash.SplashActivity;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by zeki on 21/02/2016.
 */
public class AlarmIntentService extends IntentService {

    @Inject
    DataManager mDataManager;

    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.get(this).getComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int actionType = intent.getIntExtra(AlarmReceiver.RECEIVER_ACTION_TYPE, 0);

        if (actionType == AlarmReceiver.ALARM_FOR_RESTART) {
            resetNotifications();
        } else if (actionType == AlarmReceiver.ALARM_FOR_BOOST) {
            handleBoostNotification();
        } else if (actionType == AlarmReceiver.ALARM_FOR_REMINDER) {
            handleReminderNotification();
        } else {
            Timber.e("Unknown Handle: actionType: %s", actionType);
        }
    }

    private void handleReminderNotification() {

        //set next alarm.
        mDataManager.setNextReminderNotification();

        showNotification(
                getString(R.string.reminder_notification_push_text),
                getString(R.string.reminder_notification_content));
    }

    private void handleBoostNotification() {

        showNotification(
                getString(R.string.boost_notification_push_text),
                getString(R.string.boost_notification_content));
    }

    private void resetNotifications() {
        mDataManager.resetBoostNotification();
        mDataManager.resetReminderNotification();
    }

    private void showNotification(String pushText, String content) {
        Intent diagnosticIntent = new Intent(this, SplashActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                diagnosticIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notifyManger = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(pushText)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(content))
                        .setContentText(content)
                        .setAutoCancel(true);


        mBuilder.setContentIntent(contentIntent);

        notifyManger.notify(1, mBuilder.build());
    }
}
