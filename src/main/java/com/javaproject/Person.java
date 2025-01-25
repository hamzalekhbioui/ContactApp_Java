package com.javaproject;

public class Person {
    private int id;
    private String lastName;
    private String firstName;
    private String email;

    public Person(int id, String lastName, String firstName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

//    public String getEmail() {
//        return email;
//    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
