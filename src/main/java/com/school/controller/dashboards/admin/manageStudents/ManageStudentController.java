package com.school.controller.dashboards.admin.manageStudents;

import com.school.controller.dashboards.admin.AdminController;
import com.school.model.Student;
import com.util.SchoolDataStore;
import com.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.util.Tools.animateStripe;

public class ManageStudentController extends AdminController {

    @FXML public VBox enrollStudentsButton;
    @FXML public VBox removeStudentsButton;
    @FXML public VBox viewStudentsButton;
    @FXML private Button goBackButton;
    @FXML private Label todayAttendance;
    @FXML private Label totalStudentsLabel;
    @FXML private Region heroStripe;

    @FXML
    protected void initialize() {
        animateStripe(heroStripe);
        usernameLabel.setText(SchoolDataStore.currentUser.getName());

        totalStudentsLabel.setText(String.valueOf(SchoolDataStore.students.size()));

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
    public void enrollStudents() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/school/fxml/dashboards/admin/manageStudents/enroll-students.fxml"));
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void removeStudents() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/school/fxml/dashboards/admin/manageStudents/remove-students.fxml"));
            Parent root = loader.load();

            RemoveStudents controller = loader.getController();

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void viewStudents() {
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoBack() throws IOException {
        SceneManager.loadScene(goBackButton, "/school/fxml/dashboards/admin/admin-dashboard.fxml");
    }
}
