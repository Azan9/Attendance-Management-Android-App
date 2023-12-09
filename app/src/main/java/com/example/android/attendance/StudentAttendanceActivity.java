package com.example.android.attendance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.Models.AttendanceRequest;
import com.example.android.attendance.Models.ClassRequest;
import com.example.android.attendance.Models.SessionRequest;
import com.example.android.attendance.Models.StudentListResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAttendanceActivity extends AppCompatActivity {
    private Button submitBtn;
    private Button checkBtn;
    String newSubject, classSelected, ssid, session_status = "inactive";
    private static final String SHARED_PREFS = "MyPrefs";
    public static final String USER_ID = "userIdKey";
    private SharedPreferences sharedPreferences;
    int userId, subjectId, sessionClassId, classScheduleId, student_id;
    private AutoCompleteTextView classesList;
    private Call<List<StudentListResponse>> classListResponse;
    private Call<List<ClassRequest>> classRequest;
    private Call<List<SessionRequest>> sessionRequestClass;
    private Call<SessionRequest> sessionStatusClass;
    private TextView subId;
    private TextView subName;
    private TextView studId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        androidx.appcompat.widget.Toolbar myChildToolbar =
                (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_child_toolbar_student);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Attendance");
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        Bundle extras;

        subId = (TextView) findViewById(R.id.sub_id);
        subName = (TextView) findViewById(R.id.sub_name);
        studId = (TextView) findViewById(R.id.stud_id);

        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if (extras == null) {
                newSubject = null;
            } else {
                student_id = extras.getInt("ID");
                newSubject = extras.getString("SUBJECT_NAME");
                subjectId = extras.getInt("SUBJECT_ID");
            }
        }

        studId.setText(Integer.toString(student_id));
        subName.setText(newSubject);
        subId.setText(Integer.toString(subjectId));

        classesList = (AutoCompleteTextView) findViewById(R.id.class_list_student);
        classesList.setInputType(InputType.TYPE_NULL);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(USER_ID, 0);
        Log.e("Class:", "" + userId);

        classList();

        classesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                classSelected = classesList.getText().toString();
                getClassId(classSelected);
            }
        });

        submitBtn = (Button) findViewById(R.id.btn_submit);
        checkBtn = (Button) findViewById(R.id.check_status);

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo;

                wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        ActivityCompat.requestPermissions(StudentAttendanceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                        return;
                    }
                    ssid = wifiInfo.getSSID();
                    ssid = ssid.replace("\"", "");
                    Log.e("CLASS:", ssid);
                }
                getSessionStatus();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session_status.equals("active")) {
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    saveAttendance(subjectId, student_id, currentDate, "Present");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void classList() {
        Log.e("Class:", ""+ userId);
        classListResponse = ApiClient.getUserService().studentClassResponse(userId, newSubject);
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

                    classesList.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<StudentListResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getClassId(String className) {

        classRequest = ApiClient.getUserService().getClassIdResponse(className);

        classRequest.enqueue(new Callback<List<ClassRequest>>() {
            @Override
            public void onResponse(Call<List<ClassRequest>> call, Response<List<ClassRequest>> response) {
                if (response.isSuccessful()) {
                    sessionClassId = response.body().get(0).getId();
                    getClassScheduleId(sessionClassId);
                }
            }

            @Override
            public void onFailure(Call<List<ClassRequest>> call, Throwable t) {

            }
        });
    }

    public void getClassScheduleId(int classId) {
        sessionRequestClass = ApiClient.getUserService().getClassScheduleIdResponse(classId, subjectId);
        sessionRequestClass.enqueue(new Callback<List<SessionRequest>>() {
            @Override
            public void onResponse(Call<List<SessionRequest>> call, Response<List<SessionRequest>> response) {
                if (!response.body().isEmpty()) {
                    classScheduleId = response.body().get(0).getId();
                    Log.e("SES:", ""+classScheduleId);
                }
            }

            @Override
            public void onFailure(Call<List<SessionRequest>> call, Throwable t) {
            }
        });
    }

    public void getSessionStatus() {
        sessionStatusClass = ApiClient.getUserService().getSessionStatusResponse(classScheduleId);
        sessionStatusClass.enqueue(new Callback<SessionRequest>() {
            @Override
            public void onResponse(Call<SessionRequest> call, Response<SessionRequest> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSession_status().equals("active") && response.body().getSsid().equals(ssid)) {
                        Toast.makeText(getApplicationContext(), "Session is active, you can submit your attendance now.", Toast.LENGTH_SHORT).show();
                        session_status = "active";
                    } else {
                        Toast.makeText(getApplicationContext(), "Session is not active yet.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SessionRequest> call, Throwable t) {

            }
        });
    }

    public void saveAttendance(int idSubject, int studentId, String currentDate, String att_status) {
        AttendanceRequest attendanceRequest = new AttendanceRequest();
        attendanceRequest.setSubject_id(idSubject);
        attendanceRequest.setStudent_id(studentId);
        attendanceRequest.setAtt_date(currentDate);
        attendanceRequest.setAtt_status(att_status);
        Call<AttendanceRequest> attendanceRequestCall = ApiClient.getUserService().studentAttendanceRequest(attendanceRequest);
        Call<List<AttendanceRequest>> attendanceResponseCall = ApiClient.getUserService().studentAttendanceListResponse(studentId, idSubject, currentDate);

        attendanceResponseCall.enqueue(new Callback<List<AttendanceRequest>>() {
            @Override
            public void onResponse(Call<List<AttendanceRequest>> call, Response<List<AttendanceRequest>> response) {
                if (response.body().isEmpty()) {
                    attendanceRequestCall.enqueue(new Callback<AttendanceRequest>() {
                        @Override
                        public void onResponse(Call<AttendanceRequest> call, Response<AttendanceRequest> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Attendance Saved!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AttendanceRequest> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Today's attendance has already been saved", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceRequest>> call, Throwable t) {

            }
        });
    }
}