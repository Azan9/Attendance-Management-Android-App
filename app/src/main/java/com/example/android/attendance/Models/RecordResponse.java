package com.example.android.attendance.Models;

public class RecordResponse {
    private int student_id;
    private String student_name;
    private String att_status;


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

    public String getAtt_status() {
        return att_status;
    }

    public void setAtt_status(String att_status) {
        this.att_status = att_status;
    }
}
