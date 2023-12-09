package com.example.android.attendance.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.attendance.Adapters.ScheduleAdapter;
import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.Models.ScheduleResponse;
import com.example.android.attendance.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CalenderFragment extends Fragment {

    public static final String Role = "roleKey";
    private static final String SHARED_PREFS = "MyPrefs" ;
    public static final String USER_ID = "userIdKey";
    String userRole, selectedDate, selectedDay;
    SharedPreferences sharedPreferences;
    int userId;
    Call<List<ScheduleResponse>> scheduleResponse;

    public CalenderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userRole = sharedPreferences.getString(Role, null);
        userId = sharedPreferences.getInt(USER_ID, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calender, container, false);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE/MMMM/yyyy");

        String todayDate = simpleDateFormat.format(calendar.getTime());
        String[] mDateArray = todayDate.split("/");
        selectedDay = mDateArray[0];
        Log.e("Date:", selectedDay);

        getSelectedDaySchedule(view, selectedDay);

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshcalender);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    fragmentManager.beginTransaction().detach(CalenderFragment.this).commitNow();
                    fragmentManager.beginTransaction().attach(CalenderFragment.this).commitNow();
                } else {
                    fragmentManager.beginTransaction().detach(CalenderFragment.this).attach(CalenderFragment.this).commit();
                }
            }
        });
        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calenderView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView cView, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                cView.setDate(calendar.getTimeInMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE/MMMM/yyyy");

                selectedDate = simpleDateFormat.format(cView.getDate());
                String[] dateArray = selectedDate.split("/");
                selectedDay = dateArray[0];
                getSelectedDaySchedule(view, selectedDay);
            }
        });
        return view;
    }

    public void getSelectedDaySchedule(View view, String day) {
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_schedule);

        if (userRole.equals("student")) {
            scheduleResponse = ApiClient.getUserService().studentScheduleResponse(userId, day);
        }
        if (userRole.equals("teacher")){
            scheduleResponse = ApiClient.getUserService().teacherScheduleResponse(userId, day);
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
                    ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getContext(), dataList);
                    gridView.setAdapter(scheduleAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "No Response from Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
}