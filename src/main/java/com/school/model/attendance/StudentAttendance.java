package com.school.model.attendance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.school.util.DataStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentAttendance {
    private int sapId;
    private double attendancePercentage;
    private List<Attendance> attendance = new ArrayList<>();

    public StudentAttendance() {}

    public int getSapId() { return sapId; }
    public void setSapId(int sapId) { this.sapId = sapId; }

    public List<Attendance> getAttendance() { return attendance; }
    public void setAttendance(List<Attendance> attendance) {
        this.attendance = attendance;
    }

    public double getAttendancePercentage() {
        if (attendance == null || attendance.isEmpty()) return 0;

        return attendancePercentage;
    }

    public void calculateAttendancePercentage() {
        int totalCreditHours = 0;
        int totalDays = attendance.size();

        for (Attendance att : attendance) {
            totalCreditHours += att.getCreditHours();
        }

        double percentage = ((double) totalCreditHours / (4.0 * totalDays)) * 100;
        attendancePercentage = Math.round(percentage * 100.0) / 100.0;
    }

    public void setTodayAttendance(String date, String status) {
        for (Attendance att : attendance) {
            if (att.getDate().equals(date)) {
                att.setStatus(status);
                calculateAttendancePercentage(); // needed here too
                return;
            }
        }

        attendance.add(new Attendance(date, status));
        calculateAttendancePercentage();
    }

    public String getTodayStatus(String date) {
        for (Attendance att : attendance) {
            if (att.getDate().equals(date)) {
                return att.getStatus();
            }
        }
        return null;
    }

    @JsonIgnore
    public boolean isPresent() {
        String date = String.valueOf(LocalDate.now());
        for (Attendance att : attendance) {
            if(att.getDate().equals(date)) {
                return !att.getStatus().equals("Absent");
            }
        }
        return false;
    }
}