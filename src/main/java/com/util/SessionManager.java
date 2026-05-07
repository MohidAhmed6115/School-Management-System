package com.util;

import java.io.IOException;

public class SessionManager {

    // In SessionManager.java
    public static void openLibrary() throws IOException {
        new ProcessBuilder("java", "-jar", "library-management.jar")
                .start();
    }
}
