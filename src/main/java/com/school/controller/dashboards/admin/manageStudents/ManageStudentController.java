package com.school.controller.dashboards.admin.manageStudents;

import com.school.controller.dashboards.admin.AdminController;
import com.util.SchoolDataStore;
import com.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ManageStudentController extends AdminController {

    @FXML public VBox enrollStudentsButton;
    @FXML public VBox removeStudentsButton;
    @FXML public VBox viewStudentsButton;
    @FXML private Button goBackButton;

    @FXML
    protected void initialize() {
        usernameLabel.setText(SchoolDataStore.currentUser.getName());
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
