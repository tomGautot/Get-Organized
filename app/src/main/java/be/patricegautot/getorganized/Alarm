package be.patricegautot.getorganized;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import be.patricegautot.getorganized.activities.AlarmActivity;
import be.patricegautot.getorganized.appdatabase.AppDatabase;
import be.patricegautot.getorganized.objects.WeeklyTask;
import be.patricegautot.getorganized.utilities.NotificationUtils;
import be.patricegautot.getorganized.utilities.ParcelableUtils;

public class Alarm extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.e("AlarmService", "TRIGERRED");

        //byte[] bytes = intent.getByteArrayExtra("marshall");
        //WeeklyTask task = ParcelableUtils.unmarshall(bytes, WeeklyTask.CREATOR);

        Bundle b = intent.getBundleExtra("bundle");
        WeeklyTask task = b.getParcelable("task");
        //if(task == null) Log.e("AlarmService", "FUCK MY GODDAMN LIFE");

        if (task.getNotif() == 0) return;

        //Log.e("ALARMBRD", "scheduling next alarms");
        //PREPARE NEXT NOTIFICATION FOR THIS TASK
        NotificationUtils.setScheduling(task, context);


        //Log.e("Scheduling", "Handling task " + task.getTaskName()+ " ringtone " + task.getRingtone() + " snooze option " + task.getAlarmOffMethod());


        if (task.getTypeOfTask() == WeeklyTask.TYPE_TASK_WAKING_UP) {

            //Log.e("Scheduling", "opening alarmActivity cuz wake up");

            //NotificationUtils.sendNotification(task, context);
            Intent alarmIntent = new Intent(context, AlarmActivity.class);
            alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle nb = new Bundle();
            nb.putParcelable("task", task);
            alarmIntent.putExtra("bundle", nb);
            context.startActivity(alarmIntent);

        } else {
            //Log.e("ALARMRD", "calling notif prep");
            NotificationUtils.sendNotification(task, context);
        }

    }
}
