import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupPage extends JFrame {

    private Library library;

    public SignupPage(Library library) {
        this.library = library;

        setTitle("Signup Page");
        setSize(300, 400);  // Adjusted size to accommodate additional fields
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JTextField firstNameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JTextField cardNumberField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField emailField = new JTextField(15);
        JTextField phoneNumberField = new JTextField(15);
        String[] roles = {"Regular User", "Admin"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        add(new JLabel("First Name:"));
        add(firstNameField);
        add(new JLabel("Last Name:"));
        add(lastNameField);
        add(new JLabel("Library Card Number:"));
        add(cardNumberField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Phone Number:"));
        add(phoneNumberField);
        add(new JLabel("Role:"));
        add(roleComboBox);

        // Panel for Signup and Back buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // Horizontal alignment

        JButton signupButton = new JButton("Signup");
        JButton backButton = new JButton("Back");
        buttonPanel.add(backButton);
        buttonPanel.add(signupButton);
       

        add(buttonPanel);  // Add the button panel

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String cardNumber = cardNumberField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                String phoneNumber = phoneNumberField.getText();
                String role = (String) roleComboBox.getSelectedItem();

                if (library.getUserMap().containsKey(cardNumber)) {
                    JOptionPane.showMessageDialog(SignupPage.this, "Library Card Number already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    User newUser;
                    if (role.equals("Admin")) {
                        newUser = new Admin(firstName, lastName, cardNumber, password, email, phoneNumber);
                    } else {
                        newUser = new User(firstName, lastName, cardNumber, password, email, phoneNumber);
                    }
                    library.addUser(newUser);
                    JOptionPane.showMessageDialog(SignupPage.this, "Signup Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new LoginPage(library).setVisible(true);
                    dispose();
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
