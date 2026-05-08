package com.library.controller.librarian;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.library.model.Book;
import com.library.util.LibraryDataManager;
import com.library.util.LibraryDataStore;

public class LibrarianFunctions {

    public int getLibCatalogueNumbers() {
        int lib = 0;
        int max = 0;
        for (Book b : LibraryDataStore.books) {
            if (b.getLibCatalogue().isEmpty()) continue;
            int pos = b.getLibCatalogue().lastIndexOf('-');
            String number = b.getLibCatalogue().substring(pos + 1);
            lib = Integer.parseInt(number);
            if (max < lib) max = lib;
        }
        return max + 1;
    }

    public static String totalBooks() {
        com.library.util.LibraryDataStore.loadAll();
        int books = new LibrarianFunctions().getLibCatalogueNumbers() - 1;
        return Integer.toString(books);
    }

    public void addBook(int copies, String currentYear, String bookName, String authorName, String category, String bookYear) {
        int ID = getLibCatalogueNumbers();
        for (int i = 1; i <= copies; i++) {
            String catalogueID = String.format("%06d", ID);
            LibraryDataStore.books.add(new Book(
                    "lib-" + currentYear + "-" + catalogueID,
                    bookName.toLowerCase(),
                    authorName.toLowerCase(),
                    category.toLowerCase(),
                    bookYear,
                    "available"));
            ID++;
        }
        LibraryDataManager.saveBooks(LibraryDataStore.books);
    }

    public void removeBook(String bookName, String category, String authorName) {
        LibraryDataStore.books.removeIf(b -> b.getTitle().equalsIgnoreCase(bookName)
                && b.getCategory().equalsIgnoreCase(category)
                && b.getAuthor().equalsIgnoreCase(authorName));
        LibraryDataManager.saveBooks(LibraryDataStore.books);
    }

    public boolean returnBook(int sap, String catalogue) {
        boolean isChanged = false;
        for (Book b : LibraryDataStore.books) {
            if (b.getLibCatalogue().equalsIgnoreCase(catalogue) && b.getAvailability().equalsIgnoreCase("borrowed")) {
                b.setAvailability("available");
                isChanged = true;
                break;
            }
        }
        LibraryDataStore.issuedBooks.removeIf(b -> b.getSap().equals(String.valueOf(sap))
                && b.getLibCatalogue().equalsIgnoreCase(catalogue));
        LibraryDataManager.saveBooks(LibraryDataStore.books);
        LibraryDataManager.saveIssuedBooks(LibraryDataStore.issuedBooks);
        com.library.controller.book.BookIssue.refreshIssuedList();
        return isChanged;
    }

    public static int calcFine(LocalDate current, LocalDate deadLine) {
        if (current.isAfter(deadLine)) {
            int fine = 50;
            long daysLate = ChronoUnit.DAYS.between(deadLine, current);
            fine *= daysLate;
            return fine;
        }
        return 0;
    }
}