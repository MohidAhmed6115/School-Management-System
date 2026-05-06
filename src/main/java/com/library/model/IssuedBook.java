package com.library.model;

public class IssuedBook {
    private String title, issueDate, libCatalogue, SAP, deadLine;

    public IssuedBook(String issueDate, String deadLine, String SAP, String libCatalogue, String title) {
        this.title        = title;
        this.issueDate    = issueDate;
        this.libCatalogue = libCatalogue;
        this.SAP          = SAP;
        this.deadLine     = deadLine;
    }

    public String getTitle()        { return this.title.toUpperCase(); }
    public String getIssueDate()    { return this.issueDate; }
    public String getLibCatalogue() { return this.libCatalogue; }
    public String getSap()          { return this.SAP; }
    public String getDeadLine()     { return this.deadLine; }
}
