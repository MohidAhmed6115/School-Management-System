package com.school.model.announcements;

import java.util.List;

public class StudentSchedule {
    private String department;
    private List<ClassEntry> monday;
    private List<ClassEntry> tuesday;
    private List<ClassEntry> wednesday;
    private List<ClassEntry> thursday;
    private List<ClassEntry> friday;
    private List<ClassEntry> saturday;
    private List<ClassEntry> sunday;

    public StudentSchedule() {

    }

    public StudentSchedule(String department,
                    List<ClassEntry> monday, List<ClassEntry> tuesday,
                    List<ClassEntry> wednesday, List<ClassEntry> thursday,
                    List<ClassEntry> friday, List<ClassEntry> saturday,
                    List<ClassEntry> sunday) {
        this.department = department;
        this.monday    = monday;
        this.tuesday   = tuesday;
        this.wednesday = wednesday;
        this.thursday  = thursday;
        this.friday    = friday;
        this.saturday  = saturday;
        this.sunday    = sunday;
    }

    public String getDepartment()            { return department; }
    public List<ClassEntry> getMonday()      { return monday; }
    public List<ClassEntry> getTuesday()     { return tuesday; }
    public List<ClassEntry> getWednesday()   { return wednesday; }
    public List<ClassEntry> getThursday()    { return thursday; }
    public List<ClassEntry> getFriday()      { return friday; }
    public List<ClassEntry> getSaturday()    { return saturday; }
    public List<ClassEntry> getSunday()      { return sunday; }

    public void setDepartment(String department) { this.department = department; }
    public void setMonday(List<ClassEntry> monday)       { this.monday = monday; }
    public void setTuesday(List<ClassEntry> tuesday)     { this.tuesday = tuesday; }
    public void setWednesday(List<ClassEntry> wednesday) { this.wednesday = wednesday; }
    public void setThursday(List<ClassEntry> thursday)   { this.thursday = thursday; }
    public void setFriday(List<ClassEntry> friday)       { this.friday = friday; }
    public void setSaturday(List<ClassEntry> saturday)   { this.saturday = saturday; }
    public void setSunday(List<ClassEntry> sunday)       { this.sunday = sunday; }

    @Override
    public String toString() {
        return "Schedule{department='" + department + "', monday=" + monday + ", tuesday=" + tuesday + "}";
    }
}
