package be.patricegautot.getorganized.utilities;

import android.util.Log;

import java.util.Calendar;

public class TimeUtils {

    public static long nextAppearance(int daySet, int hourSet, int minuteSet){
        Calendar calendar = Calendar.getInstance();

        daySet+=2;
        if(daySet == 8) daySet = 1;  // Me : Mon = 0 Tue = 1... -> Cal : Sun = 1 Mon = 2...

        calendar.set(Calendar.DAY_OF_WEEK, daySet);
        calendar.set(Calendar.HOUR_OF_DAY, hourSet);
        calendar.set(Calendar.MINUTE, minuteSet);
        calendar.set(Calendar.SECOND, 0);

        long timeOut = calendar.getTimeInMillis();

        if(timeOut < System.currentTimeMillis() + 5*1000){
            timeOut += 7*24*60*60*1000;
        }

        return timeOut;
    }

}
