package com.example.android.attendance.Models;

public class SessionRequest {
    private int id;
    private String session_status;
    private String ssid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSession_status() {
        return session_status;
    }

    public void setSession_status(String session_status) {
        this.session_status = session_status;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
