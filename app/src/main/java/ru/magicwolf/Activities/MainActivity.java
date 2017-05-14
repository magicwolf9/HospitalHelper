package ru.magicwolf.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.magicwolf.API_interfases.GetAPI;
import ru.magicwolf.POJO_classes.Resp;
import ru.magicwolf.R;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MyApp";
    private EditText textName;
    private EditText textPassword;
    private TextView log;
    private String result;
    private final String url = "https://hospitalhelper-hospitalhelp.rhcloud.com/";
    private String username;
    private String password;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textName = (EditText) findViewById(R.id.name);
        textPassword = (EditText) findViewById(R.id.id);
        log = (TextView) findViewById(R.id.log);

        Log.i(TAG, "Кнопки и edit texts инициализированны");
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Прошел клик по кнопке");
                UserLogin login = new UserLogin();
                login.LoginRequest();
                log.setText("Загрузка...");
                username = textName.getText().toString(); //Получение именя пользователя из EditText
                password = textPassword.getText().toString();
            }
        });
    }

    public class UserLogin {


        void LoginRequest() {
            final String username = textName.getText().toString(); //Получение именя пользователя из EditText
            final String password = textPassword.getText().toString(); //Получение id пользователя из EditText

            Log.i(TAG, "Получен текст из edit text`ов");

            Retrofit retrofit = new Retrofit.Builder() //Создаем сервис для работы с interface API
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.i(TAG, "Создан retrofit");

            GetAPI service = retrofit.create(GetAPI.class);
            Log.i(TAG, "Создан сервис с параметрами из интерфейса API");

            Call<Resp> call = service.Login(username, password);
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

            Callback<Resp> callback = new Callback<Resp>() {
                @Override
                public void onResponse(Call<Resp> call, Response<Resp> response) {
                    Log.i(TAG, "onResponse");
                    result = response.body().result;
                    Log.i(TAG, "Response " + result);
                    ResultEquals();
                }

                @Override
                public void onFailure(Call<Resp> call, Throwable t) {
                    t.printStackTrace();
                    log.setText("Не удалось подключится к серверу, проверьте свое подключение к интернету или зайдите позднее");
                    Log.i(TAG, "Failure " + t.toString());
                }

            };
            call.enqueue(callback);
        }

        public void ResultEquals(){
            Log.i(TAG, "Result set");
            if(result != null) { //Венести в отдельный метод
                try {
                    Log.i(TAG, "Сейчас будет сравнение результатов " + result);
                    Log.i(TAG, result);
                    if (result.equals("success")) {
                        Log.i(TAG, "Сейчас загрузится patient Activity");

                        Intent patientActivity = new Intent(getApplicationContext(), PatientActivity.class);
                        patientActivity.putExtra("username", username);
                        patientActivity.putExtra("password", password);
                        patientActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(patientActivity);

                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


// Нужно сделать Activity для управления кнопками Процедуры/Лекарства
// которые бы заменяли контейнер лекарств на контейнер процедур и наоборот
}
