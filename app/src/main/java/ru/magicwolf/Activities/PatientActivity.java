package ru.magicwolf.Activities;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.magicwolf.API_interfases.GetDrugsAPI;
import ru.magicwolf.Fragments.DrugFragment;
import ru.magicwolf.notification.AlarmStarter;
import ru.magicwolf.notification.MyNotificationService;
import ru.magicwolf.notification.NotificationService;
import ru.magicwolf.POJO_classes.DrugResp;
import ru.magicwolf.POJO_classes.UsersDrug;
import ru.magicwolf.R;

public class PatientActivity extends Activity {
    private static final String TAG = "MyApp";
    private final String url = "https://hospitalhelper-hospitalhelp.rhcloud.com/";
    private String username;
    private String password;
    private TextView log;
    private static final int PERIOD = 1000 * 60 ;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(savedInstanceState != null){
            savedInstanceState.clear();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        LinearLayout containerScroll = (LinearLayout) findViewById(R.id.containerScroll);
        log = (TextView) findViewById(R.id.log);

        if(containerScroll.getChildCount() == 1) {
            log.setText("Обновление данных с сервера");
        }



        try {
            Log.i(TAG, "LoadList");
            LoadList loadList = new LoadList();
            loadList.LoadingInfo();

        } catch (Exception e){
            Log.i(TAG, "LoadingInfo exception " + e.getMessage());
            log.setText("Не удалось загрузить данные с сервера, проверьте подключение к интернету");
        }


        //startService(new Intent(this, NotificationService.class).putExtra("username", username).putExtra("password", password));





    }

    private void startService(){
        try {
            Log.i(TAG, "Создание alarm manager");
            AlarmManager alarmManager =
                    (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(this, NotificationService.class).putExtra("username", username).putExtra("password", password);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
            alarmManager.cancel(pendingIntent);
            //notificationService.onStartCommand(i, 0, 0);
            Log.i(TAG, "Запуск сервиса");
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + PERIOD, PERIOD, pendingIntent); /// Перезапускать сервис каждые 60 минут
        } catch (Exception e){
            Log.i(TAG, e.getMessage());
        }
    }

    private void addFragments(String name, String time, String features) {
        //ViewGroup.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 60);
        DrugFragment drugFragment = new DrugFragment();
        drugFragment.setTexts(name, time, features);

        Log.i(TAG, "Настроины переменные");

        Log.i(TAG, "Создан fragment");

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.containerScroll, drugFragment);

        ft.commit();
        Log.i(TAG, "Добавлен fragment");

    }
    public class LoadList {
        public void LoadingInfo() {
            startService();
            username = getIntent().getStringExtra("username");
            password = getIntent().getStringExtra("password");

            Gson gsonBuilder = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder() //Создаем сервис для работы с interface API
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                    .build();
            Log.i(TAG, "Создан retrofit");

            GetDrugsAPI service = retrofit.create(GetDrugsAPI.class); //Изменить
            Log.i(TAG, "Создан сервис с параметрами из интерфейса API");

            Call<DrugResp> call = service.GetDrugs(username, password); //Изменить
            Log.i(TAG, "Создан call " + call.request().url().toString());

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
                    Log.i(TAG, "onResponse");
                    List<UsersDrug> listUsersDrug = response.body().getUsersDrug();
                    String name, wayOfUsing;
                    String time;
                    Log.i(TAG, "onResponse " + listUsersDrug.size() + " " + listUsersDrug.get(0).name);
                    for (int i = 0; i <= listUsersDrug.size() - 1; i++) {
                        name = listUsersDrug.get(i).name;
                        time = listUsersDrug.get(i).time;
                        wayOfUsing = listUsersDrug.get(i).wayOfUsing;
                        addFragments(name, time, wayOfUsing);
                    }

                    if (listUsersDrug.size() > 1) {
                        log.setText("");
                        log.setVisibility(View.GONE);
                    }
                    AlarmStarter alarmStarter = new AlarmStarter();
                    alarmStarter.alarmStarter(listUsersDrug, PatientActivity.this.getApplicationContext());
                    Log.i(TAG, "onResponse " + response.body().getUsersDrug().get(0).name + " " + response.body().getUsersDrug().get(0).time + " " + response.body().getUsersDrug().get(0).wayOfUsing);

                }

                @Override
                public void onFailure(Call<DrugResp> call, Throwable t) {
                    t.printStackTrace();
                    Log.i(TAG, "Failure " + t.toString());

                }

            };
            call.enqueue(callback);
        }
    }

    /*public class AlarmStarter {
        public void startNextAlarms (List < UsersDrug > listUsersDrug) {
            String name, timeS, wayOfUsing;
            Log.i(TAG, "prepareNotification");

            Context con = PatientActivity.this;

            ArrayList<PendingIntent> intentArray = new ArrayList<>();

            for (int i = 0; i <= listUsersDrug.size() - 1; i++) {


                name = listUsersDrug.get(i).name;
                timeS = listUsersDrug.get(i).time;
                wayOfUsing = listUsersDrug.get(i).wayOfUsing;
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
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
                    Log.i(TAG, "AlarmManager created ASN");

                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    Log.i(TAG, "AlarmManager set " + timeS);

                    intentArray.add(pendingIntent);
                }
            }
        }*/
    //}
}

