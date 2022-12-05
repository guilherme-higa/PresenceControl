package com.example.presencecontroltestapp.entities;

public class Students {
    private static final String TAG = Students.class.getSimpleName();

    private String name;
    private String email;
    private int credentialsRa;
    private String credentialsPassword;

    public Students(String name, String email, int credentialsRa, String credentialsPassword) {
        this.name = name;
        this.email = email;
        this.credentialsRa = credentialsRa;
        this.credentialsPassword = credentialsPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCredentialsRa() {
        return credentialsRa;
    }

    public void setCredentialsRa(int credentialsRa) {
        this.credentialsRa = credentialsRa;
    }

    public String  getCredentialsPassword() {
        return credentialsPassword;
    }

    public void setCredentialsPassword(String credentialsPassword) {
        this.credentialsPassword = credentialsPassword;
    }
}
