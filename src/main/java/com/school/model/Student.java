package com.school.model;

import com.util.DataStore;

import java.util.Random;

public class Student extends User {
    private String department;
    private double cgpa;
    private int currentSemester;

    public Student (String name, String department) {
        super(name);
        this.department = department;
        this.currentSemester = 1;
        this.setSapId(generateSapId());
        this.setEmail(createEmail());
    }

    public Student (String name, int sapId, String password, double cgpa, String department, String email, int currentSemester) {
        super(name, sapId, password, email);
        this.cgpa = cgpa;
        this.department = department;
        this.currentSemester = currentSemester;
    }

    public String getDepartment() { return department; }
    public double getCgpa() { return cgpa; }
    public int getCurrentSemester() { return currentSemester; }

    public void setDepartment(String department) { this.department = department; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }
    public void setCurrentSemester(int currentSemester) { this.currentSemester = currentSemester; }

    private int generateSapId() {
        int generatedSapId;

        Random random = new Random();
        do {
            generatedSapId = random.nextInt(60000, 80000);
        }
        while (sapIdExists(generatedSapId));

        return generatedSapId;
    }

    private boolean sapIdExists(int sapId) {
        for (Student s : DataStore.students) {
            if (s.getSapId() == sapId) {
                return true;
            }
        }
        return false;
    }

    private String createEmail() {return this.getSapId() + "@students.riphah.edu.pk";}
}