# CS151-Project-Library-Management-System
CS151 Final Project
Library Management System
Project Overview
The Library Management System is a Java-based application designed to manage a collection of books and users within a library. The system provides functionalities for both regular users and librarians, including borrowing books, returning books, and managing user accounts. The application utilizes Object-Oriented Programming (OOP) principles, Java Swing for the graphical user interface, and supports file I/O operations for saving and loading data.

Features
User Features
Login/Signup: Users can create an account or log in with an existing one.
Book Management: Users can view available books, borrow up to three books, and return borrowed books.
Search and Sort: Users can search for books by title, author, or ISBN and sort the list of books based on these criteria.
Librarian Features
User Management: Librarians can activate or deactivate user accounts.
Book Management: Librarians can manage the collection of books available in the library.
User Role: Librarians have enhanced permissions compared to regular users, including the ability to manage other users.
Technical Features
GUI: Built using Java Swing for an interactive and user-friendly interface.
OOP Design: The system is built on a solid OOP foundation, utilizing classes like Library, Book, and User.
Singleton Pattern: The Library class is implemented as a singleton using an enum to ensure a single instance across the application.
File I/O: Supports serialization for saving and loading the state of the library, including users and books.
JUnit Testing: The project includes a suite of JUnit tests to ensure the correctness of functionalities.

Steps to Run
git clone https://github.com/your-username/CS151-Project-Library-Management-System.git
cd CS151-Project-Library-Management-System
Compile the Project
Run the LibraryManagementSystem java applitcation
