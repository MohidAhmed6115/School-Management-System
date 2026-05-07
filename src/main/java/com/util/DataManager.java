package com.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.model.Admin;
import com.school.model.Student;
import com.school.model.Teacher;
import com.school.model.Librarian;
import com.school.model.attendance.StudentAttendance;
import com.school.model.result.SemesterResult;

import java.io.*;
import java.util.ArrayList;

public class DataManager {

    // ── Single source of truth for where data files live on disk ──
    // This creates a folder at: C:/Users/YourName/SchoolSystem/data/
    private static final String DATA_DIR = System.getProperty("user.dir") + "/src/main/resources/school/data/";

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String RESULTS_DIR = DATA_DIR + "results/";
    private static final String ATTENDANCE_DIR = DATA_DIR + "attendance/";

    // ════════════════════════════════════════════════════════════
    // INIT — Call this ONCE at app startup in com.fee.app.Main.java
    // It creates the data folder and copies the default .txt
    // files from resources into it (only if they don't exist yet)
    // ════════════════════════════════════════════════════════════
    public static void init() {
        // Create the folder if it doesn't already exist
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("Created data directory at: " + DATA_DIR);
        }

        // Copy each default file from resources → disk (only if missing)
        copyIfMissing("students.txt");
        copyIfMissing("teachers.txt");
        copyIfMissing("admins.txt");
    }

    public static void initResultsDir() {
        File dir = new File(RESULTS_DIR);
        if (!dir.exists())
            dir.mkdirs();
    }

    // Copies a file from /resources/data/ to the DATA_DIR folder
    // Only runs if the file doesn't already exist on disk
    private static void copyIfMissing(String filename) {
        File target = new File(DATA_DIR + filename);

        if (target.exists()) {
            return; // Already exists — don't overwrite
        }

        try (InputStream in = DataManager.class.getResourceAsStream("/school/data/" + filename);
                FileOutputStream out = new FileOutputStream(target)) {

            if (in == null) {
                // Resource file not found — create an empty file instead
                target.createNewFile();
                System.out.println("Created empty file: " + filename);
                return;
            }

            in.transferTo(out);
            System.out.println("Copied default data: " + filename);

        } catch (IOException e) {
            System.out.println("Could not copy " + filename + ": " + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════
    // LOAD METHODS — Read from disk into ArrayLists
    // ════════════════════════════════════════════════════════════

    public static ArrayList<Student> loadStudents() {
        ArrayList<Student> students = new ArrayList<>();
        File file = new File(DATA_DIR + "students.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                // Skip blank lines to avoid crashes
                if (line.isBlank())
                    continue;

                String[] parts = line.split("\\|");

                // Guard against corrupted/incomplete lines
                if (parts.length < 6)
                    continue;

                students.add(new Student(
                        parts[0], // name
                        Integer.parseInt(parts[1]), // sapId
                        parts[2], // password
                        Double.parseDouble(parts[3]), // gpa
                        parts[4], // department
                        parts[5], // department
                        Integer.parseInt(parts[6]) // current semester
                ));
            }
        } catch (IOException e) {
            System.out.println("Could not load students: " + e.getMessage());
        }

        return students;
    }

    public static ArrayList<Admin> loadAdmins() {
        ArrayList<Admin> admins = new ArrayList<>();
        File file = new File(DATA_DIR + "admins.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.isBlank())
                    continue;

                String[] parts = line.split("\\|");

                if (parts.length < 4)
                    continue;

                admins.add(new Admin(
                        parts[0], // name
                        Integer.parseInt(parts[1]), // sapId
                        parts[2], // password
                        parts[3] // email
                ));
            }
        } catch (IOException e) {
            System.out.println("Could not load admins: " + e.getMessage());
        }

        return admins;
    }

    public static ArrayList<Teacher> loadTeachers() {
        ArrayList<Teacher> teachers = new ArrayList<>();
        File file = new File(DATA_DIR + "teachers.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.isBlank())
                    continue;

                String[] parts = line.split("\\|");

                if (parts.length < 5)
                    continue;

                teachers.add(new Teacher(
                        parts[0], // name
                        Integer.parseInt(parts[1]), // sapId
                        parts[2], // password
                        Double.parseDouble(parts[3]), // salary
                        parts[4] // email
                ));
            }
        } catch (IOException e) {
            System.out.println("Could not load teachers: " + e.getMessage());
        }

        return teachers;
    }

    public static ArrayList<Librarian> loadLibrarians() {
        ArrayList<Librarian> librarians = new ArrayList<>();
        File file = new File(DATA_DIR + "librarians.txt");
        System.out.println("Looking for: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.isBlank())
                    continue;

                String[] parts = line.split("\\|");

                if (parts.length < 4)
                    continue;

                librarians.add(new Librarian(
                        parts[0], // name
                        Integer.parseInt(parts[1]), // sapId
                        parts[2], // password
                        parts[3] // email
                ));
            }
        } catch (IOException e) {
            System.out.println("Could not load teachers: " + e.getMessage());
        }

        return librarians;
    }

    // ════════════════════════════════════════════════════════════
    // SAVE METHODS — Write from ArrayLists back to disk
    // Uses BufferedWriter for better performance than FileWriter alone
    // ════════════════════════════════════════════════════════════

    public static void saveStudents(ArrayList<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(DATA_DIR + "students.txt"))) {

            for (Student s : students) {
                writer.write(
                        s.getName() + "|" +
                                s.getSapId() + "|" +
                                s.getPassword() + "|" +
                                s.getCgpa() + "|" +
                                s.getDepartment() + "|" +
                                s.getEmail() + "|" +
                                s.getCurrentSemester());
                writer.newLine(); // platform-safe line ending
            }

        } catch (IOException e) {
            System.out.println("Could not save students: " + e.getMessage());
        }
    }

    public static void saveAdmins(ArrayList<Admin> admins) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(DATA_DIR + "admins.txt"))) {

            for (Admin a : admins) {
                writer.write(
                        a.getName() + "|" +
                                a.getSapId() + "|" +
                                a.getPassword() + "|" +
                                a.getEmail());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Could not save admins: " + e.getMessage());
        }
    }

    public static void saveTeachers(ArrayList<Teacher> teachers) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(DATA_DIR + "teachers.txt"))) {

            for (Teacher t : teachers) {
                writer.write(
                        t.getName() + "|" +
                                t.getSapId() + "|" +
                                t.getPassword() + "|" +
                                t.getSalary() + "|" +
                                t.getEmail());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Could not save teachers: " + e.getMessage());
        }
    }

    public static void saveLibrarians(ArrayList<Librarian> librarians) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(DATA_DIR + "teachers.txt"))) {

            for (Librarian t : librarians) {
                writer.write(
                        t.getName() + "|" +
                                t.getSapId() + "|" +
                                t.getPassword() + "|" +
                                t.getEmail());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Could not save teachers: " + e.getMessage());
        }
    }

    // Convenience method — saves everything at once
    public static void saveAll() {
        saveStudents(DataStore.students);
        saveTeachers(DataStore.teachers);
        saveAdmins(DataStore.admins);
    }

    // READ — loads all students' results for a given semester
    public static ArrayList<SemesterResult> loadSemesterResults(int semester) {
        File file = new File(RESULTS_DIR + "sem" + semester + ".json");

        if (!file.exists())
            return new ArrayList<>(); // no file yet, return empty list

        try {
            return mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, SemesterResult.class));
        } catch (IOException e) {
            System.out.println("Could not load sem" + semester + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // WRITE — saves all students' results for a given semester
    public static void saveSemesterResults(ArrayList<SemesterResult> results, int semester) {
        File file = new File(RESULTS_DIR + "sem" + semester + ".json");

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, results);
        } catch (IOException e) {
            System.out.println("Could not save sem" + semester + ": " + e.getMessage());
        }
    }

    // READ — loads all students' attendance for a given semester
    public static ArrayList<StudentAttendance> loadSemesterAttendance(int semester) {
        File file = new File(ATTENDANCE_DIR + "sem" + semester + "_attendance.json");

        if (!file.exists())
            return new ArrayList<>(); // no file yet, return empty list

        try {
            return mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, StudentAttendance.class));
        } catch (IOException e) {
            System.out.println("Could not load sem" + semester + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // WRITE — saves all students' attendance for a given semester
    public static void saveSemesterAttendance(ArrayList<StudentAttendance> attendance, int semester) {
        File file = new File(ATTENDANCE_DIR + "sem" + semester + "_attendance.json");

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, attendance);
        } catch (IOException e) {
            System.out.println("Could not save sem" + semester + "_attendance: " + e.getMessage());
        }
    }
}