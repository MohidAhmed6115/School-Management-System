package com.fee.model;

import com.fee.util.FeeDataStore;
import com.fee.util.FeeDataManager;


import org.apache.pdfbox.pdmodel.PDDocument; //For representing the entire PDF file
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm; //For representing the form inside the PDF
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField; //Represents a single text field in the form, useful if you need more control over a specific field
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PrintPDF {



	private LocalDate today = LocalDate.now();

	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	private String pdfDate = today.format(fmt);

	public void generateFeeBill() {

		try {
			// Opening pdf document
			PDDocument feeBill = PDDocument.load(getClass().getResourceAsStream("/fee/template/fee-bill-template.pdf"));
			PDAcroForm editedFeeBill = feeBill.getDocumentCatalog().getAcroForm();


			// editedFeeBill.getField("student_name").setValue(student);
			// editedFeeBill.getField("father_name").setValue(student);
			// editedFeeBill.getField("sap_id").setValue(student);
			// editedFeeBill.getField("semester").setValue(student);
			// editedFeeBill.getField("doc_no").setValue(student);
			// editedFeeBill.getField("doc_date").setValue(student);
			// editedFeeBill.getField("installment_amount").setValue(student);
			// editedFeeBill.getField("late_fines").setValue(student);
			// editedFeeBill.getField("fee_amount").setValue(student);
			// editedFeeBill.getField("amount_words").setValue(student);
			// editedFeeBill.getField("valid_upto").setValue(student);
			
			editedFeeBill.flatten();	//Makes fields non-editable

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}

	}
}
