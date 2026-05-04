package com.school.model.result;

public class Course {
    private String courseName;
    private String courseCode;
    private double creditHours;
    private String grade;
    private int marks;

    public Course() {} // Jackson needs this

    public Course(String courseCode, String courseName, int creditHours, String grade, int marks) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.creditHours = creditHours;
        this.grade = grade;
        this.marks = marks;
    }

    // getters and setters
    public String getCourseName() {return courseName;}
    public void setCourseName(String courseName) {this.courseName = courseName;}

    public String getCourseCode() {return courseCode;}
    public void setCourseCode(String courseCode) {this.courseCode = courseCode;}

    public double getCreditHours() {return creditHours;}
    public void setCreditHours(double creditHours) {this.creditHours = creditHours;}

    public String getGrade() {return grade;}
    public void setGrade(String grade) {this.grade = grade;}

    public int getMarks() {return marks;}
    public void setMarks(int marks) {this.marks = marks;}
}