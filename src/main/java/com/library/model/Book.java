package com.library.model;

public class Book {
    private String libCatalogue, title, author, category, year, availability;

    public Book(String libCatalogue, String title, String author, String category, String year, String availability) {
        this.libCatalogue = libCatalogue;
        this.title = title;
        this.author = author;
        this.category = category;
        this.year = year;
        this.availability = availability;
    }

    public String getLibCatalogue() { return this.libCatalogue; }
    public String getTitle()        { return this.title.toUpperCase(); }
    public String getAuthor()       { return this.author.toUpperCase(); }
    public String getCategory()     { return this.category.toUpperCase(); }
    public String getYear()         { return this.year; }
    public String getAvailability() { return this.availability; }

    public void setAvailability(String availability) { this.availability = availability; }
}
