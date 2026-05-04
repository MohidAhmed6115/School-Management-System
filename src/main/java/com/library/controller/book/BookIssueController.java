package com.library.controller.book;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BookIssueController implements Initializable {
	// FXML injections

	// LABEL
	@FXML Label lblDeadLine;
	@FXML Label lblError;
	@FXML Label lblTitle;
	@FXML Label lblLibCatalogue;

	// Button
	@FXML Button yesButton;
	@FXML Button noButton;
	// Image View
	@FXML private ImageView coverPage;

	// Variables without FXML injections
	// LocalDate
	private LocalDate deadLine;
	private int SAP;
	private boolean isConfirmed = false;

	public void issuedBookDeadline() {
		LocalDate today = LocalDate.now();
		deadLine = today.plusDays(14);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		lblDeadLine.setText("📅 DeadLine: " + deadLine.format(formatter));
	}

	// public int getSAP() {
	// 	if (SAPField.getText().isEmpty()) {
	// 		lblError.setText("Please Enter SAP ID");
	// 		return -1;
	// 	}
	// 	if (!SAPField.getText().matches("[0-9]+")) {
	// 		lblError.setText("Please Enter only Numbers");
	// 		return -1;
	// 	}
	// 	SAP = Integer.parseInt(SAPField.getText());
	// 	isConfirmed = true;
	// 	((Stage) lblDeadLine.getScene().getWindow()).close();
	// 	return SAP;
	// }

	public void setBookData(String title,String libCatalogue){
		// For setting CoverImage
		// String coverPage_DIR = "/library/images/Book Covers/" + title + ".jpg";
		System.out.println("[DEBUG] libCatalogue: " + libCatalogue);
		Image image = new Image(getClass().getResourceAsStream("/library/images/Book Covers/" + title + ".jpg"));
		coverPage.setImage(image);
		lblTitle.setText(title);
		lblLibCatalogue.setText(libCatalogue);
	}



	public boolean confirmation() {
		return isConfirmed;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Yes Button function
		yesButton.setOnAction(e -> {
			isConfirmed = true;
			((Stage) lblDeadLine.getScene().getWindow()).close();

		});
		// No Button Lamda function
		noButton.setOnAction(e -> {
			isConfirmed = false;
			((Stage) lblDeadLine.getScene().getWindow()).close();
		});
		issuedBookDeadline();
	}
}
