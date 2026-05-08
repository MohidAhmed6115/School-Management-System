package com.school.controller.dashboards.student;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import com.library.controller.librarian.LibrarianFunctions;
import com.school.model.Student;
import com.school.model.announcements.ClassEntry;
import com.school.model.announcements.StudentSchedule;
import com.util.SceneManager;
import com.util.SchoolDataStore;
import static com.util.SchoolDataStore.getScheduleByDepartment;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    @FXML
    public void initialize() {
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
        StudentSchedule schedule = null;
        if (SchoolDataStore.currentUser instanceof Student s) {
            schedule = getScheduleByDepartment(s.getDepartment());
        }
        // get current day
        LocalDate today = LocalDate.now();

        List<ClassEntry> classes = null;
        assert schedule != null;
        switch (today.getDayOfWeek()) {
            case MONDAY -> {
                classes = schedule.getMonday();
            }
            case TUESDAY -> {
                classes = schedule.getTuesday();
            }
            case WEDNESDAY -> {
                classes = schedule.getWednesday();
            }
            case THURSDAY -> {
                classes = schedule.getThursday();
            }
            case FRIDAY -> {
                classes = schedule.getFriday();
            }
            case SATURDAY -> {
                classes = schedule.getSaturday();
            }
            case SUNDAY -> {
                classes = schedule.getSunday();
            }
        }

        if (classes == null) {
            return;
        }

        if (classes.get(0).getClassName().equals("No classes today")) {
            addSchedule();
            return;
        }

        for (ClassEntry cls : classes) {
            String time = cls.getTime();
            String course = cls.getClassName();
            String room = cls.getRoom();

            addSchedule(time, course, room);
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
}
