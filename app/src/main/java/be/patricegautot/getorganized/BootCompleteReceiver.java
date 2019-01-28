package be.patricegautot.getorganized;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationService = new Intent(context, RecreateAllAlarmsService.class);
        context.startService(notificationService);
    }
}
