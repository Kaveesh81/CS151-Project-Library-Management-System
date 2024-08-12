import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {

    private Library library;

    public LoginPage(Library library) {
        this.library = library;
        setTitle("Library Login Page");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));

        JLabel welcomeLabel = new JLabel("Welcome!!!", SwingConstants.CENTER);
        add(welcomeLabel);

        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        add(buttonPanel);

        JButton signupButton = new JButton("Signup");
        add(signupButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserLogin(library).setVisible(true);  // Navigates to UserLogin page
                dispose();
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignupPage(library).setVisible(true);  // Navigates to SignupPage
                dispose();
            }
        });
    }
}
