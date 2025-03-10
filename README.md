# Contact App - JavaFX & SQLite
## Contributors 👨‍💻👩‍💻

This project was developed by:

- **LEKHBIOUI Hamza** 
- **MOUSSAIF Mohammed Amine** 
- **BERNOUSSI Wahb** 
- **EL HASSNAOUI Akram** 

## 📌 Introduction
This is a **JavaFX-based Contact Management Application** that allows users to:
✅ **Add, Modify, Delete, and Search** contacts
✅ Categorize contacts into **Friends, Family, Work, and All Contacts**
✅ **Store contacts** in an SQLite database
✅ **Use a modern UI** with a form-based input
✅ **Ensure data validation** for phone numbers and birthdates

---

## 🛠️ Tech Stack
- **Java 17+**
- **JavaFX** (for UI)
- **SQLite** (for database)
- **Maven** (for dependency management)
- **JUnit** (for unit testing)

---

## 🚀 How to Run the Project

### **1️⃣ Clone the repository**
```sh
  git clone https://github.com/your-repo/contact-app.git
  cd contact-app
```

### **2️⃣ Build the Project**
```sh
mvn clean compile
```

### **3️⃣ Run the Project**
```sh
mvn exec:java
```

---

## 🏗️ Features Implemented
- ✅ **CRUD Operations** (Create, Read, Update, Delete contacts)
- ✅ **Search functionality** by first name, last name, nickname, or phone number
- ✅ **Contact Categories:** All Contacts, Friends, Family, Work
- ✅ **UI Improvements** with sidebar navigation
- ✅ **Database Storage** using SQLite
- ✅ **Unit Testing** 

---

## 🔍 Project Structure
```
contact-app/
│-- src/
│   ├── main/java/com/javaproject/
│   │   ├── Main.java  # Entry Point
│   │   ├── Controller.java  # JavaFX Controller
│   │   ├── DatabaseHelper.java  # SQLite DB Logic
│   │   ├── Person.java  # Data Model
│   │   ├── ContactOperations.java  # Interface for CRUD operations
│   ├── resources/com/javaproject/
│   │   ├── main.fxml  # JavaFX UI File
│-- test/java/com/javaproject/
│   ├── DatabaseHelperTest.java  # JUnit Test Cases
│-- contact.db  # SQLite Database File
│-- pom.xml  # Maven Configuration
```
# Methods Overview

## **Controller.java**
This class handles user interactions and manages data flow in the application.

- `initialize()`: Initializes the controller and sets up the initial state.
- `searchPerson()`: Searches for a person in the database.
- `loadPersonsFromDatabase()`: Loads all persons from the database into the application.
- `addPerson()`: Adds a new person to the database.
- `validateInput(String firstName, String lastName, String nickname, String phoneNumber, String emailAddress, String birthDate)`: Validates the input fields before adding/modifying a person.
- `isValidBirthDate(String birthDate)`: Checks if the birth date format is valid.
- `resetFieldStyles()`: Resets the styles of input fields (likely after validation feedback).
- `clearFormFields()`: Clears all input fields in the form.
- `showAlert(String title, String message)`: Displays an alert with a given title and message.
- `deletePerson()`: Deletes a selected person from the database.
- `fillFormFields()`: Fills the form fields with selected person's details.
- `modifyPerson()`: Modifies the details of an existing person.
- `autoResizeColumns()`: Adjusts table column widths dynamically.

---

## **DatabaseHelper.java**
Handles database connections and queries.

- `connect()`: Establishes a connection to the database.
- `executeScript(String scriptPath)`: Executes an SQL script from a file.
- `ensureTableSchema()`: Ensures that the required database tables exist.

---

## **Main.java**
The entry point of the application.

- `main(String[] args)`: Launches the application.

---

## **Person.java**
Represents a person entity with getter and setter methods.

- `Person(int id, String firstName, String lastName, String nickname, String phoneNumber, String address, String emailAddress, String birthDate, String category)`: Constructor for creating a new `Person` object.
- `getId()`: Returns the person's ID.
- `getFirstName()`: Returns the first name.
- `setFirstName(String firstName)`: Sets the first name.
- `getLastName()`: Returns the last name.
- `setLastName(String lastName)`: Sets the last name.
- `getNickname()`: Returns the nickname.
- `setNickname(String nickname)`: Sets the nickname.
- `getPhoneNumber()`: Returns the phone number.
- `setPhoneNumber(String phoneNumber)`: Sets the phone number.
- `getAddress()`: Returns the address.
- `setAddress(String address)`: Sets the address.
- `getEmailAddress()`: Returns the email address.
- `setEmailAddress(String emailAddress)`: Sets the email address.
- `getBirthDate()`: Returns the birth date.
- `setBirthDate(String birthDate)`: Sets the birth date.
- `getCategory()`: Returns the category.
- `setCategory(String category)`: Sets the category.

---

## **DatabaseHelperTest.java**
Contains test cases for database-related functions.

- `testInsertPerson()`: Tests inserting a person into the database.
- `testUpdatePerson()`: Tests updating a person in the database.
- `testExecuteInvalidSQL()`: Tests handling of invalid SQL queries.
- `testEnsureTableSchema()`: Tests whether the required database schema is properly set up.




### **🚀 Happy Coding! 💻🔥**
