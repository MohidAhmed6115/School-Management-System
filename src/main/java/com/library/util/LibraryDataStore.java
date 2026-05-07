package com.library.util;

import java.util.ArrayList;
import com.library.model.Book;
import com.library.model.IssuedBook;

public class LibraryDataStore {
    public static ArrayList<Book> books = new ArrayList<>();
    public static ArrayList<IssuedBook> issuedBooks = new ArrayList<>();

    public static void loadAll() {
        books = LibraryDataManager.loadBooks();
        issuedBooks = LibraryDataManager.loadIssuedBooks();
        com.library.controller.book.BookIssue.refreshIssuedList();
    }
}
