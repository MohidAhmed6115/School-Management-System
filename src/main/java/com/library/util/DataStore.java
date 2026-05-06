package com.library.util;

import java.util.ArrayList;
import com.library.model.Book;
import com.library.model.IssuedBook;

public class DataStore {
    public static ArrayList<Book> books = new ArrayList<>();
    public static ArrayList<IssuedBook> issuedBooks = new ArrayList<>();

    public static void loadAll() {
        books = DataManager.loadBooks();
        issuedBooks = DataManager.loadIssuedBooks();
        com.library.controller.book.BookIssue.refreshIssuedList();
    }
}
