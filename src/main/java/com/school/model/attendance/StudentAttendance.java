package com.school.model.attendance;

import java.util.List;

public class StudentAttendance {
    private int sapId;
    private double attendancePercentage;
    private List<Attendance> attendance;

    public StudentAttendance() {
        attendancePercentage = calculateAttendancePercentage();
    }

    public double calculateAttendancePercentage()
    {
        double percentage;
        int totalCreditHours = 0;
        int totalDays = attendance.size();
        for(Attendance att : attendance) {
            totalCreditHours += att.getCreditHours();
        }
        percentage = (totalCreditHours / (4 * totalDays)) * 100;

        return percentage;
    }
    // getters and setters
    public int getSapId() {return sapId;}

    public void setSapId(int sapId) {this.sapId = sapId;}

    public double getAttendancePercentage() {return attendancePercentage;}

    public void setTodayAttendance(String date, String status) {
        for(Attendance att : attendance) {
            if(att.getDate().equals(date)) {
                att.setStatus(status);
            }
        }
        // if date doesn't exist, it creates it
        this.attendance.add(new Attendance(date, status));
    }
}
