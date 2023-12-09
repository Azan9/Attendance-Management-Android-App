package com.example.android.attendance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.attendance.Adapters.ScheduleAdapter;
import com.example.android.attendance.Adapters.TimetableAdapter;
import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.Models.ScheduleResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimetableActivity extends AppCompatActivity {

    public static final String Role = "roleKey";
    private static final String SHARED_PREFS = "MyPrefs" ;
    public static final String USER_ID = "userIdKey";
    String userRole;
    SharedPreferences sharedPreferences;
    int userId;
    Call<List<ScheduleResponse>> scheduleResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        androidx.appcompat.widget.Toolbar myChildToolbar =
                (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_child_toolbar_timetable);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Timetable");
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userRole = sharedPreferences.getString(Role, null);
        userId = sharedPreferences.getInt(USER_ID, 0);

        getSelectedDaySchedule();
    }

    public void getSelectedDaySchedule() {
        GridView gridView = (GridView) findViewById(R.id.grid_view_timetable);

        if (userRole.equals("student")) {
            scheduleResponse = ApiClient.getUserService().studentsScheduleResponse(userId);
        }
        if (userRole.equals("teacher")){
            scheduleResponse = ApiClient.getUserService().teachersScheduleResponse(userId);
        }
        scheduleResponse.enqueue(new Callback<List<ScheduleResponse>>() {
            @Override
            public void onResponse(Call<List<ScheduleResponse>> call, Response<List<ScheduleResponse>> response) {
                List<ScheduleResponse> dataList;
                dataList = new ArrayList<>();

                if (response.isSuccessful()) {
                    List<ScheduleResponse> models = response.body();
                    assert models != null;
                    for (ScheduleResponse model: models) {
                        dataList.add(model);
                    }
                    TimetableAdapter scheduleAdapter = new TimetableAdapter(getApplicationContext(), dataList);
                    gridView.setAdapter(scheduleAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No Response from Server", Toast.LENGTH_LONG).show();
            }
        });
    }
}