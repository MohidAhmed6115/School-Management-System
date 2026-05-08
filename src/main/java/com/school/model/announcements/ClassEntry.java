package com.school.model.announcements;

public class ClassEntry {
    private String className;
    private String time;
    private String room;

    public ClassEntry() {

    }

    public ClassEntry(String className, String time, String room) {
        this.className = className;
        this.time = time;
        this.room = room;
    }

    public String getClassName() { return className; }
    public String getTime()      { return time; }
    public String getRoom()      { return room; }

    public void setClassName(String className) { this.className = className; }
    public void setTime(String time)           { this.time = time; }
    public void setRoom(String room)           { this.room = room; }

    @Override
    public String toString() {
        return "ClassEntry{class='" + className + "', time='" + time + "', room='" + room + "'}";
    }
}