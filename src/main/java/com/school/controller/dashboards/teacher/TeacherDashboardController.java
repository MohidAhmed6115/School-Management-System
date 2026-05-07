package com.school.controller.dashboards.teacher;

import com.school.model.Teacher;
import com.util.DataStore;
import com.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TeacherDashboardController extends TeacherController {
    @FXML public VBox manageStudentResultButton;
    @FXML public VBox viewAllStudentsButton;
    @FXML public VBox libraryButton;
    @FXML private Label teacherSalaryLabel;
    @FXML private Label nextSalaryDateLabel;
    @FXML private Label welcomeMessage;

    @FXML
    public void initialize () {
        usernameLabel.setText(DataStore.currentUser.getName());
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        welcomeMessage.setText("Good morning, " + DataStore.currentUser.getName());
        if (DataStore.currentUser instanceof Teacher t) {
            teacherSalaryLabel.setText(String.format("%,.2f$", t.getSalary()));
        }

        // calculate days left for next salary
        LocalDate today = LocalDate.now();
        LocalDate nextSalaryDate = today.withDayOfMonth(1).plusMonths(1);

        int daysTillNextSalary = (int) ChronoUnit.DAYS.between(today, nextSalaryDate);
        nextSalaryDateLabel.setText(daysTillNextSalary + " days till next Salary");
    }

    @FXML
    private void manageStudentResult() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/school/fxml/dashboards/teacher/manage-student-result.fxml"));
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(logoutButton.getScene().getWindow());

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            dialog.setScene(scene);

            dialog.showAndWait();

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewAllStudents() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/school/fxml/dashboards/admin/manageStudents/view-students.fxml"));
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(logoutButton.getScene().getWindow());

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            dialog.setScene(scene);

            dialog.showAndWait();

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAttendance() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/teacher/manage-attendance.fxml");
    }

    @FXML
    private void handleLibrary() throws IOException {
        SceneManager.loadScene(logoutButton, "/library/fxml/main-page.fxml");
    }
}
