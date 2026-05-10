package com.fee.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.fee.model.FeeStudent;
import com.fee.model.PrintPDF;
import com.fee.util.FeeDataManager;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class StudentFeeController implements Initializable {

    FeeStudent feeStudent = new FeeStudent();

    @FXML private Label clockLabel;

    @FXML private ToggleGroup group;
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
    @FXML private Label lblExtendedDate;

    @FXML private Button downloadPDFButton;
    @FXML private Button homeButton;
    @FXML private Button closePaneButton;
    @FXML private Button dateExtendButton;
    @FXML private Button installmentPlanButton;
    @FXML private Button yesButton;
    @FXML private Button noButton;

    @FXML private RadioButton installementRadioBtn2;
    @FXML private RadioButton installementRadioBtn3;

    @FXML private GridPane installmentGrid;
    @FXML private VBox detailPane;
    @FXML private VBox installementPane;
    @FXML private VBox extendDatePane;
    @FXML private StackPane contentStack;

    @FXML private void handleCloseDetailPane(ActionEvent e) {
        detailPane.setVisible(false);
        detailPane.setManaged(false);
    }
    // @FXML private void handleInstallmentPlan(ActionEvent e){
    // showPane(installementPane);
    // }
    // @FXML private void handleDateExtendRequest(ActionEvent e){
    // showPane(extendDatePane);
    // lblExtendedDate.setText(feeStudent.extendDate());

    // }

    private Label boldLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-weight: bold;");
        return l;
    }

    private void showPane(VBox paneToShow) {
        for (javafx.scene.Node node : contentStack.getChildren()) {
            node.setVisible(false);
            node.setManaged(false);
        }

        detailPane.setVisible(true);
        detailPane.setManaged(true);

        paneToShow.setVisible(true);
        paneToShow.setManaged(true);
    }

    private void buildInstallmentRows(int choice) {

        // Set column widths
        installmentGrid.getColumnConstraints().clear();

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPrefWidth(50);   // No.

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(160);  // Amount

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(130);  // Due Date

        installmentGrid.getColumnConstraints().addAll(col0, col1, col2);

        // get dates from model
        ArrayList<String> dates = feeStudent.extendDateInstallments(choice);

        // get amount per installment
        long amount = Long.parseLong(feeStudent.installments(feeStudent.getFeeAmount(),
                choice == 2 ? 1 : 2)); // case 1 = /2, case 2 = /3

        // clear previous rows
        installmentGrid.getChildren().clear();

        // header row
        installmentGrid.add(boldLabel("No."), 0, 0);
        installmentGrid.add(boldLabel("Amount (PKR)"), 1, 0);
        installmentGrid.add(boldLabel("Due Date"), 2, 0);

        // data rows
        for (int i = 0; i < dates.size(); i++) {
            installmentGrid.add(new Label(String.valueOf(i + 1)), 0, i + 1);
            installmentGrid.add(new Label("PKR " + amount), 1, i + 1);
            installmentGrid.add(new Label(dates.get(i)), 2, i + 1);
        }
    }

    private void hidePane(VBox paneToHide) {
        detailPane.setVisible(false);
        detailPane.setManaged(false);

        paneToHide.setVisible(false);
        paneToHide.setManaged(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        installmentGrid.getStyleClass().add("installmentGrid");
        // Transistions
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(500), homeButton);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(500), homeButton);
        scaleDown.setToX(1);
        scaleDown.setToY(1);

        // Date Extend button lambda Function
        // ToggleGroup already in FXML — just add listener once here
        group.selectedToggleProperty().addListener((obs, old, newVal) -> {
            if (newVal == installementRadioBtn2)
                buildInstallmentRows(2);
            else if (newVal == installementRadioBtn3)
                buildInstallmentRows(3);
        });

        // Installment button — just show the pane
        installmentPlanButton.setOnAction(e -> {
            showPane(installementPane);
            buildInstallmentRows(2); // always reset to default on open
            installementRadioBtn2.setSelected(true);
        });

        // Date extend — only call extendDate() inside Yes
        dateExtendButton.setOnAction(e -> {
            showPane(extendDatePane);

            yesButton.setOnAction(eh -> {
                lblExtendedDate.setText("Extended Date: " + feeStudent.extendDate());
                FeeDataManager.saveStudents(FeeDataStore.students);
            });

            noButton.setOnAction(eh -> {
                hidePane(extendDatePane);
            });
        });
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
        feeStudent = null;
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
            if ("paid".equalsIgnoreCase(feeStudent.getFeeStatus()))
                lblStatus.getStyleClass().add("status-paid");
            else
                lblStatus.getStyleClass().add("status-unpaid");
            lblDueDate.setText(String.valueOf(feeStudent.getDueDate()));
            lblLateFine.setText("+PKR " + Long.toString(feeStudent.calculateLateFee()));
            lblNetFee.setText("PKR " + Long.toString(feeStudent.getNetFee()));
        }

        homeButton.setOnMouseEntered(e -> scaleUp.playFromStart());
        homeButton.setOnMouseExited(e -> scaleDown.playFromStart());

        homeButton.setOnAction(e -> {

            try {

                if (currentUser instanceof Student) {
                    SceneManager.loadScene(homeButton, "/school/fxml/dashboards/student/student-dashboard.fxml");
                } else {
                    SceneManager.loadScene(homeButton, "/school/fxml/dashboards/teacher/teacher-dashboard.fxml");
                }
            } catch (IOException e1) {
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
