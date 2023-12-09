package com.example.android.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.Adapters.RecordAdapter;
import com.example.android.attendance.Adapters.StudentListAdapter;
import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.Models.RecordResponse;
import com.example.android.attendance.Models.StudentListResponse;
import com.example.android.attendance.Models.SubjectsResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceRecordActivity extends AppCompatActivity {
    View emptyView;
    String selectedDate, classSelected, subject;
    CalendarView calendarView;
    AutoCompleteTextView className;
    AutoCompleteTextView subjectName;
    GridView gridView;
    Call<List<RecordResponse>> attListResponse;
    Call<List<StudentListResponse>> classListResponse;
    Call<List<SubjectsResponse>> subjectsResponse;
    SharedPreferences sharedPreferences;
    int userId, student_id;
    private static final String SHARED_PREFS = "MyPrefs" ;
    public static final String USER_ID = "userIdKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_record);

        androidx.appcompat.widget.Toolbar myChildToolbar =
                (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_child_toolbar_attendance);
        setSupportActionBar(myChildToolbar);

        gridView = (GridView) findViewById(R.id.attlist);

        emptyView = findViewById(R.id.empty_view);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Attendance Record");
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(USER_ID, 0);

        classList();
        getSubjectList();

        calendarView = (CalendarView) findViewById(R.id.calenderViewAtt);
        className = (AutoCompleteTextView) findViewById(R.id.filled_exposed_dropdown_class);
        subjectName = (AutoCompleteTextView) findViewById(R.id.filled_exposed_dropdown_subject);

        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                view.setDate(calendar.getTimeInMillis());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate = simpleDateFormat.format(view.getDate());

                displayStudentRecord();
            }
        });

        className.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                classSelected = className.getText().toString();
                displayStudentRecord();
            }
        });

        subjectName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subject = subjectName.getText().toString();
                displayStudentRecord();
            }
        });

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView studId = gridView.getChildAt(position).findViewById(R.id.studid);
                    student_id = Integer.parseInt(studId.getText().toString());

                    Intent intent = new Intent(AttendanceRecordActivity.this, StudentRecordActivity.class);
                    intent.putExtra("STUID", student_id);
                    intent.putExtra("SUB", subject);

                    startActivity(intent);
                }
            });

    }

    public void displayStudentRecord() {
        gridView.setEmptyView(emptyView);
        gridView.setAdapter(null);
        List<RecordResponse> dataList;
        dataList = new ArrayList<>();

        attListResponse = ApiClient.getUserService().studentAttListResponse(classSelected, subject, selectedDate);

        attListResponse.enqueue(new Callback<List<RecordResponse>>() {
            @Override
            public void onResponse(Call<List<RecordResponse>> call, Response<List<RecordResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RecordResponse> models = response.body();
                    assert models != null;
                    for (RecordResponse model: models) {
                        dataList.add(model);
                    }
                    RecordAdapter studentListAdapter = new RecordAdapter(getApplicationContext(), dataList);
                    gridView.setAdapter(studentListAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<RecordResponse>> call, Throwable t) {

            }
        });
    }

    public void classList() {
        Log.e("Class:", ""+ userId);
        classListResponse = ApiClient.getUserService().teacherAllClassResponse(userId);
        classListResponse.enqueue(new Callback<List<StudentListResponse>>() {
            ArrayList<String> arrayList = new ArrayList<>();
            @Override
            public void onResponse(Call<List<StudentListResponse>> call, Response<List<StudentListResponse>> response) {
                if (response.isSuccessful()) {
                    List<StudentListResponse> models = response.body();
                    for (StudentListResponse model: models) {
                        arrayList.add(model.getClass_name());
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.class_list, arrayList);

                    className.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<StudentListResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getSubjectList() {
        subjectsResponse = ApiClient.getUserService().teacherSubjects(userId);
        subjectsResponse.enqueue(new Callback<List<SubjectsResponse>>() {
            @Override
            public void onResponse(Call<List<SubjectsResponse>> call, Response<List<SubjectsResponse>> response) {

                List<String> dataList;
                dataList = new ArrayList<>();
                String[] subs;
                if (response.isSuccessful()) {
                    List<SubjectsResponse> models = response.body();
                    assert models != null;
                    for (SubjectsResponse model: models) {
                        dataList.add(model.getSubject_name());
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.class_subjects_list, dataList);

                    subjectName.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SubjectsResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No Classes added", Toast.LENGTH_LONG).show();
            }
        });
    }
}