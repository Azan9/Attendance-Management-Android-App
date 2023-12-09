package com.example.android.attendance.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.MarkAttendanceActivity;
import com.example.android.attendance.Models.SubjectsResponse;
import com.example.android.attendance.R;
import com.example.android.attendance.Adapters.SubjectsAdapter;
import com.example.android.attendance.StudentAttendanceActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.GridView.AUTO_FIT;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AttendanceFragment extends Fragment {

    public static final String Role = "roleKey";
    private static final String SHARED_PREFS = "MyPrefs" ;
    public static final String USER_ID = "userIdKey";
    private String userRole;
    private SharedPreferences sharedPreferences;
    private int userId;
    private Call<List<SubjectsResponse>> subjectsResponse;

    public AttendanceFragment() {
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
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.grid_view);

        if (userRole.equals("student")){
            subjectsResponse = ApiClient.getUserService().studentSubjects(userId);
        }
        if (userRole.equals("teacher")){
            subjectsResponse = ApiClient.getUserService().teacherSubjects(userId);
        }
        subjectsResponse.enqueue(new Callback<List<SubjectsResponse>>() {
            @Override
            public void onResponse(Call<List<SubjectsResponse>> call, Response<List<SubjectsResponse>> response) {

                List<SubjectsResponse> dataList;
                dataList = new ArrayList<>();

                if (response.isSuccessful()) {
                    List<SubjectsResponse> models = response.body();
                    assert models != null;
                    for (SubjectsResponse model: models) {
                        dataList.add(model);
                    }
                    SubjectsAdapter subjectsAdapter = new SubjectsAdapter(getContext(), dataList);
                    gridView.setAdapter(subjectsAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<SubjectsResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "No Classes found", Toast.LENGTH_SHORT).show();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView selectUserId = (TextView) view.findViewById(R.id.user_id);
                TextView selectSubjectId = (TextView) view.findViewById(R.id.subject_id);
                TextView selectedSubject = (TextView) view.findViewById(R.id.subject_selected_text_view);
                int subjectId = Integer.parseInt(selectSubjectId.getText().toString());
                int uId = Integer.parseInt(selectUserId.getText().toString());
                String subjectName = selectedSubject.getText().toString();
                Intent intent;
                if (userRole.equals("student")){
                    intent = new Intent(getActivity(), StudentAttendanceActivity.class);
                    intent.putExtra("ID", uId);
                    intent.putExtra("SUBJECT_ID", subjectId);
                    intent.putExtra("SUBJECT_NAME", subjectName);
                    startActivity(intent);
                }
                if (userRole.equals("teacher")){
                    intent = new Intent(getActivity(), MarkAttendanceActivity.class);
                    intent.putExtra("SUBJECT_ID", subjectId);
                    intent.putExtra("SUBJECT_NAME", subjectName);
                    startActivity(intent);
                }


            }
        });

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshattendance);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    fragmentManager.beginTransaction().detach(AttendanceFragment.this).commitNow();
                    fragmentManager.beginTransaction().attach(AttendanceFragment.this).commitNow();
                } else {
                    fragmentManager.beginTransaction().detach(AttendanceFragment.this).attach(AttendanceFragment.this).commit();
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }




}