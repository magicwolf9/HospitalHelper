package ru.magicwolf.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import ru.magicwolf.R;

public class NotificationReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    private static final String TAG = "MyApp";

    @Override
    public void onReceive(Context context, Intent intent) {

        String wayOfUsing = intent.getStringExtra("wayOfUsing");
        String drugName = intent.getStringExtra("drugName");
        //Intent service = new Intent(context, NotificationService.class).putExtra("wayOfUsing", wayOfUsing).putExtra("drugName", drugName);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Log.i(TAG, "PendingIntentCreated");

        Resources res = context.getResources();
        Log.i(TAG, "Resources created");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Log.i(TAG, "NotificationBuilder created");
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.test_logo) //Мелкая иконка в строке сверху
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.test_logo))  //Большая иконка в блоке уведомлений
                .setAutoCancel(true)
                .setContentTitle("Принять лекарство") // Заголовок уведомления
                .setContentText("Принять " + wayOfUsing + " " + drugName); // Текст уведомления
        Log.i(TAG, "Builder configured");
        // notification notification = builder.getNotification(); // до API 16
        Notification notification = builder.build();
        Log.i(TAG, "notification built NR");
        notification.defaults = Notification.DEFAULT_ALL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }
}
