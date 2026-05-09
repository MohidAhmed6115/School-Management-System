package com.school.model.announcements;

public class StudentAnnouncement {
    private int importance;
    private String date;
    private String message;

    public StudentAnnouncement() {}

    public StudentAnnouncement(int importance, String date, String message) {
        this.importance = importance;
        this.date = date;
        this.message = message;
    }

    public int getImportance() {return importance;}
    public void setImportance(int importance) {this.importance = importance;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
}
