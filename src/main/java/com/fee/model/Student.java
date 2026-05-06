package com.fee.model;

import java.time.LocalDate;

public class Student {

    private String name;
    private int id;
    private String course;
    private int semester;
    private long feeAmount;
    private long submittedFee;
    private int dueDate;
    private String feeStatus;
    private LocalDate paidDate;

    public Student(String name, int id, String course, int semester,
                   long feeAmount, long submittedFee, int dueDate, String feeStatus, LocalDate paidDate) {
        this.name         = name;
        this.id           = id;
        this.course       = course;
        this.semester     = semester;
        this.feeAmount    = feeAmount;
        this.submittedFee = submittedFee;
        this.dueDate      = dueDate;
        this.feeStatus    = feeStatus;
        this.paidDate     = paidDate;
    }

    public int       getId()           { return id;           }
    public String    getName()         { return name;         }
    public String    getCourse()       { return course;       }
    public int       getSemester()     { return semester;     }
    public long      getFeeAmount()    { return feeAmount;    }
    public long      getSubmittedFee() { return submittedFee; }
    public int       getDueDate()      { return dueDate;      }
    public String    getFeeStatus()    { return feeStatus;    }
    public LocalDate getPaidDate()     { return paidDate;     }

    public long getRemainingFee() {
        return feeAmount - submittedFee;
    }

    public void payFee(long amount) {
        this.submittedFee += amount;
        this.paidDate = LocalDate.now();
        if      (submittedFee == 0)            feeStatus = "Unpaid";
        else if (submittedFee < feeAmount)     feeStatus = "Partial";
        else                                   feeStatus = "Paid";
    }

    public long calculateLateFee(int currentDay) {
        if (currentDay > dueDate && getRemainingFee() > 0) {
            int daysLate = currentDay - dueDate;
            return daysLate * 100;
        }
        return 0;
    }

    @Override
    public String toString() {
        return name + "|" + id + "|" + course + "|" + semester + "|" +
               feeAmount + "|" + submittedFee + "|" + dueDate + "|" + feeStatus + "|" +
               (paidDate != null ? paidDate.toString() : "");
    }
}
