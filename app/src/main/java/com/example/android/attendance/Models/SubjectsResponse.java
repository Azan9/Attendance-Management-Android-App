package com.example.android.attendance.Models;

public class SubjectsResponse {
    private int id;
    private int user_id;
    private String full_name;
    private String subject_name;
    private int subject_id;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public int getSubject_id() {
        return subject_id;
    }
}
