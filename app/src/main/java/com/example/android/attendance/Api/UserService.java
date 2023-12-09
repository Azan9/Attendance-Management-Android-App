package com.example.android.attendance.Api;

import com.example.android.attendance.Models.AttRecordRequest;
import com.example.android.attendance.Models.AttendanceRequest;
import com.example.android.attendance.Models.ChangePasswordRequest;
import com.example.android.attendance.Models.ClassRequest;
import com.example.android.attendance.Models.LoginRequest;
import com.example.android.attendance.Models.LoginResponse;
import com.example.android.attendance.Models.LogoutRequest;
import com.example.android.attendance.Models.ProfileResponse;
import com.example.android.attendance.Models.RecordResponse;
import com.example.android.attendance.Models.ScheduleResponse;
import com.example.android.attendance.Models.SessionRequest;
import com.example.android.attendance.Models.StudentListResponse;
import com.example.android.attendance.Models.SubjectsResponse;
import com.example.android.attendance.Models.UpdateImageRequest;
import com.example.android.attendance.Models.UpdateProfileRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {
    @POST("api/login/")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @POST("api/logout/")
    Call<ResponseBody> userLogout(@Header("Authorization") String authorization);

    @GET("api/join_student_subjects/{id}")
    Call<List<SubjectsResponse>> studentSubjects(@Path("id") int id);

    @GET("api/join_teacher_subjects/{id}")
    Call<List<SubjectsResponse>> teacherSubjects(@Path("id") int id);

    @GET("api/student_profile/{user_id}")
    Call<List<ProfileResponse>> studentProfileResponse(@Path("user_id") int user_id);

    @GET("api/teacher_profile/{user_id}")
    Call<List<ProfileResponse>> teacherProfileResponse(@Path("user_id") int user_id);

    @GET("api/user_teacher_schedule/{user_id}-{day}")
    Call<List<ScheduleResponse>> teacherScheduleResponse(@Path("user_id") int user_id, @Path("day") String day);

    @GET("api/user_student_schedule/{user_id}-{day}")
    Call<List<ScheduleResponse>> studentScheduleResponse(@Path("user_id") int user_id, @Path("day") String day);

    @GET("api/user_teachers_schedule/{user_id}")
    Call<List<ScheduleResponse>> teachersScheduleResponse(@Path("user_id") int user_id);

    @GET("api/user_students_schedule/{user_id}")
    Call<List<ScheduleResponse>> studentsScheduleResponse(@Path("user_id") int user_id);

    @GET("api/class_list_all/{user_id}")
    Call<List<StudentListResponse>> teacherAllClassResponse(@Path("user_id") int user_id);

    @GET("api/class_list/{user_id}-{subject_name}")
    Call<List<StudentListResponse>> teacherClassResponse(@Path("user_id") int user_id, @Path("subject_name") String subject_name);

    @GET("api/class_list_student/{user_id}-{subject_name}")
    Call<List<StudentListResponse>> studentClassResponse(@Path("user_id") int user_id, @Path("subject_name") String subject_name);

    @GET("api/student_attendance_list/{user_id}-{class_name}-{subject_name}")
    Call<List<StudentListResponse>> studentListResponse(@Path("user_id") int user_id, @Path("class_name") String class_name, @Path("subject_name") String subject_name);

    @POST("api/student_attendance/")
    Call<AttendanceRequest> studentAttendanceRequest(@Body AttendanceRequest attendanceRequest);

    @GET("api/student_att_list/{student_id}:{subject_id}:{att_date}")
    Call<List<AttendanceRequest>> studentAttendanceListResponse(@Path("student_id") int student_id, @Path("subject_id") int subject_id, @Path("att_date") String attDate);

    @GET("api/student_attendance/")
    Call<List<AttendanceRequest>> studentAttendanceResponse(@Body AttendanceRequest attendanceRequest);

    @PUT("api/students/{id}/")
    Call<UpdateProfileRequest> updateStudentProfile(@Body UpdateProfileRequest updateProfileRequest, @Path("id") int id);

    @PUT("api/teachers/{id}/")
    Call<UpdateProfileRequest> updateTeacherProfile(@Body UpdateProfileRequest updateProfileRequest, @Path("id") int id);

    @Multipart
    @PATCH("api/students/{id}/")
    Call<UpdateImageRequest> updateStudentImage(@Path("id") int id, @Part MultipartBody.Part profile_image);

    @Multipart
    @PATCH("api/teachers/{id}/")
    Call<UpdateImageRequest> updateTeacherImage(@Path("id") int id, @Part MultipartBody.Part image);

    @GET("api/student_attendance_list_record/{class_name}:{subject_name}:{date}")
    Call<List<RecordResponse>> studentAttListResponse(@Path("class_name") String class_name, @Path("subject_name") String subject_name, @Path("date") String dateName);

    @GET("api/student_subject_record/{user_id}-{subject_name}")
    Call<List<AttRecordRequest>> studentSubResponse(@Path("user_id") int id, @Path("subject_name") String subject_name);


    @GET("api/student_subject_teacher_record/{student_id}-{subject_name}")
    Call<List<AttRecordRequest>> studentAttListResponse(@Path("student_id") int id, @Path("subject_name") String subject_name);


    @PATCH("api/users/{id}")
    Call<ResponseBody> changePasswordResponse(@Path("id") int id, @Body ChangePasswordRequest changePasswordRequest);

    @GET("api/session_classes/{class_name}")
    Call<List<ClassRequest>> getClassIdResponse(@Path("class_name") String class_name);

    @GET("api/session/{class_id}-{subject_id}")
    Call<List<SessionRequest>> getClassScheduleIdResponse(@Path("class_id") int id, @Path("subject_id") int subject_id);

    @GET("api/class_schedules/{id}/")
    Call<SessionRequest> getSessionStatusResponse(@Path("id") int id);

    @PATCH("api/class_schedules/{id}/")
    Call<SessionRequest> sessionUpdateResponse(@Path("id") int id, @Body SessionRequest sessionRequest);
}
