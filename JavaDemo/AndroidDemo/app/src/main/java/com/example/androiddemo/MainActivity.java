package com.example.androiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.androiddemo.databinding.ActivityMainBinding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();
    private String oldName;
    private String oldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void signInUp(View view) {
        try {
            String name = binding.name.getText().toString();
            String password = Utils.encrypt(binding.password.getText().toString());
            Request request = new Request.Builder().url(NetworkSettings.SIGN_IN_UP).post(
                    RequestBody.create(mapper.writeValueAsString(new User(name, password)), mediaType)
            ).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    message.what = ResponseCode.REQUEST_FAILED;
                    handler.post(()->Utils.showMessage(getApplicationContext(),message));
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            RestResponse restResponse = mapper.readValue(body.string(), RestResponse.class);
                            message.what = restResponse.getCode();
                            if(message.what == ResponseCode.SIGN_IN_SUCCESS){
                                handler.post(()->{
                                    signInId = (int)restResponse.getData();
                                    binding.update.setVisibility(View.VISIBLE);
                                    binding.delete.setVisibility(View.VISIBLE);
                                    binding.signInUp.setText("退出");
                                    binding.signInUp.setOnClickListener(v->signOut(false));
                                    oldName = binding.name.getText().toString();
                                    oldPassword = binding.password.getText().toString();
                                });
                            }
                        } else {
                            message.what = ResponseCode.EMPTY_RESPONSE;
                            Log.e("RESPONSE_BODY_EMPTY", response.message());
                        }
                    } else {
                        message.what = ResponseCode.SERVER_ERROR;
                        Log.e("SERVER_ERROR", response.message());
                    }
                    handler.post(()->Utils.showMessage(getApplicationContext(),message));
                }
            });
        } catch (JsonProcessingException e) {
            message.what = ResponseCode.JSON_SERIALIZATION;
            Utils.showMessage(getApplicationContext(),message);
            e.printStackTrace();
        }
    }

    public void update(View view) {
        try {
            String name = binding.name.getText().toString();
            String password = binding.password.getText().toString();
            if(name.equals(oldName) && password.equals(oldPassword)){
                message.what = ResponseCode.UNCHANGED_INFORMATION;
                handler.post(()->Utils.showMessage(getApplicationContext(),message));
                return;
            }
            password = Utils.encrypt(password);
            Request request = new Request.Builder().url(NetworkSettings.UPDATE).put(
                RequestBody.create(
                    mapper.writeValueAsString(new User(signInId, name, password)),
                    mediaType
                )
            ).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    message.what = ResponseCode.REQUEST_FAILED;
                    handler.post(()->Utils.showMessage(getApplicationContext(),message));
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            RestResponse restResponse = mapper.readValue(body.string(), RestResponse.class);
                            message.what = restResponse.getCode();
                            if(message.what == ResponseCode.UPDATE_SUCCESS){
                                oldName = binding.name.getText().toString();
                                oldPassword = binding.password.getText().toString();
                            }
                        } else {
                            message.what = ResponseCode.EMPTY_RESPONSE;
                            Log.e("RESPONSE_BODY_EMPTY", response.message());
                        }
                    } else {
                        message.what = ResponseCode.SERVER_ERROR;
                        Log.e("SERVER_ERROR", response.message());
                    }
                    handler.post(()->Utils.showMessage(getApplicationContext(),message));
                }
            });
        } catch (Exception e) {
            message.what = ResponseCode.JSON_SERIALIZATION;
            Utils.showMessage(getApplicationContext(),message);
            e.printStackTrace();
        }
    }

    public void delete(View view) {
        try {
            Request request = new Request.Builder().url(NetworkSettings.DELETE+"?id="+signInId).delete().build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    message.what = ResponseCode.REQUEST_FAILED;
                    handler.post(()->Utils.showMessage(getApplicationContext(),message));
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            RestResponse restResponse = mapper.readValue(body.string(), RestResponse.class);
                            message.what = restResponse.getCode();
                            if(message.what == ResponseCode.DELETE_SUCCESS){
                                handler.post(()->signOut(true));
                            }
                        } else {
                            message.what = ResponseCode.EMPTY_RESPONSE;
                            Log.e("RESPONSE_BODY_EMPTY", response.message());
                        }
                    } else {
                        message.what = ResponseCode.SERVER_ERROR;
                        Log.e("SERVER_ERROR", response.message());
                    }
                    handler.post(()->Utils.showMessage(getApplicationContext(),message));
                }
            });
        } catch (Exception e) {
            message.what = ResponseCode.JSON_SERIALIZATION;
            Utils.showMessage(getApplicationContext(), message);
            e.printStackTrace();
        }
    }

    public void signOut(boolean isDeleted){
        signInId = 0;
        binding.signInUp.setText("注册/登录");
        binding.signInUp.setOnClickListener(this::signInUp);
        binding.name.setText("");
        binding.password.setText("");
        binding.update.setVisibility(View.INVISIBLE);
        binding.delete.setVisibility(View.INVISIBLE);
        if(!isDeleted){
            message.what = ResponseCode.EXIT_SUCCESS;
        }
        Utils.showMessage(getApplicationContext(),message);
    }
}