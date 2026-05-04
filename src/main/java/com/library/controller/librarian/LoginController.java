package com.library.controller.librarian;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable{

    // FXML Injections
    @FXML PasswordField passwordField;
    @FXML TextField usernameField;
    @FXML Button loginConfirmButton;
    @FXML Label lblIncorrect;

    public Stage mainStage;

    // For changing the main scene
    public boolean newScene = false;
    // NON-FXML Injections
    Admin admin = new Admin();
    @FXML public void submit(ActionEvent e) {
        if (admin.loginConfirmed(usernameField.getText(), passwordField.getText())) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/library/fxml/librarian-page.fxml"));
                mainStage.setScene(new Scene(root));
                ((Stage) lblIncorrect.getScene().getWindow()).close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            lblIncorrect.setText("Wrong Username or Password");
        }
    }
    // Abstract method
    @Override
    public void initialize(URL location, ResourceBundle resources){

    }
}