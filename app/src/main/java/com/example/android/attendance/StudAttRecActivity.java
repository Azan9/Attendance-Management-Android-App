package com.example.android.attendance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.Models.AttRecordRequest;
import com.example.android.attendance.Models.ProfileResponse;
import com.example.android.attendance.Models.SubjectsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudAttRecActivity extends AppCompatActivity {
    AutoCompleteTextView classList;
    public static final String Role = "roleKey";
    private static final String SHARED_PREFS = "MyPrefs" ;
    public static final String USER_ID = "userIdKey";
    String userRole;
    int userId;
    SharedPreferences sharedPreferences;
    Call<List<SubjectsResponse>> subjectsResponse;
    String subSelected;
    Call<List<AttRecordRequest>> attResponse;
    AutoCompleteTextView selectedSubject;
    TextView present;
    TextView absent;
    TextView daysTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_att_rec);

        classList = (AutoCompleteTextView) findViewById(R.id.filled_exposed_dropdown_sub);

        androidx.appcompat.widget.Toolbar myChildToolbar =
                (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_child_toolbar_strecord);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Attendance Record");
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userRole = sharedPreferences.getString(Role, null);
        userId = sharedPreferences.getInt(USER_ID, 0);

        getSubjects();

        present = (TextView) findViewById(R.id.pres);
        absent = (TextView) findViewById(R.id.abs);
        daysTotal = (TextView) findViewById(R.id.day_total);

        selectedSubject = (AutoCompleteTextView) findViewById(R.id.filled_exposed_dropdown_sub);

        selectedSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subSelected = selectedSubject.getText().toString();
                getSubjectAttendance();
            }
        });

    }

    public void getSubjects() {
        if (userRole.equals("student")){
            subjectsResponse = ApiClient.getUserService().studentSubjects(userId);
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
                        for (SubjectsResponse model : models) {
                            dataList.add(model.getSubject_name());
                        }
//
//                    Log.e("SUB:", dataList.get(0).toString());
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.class_subjects_list, dataList);

                        classList.setAdapter(arrayAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SubjectsResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No Classes added", Toast.LENGTH_LONG).show();
            }
        });
        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("SUBJECT:", "SUBJECT");
            }
        });
    }

    public void getSubjectAttendance() {
        Log.e("Att:", "" + subSelected);
        attResponse = ApiClient.getUserService().studentSubResponse(userId, subSelected);

        attResponse.enqueue(new Callback<List<AttRecordRequest>>() {
            @Override
            public void onResponse(Call<List<AttRecordRequest>> call, Response<List<AttRecordRequest>> response) {
                List<AttRecordRequest> dataList;
                dataList = new ArrayList<>();
                if(response.isSuccessful()) {
                    List<AttRecordRequest> models = response.body();
                    if (models.isEmpty()) {

                    } else {
                        for (AttRecordRequest model: models){
                            dataList.add(model);
                        }

                        present.setText(String.valueOf(dataList.get(0).getPresent()));
                        absent.setText(String.valueOf(dataList.get(0).getAbsent()));

                        int totalDays = dataList.get(0).getPresent() + dataList.get(0).getAbsent();

                        daysTotal.setText(String.valueOf(totalDays));
                    }

                }
            }

            @Override
            public void onFailure(Call<List<AttRecordRequest>> call, Throwable t) {

            }
        });
    }

}