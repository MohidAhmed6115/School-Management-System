package com.fee.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.fee.model.FeeStudent;
import com.fee.model.PrintPDF;
import com.fee.util.FeeDataStore;
import com.school.model.Student;
import com.util.SceneManager;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class StudentFeeController implements Initializable {

    @FXML private Label clockLabel;
    @FXML private Label lblName;
    @FXML private Label lblSapId;
    @FXML private Label lblSemester;
    @FXML private Label lblCourse;
    @FXML private Label lblFeeAmount;
    @FXML private Label lblStatus;
    @FXML private Label lblDueDate;
    @FXML private Label lblLateFine;
    @FXML private Label lblNetFee;
    @FXML private Label lbldetailPaneTitle;

    @FXML private Button downloadPDFButton;
    @FXML private Button homeButton;
    @FXML private Button closePaneButton;

    @FXML private VBox detailPane;
    @FXML private VBox installementPane;
    @FXML private VBox extendDatePane;


    @FXML private void handleCloseDetailPane(ActionEvent e) {
        detailPane.setVisible(false);
        detailPane.setManaged(false);
    }
    @FXML private void handleInstallmentPlan(ActionEvent e){
        detailPane.setVisible(true);
        detailPane.setManaged(true);
    }
    @FXML private void handleDateExtendRequest(ActionEvent e){
        detailPane.setVisible(true);
        detailPane.setManaged(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Transistions
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(500),homeButton);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(500),homeButton);
        scaleDown.setToX(1);
        scaleDown.setToY(1);



        // Clock
        updateClock();
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        // Load fee data
        FeeDataStore.loadAll();

        // Get current logged in school student
        com.school.model.Student currentUser = (com.school.model.Student) com.util.SchoolDataStore.currentUser;

        // Set info row labels
        lblName.setText(currentUser.getName());
        lblSapId.setText(String.valueOf(currentUser.getSapId()));
        lblSemester.setText(String.valueOf(currentUser.getCurrentSemester()));
        lblCourse.setText(currentUser.getDepartment());

        // Find matching fee record
        FeeStudent feeStudent = null;
        for (FeeStudent s : FeeDataStore.students) {
            if (s.getId() == currentUser.getSapId()) {
                feeStudent = s;
                break;
            }
        }

        // Set fee summary labels
        if (feeStudent != null) {
            lblFeeAmount.setText("PKR " + feeStudent.getFeeAmount());
            lblStatus.setText(feeStudent.getFeeStatus());
            lblStatus.getStyleClass().removeAll("status-paid", "status-unpaid");
            if("paid".equalsIgnoreCase(feeStudent.getFeeStatus())) lblStatus.getStyleClass().add("status-paid");
            else lblStatus.getStyleClass().add("status-unpaid");
            lblDueDate.setText(String.valueOf(feeStudent.getDueDate()));
            lblLateFine.setText("+PKR " + Long.toString(feeStudent.calculateLateFee()));
            lblNetFee.setText("PKR " + Long.toString(feeStudent.getNetFee()));
        }

        homeButton.setOnMouseEntered(e -> scaleUp.playFromStart());
        homeButton.setOnMouseExited(e -> scaleDown.playFromStart());

        homeButton.setOnAction(e -> {
            
            try{
                
                if(currentUser instanceof Student){
                    SceneManager.loadScene(homeButton, "/school/fxml/dashboards/student/student-dashboard.fxml");
                }else{
                    SceneManager.loadScene(homeButton, "/school/fxml/dashboards/teacher/teacher-dashboard.fxml");
                }
            }catch(IOException e1){
                System.out.println("Error: " + e1.getMessage());
            }
        });

    }

    private void updateClock() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy | hh:mm:ss a");
        clockLabel.setText(LocalDateTime.now().format(fmt));
    }

    @FXML
    private void handleDownloadChallan() {

        PrintPDF print = new PrintPDF();
        print.generateFeeBill(downloadPDFButton);

        System.out.println("PDF downloaded");
    }
}
