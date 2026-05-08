package com.fee.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.fee.model.FeeStudent;
import com.school.model.Student;
import com.util.SchoolDataStore;

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
                        field[2],                   //Course
                        Integer.parseInt(field[3]), //Semester
                        Long.parseLong(field[4]),   //Fee Amount
                        LocalDate.parse(field[5], DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        field[6]
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

    public static void syncWithSchool() {
        for(Student s : SchoolDataStore.students){
            boolean exists = false;
            for(FeeStudent fs : FeeDataStore.students){
                if(fs.getId() == s.getSapId()){
                    exists = true;
                    break;
                }
            }

            if(!exists) {
                FeeDataStore.students.add(new FeeStudent(
                    s.getName(),
                    s.getSapId(),
                    s.getDepartment(),
                    s.getCurrentSemester(),
                    147448l,
                    LocalDate.now().minusDays(5),
                    "unpaid"
                ));
            }

        }
        saveStudents(FeeDataStore.students);
    }
}
