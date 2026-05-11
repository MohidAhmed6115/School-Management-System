package com.fee.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class FeeStudent {

    private String name;
    private int id;
    private int installementChoice;
    private String course;
    private int semester;
    private long feeAmount;
    private LocalDate dueDate;
    private String feeStatus;

    public FeeStudent() {
    }

    public FeeStudent(String name, int id, String course, int semester,
            long feeAmount, LocalDate dueDate, String feeStatus, int installementChoice) {
        this.name = name;
        this.id = id;
        this.course = course;
        this.semester = semester;
        this.feeAmount = feeAmount;
        this.dueDate = dueDate;
        this.feeStatus = feeStatus;
        this.installementChoice = installementChoice;
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
    public int getInstallementChoice() {
        return this.installementChoice;
    }
    public void setInstallementChoice(int installementChoice) {
        this.installementChoice = installementChoice;
    }


    public long calculateLateFee() {
        if (LocalDate.now().isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
            return daysLate * 100; // per day  0
        }
        return 0;
    }

    @Override
    public String toString() {
        return name + "|" + id + "|" + course + "|" + semester + "|" +
                feeAmount + "|" + dueDate + "|" + feeStatus + "|" + Integer.toString(installementChoice);
    }

    // For extending the date of the feeBill
    public String extendDate() {
        this.dueDate = this.dueDate.plusDays(3);
        return this.dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // For dividing the fee bill in installments
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
    
    //
    LocalDate base = (today.isAfter(dueDate) || today.isEqual(dueDate)) 
                     ? today.plusDays(3) 
                     : dueDate.plusDays(3);

    if (choice == 2) {
        dates.add(base.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dates.add(base.plusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    } else if (choice == 3) {
        dates.add(base.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dates.add(base.plusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dates.add(base.plusDays(60).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    return dates;
}

}
