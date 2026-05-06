package com.library.controller.librarian;

import java.util.ArrayList;
import com.library.model.Book;
import com.library.util.DataStore;

public class SearchFunctions {

    public static class BookRecord {
        private String title, author, category, year;
        public int totalBooks, availableBooks;

        public String getTitle()    { return title.toUpperCase(); }
        public String getAuthor()   { return author.toUpperCase(); }
        public String getCategory() { return category.toUpperCase(); }
        public String getYear()     { return year; }
        public int getTotalBooks()     { return totalBooks; }
        public int getAvailableBooks() { return availableBooks; }
    }

    ArrayList<BookRecord> results = new ArrayList<>();

    public ArrayList<BookRecord> search(String searched, String searchBy) {
        results.clear();

        for (Book b : DataStore.books) {
            boolean matched = false;

            if (searchBy.equalsIgnoreCase("title") && b.getTitle().toLowerCase().contains(searched.toLowerCase()))
                matched = true;
            else if (searchBy.equalsIgnoreCase("author") && b.getAuthor().toLowerCase().contains(searched.toLowerCase()))
                matched = true;
            else if (searchBy.equalsIgnoreCase("category") && b.getCategory().toLowerCase().contains(searched.toLowerCase()))
                matched = true;
            else if (searchBy.equalsIgnoreCase("year") && b.getYear().toLowerCase().contains(searched.toLowerCase()))
                matched = true;
            else if (searchBy.equalsIgnoreCase("lib catalogue") && b.getLibCatalogue().toLowerCase().contains(searched.toLowerCase()))
                matched = true;

            if (matched) {
                boolean found = false;
                for (BookRecord each : results) {
                    if (each.title.equals(b.getTitle()) && each.author.equals(b.getAuthor())) {
                        each.totalBooks++;
                        if (b.getAvailability().equalsIgnoreCase("available"))
                            each.availableBooks++;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    BookRecord record = new BookRecord();
                    record.title = b.getTitle();
                    record.author = b.getAuthor();
                    record.category = b.getCategory();
                    record.year = b.getYear();
                    record.totalBooks = 1;
                    record.availableBooks = b.getAvailability().equalsIgnoreCase("available") ? 1 : 0;
                    results.add(record);
                }
            }
        }
        return results;
    }
}
