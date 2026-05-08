package com.fee.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FeeStudent {

    private String name;
    private int id;
    private String course;
    private int semester;
    private long feeAmount;
    private LocalDate dueDate;
    private String feeStatus;

    

    public FeeStudent(){};

    public FeeStudent(String name, int id, String course, int semester,
                   long feeAmount, LocalDate dueDate, String feeStatus) {
        this.name         = name;
        this.id           = id;
        this.course       = course;
        this.semester     = semester;
        this.feeAmount    = feeAmount;
        this.dueDate      = dueDate;
        this.feeStatus    = feeStatus;
        
    }

    public FeeStudent(String name,int id, int semester,long feeAmount){
        this.name = name;
        this.id = id;
        this.semester = semester;
        this.feeAmount = feeAmount;
    }

    public int       getId()           { return id;           }
    public String    getName()         { return name;         }
    public String    getCourse()       { return course;       }
    public int       getSemester()     { return semester;     }
    public long      getFeeAmount()    { return feeAmount;    }
    public LocalDate getDueDate()      { return dueDate;      }
    public String    getFeeStatus()    { return feeStatus;    }
    public long      getNetFee()       { return feeAmount + calculateLateFee(); } 

    // public long getRemainingFee() {
    //     return feeAmount - submittedFee;
    // }

    // public void payFee(long amount) {
    //     this.submittedFee += amount;
    //     this.paidDate = LocalDate.now();
    //     if      (submittedFee == 0)            feeStatus = "Unpaid";
    //     else if (submittedFee < feeAmount)     feeStatus = "Partial";
    //     else                                   feeStatus = "Paid";
    // }

    public long calculateLateFee() {
        if (LocalDate.now().isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate,LocalDate.now());
            return daysLate * 100;
        }
        return 0;
    }

    @Override
    public String toString() {
        return name + "|" + id + "|" + course + "|" + semester + "|" +
               feeAmount + "|" + dueDate + "|" + feeStatus;
    }
}
