package com.javaproject;

public class Person {
    private int id;
    private String lastname;
    private String firstname;
    private String email;

    public Person(int id, String lastname, String firstname) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setFirstname(String firstname) {
//        this.firstname = firstname;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public void setLastname(String lastname) {
//        this.lastname = lastname;
//    }
}

