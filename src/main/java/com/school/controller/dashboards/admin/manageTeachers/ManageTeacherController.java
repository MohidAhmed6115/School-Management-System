package com.school.controller.dashboards.admin.manageTeachers;

import com.school.controller.dashboards.admin.AdminController;
import com.util.SchoolDataStore;
import com.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.util.Tools.animateStripe;

public class ManageTeacherController extends AdminController {

    @FXML public VBox addTeachersButton;
    @FXML public VBox removeTeachersButton;
    @FXML public VBox viewTeachersButton;
    @FXML private Button goBackButton;
    @FXML private Region heroStripe;

    @FXML
    protected void initialize() {
        animateStripe(heroStripe);
        usernameLabel.setText(SchoolDataStore.currentUser.getName());
    }

    @FXML
    public void addTeachers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/school/fxml/dashboards/admin/manageTeachers/add-teachers.fxml"));
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
    public void removeTeachers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/school/fxml/dashboards/admin/manageTeachers/remove-teachers.fxml"));
            Parent root = loader.load();

            RemoveTeachers controller = loader.getController();

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
    public void viewTeachers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/school/fxml/dashboards/admin/manageTeachers/view-teachers.fxml"));
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
