package com.school.util;

import com.school.model.Admin;
import com.school.model.Student;
import com.school.model.Teacher;
import com.school.model.Librarian;
import com.school.model.User;
import com.school.model.result.Course;
import com.school.model.result.SemesterResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {

    public static ArrayList<Student> students = new ArrayList<>();
    public static ArrayList<Teacher> teachers = new ArrayList<>();
    public static ArrayList<Admin> admins = new ArrayList<>();
    public static ArrayList<Librarian> librarians = new ArrayList<>();

    public static User currentUser;

    public static Map<Integer, ArrayList<SemesterResult>> semesterResults = new HashMap<>();


    // ════════════════════════════════════════════════════════════
    //  LOAD ALL — Call once at startup
    // ════════════════════════════════════════════════════════════

    public static void loadAll() {
        System.out.println("Librarians loaded: " + DataStore.librarians.size());
        students = DataManager.loadStudents();
        teachers = DataManager.loadTeachers();
        admins = DataManager.loadAdmins();
        librarians = DataManager.loadLibrarians();
        loadAllSemesters();
    }

    private static void loadAllSemesters() {
        for (int i = 1; i <= 8; i++) {
            ArrayList<SemesterResult> results = DataManager.loadSemesterResults(i);
            if (!results.isEmpty()) {
                semesterResults.put(i, results);
            }
        }
    }

    // ════════════════════════════════════════════════════════════
    //  GET — Fetch a specific student's result for a semester
    // ════════════════════════════════════════════════════════════

    public static SemesterResult getStudentResult(int sapId, int semester) {
        ArrayList<SemesterResult> sem = semesterResults.get(semester);
        if (sem == null) return null;

        return sem.stream()
                .filter(r -> r.getSapId() == sapId)
                .findFirst()
                .orElse(null);
    }

    // ════════════════════════════════════════════════════════════
    //  UPDATE — Update a specific course grade for a student
    // ════════════════════════════════════════════════════════════

    public static void updateGrade(int sapId, int semester, String courseName, int marks) {
        SemesterResult studentResult = getStudentResult(sapId, semester);
        if (studentResult == null) {
            System.out.println("Student " + sapId + " not found in semester " + semester);
            return;
        }

        studentResult.getCourses().stream()
                .filter(c -> c.getCourseName().equals(courseName))
                .findFirst()
                .ifPresent(c -> c.setMarks(marks));

        studentResult.calculateGpa();
        // Save to disk immediately after updating
        DataManager.saveSemesterResults(semesterResults.get(semester), semester);
    }


    // ════════════════════════════════════════════════════════════
    //  ADD — Add a student to a semester with blank results
    //  Used when a student is new or moves to the next semester
    // ════════════════════════════════════════════════════════════

    public static void addStudentToSemester(int sapId, int semester, List<String[]> courseInfo, int creditHours) {

        List<Course> courses = new ArrayList<>();
        for (String[] course : courseInfo) {
            String courseCode = course[0];
            String courseName = course[1];
            courses.add(new Course(courseCode, courseName, creditHours, "Pending", 0));
        }

        SemesterResult newEntry = new SemesterResult(sapId, courses, 0.0);

        // If this semester doesn't exist in the map yet, create it
        semesterResults.putIfAbsent(semester, new ArrayList<>());

        // Check if student already exists in this semester to avoid duplicates
        boolean alreadyExists = semesterResults.get(semester).stream()
                .anyMatch(r -> r.getSapId() == sapId);

        if (alreadyExists) {
            System.out.println("Student " + sapId + " already exists in semester " + semester);
            return;
        }

        semesterResults.get(semester).add(newEntry);

        // Save to disk immediately after adding
        DataManager.saveSemesterResults(semesterResults.get(semester), semester);
    }


    // ════════════════════════════════════════════════════════════
    //  SAVE ALL — Force save everything to disk at once
    // ════════════════════════════════════════════════════════════

    public static void saveAll() {
        DataManager.saveStudents(students);
        DataManager.saveTeachers(teachers);
        DataManager.saveAdmins(admins);

        for (Map.Entry<Integer, ArrayList<SemesterResult>> entry : semesterResults.entrySet()) {
            DataManager.saveSemesterResults(entry.getValue(), entry.getKey());
        }
    }
}