

import javax.swing.*;
import java.awt.*;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Library library = Library.getInstance();
            library.loadUsersFromFile("Users.txt");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> library.saveUsersToFile("users.ser")));
            LoginPage loginPage = new LoginPage(library);
            loginPage.setVisible(true);
        });
    }
}
