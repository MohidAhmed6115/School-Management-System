package com.fee.model;

public class FeeRecord {
    private String semester;
    private String amount;
    private String paidOn;
    private String status;

    public FeeRecord(String semester, String amount, String paidOn, String status) {
        this.semester = semester;
        this.amount   = amount;
        this.paidOn   = paidOn;
        this.status   = status;
    }

    public String getSemester() { return semester; }
    public String getAmount()   { return amount;   }
    public String getPaidOn()   { return paidOn;   }
    public String getStatus()   { return status;   }
}
