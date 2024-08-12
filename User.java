import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Comparable<User>, Serializable {
    private String firstName;
    private String lastName;
    private String libraryCardNumber;
    private String password;
    private boolean active;
    private String email;
    private String phoneNumber;
    private List<Book> checkedOutBooks;

    public User(String firstName, String lastName, String libraryCardNumber, String password, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.libraryCardNumber = libraryCardNumber;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.active = true;
        this.checkedOutBooks = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLibraryCardNumber() {
        return libraryCardNumber;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Book> getCheckedOutBooks() {
        return checkedOutBooks;
    }

    public void checkoutBook(Book book) {
        checkedOutBooks.add(book);
        book.setCheckedOut(true);
    }

    public void checkinBook(Book book) {
        checkedOutBooks.remove(book);
        book.setCheckedOut(false);
    }

    @Override
    public int compareTo(User other) {
        return this.lastName.compareTo(other.lastName);
    }

    @Override
    public String toString() {
        return lastName + ", " + firstName + " (" + libraryCardNumber + ")";
    }
}
