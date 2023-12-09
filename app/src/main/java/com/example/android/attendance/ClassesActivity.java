package com.example.android.attendance;

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
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.Adapters.RecordAdapter;
import com.example.android.attendance.Adapters.StudentListAdapter;
import com.example.android.attendance.Adapters.SubAdapter;
import com.example.android.attendance.Adapters.SubStudentsListAdapter;
import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.Models.RecordResponse;
import com.example.android.attendance.Models.StudentListResponse;
import com.example.android.attendance.Models.SubjectsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassesActivity extends AppCompatActivity {
    public static final String Role = "roleKey";
    private static final String SHARED_PREFS = "MyPrefs" ;
    public static final String USER_ID = "userIdKey";
    View emptyView;
    String userRole, classSelected, subject;
    AutoCompleteTextView subjectList;
    AutoCompleteTextView className;
    GridView gridView;
    int userId, student_id;;
    SharedPreferences sharedPreferences;
    Call<List<StudentListResponse>> studListResponse;
    Call<List<SubjectsResponse>> subjectsResponse;
    Call<List<StudentListResponse>> classListResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        subjectList = (AutoCompleteTextView) findViewById(R.id.filled_exposed_dropdown_subject);

        Log.e("TEXT:", subjectList.getText().toString());
        emptyView = findViewById(R.id.empty_view);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userRole = sharedPreferences.getString(Role, null);
        userId = sharedPreferences.getInt(USER_ID, 0);

        className = (AutoCompleteTextView) findViewById(R.id.filled_exposed_dropdown_classes);

        gridView = (GridView) findViewById(R.id.stud_List);

        androidx.appcompat.widget.Toolbar myChildToolbar =
                (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_child_toolbar_classes);
        setSupportActionBar(myChildToolbar);

        View emptyView = findViewById(R.id.empty_view);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Subjects");
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        if (userRole.equals("student")){
//            subjectsResponse = ApiClient.getUserService().studentSubjects(userId);
        }
        if (userRole.equals("teacher")){
            subjectsResponse = ApiClient.getUserService().teacherSubjects(userId);
        }

        subjectsResponse.enqueue(new Callback<List<SubjectsResponse>>() {
            @Override
            public void onResponse(Call<List<SubjectsResponse>> call, Response<List<SubjectsResponse>> response) {

                List<String> dataList;
                dataList = new ArrayList<>();
                String[] subs;
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<SubjectsResponse> models = response.body();
                        assert models != null;
                        for (SubjectsResponse model: models) {
                            dataList.add(model.getSubject_name());
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.class_subjects_list, dataList);

                        subjectList.setAdapter(arrayAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SubjectsResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No Classes added", Toast.LENGTH_LONG).show();
            }
        });

        subjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subject = subjectList.getText().toString();
                studentList();
                Log.e("SUBJECT:", "SUBJECT");
            }
        });

        classesList();

        className.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                classSelected = className.getText().toString();
                studentList();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView studId = gridView.getChildAt(position).findViewById(R.id.stuid);
                student_id = Integer.parseInt(studId.getText().toString());

                Intent intent = new Intent(ClassesActivity.this, StudentRecordActivity.class);
                intent.putExtra("STUID", student_id);
                intent.putExtra("SUB", subject);

                startActivity(intent);
            }
        });
    }

    public void classesList() {
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

    public void studentList() {
        gridView.setEmptyView(emptyView);
        Log.e("StudentList:", ""+ userId);

        studListResponse = ApiClient.getUserService().studentListResponse(userId, classSelected, subject);

        studListResponse.enqueue(new Callback<List<StudentListResponse>>() {
            @Override
            public void onResponse(Call<List<StudentListResponse>> call, Response<List<StudentListResponse>> response) {
                List<StudentListResponse> dataList;
                dataList = new ArrayList<>();

                if (response.isSuccessful()) {
                    List<StudentListResponse> models = response.body();
                    assert models != null;
                    for (StudentListResponse model: models) {
                        dataList.add(model);
                    }
                    SubStudentsListAdapter studentListAdapter = new SubStudentsListAdapter(getApplicationContext(), dataList);
                    gridView.setAdapter(studentListAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<StudentListResponse>> call, Throwable t) {

            }
        });
    }
}