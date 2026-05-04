package com.school.controller.dashboards.student;

import com.school.util.DataStore;
import com.school.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StudentController {

    @FXML public MenuButton topMenuButton;
    @FXML public Label usernameLabel;
    @FXML public Label dateLabel;
    @FXML public Button logoutButton;

    @FXML
    protected void handleLogout() throws IOException {
        DataStore.currentUser = null;
        SceneManager.loadScene(logoutButton, "/school/fxml/main-page.fxml");
        System.out.println("Logout Successfully!");
    }

    @FXML
    protected void handleProfileButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/school/fxml/popUpWindows/profile-dialog.fxml"));
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
    protected void handleChangePasswordButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/school/fxml/popUpWindows/change-password-dialog.fxml"));
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
}