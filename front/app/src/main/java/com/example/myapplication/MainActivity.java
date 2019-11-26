package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.example.myapplication.model.dao.*;
import com.example.myapplication.model.entity.*;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button register = findViewById(R.id.register);
        register.setOnClickListener(
                v ->
                {
                    new Thread(()-> {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        String name = ((EditText) findViewById(R.id.name)).getText().toString();
                        FormBody formBody = new FormBody.Builder().add("name", name).build();
                        Request request = new Request.Builder()
                                .url(Constant.ADD)
                                .post(formBody)
                                .build();
                        try (Response response = okHttpClient.newCall(request).execute()) {
                            Looper.prepare();
                            if (Boolean.parseBoolean(response.body().string()))
                            {
                                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
        );
        Button login = findViewById(R.id.login);
        login.setOnClickListener(
                v ->
                {
                    new Thread(()-> {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        String name = ((EditText) findViewById(R.id.name)).getText().toString();
                        FormBody formBody = new FormBody.Builder().add("name", name).build();
                        Request request = new Request.Builder()
                                .url(Constant.GET)
                                .post(formBody)
                                .build();
                        try (Response response = okHttpClient.newCall(request).execute()) {
                            List<User> users = JSONArray.parseArray(response.body().string(),User.class);
                            Looper.prepare();
                            if(users.size() == 0)
                            {
                                Toast.makeText(this,"登录失败",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
        );

        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(
                v ->
                {
                    new Thread(()-> {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        String name = ((EditText) findViewById(R.id.name)).getText().toString();
                        FormBody formBody = new FormBody.Builder().add("name", name).build();
                        Request request = new Request.Builder()
                                .url(Constant.DELETE)
                                .post(formBody)
                                .build();
                        try (Response response = okHttpClient.newCall(request).execute()) {
                            Looper.prepare();
                            if (Boolean.parseBoolean(response.body().string()))
                            {
                                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
        );

        Button modify = findViewById(R.id.modify);
        modify.setOnClickListener(
                v ->
                {
                    new Thread(()-> {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        String name = ((EditText) findViewById(R.id.name)).getText().toString();
                        String id = ((EditText)findViewById(R.id.id)).getText().toString();
                        FormBody formBody = new FormBody.Builder()
                                .add("name", name)
                                .add("id",id)
                                .build();
                        Request request = new Request.Builder()
                                .url(Constant.MODIFY)
                                .post(formBody)
                                .build();
                        try (Response response = okHttpClient.newCall(request).execute()) {
                            Looper.prepare();
                            if (Boolean.parseBoolean(response.body().string()))
                            {
                                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
        );
    }
}
