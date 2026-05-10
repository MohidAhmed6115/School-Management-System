package com.fee.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class FeeStudent {

    private String name;
    private int id;
    private String course;
    private int semester;
    private long feeAmount;
    private LocalDate dueDate;
    private String feeStatus;

    public FeeStudent() {
    };

    public FeeStudent(String name, int id, String course, int semester,
            long feeAmount, LocalDate dueDate, String feeStatus) {
        this.name = name;
        this.id = id;
        this.course = course;
        this.semester = semester;
        this.feeAmount = feeAmount;
        this.dueDate = dueDate;
        this.feeStatus = feeStatus;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public int getSemester() {
        return semester;
    }

    public long getFeeAmount() {
        return feeAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getFeeStatus() {
        return feeStatus;
    }

    public long getNetFee() {
        return feeAmount + calculateLateFee();
    }

    // public long getRemainingFee() {
    // return feeAmount - submittedFee;
    // }

    // public void payFee(long amount) {
    // this.submittedFee += amount;
    // this.paidDate = LocalDate.now();
    // if (submittedFee == 0) feeStatus = "Unpaid";
    // else if (submittedFee < feeAmount) feeStatus = "Partial";
    // else feeStatus = "Paid";
    // }

    public long calculateLateFee() {
        if (LocalDate.now().isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
            return daysLate * 100;
        }
        return 0;
    }

    @Override
    public String toString() {
        return name + "|" + id + "|" + course + "|" + semester + "|" +
                feeAmount + "|" + dueDate + "|" + feeStatus;
    }

    public String extendDate(LocalDate extendedDate) {
        extendedDate = extendedDate.plusDays(3);
        return extendedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String installments(long amount, int choice) {
        switch (choice) {
            case 1:
                amount = amount / 2;
                return Long.toString(amount);
            case 2:
                amount = amount / 3;
                return Long.toString(amount);
        }
        return "You cannot get installments";
    }

    public ArrayList<String> extendDateInstallments(int choice) {

        LocalDate today = LocalDate.now();
        ArrayList<String> dates = new ArrayList<>();

        if (choice == 2 && (dueDate.isEqual(today) || today.isAfter(dueDate))) {
            dueDate = dueDate.plusDays(3);
            dates.add(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dueDate = dueDate.plusDays(30);
            dates.add(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return dates;
        } else if (choice == 3 && (dueDate.isEqual(today) || today.isAfter(dueDate))) {
            dueDate = dueDate.plusDays(3);
            dates.add(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dueDate = dueDate.plusDays(30);
            dates.add(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dueDate = dueDate.plusDays(30);
            dates.add(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return dates;
        }
        return dates;
    }

}
