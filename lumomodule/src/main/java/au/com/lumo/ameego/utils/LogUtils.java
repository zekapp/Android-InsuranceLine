package au.com.lumo.ameego.utils;

import android.util.Log;

/**
 * Created by appscoredev2 on 8/07/15.
 */
public class LogUtils {

    public static String makeLogTag(Object obj){ return  obj.getClass().getSimpleName();}

    public static void i(String tag, String str) {
        longInfo(tag, str);
    }

    public static void longInfo(String TAG, String str) {
        if(str.length() > 4000) {
//            Log.i(TAG, str.substring(0, 4000));
            longInfo(TAG, str.substring(4000));
        }
//        else
//            Log.i(TAG, str);
    }

    public static void LOGD(final Object obj, String message) {
        LOGD(makeLogTag(obj), message);
    }

    public static void LOGE(final Object obj, String message) {
        LOGE(makeLogTag(obj), message);
    }
}
