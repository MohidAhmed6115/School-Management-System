package com.library.controller.book;

import com.library.model.Book;
import com.library.model.IssuedBook;
import com.library.util.LibraryDataManager;
import com.library.util.LibraryDataStore;
import java.util.ArrayList;

public class BookIssue {

    // ── View model for the Librarian issued-books table ──────────────────────
    public static class IssuedBookData {
        private final String date, deadLine, sap, libCatalogue, title;

        public IssuedBookData(String date, String deadLine, String sap,
                              String libCatalogue, String title) {
            this.date         = date;
            this.deadLine     = deadLine;
            this.sap          = sap;
            this.libCatalogue = libCatalogue;
            this.title        = title;
        }

        public String getDate()        { return date; }
        public String getDeadLine()    { return deadLine; }
        public String getSap()         { return sap; }
        public String getLibCatalogue(){ return libCatalogue; }
        public String getTitle()       { return title; }
    }

    /** Live list of issued books in IssuedBookData form, kept in sync with DataStore. */
    public static ArrayList<IssuedBookData> issuedList = new ArrayList<>();

    /** Rebuild issuedList from DataStore.issuedBooks (call after any load/change). */
    public static void refreshIssuedList() {
        issuedList.clear();
        for (IssuedBook b : LibraryDataStore.issuedBooks) {
            issuedList.add(new IssuedBookData(
                    b.getIssueDate(), b.getDeadLine(),
                    b.getSap(), b.getLibCatalogue(), b.getTitle()));
        }
    }


    public static String changingBooksFile(String bookTitle) {
        String foundCatalogue = null;
        for (Book b : LibraryDataStore.books) {
            if (b.getTitle().equalsIgnoreCase(bookTitle) && b.getAvailability().equalsIgnoreCase("available")) {
                b.setAvailability("borrowed");
                foundCatalogue = b.getLibCatalogue();
                break;
            }
        }
        LibraryDataManager.saveBooks(LibraryDataStore.books);
        return foundCatalogue;
    }

    public static void issueBook(String title, String currentDay, String libCatalogue, String deadLine) {
        int SAP = com.util.SchoolDataStore.currentUser.getSapId();
        LibraryDataStore.issuedBooks.add(new IssuedBook(currentDay, deadLine, String.valueOf(SAP), libCatalogue, title));
        LibraryDataManager.saveIssuedBooks(LibraryDataStore.issuedBooks);
        refreshIssuedList();
    }
}
