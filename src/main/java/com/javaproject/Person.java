package com.javaproject;

import javafx.beans.property.SimpleStringProperty;

public class Person {
    private final int id;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty nickname;
    private final SimpleStringProperty phoneNumber;
    private final SimpleStringProperty address;
    private final SimpleStringProperty emailAddress;
    private final SimpleStringProperty birthDate;
    private final SimpleStringProperty category;

    public Person(int id, String firstName, String lastName, String nickname, String phoneNumber, String address, String emailAddress, String birthDate, String category) {
        this.id = id;
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.nickname = new SimpleStringProperty(nickname);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.address = new SimpleStringProperty(address);
        this.emailAddress = new SimpleStringProperty(emailAddress);
        this.birthDate = new SimpleStringProperty(birthDate);
        this.category = new SimpleStringProperty(category);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getNickname() {
        return nickname.get();
    }

    public void setNickname(String nickname) {
        this.nickname.set(nickname);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getEmailAddress() {
        return emailAddress.get();
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress.set(emailAddress);
    }

    public String getBirthDate() {
        return birthDate.get();
    }

    public void setBirthDate(String birthDate) {
        this.birthDate.set(birthDate);
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }
}
