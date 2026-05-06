package com.fee.util;

import java.util.ArrayList;
import com.fee.model.Student;

public class DataStore {
    public static ArrayList<Student> students = new ArrayList<>();

    public static void loadAll() {
        students = DataManager.loadStudents();
    }
}
