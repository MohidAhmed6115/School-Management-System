package com.school.controller.popUpWindows;

import com.school.model.User;
import com.util.SchoolDataManager;
import com.util.SchoolDataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static com.util.PasswordUtil.*;

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
    @FXML private StackPane rootWrapper;
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

        // Apply rounded clip to the root so corners are truly clipped
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(340, 420);
        clip.setArcWidth(32);
        clip.setArcHeight(32);
        rootWrapper.setClip(clip);
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


        if (!checkCurrentPassword(SchoolDataStore.currentUser, enteredPassword)) {
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

        SchoolDataStore.currentUser.setPassword(enteredNewPassword);
        invalidMessage.setText("Password changed successfully.");
        invalidMessage.setTextFill(Color.GREEN);

        // saves the password in the file
        System.out.println("User password now: " + SchoolDataStore.currentUser.getPassword());
        SchoolDataManager.saveAll();
        System.out.println("Save complete!");

        // shows close button instead of other buttons
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        closeButton.setVisible(true);
    }
}
