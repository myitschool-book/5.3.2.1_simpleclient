package ru.samsung.itschool.mdev.simpleclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private TextView result;
    private Button button;
    private String lastnameS, firstnameS;
    private OkHttpClient client = new OkHttpClient();
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
        button = findViewById(R.id.sendPOST);

        // Необходим, для доступа к главному потоку - UI
        mHandler = new Handler(Looper.getMainLooper());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText lastname = findViewById(R.id.lastname);
                EditText firstname = findViewById(R.id.firstname);
                lastnameS = lastname.getText().toString();
                firstnameS = firstname.getText().toString();
                // Тело Post сообщения - через паттерн Builder
                RequestBody formBody = new FormBody.Builder()
                        .add("lastname", lastnameS)
                        .add("firstname", firstnameS)
                        .build();
                // Формируем запрос
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8000/result")
                        .post(formBody)
                        .build();
                // Выполняем асинхронный запрос
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                        final String resbody = response.body().string();
                        // Обновляем элемент TextView в основном UI
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                result.setText(resbody);
                            }
                        });
                    }
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                        // Обновляем элемент TextView в основном UI
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                result.setText(e.toString());
                            }
                        });
                    }
                });
            }
        });
    }

}