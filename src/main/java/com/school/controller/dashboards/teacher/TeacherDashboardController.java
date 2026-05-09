package com.school.controller.dashboards.teacher;

import com.school.model.Teacher;
import com.school.model.announcements.Announcements;
import com.util.SchoolDataManager;
import com.util.SchoolDataStore;
import com.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;

public class TeacherDashboardController extends TeacherController {
    @FXML public VBox manageStudentResultButton;
    @FXML public VBox viewAllStudentsButton;
    @FXML public VBox libraryButton;
    @FXML private VBox announcementBox;
    @FXML private Label teacherSalaryLabel;
    @FXML private Label nextSalaryDateLabel;
    @FXML private Label welcomeMessage;
    @FXML private Label totalBooksLabel;


    @FXML
    public void initialize () {
        SchoolDataStore.teacherAnnouncements  = SchoolDataManager.loadTeacherAnnouncements();
        sortAnnouncements();

        usernameLabel.setText(SchoolDataStore.currentUser.getName());
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        welcomeMessage.setText("Good morning, " + SchoolDataStore.currentUser.getName());
        if (SchoolDataStore.currentUser instanceof Teacher t) {
            teacherSalaryLabel.setText(String.format("%,.2f$", t.getSalary()));
        }

        // getting total books
        totalBooksLabel.setText(String.valueOf(com.library.controller.librarian.LibrarianFunctions.totalBooks()));

        // calculate days left for next salary
        LocalDate today = LocalDate.now();
        LocalDate nextSalaryDate = today.withDayOfMonth(1).plusMonths(1);

        int daysTillNextSalary = (int) ChronoUnit.DAYS.between(today, nextSalaryDate);
        nextSalaryDateLabel.setText(daysTillNextSalary + " days till next Salary");

        // display the announcements
        showAnnouncements();
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

            Stage mainStage = (Stage) logoutButton.getScene().getWindow();
            // Blur background of main stage
            Parent backgroundRoot = mainStage.getScene().getRoot();
            GaussianBlur blur = new GaussianBlur(0);
            backgroundRoot.setEffect(blur);

            dialog.setOnShowing(e -> blur.setRadius(4));
            dialog.setOnHiding(e -> backgroundRoot.setEffect(null));

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


    private void showAnnouncements() {
        ArrayList<Announcements> announcements = new ArrayList<>();
        announcements = SchoolDataStore.teacherAnnouncements;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");

        LocalDate today = LocalDate.now();

        for (Announcements announcement : announcements) {
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
        SchoolDataStore.teacherAnnouncements.sort(Comparator.comparingInt(Announcements::getImportance));
        SchoolDataManager.saveTeacherAnnouncements(SchoolDataStore.teacherAnnouncements);
    }
}
