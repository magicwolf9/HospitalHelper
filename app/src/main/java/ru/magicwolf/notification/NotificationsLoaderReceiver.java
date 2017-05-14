package ru.magicwolf.notification;

/*import android.app.AlarmManager;
import android.app.notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.magicwolf.API_interfases.GetDrugsAPI;
import ru.magicwolf.Activities.PatientActivity;
import ru.magicwolf.POJO_classes.DrugResp;
import ru.magicwolf.POJO_classes.UsersDrug;
import ru.magicwolf.R;

public class NotificationsLoaderReceiver extends BroadcastReceiver {

    private static final String TAG = "MyApp";
    private final String url = "https://hospitalhelper-hospitalhelp.rhcloud.com/";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        Gson gsonBuilder = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder() //Создаем сервис для работы с interface API
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                .build();
        Log.i(TAG, "Создан retrofit NLR");

        GetDrugsAPI service = retrofit.create(GetDrugsAPI.class); //Изменить
        Log.i(TAG, "Создан сервис с параметрами из интерфейса API NLR");

        Call<DrugResp> call = service.GetDrugs(username, password); //Изменить
        Log.i(TAG, "Создан call NLR " + call.request().url().toString());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress i = InetAddress.getByName(url);
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }
            }
        });
        thread.start();

        Callback<DrugResp> callback = new Callback<DrugResp>() {
            @Override
            public void onResponse(Call<DrugResp> call, Response<DrugResp> response) {
                Log.i(TAG, "onResponse NLR");
                List<UsersDrug> listUsersDrug = response.body().getUsersDrug();
                //String name, wayOfUsing;
                //String time;
                Log.i(TAG, "onResponse NLR" + listUsersDrug.size() + " " + listUsersDrug.get(0).name);
                *//*for (int i = 0; i <= listUsersDrug.size() - 1; i++) {
                    name = listUsersDrug.get(i).name;
                    time = listUsersDrug.get(i).time;
                    wayOfUsing = listUsersDrug.get(i).wayOfUsing;
                }*//*



                startNextAlarms(listUsersDrug, context);
                notificationDONE(true, context, intent);
                Log.i(TAG, "onResponse NLR " + response.body().getUsersDrug().get(0).name + " " + response.body().getUsersDrug().get(0).time + " " + response.body().getUsersDrug().get(0).wayOfUsing);

            }

            @Override
            public void onFailure(Call<DrugResp> call, Throwable t) {
                t.printStackTrace();
                notificationDONE(false, context, intent);
                Log.i(TAG, "Failure NLR " + t.toString());

            }

        };
        call.enqueue(callback);

    }

    private void startNextAlarms(List<UsersDrug> listUsersDrug, Context con) {
        String name, timeS, wayOfUsing;
        Log.i(TAG, "prepareNotification NLR");
        AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
        AlarmStarter.setAlarmManager(alarmManager);
        ArrayList<PendingIntent> intentArray = new ArrayList<>();
        for (int i = 0; i <= listUsersDrug.size() - 1; i++) {


            name = listUsersDrug.get(i).name;
            timeS = listUsersDrug.get(i).time;
            wayOfUsing = listUsersDrug.get(i).wayOfUsing;

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
                Log.i(TAG, "AlarmManager created NLR ");
                alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.i(TAG, "AlarmManager set NLR " + timeS);

                intentArray.add(pendingIntent);
            }
        }
    }
    void notificationDONE(boolean tf, Context context, Intent intent){
        String result;
        if(tf){
            result = "NotificationUploaded";
        } else {
            result = "No internet connection";
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Log.i(TAG, "NotificationBuilder created NLR");
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT))
                .setSmallIcon(R.drawable.test_logo) //Мелкая иконка в строке сверху
                  //Большая иконка в блоке уведомлений
                .setAutoCancel(true)
                .setContentTitle("Загрузка данных") // Заголовок уведомления
                .setContentText(result); // Текст уведомления
        Log.i(TAG, "Builder configured NLR");
        // notification notification = builder.getNotification(); // до API 16
        notification notification = builder.build();
        Log.i(TAG, "notification built NLR");
        notification.defaults = notification.DEFAULT_ALL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}*/
