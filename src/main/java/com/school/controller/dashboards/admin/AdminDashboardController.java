package com.school.controller.dashboards.admin;

import com.school.model.Student;
import com.util.SchoolDataStore;
import com.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminDashboardController extends AdminController {
    @FXML public VBox manageTeacherButton;
    @FXML public VBox manageStudentButton;
    @FXML public VBox libraryButton;
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalTeachersLabel;
    @FXML private Label todayAttendance;

    @FXML
    protected void initialize() {
        usernameLabel.setText(SchoolDataStore.currentUser.getName());
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));

        totalStudentsLabel.setText(String.valueOf(SchoolDataStore.students.size()));
        totalTeachersLabel.setText(String.valueOf(SchoolDataStore.teachers.size()));

        // calculating total attendance
        int totalAttendance = 0;
        int totalPresent = 0;
        for (Student st : SchoolDataStore.students) {
            boolean isPresent = SchoolDataStore.getStudentAttendance(st.getSapId(), st.getCurrentSemester()).isPresent();

            totalAttendance++;
            if (isPresent) {
                totalPresent++;
            }
        }
        int attendancePercentage = (totalPresent*100)/totalAttendance;
        todayAttendance.setText(attendancePercentage + "%");
    }

    @FXML
    private void handleManageTeachers() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/admin/manageTeachers/manage-teachers.fxml");
    }

    @FXML
    private void handleManageStudents() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/admin/manageStudents/manage-students.fxml");
    }

    @FXML
    private void handleLibrary() throws IOException {
        SceneManager.loadScene(logoutButton, "/library/fxml/librarian-page.fxml");
    }
}
