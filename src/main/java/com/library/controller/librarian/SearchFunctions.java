package com.library.controller.librarian;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SearchFunctions {

    public static class BookRecord {
        private String title, author, category, year, printLine;
        public int totalBooks, availableBooks;

        public String getTitle() {
            return title.toUpperCase();
        }

        public String getAuthor() {
            return author.toUpperCase();
        }

        public String getCategory() {
            return category.toUpperCase();
        }

        public String getYear() {
            return year;
        }

        public int getTotalBooks() {
            return totalBooks;
        }

        public int getAvailableBooks() {
            return availableBooks;
        }
    };

    ArrayList<BookRecord> results = new ArrayList<>();

    public ArrayList<BookRecord> search(String searched, String searchBy) {
        results.clear();

        try (BufferedReader read = new BufferedReader(
                new FileReader("src/main/resources/library/data/books.txt"))) {
            String line;
            while ((line = read.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "|");
                String id = st.nextToken();
                String title = st.nextToken();
                String author = st.nextToken();
                String category = st.nextToken();
                String year = st.nextToken();
                String availability = st.nextToken();

                boolean matched = false;
               
                if (searchBy.equalsIgnoreCase("title") && title.toLowerCase().contains(searched.toLowerCase()))
                    matched = true;
                else if (searchBy.equalsIgnoreCase("author") && author.toLowerCase().contains(searched.toLowerCase()))
                    matched = true;
                else if (searchBy.equalsIgnoreCase("category")
                        && category.toLowerCase().contains(searched.toLowerCase()))
                    matched = true;
                else if (searchBy.equalsIgnoreCase("year") && year.toLowerCase().contains(searched.toLowerCase()))
                    matched = true;
                else if (searchBy.equalsIgnoreCase("lib catalogue")
                        && id.toLowerCase().contains(searched.toLowerCase()))
                    matched = true;

                if (matched) {
                    boolean found = false;
                    for (BookRecord each : results) {
                        if (each.title.equals(title) && each.author.equals(author)) {
                            each.totalBooks++;
                            if (availability.equals("available"))
                                each.availableBooks++;
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        BookRecord record = new BookRecord();
                        record.title = title;
                        record.author = author;
                        record.category = category;
                        record.year = year;
                        record.printLine = line;
                        record.totalBooks = 1;
                        record.availableBooks = availability.equals("available") ? 1 : 0;
                        results.add(record);
                    }
                }
            }
 
        } catch (IOException e) {
            System.out.println("File isn't open");
        }

        if(results != null){
            return results;
        }else{
            return new ArrayList<>();
        }
    }
}
