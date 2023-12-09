package com.example.android.attendance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.Adapters.StudentListAdapter;
import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.Models.AttendanceRequest;
import com.example.android.attendance.Models.ClassRequest;
import com.example.android.attendance.Models.ProfileResponse;
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

public class MarkAttendanceActivity extends AppCompatActivity {
    private GridView gridView;
    private String newSubject;
    private static final String SHARED_PREFS = "MyPrefs";
    public static final String USER_ID = "userIdKey";
    private SharedPreferences sharedPreferences;
    int userId, subjectId, classScheduleId, sessionClassId, cid;
    private Call<List<StudentListResponse>> studentListResponse, classListResponse;
    private String classSelected, ssid, session_status;
    private AutoCompleteTextView classesList;
    private View emptyView;
    private Button startBtn;
    private Button stopBtn;
    private Call<List<SessionRequest>> sessionRequestClass;
    private Call<SessionRequest> sessionStartClass;
    private Call<List<ClassRequest>> classRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        // my_child_toolbar is defined in the layout file
        androidx.appcompat.widget.Toolbar myChildToolbar =
                (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_child_toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        Bundle extras;

        gridView = (GridView) findViewById(R.id.student_list_view);

        emptyView = findViewById(R.id.empty_students_view);

        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if (extras == null) {
                newSubject = null;
            } else {
                newSubject = extras.getString("SUBJECT_NAME");
                subjectId = extras.getInt("SUBJECT_ID");
            }
        }

        classesList = (AutoCompleteTextView) findViewById(R.id.class_list);

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
                studentList();
            }
        });

        Button selectAll = (Button) findViewById(R.id.select_all_btn);
        Button unselectAll = (Button) findViewById(R.id.unselect_all_btn);

        startBtn = (Button) findViewById(R.id.start_btn);
        stopBtn = (Button) findViewById(R.id.stop_btn);

        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < gridView.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) gridView.getChildAt(i).findViewById(R.id.checkbox_attendance);
                    checkBox.setChecked(true);
                }
            }
        });

        unselectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < gridView.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) gridView.getChildAt(i).findViewById(R.id.checkbox_attendance);
                    checkBox.setChecked(false);
                }
            }
        });

        Button btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < gridView.getChildCount(); i++) {
                    TextView sid = (TextView) gridView.getChildAt(i).findViewById(R.id.student_id);
                    int idSubject = subjectId;
                    int studentId = Integer.parseInt(sid.getText().toString());
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    CheckBox checkBox = (CheckBox) gridView.getChildAt(i).findViewById(R.id.checkbox_attendance);
                    String att_status;
                    if (checkBox.isChecked()) {
                        att_status = "Present";
                    } else {
                        att_status = "Absent";
                    }
                    saveAttendance(idSubject, studentId, currentDate, att_status);
                }
            }
        });
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshmark);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWifiSSID();
                sessionStart();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionStop();
            }
        });
    }

    public void sessionStart() {
        SessionRequest request = new SessionRequest();
        request.setSession_status("active");
        request.setSsid(ssid);
        sessionStartClass = ApiClient.getUserService().sessionUpdateResponse(classScheduleId, request);
        sessionStartClass.enqueue(new Callback<SessionRequest>() {
            @Override
            public void onResponse(Call<SessionRequest> call, Response<SessionRequest> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Session Started.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SessionRequest> call, Throwable t) {

            }
        });
    }

    public void sessionStop() {
        SessionRequest request = new SessionRequest();
        request.setSession_status("inactive");
        request.setSsid(ssid);
        sessionStartClass = ApiClient.getUserService().sessionUpdateResponse(classScheduleId, request);
        sessionStartClass.enqueue(new Callback<SessionRequest>() {
            @Override
            public void onResponse(Call<SessionRequest> call, Response<SessionRequest> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Session Stopped.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SessionRequest> call, Throwable t) {

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

    public void setWifiSSID() {
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
                ActivityCompat.requestPermissions(MarkAttendanceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return;
            }
            ssid = wifiInfo.getSSID();
            ssid = ssid.replace("\"", "");
        }
    }

    public void getClassScheduleId(int classId) {
        sessionRequestClass = ApiClient.getUserService().getClassScheduleIdResponse(classId, subjectId);
        sessionRequestClass.enqueue(new Callback<List<SessionRequest>>() {
            @Override
            public void onResponse(Call<List<SessionRequest>> call, Response<List<SessionRequest>> response) {
                if (!response.body().isEmpty()) {
                    classScheduleId = response.body().get(0).getId();
                }
            }

            @Override
            public void onFailure(Call<List<SessionRequest>> call, Throwable t) {
            }
        });
    }

    public void classList() {
        Log.e("Class:", ""+ userId);
        classListResponse = ApiClient.getUserService().teacherClassResponse(userId, newSubject);
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

    public void studentList() {

        gridView.setEmptyView(emptyView);
        Log.e("StudentList:", ""+ userId);

        studentListResponse = ApiClient.getUserService().studentListResponse(userId, classSelected, newSubject);

        studentListResponse.enqueue(new Callback<List<StudentListResponse>>() {
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
                    StudentListAdapter studentListAdapter = new StudentListAdapter(getApplicationContext(), dataList);
                    gridView.setAdapter(studentListAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<StudentListResponse>> call, Throwable t) {

            }
        });
    }

    public void saveAttendance(int idSubject, int studentId, String currentDate, String att_status) {
        AttendanceRequest attendanceRequest = new AttendanceRequest();
        attendanceRequest.setSubject_id(idSubject);
        attendanceRequest.setStudent_id(studentId);
        attendanceRequest.setAtt_date(currentDate);
        attendanceRequest.setAtt_status(att_status);
        List<AttendanceRequest> attList = new ArrayList<>();

        Call<List<AttendanceRequest>> attendanceResponseCall = ApiClient.getUserService().studentAttendanceListResponse(studentId, idSubject, currentDate);

        attendanceResponseCall.enqueue(new Callback<List<AttendanceRequest>>() {
            @Override
            public void onResponse(Call<List<AttendanceRequest>> call, Response<List<AttendanceRequest>> response) {
                if (response.body().isEmpty()) {
                    Call<AttendanceRequest> attendanceRequestCall = ApiClient.getUserService().studentAttendanceRequest(attendanceRequest);
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
//                    Toast.makeText(getApplicationContext(), "Today's attendance for the selected class and subject has already been taken", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceRequest>> call, Throwable t) {

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
}