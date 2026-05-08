package com.fee.model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import com.fee.util.FeeDataStore;
import com.school.model.Student; //For representing the entire PDF file
import com.util.SchoolDataStore; //For representing the form inside the PDF
import static com.util.SchoolDataStore.currentUser;

import javafx.stage.FileChooser; //Represents a single text field in the form, useful if you need more control over a specific field
import javafx.scene.Node;

public class PrintPDF {

	private LocalDate today = LocalDate.now();

	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	DateTimeFormatter fmt1 = DateTimeFormatter.ofPattern("ddMMyyyy");

	private String pdfDate = today.format(fmt);
	private String docNoDate = today.format(fmt1);
	private String fileName = null;

	public void generateFeeBill(Node node) {

		try {
			// Opening pdf document
			PDDocument feeBill = PDDocument.load(getClass().getResourceAsStream("/fee/template/fee-bill-template.pdf"));
			PDAcroForm editedFeeBill = feeBill.getDocumentCatalog().getAcroForm();
			String docNo = autoDocNo();
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

			for (FeeStudent feeStudent : FeeDataStore.students) {

				if (currentUser instanceof Student s) {

					if (s.getName().equalsIgnoreCase(feeStudent.getName())
							&& s.getSapId() == feeStudent.getId()
							&& s.getCurrentSemester() == feeStudent.getSemester()) {
						// Editing the pdf fields
						for (PDField field : editedFeeBill.getFieldTree()) {
							switch (field.getFullyQualifiedName()) {
								case "student_name" -> field.setValue(feeStudent.getName());
								case "sap_id" -> field.setValue(Integer.toString(feeStudent.getId()));
								case "semester" -> field.setValue(Integer.toString(feeStudent.getSemester()));
								case "fee_amount" -> field.setValue(Long.toString(feeStudent.getFeeAmount()));
								case "amount_words" ->
									field.setValue(amountInWords(feeStudent.getFeeAmount()).toUpperCase());
								case "doc_no" -> field.setValue(docNo);
								case "doc_date" -> field.setValue(pdfDate);
								case "valid_upto" -> field.setValue(today.plusDays(7).format(fmt));
								case "late_fines" -> field.setValue("0");
							}
						}
						
						

						fileName = "fee-bill " + s.getSapId() + ".pdf";

					}
				}

			}
			editedFeeBill.flatten(); // Makes fields non-editable

			FileChooser download = new FileChooser();
			download.setTitle("Save Fee Bill");
			download.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
			download.setInitialFileName(fileName);

			File file = download.showSaveDialog(node.getScene().getWindow());
			if (file != null) {
				feeBill.save(file.getAbsolutePath());
			}
			feeBill.close();

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	public String amountInWords(long amount) {

		String[] single = {
				"zero", "one", "two", "three", "four",
				"five", "six", "seven", "eight", "nine"
		};

		String[] tenTo19 = {
				"ten", "eleven", "twelve", "thirteen", "fourteen",
				"fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
		};

		String[] tens = {
				"", "", "twenty", "thirty", "forty",
				"fifty", "sixty", "seventy", "eighty", "ninety"
		};

		if (amount < 0) {
			return "invalid";
		}

		if (amount < 10) {
			return single[(int) amount];
		}

		if (amount < 20) {
			return tenTo19[(int) amount - 10];
		}

		if (amount < 100) {
			int t = (int) (amount / 10);
			int u = (int) (amount % 10);

			if (u == 0) {
				return tens[t];
			}

			return tens[t] + " " + single[u];
		}

		// For Hundreds
		if (amount < 1000) {
			int h = (int) (amount / 100);
			int rem = (int) (amount % 100);

			if (h == 0)
				return amountInWords(rem);

			if (rem == 0)
				return single[h] + "hundred";

			return single[h] + " hundred " + amountInWords(rem);
		}

		// For Thousands
		if (amount < 10000) {
			int th = (int) (amount / 1000);
			int rem = (int) (amount % 1000);

			if (th == 0)
				return amountInWords(rem);

			if (rem == 0)
				return single[th] + " thousand ";

			return single[th] + " thousand " + amountInWords(rem);
		}

		// For Ten Thousands
		if (amount < 100000) {
			int tth = (int) (amount / 1000);
			int rem = (int) (amount % 1000);

			if (tth == 0)
				return amountInWords(rem);

			if (rem == 0)
				return amountInWords(tth) + " thousand ";

			return amountInWords(tth) + " thousand " + amountInWords(rem);
		}

		// For Hundred Thousands
		if (amount < 1000000) {
			int hth = (int) (amount / 100000);
			int rem = (int) (amount % 100000);

			if (hth == 0)
				return amountInWords(rem);

			if (rem == 0)
				return amountInWords(hth) + " hundred thousand ";

			return amountInWords(hth) + " hundred " + amountInWords(rem);
		}

		// For Million
		if (amount < 10000000) {
			int m = (int) (amount / 1000000);
			int rem = (int) (amount % 1000000);

			if (m == 0)
				return amountInWords(rem);

			if (rem == 0)
				return amountInWords(m) + " million ";

			return amountInWords(m) + " million " + amountInWords(rem);
		}

		// For ten million
		if (amount < 100000000) {
			int tm = (int) (amount / 1000000);
			int rem = (int) (amount % 1000000);

			if (tm == 0)
				return amountInWords(rem);

			if (rem == 0)
				return amountInWords(tm) + " million ";

			return amountInWords(tm) + " million " + amountInWords(rem);
		}

		return "Out of Bound";
	}

	public String autoDocNo() {
		FeeStudent feeStudent = new FeeStudent();
		return Integer.toString(feeStudent.getId()) + docNoDate + Integer.toString(feeStudent.getSemester());
	}

}
