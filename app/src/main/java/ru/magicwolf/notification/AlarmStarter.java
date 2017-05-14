package ru.magicwolf.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.magicwolf.POJO_classes.UsersDrug;

public class AlarmStarter {
    private static final String TAG = "MyApp";
    private static AlarmManager alarmManager;

    public void alarmStarter(List< UsersDrug > listUsersDrug, Context _con){
        String name, timeS, wayOfUsing;
        Log.i(TAG, "prepareNotification AS");

        Context con = _con;

        ArrayList<PendingIntent> intentArray = new ArrayList<>();

        for (int i = 0; i <= listUsersDrug.size() - 1; i++) {
            Log.i(TAG, "into loop");

            name = listUsersDrug.get(i).name;
            timeS = listUsersDrug.get(i).time;
            wayOfUsing = listUsersDrug.get(i).wayOfUsing;
            AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(con, NotificationReceiver.class).putExtra("wayOfUsing", wayOfUsing).putExtra("drugName", name);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(con, i, intent, 0);

            DateFormat sdf = new SimpleDateFormat("hh:mm:ss", Locale.ROOT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            try {
                Date date = sdf.parse(timeS);
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, date.getHours());
                calendar.set(Calendar.MINUTE, date.getMinutes());
                calendar.set(Calendar.SECOND, date.getSeconds());
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
            if (calendar.getTime().getTime() > System.currentTimeMillis()) {
                Log.i(TAG, "AlarmManager created AS");

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.i(TAG, "AlarmManager set AS " + timeS);

                intentArray.add(pendingIntent);
            }
        }
    }

    public static void setAlarmManager(AlarmManager _alarmManager){
        alarmManager = _alarmManager;
    }
}
