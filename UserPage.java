import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;

public class UserPage extends JFrame {

    private Library library; // Reference to the Library instance
    private User user; // The user currently logged in
    private DefaultListModel<String> checkedOutModel; // Model for displaying checked-out books
    private DefaultListModel<String> libraryModel; // Model for displaying available books
    private JList<String> checkedOutList; // List component for checked-out books
    private JList<String> libraryList; // List component for library books

    // Constructor to initialize the UserPage
    public UserPage(Library library, User user) {
        this.library = library;
        this.user = user;

        // Set up the frame properties
        setTitle("USER PAGE");
        setSize(800, 600); // Set default size for the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setLayout(new BorderLayout());

        // Welcome label at the top
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getFirstName(), SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        // Main panel to display checked-out and available books
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 0, 0));

        JLabel checkedOutTitle = new JLabel("Checked Out Books:", SwingConstants.LEFT);
        mainPanel.add(checkedOutTitle);

        JLabel libraryTitle = new JLabel("Books in Library:", SwingConstants.LEFT);
        mainPanel.add(libraryTitle);

        // Initialize the model for checked-out books and populate it
        checkedOutModel = new DefaultListModel<>();
        for (Book book : user.getCheckedOutBooks()) {
            checkedOutModel.addElement(book.getTitle());
        }
        checkedOutList = new JList<>(checkedOutModel);
        checkedOutList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainPanel.add(new JScrollPane(checkedOutList));

        // Initialize the model for library books and populate it
        libraryModel = new DefaultListModel<>();
        for (Book book : library.getBooks()) {
            libraryModel.addElement(book.getTitle());
        }
        libraryList = new JList<>(libraryModel);
        libraryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainPanel.add(new JScrollPane(libraryList));

        // Add padding around the titles and lists
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5)); // Spacing between buttons

        // Panel for action buttons (Check Out, Check In, Book Info, Logout)
        JPanel actionsPanel = new JPanel();
        JButton checkoutButton = new JButton("Check Out");
        JButton checkinButton = new JButton("Check In");
        JButton bookInfoButton = new JButton("Book Info");
        JButton logoutButton = new JButton("Logout");

        actionsPanel.add(checkoutButton);
        actionsPanel.add(checkinButton);
        actionsPanel.add(bookInfoButton);
        actionsPanel.add(logoutButton);
        buttonPanel.add(actionsPanel);

        // Panel for search and sort functionality
        JPanel searchSortPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // Search functionality
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        searchPanel.add(new JLabel("Search Books:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchSortPanel.add(searchPanel);

        // Sort functionality
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] sortOptions = {"Title", "Author", "ISBN"};
        JComboBox<String> sortCriteriaComboBox = new JComboBox<>(sortOptions);
        JButton sortAscButton = new JButton("Sort Ascending");
        JButton sortDescButton = new JButton("Sort Descending");

        sortPanel.add(new JLabel("Sort by:"));
        sortPanel.add(sortCriteriaComboBox);
        sortPanel.add(sortAscButton);
        sortPanel.add(sortDescButton);
        searchSortPanel.add(sortPanel);

        buttonPanel.add(searchSortPanel);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners for buttons
        checkoutButton.addActionListener(e -> checkoutBook());
        checkinButton.addActionListener(e -> checkinBook());
        bookInfoButton.addActionListener(e -> showBookInfo());
        logoutButton.addActionListener(e -> logout());

        searchButton.addActionListener(e -> searchBooks(searchField.getText()));
        sortAscButton.addActionListener(e -> sortBooks(sortCriteriaComboBox.getSelectedItem().toString(), true));
        sortDescButton.addActionListener(e -> sortBooks(sortCriteriaComboBox.getSelectedItem().toString(), false));
    }

    // Method to handle checking out a book
    private void checkoutBook() {
        if (user.getCheckedOutBooks().size() >= 3) {
            JOptionPane.showMessageDialog(this, "You can only check out a maximum of 3 books at a time.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedBookTitle = libraryList.getSelectedValue();
        if (selectedBookTitle != null) {
            library.checkoutBook(user, selectedBookTitle);
            checkedOutModel.addElement(selectedBookTitle);
            libraryModel.removeElement(selectedBookTitle);
        } else {
            JOptionPane.showMessageDialog(this, "No book selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to handle checking in a book
    private void checkinBook() {
        String selectedBookTitle = checkedOutList.getSelectedValue();
        if (selectedBookTitle != null) {
            library.checkinBook(user, selectedBookTitle);
            libraryModel.addElement(selectedBookTitle);
            checkedOutModel.removeElement(selectedBookTitle);
        } else {
            JOptionPane.showMessageDialog(this, "No book selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to display information about a selected book
    private void showBookInfo() {
        String selectedBookTitle = libraryList.getSelectedValue();
        if (selectedBookTitle != null) {
            Book book = library.getBookByTitle(selectedBookTitle);
            if (book != null) {
                JOptionPane.showMessageDialog(this, 
                    "Title: " + book.getTitle() + "\n" +
                    "Author: " + book.getAuthor() + "\n" +
                    "ISBN: " + book.getIsbn() + "\n" +
                    "Checked Out: " + book.isCheckedOut(),
                    "Book Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No book selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to handle logging out of the user page
    private void logout() {
        new LoginPage(library).setVisible(true);
        dispose(); // Close the current UserPage window
    }

    // Method to search for books based on a query
    private void searchBooks(String query) {
        libraryModel.clear();
        for (Book book : library.getBooks()) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                book.getIsbn().toLowerCase().contains(query.toLowerCase())) {
                libraryModel.addElement(book.getTitle());
            }
        }
    }

    // Method to sort books based on a selected criterion and order
    private void sortBooks(String criteria, boolean ascending) {
        Comparator<Book> comparator = null;
        switch (criteria) {
            case "Title":
                comparator = Comparator.comparing(Book::getTitle);
                break;
            case "Author":
                comparator = Comparator.comparing(Book::getAuthor);
                break;
            case "ISBN":
                comparator = Comparator.comparing(Book::getIsbn);
                break;
        }

        if (comparator != null) {
            if (!ascending) {
                comparator = comparator.reversed();
            }
            Collections.sort(library.getBooks(), comparator);

            libraryModel.clear();
            for (Book book : library.getBooks()) {
                libraryModel.addElement(book.getTitle());
            }
        }
    }
}
