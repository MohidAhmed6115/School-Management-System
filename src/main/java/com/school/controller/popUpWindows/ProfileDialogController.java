package com.school.controller.popUpWindows;

import com.school.model.Student;
import com.school.model.Teacher;
import com.school.model.User;
import com.school.util.DataStore;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProfileDialogController {

    @FXML private VBox profilePopUp;
    @FXML private HBox departmentRow;
    @FXML private HBox salaryRow;
    @FXML private Button closeButton;
    @FXML private Label userNameLabel;
    @FXML private Label userSapIdLabel;
    @FXML private Label userEmailLabel;
    @FXML private Label userDepartmentLabel;
    @FXML private Label userSalaryLabel;

    private double xOffset, yOffset;

    @FXML
    private void initialize() {
        profilePopUp.getStylesheets().add(
                getClass().getResource("/school/css/popUpWindows/popup-window.css").toExternalForm()
        );

        profilePopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        profilePopUp.setOnMouseDragged(e -> {
            Stage stage = (Stage) profilePopUp.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        setProfile();
    }

    public void setData (User user) {
    }

    private void setProfile() {
        System.out.println("User type: " + DataStore.currentUser.getClass().getSimpleName());

        if (DataStore.currentUser instanceof Teacher t) {
            // show the salary field
            salaryRow.setVisible(true);
            salaryRow.setManaged(true);

            // show the information of user
            userNameLabel.setText(t.getName());
            userSapIdLabel.setText(String.valueOf(t.getSapId()));
            userEmailLabel.setText(t.getEmail());
            userSalaryLabel.setText(String.valueOf(t.getSalary()));
        }
        else if (DataStore.currentUser instanceof Student s) {
            // show the salary field
            departmentRow.setVisible(true);
            departmentRow.setManaged(true);

            // show the information of user
            userNameLabel.setText(s.getName());
            userSapIdLabel.setText(String.valueOf(s.getSapId()));
            userEmailLabel.setText(s.getEmail());
            userDepartmentLabel.setText(s.getDepartment());
        }
        else {
            // show the information of user
            userNameLabel.setText(DataStore.currentUser.getName());
            userSapIdLabel.setText(String.valueOf(DataStore.currentUser.getSapId()));
            userEmailLabel.setText(DataStore.currentUser.getEmail());
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
