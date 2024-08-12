import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserLogin extends JFrame {

    private Library library;

    public UserLogin(Library library) {
        this.library = library;
        setTitle("User Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JTextField cardNumberField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        add(new JLabel("Library Card Number:"));
        add(cardNumberField);
        add(new JLabel("Password:"));
        add(passwordField);

        // Panel for Login and Back buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // Horizontal alignment

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");
        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);

        add(buttonPanel);  // Add the button panel

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardNumber = cardNumberField.getText();
                String password = new String(passwordField.getPassword());

                User user = library.validateUser(cardNumber, password);
                if (user != null) {
                    if (!user.isActive()) {
                        JOptionPane.showMessageDialog(UserLogin.this, "Your account is inactive. Please contact the admin.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (user instanceof Admin) {
                            new AdminPage(library, user).setVisible(true);
                        } else {
                            new UserPage(library, user).setVisible(true);
                        }
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(UserLogin.this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage(library).setVisible(true);
                dispose();
            }
        });
    }
}
