package com.library.controller.librarian;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AddBookController implements Initializable {
	// Objects
	LibrarianFunctions functions = new LibrarianFunctions();

	private File selectedCoverFile;

	// FXML Injunctions
	// Buttons
	@FXML
	Button doneButton;
	@FXML
	Button imageChooser;
	// Labels
	@FXML
	Label lblWarning;
	// Spinners
	@FXML
	Spinner<Integer> onlyYearSpinner, noOfCopiesSpinner;
	// TextFields
	@FXML
	TextField bookNameField, authorNameField;
	// Combo Boxes
	@FXML
	ComboBox<String> categoryComboBox;

	@FXML
	public void chooseImage() {
		FileChooser coverChooser = new FileChooser();
		coverChooser.setTitle("Select Book Cover");
		coverChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
		selectedCoverFile = coverChooser.showOpenDialog(null);
	}

	// NON-FXML Injunctions
	String[] categories = { "Fiction", "Non-Fiction", "Science", "Technology", "History", "Biography", "Auto-Biography",
			"Religion", "Philosophy", "Children/Young Adult", "Arts/Literature", "Science Fiction/Fantasy",
			"Self-Help/Psychology", "Law/Politics", "Medical/Health", "Education/Reference", "Travel/Geography",
			"Comics/Graphics Novels" };

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Setting Category Combo Box
		categoryComboBox.getItems().addAll(categories);

		// Setting onlyYearSpinner for taking custom value
		// .getYear() will help us in getting year only
		int defaultYear = LocalDate.now().getYear();

		// (minimum valued,maximum value,default value) For spinner
		SpinnerValueFactory<Integer> onlyYear = new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE,
				defaultYear, defaultYear);
		SpinnerValueFactory<Integer> noOfCopies = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1);

		// Setting Value of onlyYearSpinner
		onlyYearSpinner.setValueFactory(onlyYear);
		onlyYearSpinner.setEditable(true);
		// Setting Value of noOfCopiesSpinner
		noOfCopiesSpinner.setValueFactory(noOfCopies);
		noOfCopiesSpinner.setEditable(true);

		doneButton.setOnAction(e -> {
			// Store values in variables
			String bookName = getBookName();
			int bookYear = getBookYear();
			int copies = getNoOfCopies();
			String category = getCategory();
			String authorName = getAuthorName();
			String currentYear = String.valueOf(LocalDate.now().getYear());

			// Validate before saving
			if (bookName.isEmpty() || authorName.isEmpty() || category == null) {
				lblWarning.setText("Please fill all fields!");
				return; // don't close window if validation fails
			}
			// Cover Page
			if(selectedCoverFile != null){
				try{
					String destination = "src/main/resources/library/images/Book Covers/" + bookName + ".jpg";
					Files.copy(selectedCoverFile.toPath(), Path.of(destination), StandardCopyOption.REPLACE_EXISTING);
				}
				catch(IOException ex){
					System.out.println("Could not copy cover image: " + ex.getMessage());
				}
			}else{
				lblWarning.setText("Please select a cover image");
				return;
			}

			// Add book Function
			functions.addBook(copies, currentYear, bookName, authorName, category,
					String.valueOf(bookYear));

			System.out.println("Book Added Successfully!");

			// Close window after saving
			((Stage) doneButton.getScene().getWindow()).close();
		});
	}
	

	public String getBookName() {
		return bookNameField.getText();
	}

	public int getBookYear() {
		return onlyYearSpinner.getValue();
	}

	public int getNoOfCopies() {
		return noOfCopiesSpinner.getValue();
	}

	public String getCategory() {
		return categoryComboBox.getValue();
	}

	public String getAuthorName() {
		return authorNameField.getText();
	}
}
