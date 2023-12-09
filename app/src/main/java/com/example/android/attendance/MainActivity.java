package com.example.android.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "MyPrefs" ;
    public static final String Name = "usernameKey";
    public static final String Pass = "passwordKey";
    public static final String Role = "roleKey";
    SharedPreferences sharedPreferences;
    String userName, userPassword, userSelectedRole;
    CardView teacherCard;
    CardView studentCard;
    boolean roleSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);


        userName = sharedPreferences.getString(Name, null);
        userPassword = sharedPreferences.getString(Pass, null);

        teacherCard = (CardView) findViewById(R.id.role_teacher);
        studentCard = (CardView) findViewById(R.id.role_student);

        selectedCardView();

        Button confirmBtn = (Button) findViewById(R.id.confirm_button);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roleSelected) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select a role.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void selectedCardView() {
        teacherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorCode = Color.parseColor("#6C7880");
                teacherCard.setCardBackgroundColor(colorCode);
                teacherCard.animate().scaleX(1.1f).scaleY(1.1f);

                studentCard.setCardBackgroundColor(Color.parseColor("#F4EEE1"));
                studentCard.animate().scaleX(1).scaleY(1);

                userSelectedRole = "teacher";

                roleSelected = true;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Role, userSelectedRole);
                editor.apply();
            }
        });

        studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorCode = Color.parseColor("#6C7880");
                studentCard.setCardBackgroundColor(colorCode);
                studentCard.animate().scaleX(1.1f).scaleY(1.1f);

                teacherCard.setCardBackgroundColor(Color.parseColor("#F4EEE1"));
                teacherCard.animate().scaleX(1).scaleY(1);

                userSelectedRole = "student";

                roleSelected = true;

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Role, userSelectedRole);
                editor.apply();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userName != null && userPassword != null) {
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
        }
    }
}