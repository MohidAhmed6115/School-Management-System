package com.library.controller.librarian;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RemoveBookController implements Initializable {
	// Objects
	LibrarianFunctions functions = new LibrarianFunctions();


	// FXML Injunctions
	@FXML private Button doneButton;
	@FXML private Label lblWarning;
	@FXML private TextField bookNameField, authorNameField;
	@FXML private ComboBox<String> categoryComboBox;

	// NON-FXML Injunctions
	private final String[] categories = { "Fiction", "Non-Fiction", "Science", "Technology", "History", "Biography", "Auto-Biography",
			"Religion", "Philosophy", "Children/Young Adult", "Arts/Literature", "Science Fiction/Fantasy",
			"Self-Help/Psychology", "Law/Politics", "Medical/Health", "Education/Reference", "Travel/Geography",
			"Comics/Graphics Novels" };

	// Abstract Method
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Setting Category Combo Box
		categoryComboBox.getItems().addAll(categories);

		// Setting onlyYearSpinner for taking custom value
		// .getYear() will help us in getting year only


		// (minimum valued,maximum value,default value) For spinner
		SpinnerValueFactory<Integer> noOfCopies = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1);


		doneButton.setOnAction(e -> {
			// Store values in variables
			String bookName = getBookName();
			String category = getCategory();
			String authorName = getAuthorName();

			// Validate before saving
			if (bookName.isEmpty() || authorName.isEmpty() || category == null) {
				lblWarning.setText("Please fill all fields!");
				return; // don't close window if validation fails
			}

			// Add book Function
			functions.removeBook(bookName, category, authorName);

			System.out.println("Book Removed Successfully!");

			// Close window after saving
			((Stage) doneButton.getScene().getWindow()).close();
		});
	}

	// Getters
	public String getBookName() {
		return bookNameField.getText();
	}

	public String getCategory() {
		return categoryComboBox.getValue();
	}

	public String getAuthorName() {
		return authorNameField.getText();
	}
}
