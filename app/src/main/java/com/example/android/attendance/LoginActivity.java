package com.example.android.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.Models.LoginRequest;
import com.example.android.attendance.Models.LoginResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginBtn;
    boolean isPasswordVisible = false;
    public static final String SHARED_PREFS = "MyPrefs" ;
    public static final String Name = "usernameKey";
    public static final String Pass = "passwordKey";
    public static final String USER_ID = "userIdKey";
    public static final String Token = "tokenKey";
    SharedPreferences sharedPreferences;
    String user_name, user_password;
    public static final String Role = "roleKey";
    String userRole;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username_text);

        password = (EditText) findViewById(R.id.password_text);

        loginBtn = (Button) findViewById(R.id.login_button);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        user_name = sharedPreferences.getString(Name, null);
        user_password = sharedPreferences.getString(Pass, null);
        userRole = sharedPreferences.getString(Role, null);

        userId = sharedPreferences.getInt(USER_ID, 0);

        eventOnTouchPassword();

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                    startActivity(intent);

                    if(TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
                        Toast.makeText(LoginActivity.this, "It seems you have not entered your username or password.", Toast.LENGTH_LONG).show();

                    } else {
                            login();
                    }
                }
            });
    }

    public void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username.getText().toString());
        loginRequest.setPassword(password.getText().toString());
        Call<LoginResponse> loginResponseCall = ApiClient.getUserService().userLogin(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if(response.isSuccessful() && Objects.requireNonNull(response.body()).getUser_type().equals(userRole)){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(Name, username.getText().toString());
                            editor.putString(Pass, password.getText().toString());
                            editor.putString(Token, response.body().getToken());

                            editor.putInt(USER_ID, response.body().getId());

                            editor.apply();

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                            finish();
                        }
                    },700);
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Throwable"+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user_name != null && user_password != null) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void eventOnTouchPassword() {
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = password.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                            // hide Password
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordVisible = false;
                        } else {
                            // set drawable image
                            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_24, 0);
                            // show Password
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
    }
}