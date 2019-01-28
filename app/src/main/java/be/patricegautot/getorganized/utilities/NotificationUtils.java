package be.patricegautot.getorganized.utilities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

import be.patricegautot.getorganized.Alarm;
import be.patricegautot.getorganized.MainActivity;
import be.patricegautot.getorganized.R;
import be.patricegautot.getorganized.activities.AddTaskActivity;
import be.patricegautot.getorganized.objects.WeeklyTask;

public class NotificationUtils {

    public static final String NOTIFICATION_CHANNEL_ID = "Notifications";
    public static final int MAIN_ACTIVITY_RETURN_PENDING_INTENT_ID = 257984;

    public static void setScheduling(WeeklyTask task, Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, Alarm.class);
        /*byte[] bytes = ParcelableUtils.marshall(task);
        intent.putExtra("marshall", bytes);*/

        Bundle b = new Bundle();
        b.putParcelable("task", task);
        intent.putExtra("bundle", b);

        long firstTrigger;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        boolean[] days = task.getDays();
        int timeInt = task.getHourInt();
        int hour = timeInt/100;
        int minute = timeInt-(hour*100);

        long bestTime = Long.MAX_VALUE;
        int bestDay = -1;

        for (int i = 0; i < 7; i++) {
            if (days[i]) {
                firstTrigger = TimeUtils.nextAppearance(i, hour, minute);
                if(firstTrigger < bestTime){
                    bestTime = firstTrigger;
                    bestDay = i;
                }
            }
        }

        //Log.e("alarmScheduling", "best time for alarm is " + bestTime);

        if(bestDay != -1){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(bestTime, null), pendingIntent);
                //Log.e("Scheduling", "It's an alarm clock");
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, bestTime, pendingIntent);
                //Log.e("Scheduling", "Alarm set and allowed whilde idle");
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, bestTime, pendingIntent);
                //Log.e("Scheduling", "Alarm set and simple");
            }

            //Log.e("Scheduling", "time for next service call " + bestTime + " of task " + task.getTaskName() + " id :" + task.getId());
        }

    }

    public static void cancelAlarmForTask(WeeklyTask task, Context context){
        //Log.e("Scheduling", "cancel notif for task with id " + task.getId());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, Alarm.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public static void cancelAlarmForTaskId(int id, Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, Alarm.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public static void sendNotification(WeeklyTask task, Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String title = getTitle(context, task);

        String content = generateQuote(context);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    title,
                    NotificationManager.IMPORTANCE_HIGH);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(getPendingIntent(context))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(task.getId(), mBuilder.build());

        //Log.e("NotifUtils", "Notification Sent with id " + task.getId() + " = " + task.getTaskName());

    }

    public static String generateQuote(Context context){
        String[] quotesArray = context.getResources().getStringArray(R.array.motivational_quotes);
        final int randomIndex = new Random().nextInt(30);

        return quotesArray[randomIndex];
    }

    public static int getAudioFileIdFromRingtoneId(int ringtoneOption){

        switch (ringtoneOption){
            case WeeklyTask.TYPE_RINGTONE_EASY:
                return R.raw.ringtone_easy_soundfile;
            case WeeklyTask.TYPE_RINGTONE_HARD:
                return R.raw.ringtone_hard_soundfile;
            case WeeklyTask.TYPE_RINGTONE_VERY_HARD:
                return R.raw.ringtone_very_hard_soundfile;
            default:
                return R.raw.ringtone_very_easy_soundfile;
        }

    }

    public static void cancelAllAlarms(Context context){
        for(int i = 0; i < 10000; i++){
            cancelAlarmForTaskId(i, context);
        }
    }

    private static String getTitle(Context context, WeeklyTask task){
        int typeOfTask = task.getTypeOfTask();
        switch (typeOfTask){
            case WeeklyTask.TYPE_TASK_SLEEP:
                return context.getString(R.string.notif_title_sleep) + " - " + task.getTaskName();
            case WeeklyTask.TYPE_TASK_SPORT:
                return context.getString(R.string.notif_title_sport) + " - " + task.getTaskName();
            case WeeklyTask.TYPE_TASK_WORK:
                return context.getString(R.string.notif_title_work) + " - " + task.getTaskName();
            default:
                return task.getTaskName();
        }

    }

    private static PendingIntent getPendingIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                MAIN_ACTIVITY_RETURN_PENDING_INTENT_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
