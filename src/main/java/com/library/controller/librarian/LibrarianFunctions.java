package com.library.controller.librarian;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.StringTokenizer;

import com.library.controller.book.BookIssue;

public class LibrarianFunctions {
	// class

	// getting the latest number of lib-catalogue
	public int getLibCatalogueNumbers() {
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/library/data/books.txt"))) {
			// Whole Line
			String line;
			// Using linear Search for finding maximum number of lib catalogue
			int lib = 0;
			int max = 0;
			// File reading
			while ((line = br.readLine()) != null) {
				if (line.isEmpty())
					continue;

				StringTokenizer st = new StringTokenizer(line, "|");
				String catalogue = st.nextToken(); // For getting Lib Catalogue

				if (catalogue.isEmpty())
					continue;
				// Finding the maximum number
				int pos = catalogue.lastIndexOf('-');
				String number = catalogue.substring(pos + 1, catalogue.length());
				// Conversion of maximum number to Integer
				lib = Integer.parseInt(number);
				// Linear Search
				if (max < lib)
					max = lib;
			}
			return ++max;

		} catch (IOException e) {
			System.out.println("Problem here! hahahha");
			return -1;
		}
	}

	// Book addition by Librarian
	public void addBook(int copies, String currentYear, String bookName, String authorName, String category,
			String bookYear) {
		try (BufferedWriter bw = new BufferedWriter(
				new FileWriter("src/main/resources/library/data/books.txt", true))) {
			int ID = getLibCatalogueNumbers();
			for (int i = 1; i <= copies; i++) {
				String catalogueID = String.format("%06d", ID);
				// Building readLine
				String line = "lib-" + currentYear + "-" + catalogueID + "|"
						+ bookName.toLowerCase() + "|"
						+ authorName.toLowerCase() + "|"
						+ category.toLowerCase() + "|"
						+ bookYear + "|"
						+ "available";

				bw.write(line);
				bw.newLine();
				ID++;
			}

		} catch (IOException e) {
			System.out.println("Problem here hahaha");
		}
	}

	// Book removal by librarian
	public void removeBook(String bookName, String category, String authorName) {

		// using try with resources for BufferedWriter and FileWriter
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/library/data/books.txt"));
				BufferedWriter bw = new BufferedWriter(
						new FileWriter("/library/data/temp.txt"))) {

			String line;
			while ((line = br.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line, "|");
				st.nextToken();
				String title = st.nextToken();
				String author = st.nextToken();
				String bookType = st.nextToken();
				if (!(title.equalsIgnoreCase(bookName) && bookType.equalsIgnoreCase(category)
						&& author.equalsIgnoreCase(authorName))) {
					bw.write(line);
					bw.newLine();
				}
			}

		} catch (IOException e) {
			System.out.println(e);
		}
		File original = new File("src/main/resources/library/data/books.txt");
		File temp = new File("/library/data/temp.txt");
		original.delete();
		temp.renameTo(original);
	}

	// Book return by Librarian
	public boolean returnBook(int Sap, String catalogue) {

		boolean isChanged = false;

		try (BufferedReader issueBookReader = new BufferedReader(
				new FileReader("src/main/resources/library/data/issued-book.txt"));
				BufferedReader booksReader = new BufferedReader(
						new FileReader("src/main/resources/library/data/books.txt"));
				BufferedWriter issueBookWriter = new BufferedWriter(
						new FileWriter("src/main/resources/library/data/temp-issue.txt"));
				BufferedWriter booksWriter = new BufferedWriter(
						new FileWriter("src/main/resources/library/data/temp-books.txt"))) {
			// Writing and reading in issued-book.txt
			String lineIssuedBook;
			while ((lineIssuedBook = issueBookReader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(lineIssuedBook, "|");
				st.nextToken();
				st.nextToken();
				String SAPid = st.nextToken();
				String libCatalogue = st.nextToken();

				if (!(Integer.toString(Sap).equals(SAPid) && libCatalogue.equalsIgnoreCase(catalogue))) {
					issueBookWriter.write(lineIssuedBook);
					issueBookWriter.newLine();
				} else {
					BookIssue.issuedList.removeIf(
							book -> book.getSap().equals(SAPid) && book.getLibCatalogue().equalsIgnoreCase(catalogue));
				}
			}

			// Writing and reading in books.txt
			String lineBooks;
			while ((lineBooks = booksReader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(lineBooks, "|");
				String booksCatalogue = st.nextToken();
				String title = st.nextToken();
				String author = st.nextToken();
				String category = st.nextToken();
				String year = st.nextToken();
				String availability = st.nextToken();

				if (booksCatalogue.equalsIgnoreCase(catalogue) && availability.equalsIgnoreCase("borrowed")) {
					lineBooks = booksCatalogue + "|" + title + "|" + author + "|" + category + "|" + year + "|"
							+ "available";
					booksWriter.write(lineBooks);
					booksWriter.newLine();
					isChanged = true;
				} else {
					booksWriter.write(lineBooks);
					booksWriter.newLine();
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}

		File originalBook = new File("src/main/resources/library/data/books.txt");
		File tempBook = new File("src/main/resources/library/data/temp-books.txt");
		originalBook.delete();
		tempBook.renameTo(originalBook);
		File originalIssueBook = new File("src/main/resources/library/data/issued-book.txt");
		File tempIssueBook = new File("src/main/resources/library/data/temp-issue.txt");
		originalIssueBook.delete();
		tempIssueBook.renameTo(originalIssueBook);

		return isChanged;
	}

	public static void calcFine(LocalDate current, LocalDate deadLine) {
		if (current.isAfter(deadLine)) {
			int fine = 50;
			long daysLate = ChronoUnit.DAYS.between(deadLine, current);
			fine *= daysLate;
		} else {

		}
	}
}
