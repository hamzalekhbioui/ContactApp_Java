package com.javaproject;

public class Person {
    private int id;
    private String lastName;
    private String firstName;
    private String nickname;
    private String phoneNumber;
    private String address;
    private String emailAddress;
    private String birthDate;

    public Person(int id, String lastName, String firstName,String nickname,
                  String phoneNumber, String address, String emailAddress, String birthDate) {

        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.emailAddress = emailAddress;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber;}

    public String getAddress() {
        return address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
