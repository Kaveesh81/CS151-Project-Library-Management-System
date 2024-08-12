

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public enum Library {
    INSTANCE;

    private List<Book> books;
    private List<User> users;
    private HashMap<String, User> userMap;

    private Library() {
        books = new ArrayList<>();
        users = new ArrayList<>();
        userMap = new HashMap<>();

        // Adding default books for testing
        books.add(new Book("The Very Hungry Caterpillar", "Eric Carle", "9780399226908"));
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565"));
        books.add(new Book("Adventures of Huckleberry Finn", "Mark Twain", "9780486280615"));
        books.add(new Book("All the Right Places", "Jennifer Niven", "9780385755917"));
        books.add(new Book("Goodnight Moon", "Margaret Wise Brown", "9780061119774"));
        books.add(new Book("The Fault in Our Stars", "John Green", "9780525478812"));
        books.add(new Book("Anne of Green Gables", "L.M. Montgomery", "9780140367416"));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "9780060935467"));
        books.add(new Book("Animal Farm", "George Orwell", "9780451526342"));
        books.add(new Book("italkinmemes", "Internet Culture", "9781234567897"));
    }
    public static Library getInstance() {
        return INSTANCE;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<User> getActiveUsers() {
        List<User> activeUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isActive()) {
                activeUsers.add(user);
            }
        }
        return activeUsers;
    }

    public List<User> getInactiveUsers() {
        List<User> inactiveUsers = new ArrayList<>();
        for (User user : users) {
            if (!user.isActive()) {
                inactiveUsers.add(user);
            }
        }
        return inactiveUsers;
    }

    public User validateUser(String cardNumber, String password) {
        User user = userMap.get(cardNumber);
        if (user != null && user.getPassword().equals(password) && user.isActive()) {
            return user;
        }
        return null;
    }

    public void checkoutBook(User user, String title) {
        if (user.getCheckedOutBooks().size() >= 3) {
            System.out.println("User cannot check out more than 3 books.");
            return;
        }
        
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                user.checkoutBook(book);
                books.remove(book);
                break;
            }
        }
    }


    public void checkinBook(User user, String title) {
        for (Book book : user.getCheckedOutBooks()) {
            if (book.getTitle().equals(title)) {
                user.checkinBook(book);
                books.add(book);
                break;
            }
        }
    }
    public void addUser(User user) {
        users.add(user);  // users is the internal list of users in the Library class
        userMap.put(user.getLibraryCardNumber(), user);
    }

    public void activateUser(String name) {
        User user = getUserByName(name);
        if (user != null) {
            user.setActive(true);
            userMap.put(user.getLibraryCardNumber(), user); 
        }
    }

    public void deactivateUser(String name) {
        User user = getUserByName(name);
        if (user != null) {
            user.setActive(false);
            userMap.put(user.getLibraryCardNumber(), user); 
        }
    }

	public HashMap<String, User> getUserMap() {
		return userMap;
	}
	public Book getBookByTitle(String title) {
	    for (Book book : books) {
	        if (book.getTitle().equals(title)) {
	            return book;
	        }
	    }
	    return null;
	}

	public User getUserByName(String name) {
	    for (User user : users) {
	        if (user.getFirstName().equals(name)) {
	            return user;
	        }
	    }
	    return null;
	}
	
	public void loadUsersFromFile(String fileName) {
	    try (Scanner scanner = new Scanner(getClass().getClassLoader().getResourceAsStream(fileName))) {

	        while (scanner.hasNextLine()) {
	            String status = scanner.nextLine().trim();
	            String firstName = scanner.nextLine().trim();
	            String lastName = scanner.nextLine().trim();
	            String email = scanner.nextLine().trim();
	            String password = scanner.nextLine().trim();
	            String libraryCardNumber = scanner.nextLine().trim();
	            
	            User user = new User(firstName, lastName, libraryCardNumber, password, email, null);

	            // Set user as inactive if the status is "inactive"
	            if (status.equalsIgnoreCase("inactive")) {
	                user.setActive(false);
	            }

	            // Load checked-out books
	            while (scanner.hasNextLine()) {
	                String isbn = scanner.nextLine().trim();
	                if (isbn.isEmpty()) {
	                    break;
	                }
	                Book book = getBookByIsbn(isbn);
	                if (book != null) {
	                    user.checkoutBook(book);
	                }
	            }
	            addUser(user);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	 private Book getBookByIsbn(String isbn) {
	        for (Book book : books) {
	            if (book.getIsbn().equals(isbn)) {
	                return book;
	            }
	        }
	        return null;
	    }
	 public void saveUsersToFile(String fileName) {
	        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
	            oos.writeObject(users);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 @SuppressWarnings("unchecked")
	    public void loadUsersFromSerializedFile(String fileName) {
	        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
	            users = (List<User>) ois.readObject();
	            for (User user : users) {
	                userMap.put(user.getLibraryCardNumber(), user);
	            }
	        } catch (IOException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	    }


}
