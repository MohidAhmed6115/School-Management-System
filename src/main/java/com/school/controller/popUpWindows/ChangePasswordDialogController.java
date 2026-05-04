package com.school.controller.popUpWindows;

import com.school.model.User;
import com.school.util.DataManager;
import com.school.util.DataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static com.school.util.PasswordUtil.*;

public class ChangePasswordDialogController {

    // User enters current password
    @FXML private PasswordField userPassword;
    @FXML private TextField userPasswordVisible;
    @FXML private Button togglePassword;
    // User enters new Password
    @FXML private PasswordField newPassword;
    @FXML private TextField newPasswordVisible;
    @FXML private Button toggleNewPassword;
    // User confirms new Password
    @FXML private PasswordField confirmNewPassword;
    @FXML private TextField confirmNewPasswordVisible;
    @FXML private Button toggleConfirmNewPassword;


    @FXML private AnchorPane changePasswordPopUp;
    @FXML private Label invalidMessage;
    @FXML private Button closeButton;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private double xOffset, yOffset;

    @FXML
    private void initialize() {
        changePasswordPopUp.getStylesheets().add(
                getClass().getResource("/school/css/popUpWindows/popup-window.css").toExternalForm()
        );

        changePasswordPopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        changePasswordPopUp.setOnMouseDragged(e -> {
            Stage stage = (Stage) changePasswordPopUp.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
    }

    public void setData(User user) {}

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }

    @FXML
    private void handleTogglePassword(ActionEvent event) {

        Button clickedButton = (Button) event.getSource();

        PasswordField hiddenField = null;
        TextField visibleField = null;

        if (clickedButton == togglePassword) {
            hiddenField = userPassword;
            visibleField = userPasswordVisible;
        } else if (clickedButton == toggleNewPassword) {
            hiddenField = newPassword;
            visibleField = newPasswordVisible;
        } else if (clickedButton == toggleConfirmNewPassword) {
            hiddenField = confirmNewPassword;
            visibleField = confirmNewPasswordVisible;
        }

        boolean isVisible = visibleField.isVisible();

        if (!isVisible) {
            visibleField.setText(hiddenField.getText());
            hiddenField.setVisible(false);
            hiddenField.setManaged(false);

            visibleField.setVisible(true);
            visibleField.setManaged(true);

            clickedButton.setText("🙈");
        } else {
            hiddenField.setText(visibleField.getText());
            visibleField.setVisible(false);
            visibleField.setManaged(false);

            hiddenField.setVisible(true);
            hiddenField.setManaged(true);

            clickedButton.setText("👁");
        }
    }

    public void managePasswordFields() {
        // This stores the values from the labels
        String enteredPassword =
                userPassword.isVisible()
                        ? userPassword.getText()
                        : userPasswordVisible.getText();

        String enteredNewPassword =
                newPassword.isVisible()
                        ? newPassword.getText()
                        : newPasswordVisible.getText();

        String enteredConfirmNewPassword =
                confirmNewPassword.isVisible()
                        ? confirmNewPassword.getText()
                        : confirmNewPasswordVisible.getText();


        if (!checkCurrentPassword(DataStore.currentUser, enteredPassword)) {
            invalidMessage.setText("Incorrect Password!");
            invalidMessage.setTextFill(Color.RED);
            return;
        }

        if (!checkNewPassword(enteredNewPassword)) {
            invalidMessage.setText("Weak Password! Your password must include uppercase/lowercase letters, a symbol and a number.");
            invalidMessage.setTextFill(Color.RED);
            return;
        }

        if (!checkConfirmPassword(enteredNewPassword, enteredConfirmNewPassword)) {
            invalidMessage.setText("Passwords do not match.");
            invalidMessage.setTextFill(Color.RED);
            return;
        }

        DataStore.currentUser.setPassword(enteredNewPassword);
        invalidMessage.setText("Password changed successfully.");
        invalidMessage.setTextFill(Color.GREEN);

        // saves the password in the file
        System.out.println("User password now: " + DataStore.currentUser.getPassword());
        DataManager.saveAll();
        System.out.println("Save complete!");

        // shows close button instead of other buttons
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        closeButton.setVisible(true);
    }
}
