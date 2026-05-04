package com.school.model.result;

import java.util.List;

public class SemesterResult {
    private int sapId;
    private List<Course> courses;
    private double gpa;

    public SemesterResult() {} // Jackson needs this

    public SemesterResult(int sapId, List<Course> courses, double gpa) {
        this.sapId = sapId;
        this.courses = courses;
        this.gpa = gpa;
    }

    // getters and setters
    public int getSapId() {return sapId;}

    public void setSapId(int sapId) {this.sapId = sapId;}

    public List<Course> getCourses() {return courses;}

    public void setCourses(List<Course> courses) {this.courses = courses;}

    public double getGpa() {return gpa;}

    public void setGpa(double gpa) {this.gpa = gpa;}

    public void calculateGpa() {
        double totalCreditHours = 0;
        double totalCreditPoints = 0;

        for (Course course : courses) {
            int marks = course.getMarks();
            course.setGrade(calculateGrade(marks));

            double creditPoints;
            if (marks >= 80) {
                creditPoints = course.getCreditHours() * 4;
            } else if (marks >= 50) {
                creditPoints = course.getCreditHours() * (1 + (marks - 50) * 0.1);
            } else {
                creditPoints = 0;
            }

            totalCreditHours += course.getCreditHours();
            totalCreditPoints += creditPoints;
        }

        if (totalCreditHours == 0)
            gpa = 0;
        else
            gpa = totalCreditPoints / totalCreditHours;
    }

    public static String calculateGrade(int marks) {
        if (marks >= 90) {
            return "A+";
        } else if (marks >= 80) {
            return "A";
        } else if (marks >= 78) {
            return "A-";
        } else if (marks >= 75) {
            return "B+";
        } else if (marks >= 70) {
            return "B";
        } else if (marks >= 68) {
            return "B-";
        } else if (marks >= 65) {
            return "C+";
        } else if (marks >= 60) {
            return "C";
        } else if (marks >= 58) {
            return "C-";
        } else if (marks >= 55) {
            return "D+";
        } else if (marks >= 50) {
            return "D";
        } else {
            return "F";
        }
    }
}
