package com.school.model;

import com.util.SchoolDataStore;

import java.util.Random;

public class Teacher extends User {
    private double salary;

    public Teacher(String name, double salary) {
        super(name);
        this.salary = salary;
        // generates a unique sapId
        this.setSapId(generateSapId());
        this.setEmail(createEmail());
    }

    public Teacher(String name, int sapId, String password, double salary, String email) {
        super(name, sapId, password, email);
        this.salary = salary;
    }

    public double getSalary() {return salary;}

    private int generateSapId() {
        int generatedSapId;

        Random random = new Random();
        do {
            generatedSapId = random.nextInt(50000, 60000);
        }
        while (sapIdExists(generatedSapId));

        return generatedSapId;
    }

    private boolean sapIdExists(int sapId) {
        for (Teacher t : SchoolDataStore.teachers) {
            if (t.getSapId() == sapId) {
                return true;
            }
        }
        return false;
    }

    private String createEmail() {return this.getSapId() + "@teachers.riphah.edu.pk";}

}