
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LibTest {

    private Library library;

    @BeforeEach
    public void setup() {
        library = Library.getInstance();
        library.getActiveUsers().clear();
        library.getInactiveUsers().clear();
        library.getBooks().clear();
    }

    @Test
    public void testUserCreationAndValidation() {
        User user = new User("John", "Doe", "12345", "password", "john.doe@example.com", "555-5555");
        library.addUser(user);

        User validatedUser = library.validateUser("12345", "password");
        assertNotNull(validatedUser, "User should be validated successfully");
        assertEquals("John", validatedUser.getFirstName(), "First name should match");
        assertEquals("Doe", validatedUser.getLastName(), "Last name should match");
    }

    @Test
    public void testInvalidUserValidation() {
        User user = new User("John", "Doe", "12345", "password", "john.doe@example.com", "555-5555");
        library.addUser(user);

        User invalidUser = library.validateUser("12345", "wrongpassword");
        assertNull(invalidUser, "User validation should fail for wrong password");
    }

    @Test
    public void testBookCheckoutAndCheckin() {
        User user = new User("Jane", "Doe", "67890", "password", "jane.doe@example.com", "555-5556");
        library.addUser(user);

        Book book = new Book("Test Book", "Author", "1111");
        library.getBooks().add(book);

        library.checkoutBook(user, "Test Book");
        assertTrue(user.getCheckedOutBooks().contains(book), "User should have checked out the book");
        assertFalse(library.getBooks().contains(book), "Library should no longer have the book");

        library.checkinBook(user, "Test Book");
        assertFalse(user.getCheckedOutBooks().contains(book), "User should have returned the book");
        assertTrue(library.getBooks().contains(book), "Library should have the book back");
    }

    @Test
    public void testActivateAndDeactivateUser() {
        User user = new User("Mark", "Smith", "54321", "password", "mark.smith@example.com", "555-5557");
        library.addUser(user);
        assertTrue(user.isActive(), "User should be active initially");

        library.deactivateUser("Mark");
        assertFalse(user.isActive(), "User should be deactivated");

        library.activateUser("Mark");
        assertTrue(user.isActive(), "User should be activated");
    }

    @Test
    public void testSortUsersByLastName() {
        User user1 = new User("John", "Smith", "11111", "password", "john.smith@example.com", "555-5558");
        User user2 = new User("Jane", "Doe", "22222", "password", "jane.doe@example.com", "555-5559");
        library.addUser(user1);
        library.addUser(user2);

        List<User> users = library.getActiveUsers();
        Collections.sort(users);
        assertEquals("Doe", users.get(0).getLastName(), "Users should be sorted by last name");
    }

    @Test
    public void testMaxBookCheckoutLimit() {
        User user = new User("Alice", "Johnson", "33333", "password", "alice.johnson@example.com", "555-5560");
        library.addUser(user);

        // Adding 4 books to the library
        library.getBooks().add(new Book("Book 1", "Author 1", "0001"));
        library.getBooks().add(new Book("Book 2", "Author 2", "0002"));
        library.getBooks().add(new Book("Book 3", "Author 3", "0003"));
        library.getBooks().add(new Book("Book 4", "Author 4", "0004"));

        library.checkoutBook(user, "Book 1");
        library.checkoutBook(user, "Book 2");
        library.checkoutBook(user, "Book 3");

        // Attempting to check out a 4th book should not be allowed
        library.checkoutBook(user, "Book 4");
        assertEquals(3, user.getCheckedOutBooks().size(), "User should only be able to check out a maximum of 3 books");
    }

    @Test
    public void testLoadAndSaveUsers() {
        User user = new User("Tom", "Brown", "44444", "password", "tom.brown@example.com", "555-5561");
        library.addUser(user);

        library.saveUsersToFile("test_users.ser");

        // Clear the current users
        library.getActiveUsers().clear();
        library.getInactiveUsers().clear();
        library.getUserMap().clear();

        library.loadUsersFromSerializedFile("test_users.ser");

        User loadedUser = library.getUserByName("Tom");
        assertNotNull(loadedUser, "User should be loaded successfully from the file");
        assertEquals("Brown", loadedUser.getLastName(), "Last name should match after loading");
    }
}
