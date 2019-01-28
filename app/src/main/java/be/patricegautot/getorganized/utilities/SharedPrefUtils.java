package be.patricegautot.getorganized.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

public class SharedPrefUtils {

    public static final String SHARED_PREFERENCES_FILE = "SharedPreferences101";

    public static final String SP_ALARM_VOLUME_KEY = "AlarmVolume";
    public static final int SP_ALARM_VOLUME_DEFAULT = 20;
    public static final int SP_ALARM_VOLUME_MIN = 0;
    public static final int SP_ALARM_VOLUME_MAX = 10;

    public static final String SP_ALARM_DURATION_KEY = "AlarmDuration";
    public static final int SP_ALARM_DURATION_DEFAULT = 10;
    public static final int SP_ALARM_DURATION_MIN = 2;
    public static final int SP_ALARM_DURATION_MAX = 20;

    public static final String SP_ALERT_DIALOG_SHOW_KEY = "AlertDialogShouldShow";
    public static final boolean SP_ALERT_DIALOG_SHOW_DEFAULT = true;

    public static final String SP_SHOW_HINT_KEY = "ShowHint";
    public static final int SP_SHOW_HINT_DEFAULT = 0;

    public static final String SP_LAST_AD_KEY = "LastAd";
    public static final long SP_LAST_AD_DEFAULT = 0;

    public static final String SP_TOTAL_AD_TODAY_KEY = "TotalAdToday";
    public static final int SP_TOTAL_AD_TODAY_DEFAULT = 0;
    public static final int SP_TOTAL_AD_TODAY_MAX = 10;

    public static boolean getShowAlertDialog(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        boolean ans = sharedPreferences.getBoolean(SP_ALERT_DIALOG_SHOW_KEY, SP_ALERT_DIALOG_SHOW_DEFAULT);
        //Log.e("SharedPrefUtils", "showalertdialog is " + (ans ? "1" : "0") );
        return ans;
    }

    public static int getAlarmVolume(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SP_ALARM_VOLUME_KEY, SP_ALARM_VOLUME_DEFAULT);
    }

    public static int getAlarmDuration(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SP_ALARM_DURATION_KEY, SP_ALARM_DURATION_DEFAULT);
    }

    public static boolean getShowHint(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        int val = sharedPreferences.getInt(SP_SHOW_HINT_KEY, SP_SHOW_HINT_DEFAULT);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SP_SHOW_HINT_KEY, val+1);
        editor.apply();
        if(val%3  == 0){
            return true;
        } else {
            return false;
        }
    }

    public static long getLastAd(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(SP_LAST_AD_KEY, SP_LAST_AD_DEFAULT);
    }

    public static int getTotalAdToday(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SP_TOTAL_AD_TODAY_KEY, SP_TOTAL_AD_TODAY_DEFAULT);
    }


    //----------------------------SETTERS--------------------------------\\
    public static void setShowAlertDialog(Context context, boolean choice) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SP_ALERT_DIALOG_SHOW_KEY, choice);
        editor.apply();
        //Log.e("SharedPrefUtils", "ShowAlertDialog now to " + (choice ? "1" : "0"));
    }

    public static void setAlarmVolume(Context context, int volume){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(volume > SP_ALARM_VOLUME_MAX) volume = SP_ALARM_VOLUME_MAX;
        if(volume < SP_ALARM_VOLUME_MIN) volume = SP_ALARM_VOLUME_MIN;
        editor.putInt(SP_ALARM_VOLUME_KEY, volume);
        editor.apply();
    }

    public static void setAlarmDuration(Context context, int duration){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(duration > SP_ALARM_DURATION_MAX) duration = SP_ALARM_DURATION_MAX;
        if(duration < SP_ALARM_DURATION_MIN) duration = SP_ALARM_DURATION_MIN;
        editor.putInt(SP_ALARM_DURATION_KEY, duration);
        editor.apply();
    }

    public static void setLastAd(Context context, long lastAd){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(SP_LAST_AD_KEY, lastAd);
        editor.apply();
    }

    public static void setTotalAdToday(Context context, int adToday){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SP_TOTAL_AD_TODAY_KEY, adToday);
        editor.apply();
    }

    //-----------------------HELPERS----------------------------\\
    public static boolean canShowAd(Context context){
        long lastTime = getLastAd(context);
        long currentTime = Calendar.getInstance().getTimeInMillis();
        int totalAdToday = getTotalAdToday(context);

        //Log.e("AdUtils", "LastTime : " + lastTime + " currentTime : " + currentTime + " totalAdToday : " + totalAdToday + " for condition : lastTime/x : " + lastTime/(12*60*60*1000) + " currentTime/x : " + currentTime/(12*60*60*1000));

        if(lastTime/(12*60*60*1000) < currentTime/(12*60*60*1000)){
            setLastAd(context, currentTime);
            setTotalAdToday(context, 1);
            return true;
        }
        else if (totalAdToday < SP_TOTAL_AD_TODAY_MAX){
            setTotalAdToday(context, totalAdToday+1);
            return true;
        }

        return false;
    }

}
