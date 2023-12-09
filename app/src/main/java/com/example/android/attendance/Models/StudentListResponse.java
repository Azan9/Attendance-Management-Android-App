package com.example.android.attendance.Models;

public class StudentListResponse {
    private int id;
    private int teacher_user_id;
    private int student_id;
    private String student_name;
    private String profile_image;
    private int class_id;
    private String class_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacher_user_id() {
        return teacher_user_id;
    }

    public void setTeacher_user_id(int teacher_user_id) {
        this.teacher_user_id = teacher_user_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }
}
