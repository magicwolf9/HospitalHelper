package ru.magicwolf.notification;

/*import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;

import ru.magicwolf.Activities.PatientActivity;

public class NotificationServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "MyApp";
    private static final long REPEAT_TIME = 60 * 60;

    @Override
    public void onReceive(Context context, Intent i) {
        scheduleAlarms(context);
    }

    private static void scheduleAlarms(Context context) {
        AlarmManager service = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        Log.i(TAG, "got AlarmManager NSR ");

        Intent i = new Intent(context, NotificationsLoaderReceiver.class); //!

        PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 30);

        service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), REPEAT_TIME, pending);
    }
}*/
