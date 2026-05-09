package com.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fee.util.FeeDataStore;
import com.school.model.Admin;
import com.school.model.Librarian;
import com.school.model.Student;
import com.school.model.Teacher;
import com.school.model.User;
import com.school.model.announcements.Announcements;
import com.school.model.announcements.StudentSchedule;
import com.school.model.attendance.StudentAttendance;
import com.school.model.result.Course;
import com.school.model.result.SemesterResult;

public class SchoolDataStore {

    public static ArrayList<Student> students = new ArrayList<>();
    public static ArrayList<Teacher> teachers = new ArrayList<>();
    public static ArrayList<Admin> admins = new ArrayList<>();
    public static ArrayList<Librarian> librarians = new ArrayList<>();
    public static ArrayList<StudentSchedule> studentSchedules = new ArrayList<>();
    public static ArrayList<Announcements> studentAnnouncements = new ArrayList<>();

    public static User currentUser;

    public static Map<Integer, ArrayList<SemesterResult>> semesterResults = new HashMap<>();
    public static Map<Integer, ArrayList<StudentAttendance>> semesterAttendance = new HashMap<>();


    // ════════════════════════════════════════════════════════════
    //  LOAD ALL — Call once at startup
    // ════════════════════════════════════════════════════════════

    public static void loadAll() {
        System.out.println("Librarians loaded: " + SchoolDataStore.librarians.size());
        students   = SchoolDataManager.loadStudents();
        teachers   = SchoolDataManager.loadTeachers();
        admins     = SchoolDataManager.loadAdmins();
        librarians = SchoolDataManager.loadLibrarians();
        FeeDataStore.loadAll();
        loadAllSemesters();
    }

    private static void loadAllSemesters() {
        for (int i = 1; i <= 8; i++) {
            ArrayList<SemesterResult> results = SchoolDataManager.loadSemesterResults(i);
            if (!results.isEmpty()) {
                semesterResults.put(i, results);
            }
            ArrayList<StudentAttendance> attendance = SchoolDataManager.loadSemesterAttendance(i);
            if (!attendance.isEmpty()) {
                semesterAttendance.put(i, attendance);
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
    //  GET — Fetch a specific student's attendance for a semester
    // ════════════════════════════════════════════════════════════

    public static StudentAttendance getStudentAttendance(int sapId, int semester) {
        ArrayList<StudentAttendance> sem = semesterAttendance.get(semester);
        if (sem == null) return null;

        return sem.stream()
                .filter(r -> r.getSapId() == sapId)
                .findFirst()
                .orElse(null);
    }

    // ════════════════════════════════════════════════════════════
    //  GET — Fetch schedule by department name
    // ════════════════════════════════════════════════════════════

    public static StudentSchedule getScheduleByDepartment(String department) {
        return studentSchedules.stream()
                .filter(s -> s.getDepartment().equalsIgnoreCase(department))
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
        SchoolDataManager.saveSemesterResults(semesterResults.get(semester), semester);
    }

    // ════════════════════════════════════════════════════════════
    //  UPDATE — Update a specific course grade for a student
    // ════════════════════════════════════════════════════════════

    public static void updateAttendance(int sapId, int semester, LocalDate DATE, String status) {
        String date = String.valueOf(DATE);
        StudentAttendance studentAttendance = getStudentAttendance(sapId, semester);
        if (studentAttendance == null) {
            System.out.println("Student " + sapId + " not found in semester " + semester);
            return;
        }

        studentAttendance.setTodayAttendance(date, status);
        // Save to disk immediately after updating
        SchoolDataManager.saveSemesterAttendance(semesterAttendance.get(semester), semester);
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
        SchoolDataManager.saveSemesterResults(semesterResults.get(semester), semester);
    }

    // ════════════════════════════════════════════════════════════
    //  SAVE ALL — Force save everything to disk at once
    // ════════════════════════════════════════════════════════════

    public static void saveAll() {
        SchoolDataManager.saveStudents(students);
        SchoolDataManager.saveTeachers(teachers);
        SchoolDataManager.saveAdmins(admins);

        for (Map.Entry<Integer, ArrayList<SemesterResult>> entry : semesterResults.entrySet()) {
            SchoolDataManager.saveSemesterResults(entry.getValue(), entry.getKey());
        }
        for (Map.Entry<Integer, ArrayList<StudentAttendance>> entry : semesterAttendance.entrySet()) {
            SchoolDataManager.saveSemesterAttendance(entry.getValue(), entry.getKey());
        }
    }
}