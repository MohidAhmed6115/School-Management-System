package com.fee.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.fee.model.FeeRecord;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

public class Controller implements Initializable {

    @FXML private Label clockLabel;
    @FXML private TableView<FeeRecord> feeHistoryTable;
    @FXML private TableColumn<FeeRecord, String> colSemester;
    @FXML private TableColumn<FeeRecord, String> colAmount;
    @FXML private TableColumn<FeeRecord, String> colPaidOn;
    @FXML private TableColumn<FeeRecord, String> colStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Start live clock
        updateClock();
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        // Setup table columns
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colPaidOn.setCellValueFactory(new PropertyValueFactory<>("paidOn"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Status column with colored badge
        colStatus.setCellFactory(col -> new TableCell<FeeRecord, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("PAID".equalsIgnoreCase(item)) {
                        setStyle("-fx-background-color: #1a6b3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: CENTER; -fx-background-radius: 4;");
                    } else if ("OVERDUE".equalsIgnoreCase(item)) {
                        setStyle("-fx-background-color: #ffe5e5; -fx-text-fill: #cc2222; -fx-font-weight: bold; -fx-alignment: CENTER; -fx-background-radius: 4; -fx-border-color: #cc2222; -fx-border-radius: 4;");
                    } else {
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            }
        });

        // Table is empty — no data added intentionally
    }

    private void updateClock() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy | hh:mm:ss a");
        clockLabel.setText(LocalDateTime.now().format(fmt));
    }

    @FXML
    private void handleDownloadChallan() {
        System.out.println("Download Challan PDF clicked");
    }



}
