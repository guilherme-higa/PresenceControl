package com.example.presencecontroltestapp.entities;

import org.bson.types.ObjectId;

public class Students {
    private static final String TAG = Students.class.getSimpleName();

    private ObjectId id;
    private String name;
    private String email;
    private int credentialsRa;
    private int credentialsPassword;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public int getCredentialsPassword() {
        return credentialsPassword;
    }

    public void setCredentialsPassword(int credentialsPassword) {
        this.credentialsPassword = credentialsPassword;
    }
}
