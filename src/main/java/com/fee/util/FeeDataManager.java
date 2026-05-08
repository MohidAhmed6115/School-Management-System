package com.fee.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.fee.model.FeeStudent;

public class FeeDataManager {

    private static final String DATA_DIR = System.getProperty("user.dir") + "/src/main/resources/fee/data/";

    public static ArrayList<FeeStudent> loadStudents() {
        ArrayList<FeeStudent> studentData = new ArrayList<>();
        try (BufferedReader loader = new BufferedReader(new FileReader(DATA_DIR + "fee-record.txt"))) {
            String line;
            while ((line = loader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] field = line.split("\\|");
                // if (field.length < 8) continue;
                // LocalDate paidDate = (field.length >= 9 && !field[8].isBlank()) ? LocalDate.parse(field[8]) : null;
                studentData.add(new FeeStudent(
                        field[0],                   //Name
                        Integer.parseInt(field[1]), //Sap
                        Integer.parseInt(field[2]), //Semester
                        Long.parseLong(field[3])    //Fee Amount
                ));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return studentData;
    }

    public static void saveStudents(ArrayList<FeeStudent> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIR + "fee-record.txt"))) {
            for (FeeStudent s : students) {
                writer.write(s.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save students: " + e.getMessage());
        }
    }
}
