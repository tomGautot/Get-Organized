package be.patricegautot.getorganized;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import be.patricegautot.getorganized.appdatabase.AppDatabase;
import be.patricegautot.getorganized.objects.WeeklyTask;
import be.patricegautot.getorganized.utilities.NotificationUtils;

public class RecreateAllAlarmsService extends IntentService {

    public RecreateAllAlarmsService(){
        super("randomcuzuseless");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        List<WeeklyTask> taskList = AppDatabase.getInstance(this).weeklyTaskDao().loadAllTasksRaw();
        for(int i = 0; i < taskList.size(); i++){
            NotificationUtils.setScheduling(taskList.get(i), getApplicationContext());
        }

    }
}
