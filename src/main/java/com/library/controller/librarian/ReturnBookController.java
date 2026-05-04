package com.library.controller.librarian;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ReturnBookController implements Initializable{

	// Objects Here
	LibrarianFunctions functions = new LibrarianFunctions();

	// FXML Injections
	@FXML private TextField sapTextField,libCatalogueField;
	@FXML private Button doneButton;
	@FXML private Label lblValidation;
	// Non FXML Injections


	// Functions here

	// Abstract Method 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        doneButton.setOnAction(e -> {

			// For checking if the text field only contains numbers
			if(sapTextField.getText().matches("\\d+")){

				if( functions.returnBook(Integer.parseInt(sapTextField.getText()), libCatalogueField.getText()) ){
					((Stage) doneButton.getScene().getWindow()).close();

				}else{
					lblValidation.setText("Wrong Inputs");
				}
			}
			else{
				lblValidation.setText("Only number in Sap field");
			}

		});
    }
	
}
