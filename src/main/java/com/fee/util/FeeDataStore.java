package com.fee.util;

import java.util.ArrayList;

import com.fee.model.FeeStudent;

public class FeeDataStore {
    public static ArrayList<FeeStudent> students = new ArrayList<>();

    public static void loadAll() {
        students = FeeDataManager.loadStudents();
        FeeDataManager.syncWithSchool();
    }
}
