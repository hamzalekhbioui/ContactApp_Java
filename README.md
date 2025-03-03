# Contact App - JavaFX & SQLite

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


### **🚀 Happy Coding! 💻🔥**

