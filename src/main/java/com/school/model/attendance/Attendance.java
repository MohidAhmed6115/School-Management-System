package com.school.model.attendance;


import java.util.Date;

public class Attendance {
    private String date;        // "2026-04-21"
    private String status;      // "Full Attendance", "Half Attendance", "Absent"
    private int creditHours;    // 4 total credit hours per day

    public Attendance(String date, String status) {
        this.date = date;
        this.status = status;
        setCreditHours();
    }

    public int getCreditHours() {return this.creditHours;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public void setStatus(String status) {
        this.status = status;
        setCreditHours();
    }

    public void setCreditHours() {
        if(status.equals("Full Attendance")) {
            creditHours = 4;
        }
        else if (status.equals("Half Attendance")) {
            creditHours = 2;
        }
        else {
            creditHours = 0;
        }
    }

}
