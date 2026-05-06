package com.library.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.library.model.Book;
import com.library.model.IssuedBook;

public class DataManager {

    private static final String DATA_DIR = System.getProperty("user.dir") + "/src/main/resources/library/data/";

    public static ArrayList<Book> loadBooks() {
        ArrayList<Book> bookData = new ArrayList<>();
        try (BufferedReader loader = new BufferedReader(new FileReader(DATA_DIR + "Books.txt"))) {
            String line;
            while ((line = loader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] field = line.split("\\|");
                if (field.length < 6) continue;
                bookData.add(new Book(field[0], field[1], field[2], field[3], field[4], field[5]));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return bookData;
    }

    public static ArrayList<IssuedBook> loadIssuedBooks() {
        ArrayList<IssuedBook> issuedBookData = new ArrayList<>();
        try (BufferedReader loader = new BufferedReader(new FileReader(DATA_DIR + "issued-book.txt"))) {
            String line;
            while ((line = loader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] field = line.split("\\|");
                if (field.length < 5) continue;
                issuedBookData.add(new IssuedBook(field[0], field[1], field[2], field[3], field[4]));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return issuedBookData;
    }

    public static void saveBooks(ArrayList<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIR + "Books.txt"))) {
            for (Book b : books) {
                writer.write(b.getLibCatalogue() + "|" + b.getTitle() + "|" + b.getAuthor() + "|" +
                        b.getCategory() + "|" + b.getYear() + "|" + b.getAvailability());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save books: " + e.getMessage());
        }
    }

    public static void saveIssuedBooks(ArrayList<IssuedBook> issueBooks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIR + "issued-book.txt"))) {
            for (IssuedBook b : issueBooks) {
                writer.write(b.getIssueDate() + "|" + b.getDeadLine() + "|" + b.getSap() + "|" +
                        b.getLibCatalogue() + "|" + b.getTitle());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save issued books: " + e.getMessage());
        }
    }
}
