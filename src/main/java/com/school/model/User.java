package com.school.model;

import java.util.Random;

public class User {
    private String name;
    private int sapId;
    private String password;
    private String email;

    public User (String name) {
        this.name = name;
        this.password = "abc123";
    }

    public User (String name, int sapId, String password, String email) {
        this.name = name;
        this.sapId = sapId;
        this.password = password;
        this.email = email;
    }

    public int getSapId() {return sapId;}

    public String getPassword() {return password;}

    public String getName() {return name;}

    public String getEmail() {return email;}

    public void setPassword(String password) {this.password = password;}

    public void setSapId(int sapId) {this.sapId = sapId;}

    public void setEmail(String email) {this.email = email;}

}
