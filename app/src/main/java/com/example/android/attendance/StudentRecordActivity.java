package com.example.android.attendance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.Models.AttRecordRequest;
import com.example.android.attendance.Models.SubjectsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRecordActivity extends AppCompatActivity {
    private Call<List<SubjectsResponse>> subjectsResponse;
    private String subSelected;
    private Call<List<AttRecordRequest>> attResponse;
    private int student_id;
    private TextView present;
    private TextView absent;
    private TextView daysTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_record);

        androidx.appcompat.widget.Toolbar myChildToolbar =
                (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_child_toolbar_record);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Attendance Record");
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        present = (TextView) findViewById(R.id.present);
        absent = (TextView) findViewById(R.id.absent);
        daysTotal = (TextView) findViewById(R.id.total_days);

        Bundle extras;

        if(savedInstanceState == null) {
            extras = getIntent().getExtras();
            if (extras == null) {
                subSelected = null;
            } else {
                subSelected = extras.getString("SUB");
                student_id = extras.getInt("STUID");
            }
        }

        getSubjectAttendance();
    }

    public void getSubjectAttendance() {

        attResponse = ApiClient.getUserService().studentAttListResponse(student_id, subSelected);

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