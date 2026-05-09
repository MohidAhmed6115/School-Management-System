package com.school.controller.dashboards.student;

import com.library.controller.librarian.LibrarianFunctions;
import com.school.model.Student;
import com.school.model.User;
import com.school.model.announcements.ClassEntry;
import com.school.model.announcements.StudentAnnouncement;
import com.school.model.announcements.StudentSchedule;
import com.util.SchoolDataManager;
import com.util.SchoolDataStore;
import com.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

import static com.util.SchoolDataStore.getScheduleByDepartment;

public class StudentDashboardController extends StudentController {

    @FXML public VBox checkResultButton;
    @FXML public VBox changeDepartmentButton;
    @FXML public VBox feeButton;
    @FXML public VBox libraryButton;
    @FXML private Label studentCgpa;
    @FXML private Label welcomeMessage;
    @FXML private Label dateLabel;
    @FXML private Label attendancePercentageLabel;
    @FXML private Label totalBooksLabel;
    @FXML private VBox scheduleBox;
    @FXML private VBox announcementBox;

    @FXML
    public void initialize() {
        SchoolDataStore.studentAnnouncements  = SchoolDataManager.loadStudentAnnouncements();
        sortAnnouncements();

        usernameLabel.setText(SchoolDataStore.currentUser.getName());
        welcomeMessage.setText("Good morning, " + SchoolDataStore.currentUser.getName());
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        if (SchoolDataStore.currentUser instanceof Student s) {
            studentCgpa.setText(String.valueOf(s.getCgpa()));
        }

        // getting total books
        totalBooksLabel.setText(String.valueOf(LibrarianFunctions.totalBooks()));

        // calculating total attendance
        int attendancePercentage = 0;
        if (SchoolDataStore.currentUser instanceof Student st) {
            attendancePercentage = (int) Objects.requireNonNull(SchoolDataStore.getStudentAttendance(st.getSapId(), st.getCurrentSemester())).getAttendancePercentage();
        }

        attendancePercentageLabel.setText(attendancePercentage + "%");

        // display the schedule
        showSchedule();
        // display the announcements
        showAnnouncements();
    }

    @FXML
    private void handleCheckResult() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/student/result-card.fxml");
    }

    @FXML
    private void handleChangeDepartment() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/student/change-department.fxml");
    }

    @FXML
    private void handleFeeSystem() throws IOException {
        SceneManager.loadScene(logoutButton, "/fee/fxml/main-page.fxml");
    }

    @FXML
    private void handleLibrary() throws IOException {
        SceneManager.loadScene(logoutButton, "/library/fxml/main-page.fxml");
    }

    private void showSchedule() {
        if (!(SchoolDataStore.currentUser instanceof Student s)) return;

        StudentSchedule schedule = getScheduleByDepartment(s.getDepartment());

        // DEBUG — remove these after fixing
        System.out.println("Student department: '" + s.getDepartment() + "'");
        System.out.println("Available schedules:");
        for (StudentSchedule sc : SchoolDataStore.studentSchedules) {
            System.out.println("  -> '" + sc.getDepartment() + "'");
        }

        if (schedule == null) {
            addSchedule(); // shows "No classes today"
            return;
        }

        LocalDate today = LocalDate.now();
        List<ClassEntry> classes = null;

        switch (today.getDayOfWeek()) {
            case MONDAY    -> classes = schedule.getMonday();
            case TUESDAY   -> classes = schedule.getTuesday();
            case WEDNESDAY -> classes = schedule.getWednesday();
            case THURSDAY  -> classes = schedule.getThursday();
            case FRIDAY    -> classes = schedule.getFriday();
            case SATURDAY  -> classes = schedule.getSaturday();
            case SUNDAY    -> classes = schedule.getSunday();
        }

        if (classes == null || classes.isEmpty()) {
            addSchedule();
            return;
        }

        if (classes.get(0).getClassName().equals("none")) {
            addSchedule();
            return;
        }

        for (ClassEntry cls : classes) {
            addSchedule(cls.getTime(), cls.getClassName(), cls.getRoom());
        }
    }

    private void addSchedule(String time, String course, String room) {
        HBox row = new HBox();
        row.getStyleClass().add("schedule-row");

        Label timeLabel = new Label(time);
        timeLabel.getStyleClass().add("schedule-time");

        Label classInfoLabel = new Label(course + " — " + room);
        classInfoLabel.getStyleClass().add("schedule-subject");

        row.getChildren().addAll(timeLabel, classInfoLabel);
        scheduleBox.getChildren().add(row);
    }
    private void addSchedule() {
        HBox row = new HBox();
        row.getStyleClass().add("schedule-row");

        Label classInfoLabel = new Label("No classes today —");
        classInfoLabel.getStyleClass().add("schedule-subject");

        row.getChildren().add(classInfoLabel);
        scheduleBox.getChildren().add(row);
    }

    private void showAnnouncements() {
        ArrayList<StudentAnnouncement> announcements = new ArrayList<>();
        announcements = SchoolDataStore.studentAnnouncements;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");

        LocalDate today = LocalDate.now();

        for (StudentAnnouncement announcement : announcements) {
            LocalDate date = LocalDate.parse(announcement.getDate(), formatter);
            String message = announcement.getMessage();

            if (date.isAfter(today)) {
                addAnnouncements(message);
            }
        }
    }

    private void addAnnouncements(String announcement) {
        Label row = new Label("• " + announcement);
        row.getStyleClass().add("notice-item");

        announcementBox.getChildren().add(row);
    }

    private void sortAnnouncements() {
        SchoolDataStore.studentAnnouncements.sort(Comparator.comparingInt(StudentAnnouncement::getImportance));
        SchoolDataManager.saveStudentAnnouncements(SchoolDataStore.studentAnnouncements);
    }
}