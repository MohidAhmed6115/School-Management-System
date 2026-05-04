package com.library.controller.book;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import com.school.util.DataStore;

public class BookIssue {

    // ───── Inner class — constructor + getters ONLY ─────
    public static class IssuedBookData {
        private String title, Date, libCatalogue, SAP, deadLine;

        public IssuedBookData(String title, String Date, String libCatalogue, String SAP, String deadLine) {
            this.title        = title;
            this.Date         = Date;
            this.libCatalogue = libCatalogue;
            this.SAP          = SAP;
            this.deadLine     = deadLine;
        }

        public String getTitle()        { return title.toUpperCase(); }
        public String getDate()         { return Date; }
        public String getLibCatalogue() { return libCatalogue; }
        public String getSap()          { return SAP; }
        public String getDeadLine()     { return deadLine; }
    }
    
	
    public static ArrayList<IssuedBookData> issuedList = new ArrayList<>();

    private static final String ISSUED_FILE = "src/main/resources/library/data/issued-book.txt";

    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(ISSUED_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                StringTokenizer st  = new StringTokenizer(line, "|");
                String date         = st.nextToken();
                String deadLine     = st.nextToken();
                String sap          = st.nextToken();
                String libCatalogue = st.nextToken();
                String title        = st.nextToken();
                issuedList.add(new IssuedBookData(title, date, libCatalogue, sap, deadLine));
            }
        } catch (Exception e) {
            System.out.println("Could not load issued books: " + e.getMessage());
        }
    }


    // ───── Outer class methods ─────
    public static String changingBooksFile(String bookTitle) {
        boolean alreadyChanged = false;
        String foundCatalogue  = null;

        try (BufferedReader originalBookReading = new BufferedReader(
                new FileReader("src/main/resources/library/data/Books.txt"));
             BufferedWriter tempFile = new BufferedWriter(
                new FileWriter("src/main/resources/library/data/temp.txt"))) {

            String line;
            while ((line = originalBookReading.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                StringTokenizer st = new StringTokenizer(line, "|");
                String catalogue   = st.nextToken();
                String title       = st.nextToken();
                st.nextToken(); st.nextToken(); st.nextToken();
                String availability = st.nextToken();

                if (!alreadyChanged && title.equalsIgnoreCase(bookTitle)
                        && availability.equalsIgnoreCase("available")) {
                    tempFile.write(line.substring(0, line.lastIndexOf("|") + 1) + "borrowed");
                    tempFile.newLine();
                    alreadyChanged = true;
                    foundCatalogue = catalogue;
                } else {
                    tempFile.write(line);
                    tempFile.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("File Not Found");
        }

        File original = new File("src/main/resources/library/data/Books.txt");
        File temp     = new File("src/main/resources/library/data/temp.txt");
        original.delete();
        temp.renameTo(original);
        return foundCatalogue;
    }

    public static void issueBook(String title, String currentDay,
                                 String libCatalogue, String deadLine) {
                                    int SAP = DataStore.currentUser.getSapId();
        try (BufferedWriter issue = new BufferedWriter(
                new FileWriter("src/main/resources/library/data/issued-book.txt", true))) {
            issue.write(currentDay + "|" + deadLine + "|" + SAP + "|" + libCatalogue + "|" + title);
            issue.newLine();
            issuedList.add(new IssuedBookData(title, currentDay, libCatalogue, Integer.toString(SAP), deadLine));
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }

    
}