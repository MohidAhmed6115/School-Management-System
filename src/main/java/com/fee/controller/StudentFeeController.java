package com.fee.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.fee.model.FeeRecord;
import com.fee.model.Student;
import com.fee.util.FeeDataStore;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private Label lblRemaining;

    @FXML private TableView<FeeRecord> feeHistoryTable;
    @FXML private TableColumn<FeeRecord, String> colSemester;
    @FXML private TableColumn<FeeRecord, String> colAmount;
    @FXML private TableColumn<FeeRecord, String> colPaidOn;
    @FXML private TableColumn<FeeRecord, String> colStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
        Student feeStudent = null;
        for (Student s : FeeDataStore.students) {
            if (s.getId() == currentUser.getSapId()) {
                feeStudent = s;
                break;
            }
        }

        // Set fee summary labels
        if (feeStudent != null) {
            lblFeeAmount.setText("PKR " + feeStudent.getFeeAmount());
            lblStatus.setText(feeStudent.getFeeStatus());
            lblDueDate.setText(String.valueOf(feeStudent.getDueDate()));
            lblRemaining.setText("PKR " + feeStudent.getRemainingFee());

            // Table
            colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
            colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            colPaidOn.setCellValueFactory(new PropertyValueFactory<>("paidOn"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

            colStatus.setCellFactory(col -> new TableCell<FeeRecord, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if ("Paid".equalsIgnoreCase(item))
                            setStyle("-fx-background-color: #1a6b3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: CENTER; -fx-background-radius: 4;");
                        else if ("Unpaid".equalsIgnoreCase(item) || "Overdue".equalsIgnoreCase(item))
                            setStyle("-fx-background-color: #ffe5e5; -fx-text-fill: #cc2222; -fx-font-weight: bold; -fx-alignment: CENTER; -fx-background-radius: 4;");
                        else
                            setStyle("-fx-alignment: CENTER;");
                    }
                }
            });

            String paidOn = feeStudent.getPaidDate() != null ? feeStudent.getPaidDate().toString() : "--";
            FeeRecord row = new FeeRecord(
                    "Semester " + feeStudent.getSemester(),
                    "PKR " + feeStudent.getFeeAmount(),
                    paidOn,
                    feeStudent.getFeeStatus()
            );
            feeHistoryTable.setItems(FXCollections.observableArrayList(row));
        }
    }

    private void updateClock() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy | hh:mm:ss a");
        clockLabel.setText(LocalDateTime.now().format(fmt));
    }

    @FXML
    private void handleDownloadChallan() {
        System.out.println("Download Challan clicked");
    }
}
