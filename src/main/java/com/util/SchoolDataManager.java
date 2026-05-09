package com.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.model.Admin;
import com.school.model.Student;
import com.school.model.Teacher;
import com.school.model.Librarian;
import com.school.model.announcements.StudentAnnouncement;
import com.school.model.attendance.StudentAttendance;
import com.school.model.result.SemesterResult;
import com.school.model.announcements.StudentSchedule;

import java.io.*;
import java.util.ArrayList;

public class SchoolDataManager {

    // ── Single source of truth for where data files live on disk ──
    private static final String DATA_DIR      = System.getProperty("user.dir") + "/src/main/resources/school/data/";
    private static final String RESULTS_DIR   = DATA_DIR + "results/";
    private static final String ATTENDANCE_DIR = DATA_DIR + "attendance/";
    private static final String ANNOUNCEMENTS_DIR  = DATA_DIR + "announcements/";

    private static final ObjectMapper mapper = new ObjectMapper();

    // ════════════════════════════════════════════════════════════
    // INIT — Call this ONCE at app startup in com.fee.app.Main.java
    // ════════════════════════════════════════════════════════════
    public static void init() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("Created data directory at: " + DATA_DIR);
        }

        copyIfMissing("students.txt");
        copyIfMissing("teachers.txt");
        copyIfMissing("admins.txt");
    }

    public static void initResultsDir() {
        File dir = new File(RESULTS_DIR);
        if (!dir.exists())
            dir.mkdirs();
    }

    public static void initSchedulesDir() {
        File dir = new File(ANNOUNCEMENTS_DIR);
        if (!dir.exists())
            dir.mkdirs();
    }

    private static void copyIfMissing(String filename) {
        File target = new File(DATA_DIR + filename);

        if (target.exists()) {
            return;
        }

        try (InputStream in = SchoolDataManager.class.getResourceAsStream("/school/data/" + filename);
             FileOutputStream out = new FileOutputStream(target)) {

            if (in == null) {
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

                if (line.isBlank())
                    continue;

                String[] parts = line.split("\\|");

                if (parts.length < 6)
                    continue;

                students.add(new Student(
                        parts[0],
                        Integer.parseInt(parts[1]),
                        parts[2],
                        Double.parseDouble(parts[3]),
                        parts[4],
                        parts[5],
                        Integer.parseInt(parts[6])
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
                        parts[0],
                        Integer.parseInt(parts[1]),
                        parts[2],
                        parts[3]
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
                        parts[0],
                        Integer.parseInt(parts[1]),
                        parts[2],
                        Double.parseDouble(parts[3]),
                        parts[4]
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
                        parts[0],
                        Integer.parseInt(parts[1]),
                        parts[2],
                        parts[3]
                ));
            }
        } catch (IOException e) {
            System.out.println("Could not load librarians: " + e.getMessage());
        }

        return librarians;
    }

    // ════════════════════════════════════════════════════════════
    // SAVE METHODS — Write from ArrayLists back to disk
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
                writer.newLine();
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
                new FileWriter(DATA_DIR + "librarians.txt"))) {

            for (Librarian t : librarians) {
                writer.write(
                        t.getName() + "|" +
                                t.getSapId() + "|" +
                                t.getPassword() + "|" +
                                t.getEmail());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Could not save librarians: " + e.getMessage());
        }
    }

    // Convenience method — saves everything at once
    public static void saveAll() {
        saveStudents(SchoolDataStore.students);
        saveTeachers(SchoolDataStore.teachers);
        saveAdmins(SchoolDataStore.admins);
        saveStudentSchedules(SchoolDataStore.studentSchedules);
    }

    // ════════════════════════════════════════════════════════════
    // SEMESTER RESULTS — JSON read/write
    // ════════════════════════════════════════════════════════════

    public static ArrayList<SemesterResult> loadSemesterResults(int semester) {
        File file = new File(RESULTS_DIR + "sem" + semester + ".json");

        if (!file.exists())
            return new ArrayList<>();

        try {
            return mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, SemesterResult.class));
        } catch (IOException e) {
            System.out.println("Could not load sem" + semester + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveSemesterResults(ArrayList<SemesterResult> results, int semester) {
        File file = new File(RESULTS_DIR + "sem" + semester + ".json");

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, results);
        } catch (IOException e) {
            System.out.println("Could not save sem" + semester + ": " + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════
    // SEMESTER ATTENDANCE — JSON read/write
    // ════════════════════════════════════════════════════════════

    public static ArrayList<StudentAttendance> loadSemesterAttendance(int semester) {
        File file = new File(ATTENDANCE_DIR + "sem" + semester + "_attendance.json");

        if (!file.exists())
            return new ArrayList<>();

        try {
            return mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, StudentAttendance.class));
        } catch (IOException e) {
            System.out.println("Could not load sem" + semester + " attendance: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveSemesterAttendance(ArrayList<StudentAttendance> attendance, int semester) {
        File file = new File(ATTENDANCE_DIR + "sem" + semester + "_attendance.json");

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, attendance);
        } catch (IOException e) {
            System.out.println("Could not save sem" + semester + "_attendance: " + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════
    // SCHEDULES — JSON read/write
    // ════════════════════════════════════════════════════════════

    public static ArrayList<StudentSchedule> loadStudentSchedules() {
        File file = new File(ANNOUNCEMENTS_DIR + "student-classes.json");

        if (!file.exists())
            return new ArrayList<>();

        try {
            return mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, StudentSchedule.class));
        } catch (IOException e) {
            System.out.println("Could not load student schedules: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveStudentSchedules(ArrayList<StudentSchedule> schedules) {
        File file = new File(ANNOUNCEMENTS_DIR + "student-classes.json");

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, schedules);
        } catch (IOException e) {
            System.out.println("Could not save student schedules: " + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════
    // Annoucements — JSON read/write
    // ════════════════════════════════════════════════════════════

    public static ArrayList<StudentAnnouncement> loadStudentAnnouncements() {
        File file = new File(ANNOUNCEMENTS_DIR + "student-announcements.json");

        if (!file.exists())
            return new ArrayList<>();

        try {
            return mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, StudentAnnouncement.class));
        } catch (IOException e) {
            System.out.println("Could not load student announcements: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveStudentAnnouncements(ArrayList<StudentAnnouncement> announcements) {
        File file = new File(ANNOUNCEMENTS_DIR + "student-announcements.json");

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, announcements);
        } catch (IOException e) {
            System.out.println("Could not save student announcements: " + e.getMessage());
        }
    }
}