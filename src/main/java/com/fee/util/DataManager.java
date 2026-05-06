package com.fee.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import com.fee.model.Student;

public class DataManager {

    private static final String DATA_DIR = System.getProperty("user.dir") + "/src/main/resources/fee/data/";

    public static ArrayList<Student> loadStudents() {
        ArrayList<Student> studentData = new ArrayList<>();
        try (BufferedReader loader = new BufferedReader(new FileReader(DATA_DIR + "records.txt"))) {
            String line;
            while ((line = loader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] field = line.split("\\|");
                if (field.length < 8) continue;
                LocalDate paidDate = (field.length >= 9 && !field[8].isBlank()) ? LocalDate.parse(field[8]) : null;
                studentData.add(new Student(
                        field[0],
                        Integer.parseInt(field[1]),
                        field[2],
                        Integer.parseInt(field[3]),
                        Long.parseLong(field[4]),
                        Long.parseLong(field[5]),
                        Integer.parseInt(field[6]),
                        field[7],
                        paidDate
                ));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return studentData;
    }

    public static void saveStudents(ArrayList<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIR + "records.txt"))) {
            for (Student s : students) {
                writer.write(s.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save students: " + e.getMessage());
        }
    }
}
