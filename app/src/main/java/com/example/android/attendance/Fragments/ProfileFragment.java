package com.example.android.attendance.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.HomeActivity;
import com.example.android.attendance.MainActivity;
import com.example.android.attendance.Models.ChangePasswordRequest;
import com.example.android.attendance.Models.LoginResponse;
import com.example.android.attendance.Models.ProfileResponse;
import com.example.android.attendance.Models.UpdateImageRequest;
import com.example.android.attendance.Models.UpdateProfileRequest;
import com.example.android.attendance.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    public static final String StudId = "studIdKey"; // student id key
    int userId, id;
    public static final String Token = "tokenKey";
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "MyPrefs";
    public static final String UserId = "userIdKey";
    public static final String Role = "roleKey";
    public static final String Pass = "passwordKey";
    String userRole, userPassword, token;
    Call<List<ProfileResponse>> profileResponseCall;
    Call<UpdateProfileRequest> updateProfileRequestCall;
    Call<UpdateImageRequest> updateImageResponse;
    ActivityResultLauncher<Intent> getProfileImage;
    Call<ResponseBody> responseBodyCall;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(UserId, 0);
        userRole = sharedPreferences.getString(Role, null);
        userPassword = sharedPreferences.getString(Pass, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        if (userRole.equals("student")) {
            profileResponseCall = ApiClient.getUserService().studentProfileResponse(userId);
        }
        if (userRole.equals("teacher")) {
            profileResponseCall = ApiClient.getUserService().teacherProfileResponse(userId);
        }
            profileResponseCall.enqueue(new Callback<List<ProfileResponse>>() {
                @SuppressLint("ResourceType")
                @Override
                public void onResponse(Call<List<ProfileResponse>> call, Response<List<ProfileResponse>> response) {
                    List<ProfileResponse> dataList;
                    dataList = new ArrayList<>();
                    if (response.isSuccessful()) {
                        List<ProfileResponse> models = response.body();
                        TextView username = (TextView) view.findViewById(R.id.user_name);
                        TextView userRoleType = (TextView) view.findViewById(R.id.user_role_view);
                        EditText name = (EditText) view.findViewById(R.id.edit_full_name);
                        EditText email = (EditText) view.findViewById(R.id.edit_email_text);
                        EditText phone_number = (EditText) view.findViewById(R.id.edit_phone_text);
                        EditText gender = (EditText) view.findViewById(R.id.edit_gender_text);
                        EditText birthdate = (EditText) view.findViewById(R.id.edit_birthday_text);
                        ImageView profileImage = (ImageView) view.findViewById(R.id.user_image);

                        if (models.isEmpty()) {

                        } else {
                            for (ProfileResponse model: models){
                                dataList.add(model);
                            }
                            id = dataList.get(0).getId();
                            String imageURL = dataList.get(0).getProfile_image();
                            username.setText(dataList.get(0).getFull_name());
                            userRoleType.setText(userRole.substring(0,1).toUpperCase() + userRole.substring(1).toLowerCase());
                            name.setText(dataList.get(0).getFull_name());
                            email.setText(dataList.get(0).getEmail());
                            phone_number.setText(Integer.toString(dataList.get(0).getPhone_number()));
                            gender.setText(dataList.get(0).getGender());
                            birthdate.setText(dataList.get(0).getBirthdate());
                            Picasso.get().load("http://192.168.1.71:8000" + imageURL).into(profileImage);

                        }

                    } else {

                    }
                }
                @Override
                public void onFailure(Call<List<ProfileResponse>> call, Throwable t) {

                }
            });
        Button saveBtn = (Button) view.findViewById(R.id.save_btn);
        Button updateBtn = (Button) view.findViewById(R.id.update_pass_btn);
        EditText fullName = (EditText) view.findViewById(R.id.edit_full_name);
        EditText email = (EditText) view.findViewById(R.id.edit_email_text);
        EditText phoneNumber = (EditText) view.findViewById(R.id.edit_phone_text);
        EditText gender = (EditText) view.findViewById(R.id.edit_gender_text);
        EditText birthdate = (EditText) view.findViewById(R.id.edit_birthday_text);
        EditText currentPassword = (EditText) view.findViewById(R.id.current_password);
        EditText newPassword = (EditText) view.findViewById(R.id.change_password1);
        EditText newPasswordAgain = (EditText) view.findViewById(R.id.change_password2);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
                String mName = fullName.getText().toString();
                String mEmail = email.getText().toString();
                int mNumber = Integer.parseInt(phoneNumber.getText().toString());
                String mGender = gender.getText().toString();
                String mBirthdate = birthdate.getText().toString();
                updateProfileRequest.setUser_id(userId);
                updateProfileRequest.setFull_name(mName);
                updateProfileRequest.setEmail(mEmail);
                updateProfileRequest.setPhone_number(mNumber);
                updateProfileRequest.setGender(mGender);
                updateProfileRequest.setBirthdate(mBirthdate);
                if (userRole.equals("student")) {
                    updateProfileRequestCall = ApiClient.getUserService().updateStudentProfile(updateProfileRequest, id);
                }
                if (userRole.equals("teacher")) {
                    updateProfileRequestCall = ApiClient.getUserService().updateTeacherProfile(updateProfileRequest, id);
                }
                updateProfileRequestCall.enqueue(new Callback<UpdateProfileRequest>() {
                    @Override
                    public void onResponse(Call<UpdateProfileRequest> call, Response<UpdateProfileRequest> response) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            fragmentManager.beginTransaction().detach(ProfileFragment.this).commitNow();
                            fragmentManager.beginTransaction().attach(ProfileFragment.this).commitNow();
                        } else {
                            fragmentManager.beginTransaction().detach(ProfileFragment.this).attach(ProfileFragment.this).commit();
                        }
                        Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileRequest> call, Throwable t) {

                    }
                });
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPassword.getText().toString().equals(userPassword) && newPassword.getText().toString().equals(newPasswordAgain.getText().toString())) {
                    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
                    changePasswordRequest.setPassword(newPassword.getText().toString());
                    responseBodyCall = ApiClient.getUserService().changePasswordResponse(userId, changePasswordRequest);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                logout();
                                Toast.makeText(getActivity(), "Password Changed.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });

        ShapeableImageView profileImage = (ShapeableImageView) view.findViewById(R.id.user_image);
        ImageView editProfileImage = (ImageView) view.findViewById(R.id.edit_image);

        getProfileImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri selectedImageUri = data.getData();

                            try {
                                InputStream is = getActivity().getContentResolver().openInputStream(data.getData());
                                uploadImage(getBytes(is));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            profileImage.setImageURI(selectedImageUri);
                        }
                    }
                });

        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                getProfileImage.launch(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    private void uploadImage(byte[] imageBytes) {

        RequestBody requestFile = RequestBody.create(imageBytes, MediaType.parse("image/*"));

        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", "user.jpg", requestFile);

        if (userRole.equals("student")) {
            updateImageResponse = ApiClient.getUserService().updateStudentImage(id, body);
        }

        if (userRole.equals("teacher")) {
            updateImageResponse = ApiClient.getUserService().updateTeacherImage(id, body);
        }

        updateImageResponse.enqueue(new Callback<UpdateImageRequest>() {
            @Override
            public void onResponse(Call<UpdateImageRequest> call, Response<UpdateImageRequest> response) {
                Toast.makeText(getActivity(), "Profile Picture Updated.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UpdateImageRequest> call, Throwable t) {
                Toast.makeText(getActivity(), "Profile Picture Failed to Update.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logout(){
        // calling method to edit values in shared prefs.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        token = sharedPreferences.getString(Token, null);

        Call<ResponseBody> logoutResponseCall = ApiClient.getUserService().userLogout("Token " + token);

        logoutResponseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 204 || response.isSuccessful()) {
                    // Clearing the data in shared prefs.
                    editor.clear();
                    // Applying empty data to shared prefs.
                    editor.apply();
                    // starting main activity after clearing values in shared preferences.
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "No Response from server", Toast.LENGTH_LONG).show();
            }
        });
    }

}