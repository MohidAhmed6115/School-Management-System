package com.school.model.attendance;

public class Attendance {
    private String date;        // "2026-04-21"
    private String status;      // "Full Attendance", "Half Attendance", "Absent"
    private int creditHours;    // 4 total credit hours per day

    public Attendance() {} // Jackson needs this

    public Attendance(String date, String status) {
        this.date = date;
        this.status = status;
        setCreditHours();
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
        setCreditHours();
    }

    public int getCreditHours() { return creditHours; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }

    public void setCreditHours() {
        if (status == null) { creditHours = 0; return; }
        switch (status) {
            case "Full Attendance" -> creditHours = 4;
            case "Half Attendance" -> creditHours = 2;
            default                -> creditHours = 0;
        }
    }
}