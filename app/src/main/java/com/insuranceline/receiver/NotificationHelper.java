package com.insuranceline.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.di.qualifier.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by zeki on 21/02/2016.
 */
@Singleton
public class NotificationHelper {

    private final Context context;
    private final PreferencesHelper mPreferencesHelper;
    private final AlarmManager mAlarmManager;

    @Inject
    NotificationHelper(@ApplicationContext Context context, PreferencesHelper preferencesHelper,
                       AlarmManager alarmManager){
        this.context = context;
        this.mPreferencesHelper = preferencesHelper;
        this.mAlarmManager = alarmManager;
    }

    /**
     * Set boost Notification
     *
     * After starting one of the goal, this notification sets for next 21 days.
     *
     * */
    public void setBoostNotification(){
        long period = mPreferencesHelper.getBoostNotificationPeriod(); // default is 21 days.
        long baseTime = System.currentTimeMillis();
        mPreferencesHelper.saveBaseTimeOfBoostNotification(baseTime);
        //set boost notification
        setAlarmForBoostNotification(baseTime + period);
    }

    /**
     * REBOOT
     * If device reboots then call this function
     *
     * */
    public void resetBoostNotification(){
        long period = mPreferencesHelper.getBoostNotificationPeriod(); // default is 21 days.
        long baseTime = mPreferencesHelper.getBaseTimeOfBoostNotification();

        setAlarmForBoostNotification(period + baseTime);

    }

    /**
     * Set Reminder notification that reminds the user for open app every 14 days
     *
     * Each time app comes foreground, base time sets
     * */
    public void setNextReminderNotification(){

        long period   = mPreferencesHelper.getReminderNotificationPeriod(); // default is 14 days
        long baseTime = System.currentTimeMillis();
        mPreferencesHelper.saveBaseTimeOfReminderNotification(baseTime);

        setAlarmForReminderNotification(period + baseTime);
    }

    /**
     * REBOOT
     * If device reboots then call this function
     * */
    public void resetReminderNotification(){

        long period = mPreferencesHelper.getReminderNotificationPeriod(); // default is 14 days
        long baseTime = mPreferencesHelper.getBaseTimeOfReminderNotification();

        setAlarmForReminderNotification(period + baseTime);
    }

    /**
     * Set Boost notification pending intent
     * */
    private void setAlarmForBoostNotification(long fireTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, fireTime, getBoostPendingIntent());
        }else {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, fireTime, getBoostPendingIntent());
        }
    }

    /**
     * Set Reminder Notifcation Pending intent
     * */
    private void setAlarmForReminderNotification(long fireTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, fireTime, getReminderPendingIntent());
        }else {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, fireTime, getReminderPendingIntent());
        }
    }

    private PendingIntent getReminderPendingIntent() {
        Intent intent = new Intent(context, ReminderReceiver.class);
        return PendingIntent.getBroadcast(context,
                AlarmReceiver.ALARM_FOR_REMINDER,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getBoostPendingIntent() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        return PendingIntent.getBroadcast(context,
                AlarmReceiver.ALARM_FOR_BOOST,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
