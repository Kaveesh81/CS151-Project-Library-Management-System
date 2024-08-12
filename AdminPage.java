import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//Creates an admin page
public class AdminPage extends JFrame implements ActionListener {

    private Library library; 
    private User admin;
    private DefaultListModel<String> inactiveUsersModel;
    private DefaultListModel<String> activeUsersModel;
    private JList<String> inactiveUsersList;
    private JList<String> activeUsersList;
    private JComboBox<String> cbxSortCriteria;
    private JTextField txtSearch;
    private JButton btnSort, btnSearch, btnExit, btnActivate, btnInactivate, btnLogout;
    private JRadioButton btnAscending, btnDescending;

    public AdminPage(Library library, User admin) {
        this.library = library;
        this.admin = admin;

        // Frame setup
        setTitle("Admin USER PAGE");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setBackground(Color.green);

        // Initialize components
        btnExit = new JButton("Exit");
        btnSort = new JButton("Sort");
        btnSearch = new JButton("Search");
        btnActivate = new JButton("Activate");
        btnInactivate = new JButton("Inactivate");
        btnLogout = new JButton("Logout"); // Logout button added
        btnAscending = new JRadioButton("Ascending");
        btnDescending = new JRadioButton("Descending");
        txtSearch = new JTextField("a regular expression ...");

        // Top panel
        JPanel pnlTop = buildPnlTop();
        add(pnlTop);

        // User display panel
        JPanel userDisplayPanel = buildUserDisplayPanel();
        add(userDisplayPanel);

        // Sort panel
        JPanel pnlSort = buildPnlSort();
        add(pnlSort);

        // Search panel
        JPanel pnlSearch = buildPnlSearch();
        add(pnlSearch);

        // Button listeners
        btnExit.addActionListener(this);
        btnSort.addActionListener(this);
        btnSearch.addActionListener(this);
        btnActivate.addActionListener(this);
        btnInactivate.addActionListener(this);
        btnLogout.addActionListener(this); // Adding logout button listener
        btnAscending.setSelected(true);
    }
    //top half of the page
    private JPanel buildPnlTop() {
        JPanel p = new JPanel();
        JLabel lblTitle = new JLabel("Library Users");
        p.add(lblTitle);
        p.add(btnLogout); // Add logout button to the top panel
        p.add(btnExit);
        p.setBounds(0, 0, 700, 30);
        p.setBackground(Color.green);
        return p;
    }
    //users panel
    private JPanel buildUserDisplayPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBounds(50, 50, 600, 300);
        p.setBackground(Color.green);

        inactiveUsersModel = new DefaultListModel<>();
        activeUsersModel = new DefaultListModel<>();

        for (User user : library.getInactiveUsers()) {
            inactiveUsersModel.addElement(user.toString());
        }
        for (User user : library.getActiveUsers()) {
            activeUsersModel.addElement(user.toString());
        }

        inactiveUsersList = new JList<>(inactiveUsersModel);
        activeUsersList = new JList<>(activeUsersModel);

        JScrollPane scrollInactive = new JScrollPane(inactiveUsersList);
        JScrollPane scrollActive = new JScrollPane(activeUsersList);

        p.add(new JLabel("Inactive Users:"));
        p.add(scrollInactive);
        p.add(new JLabel("Active Users:"));
        p.add(scrollActive);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2));
        btnPanel.add(btnActivate);
        btnPanel.add(btnInactivate);

        p.add(btnPanel);

        return p;
    }

    private JPanel buildPnlSort() {
        ButtonGroup rbgOrder = new ButtonGroup();
        rbgOrder.add(btnAscending);
        rbgOrder.add(btnDescending);

        JLabel lblSort = new JLabel("Sort By");
        String[] criteria = {"First Name", "Last Name", "Library Card Number"};
        cbxSortCriteria = new JComboBox<>(criteria);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(lblSort);
        p.add(cbxSortCriteria);
        p.add(btnAscending);
        p.add(btnDescending);
        p.add(btnSort);
        p.setBounds(0, 400, 700, 50);
        p.setBackground(Color.green);

        return p;
    }

    private JPanel buildPnlSearch() {
        JLabel lblSearch = new JLabel("Search By");
        String[] criteria = {"First Name", "Last Name", "Library Card Number"};
        JComboBox<String> cbxSearch = new JComboBox<>(criteria);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(lblSearch);
        p.add(cbxSearch);
        p.add(txtSearch);
        p.add(btnSearch);
        p.setBounds(0, 450, 700, 50);
        p.setBackground(Color.green);

        txtSearch.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev) {
                if (txtSearch.getText().equals("a regular expression ...")) {
                    txtSearch.setText("");
                }
            }
        });

        return p;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnExit) {
            System.exit(0);
        } else if (e.getSource() == btnSort) {
            String criteria = (String) cbxSortCriteria.getSelectedItem();
            boolean ascending = btnAscending.isSelected();
            sortUsers(criteria, ascending);
        } else if (e.getSource() == btnSearch) {
            String searchText = txtSearch.getText().trim();
            searchUsers(searchText);
        } else if (e.getSource() == btnActivate) {
            activateUser();
        } else if (e.getSource() == btnInactivate) {
            inactivateUser();
        } else if (e.getSource() == btnLogout) {
            logout();
        }
    }

    private void sortUsers(String criteria, boolean ascending) {
        Comparator<User> comparator = null;

        // Determine the comparator based on the selected criteria
        switch (criteria) {
            case "First Name":
                comparator = Comparator.comparing(User::getFirstName);
                break;
            case "Last Name":
                comparator = Comparator.comparing(User::getLastName);
                break;
            case "Library Card Number":
                comparator = Comparator.comparing(User::getLibraryCardNumber);
                break;
            default:
                return; // Exit if criteria are unknown
        }

        // Reverse the comparator if descending order is selected
        if (!ascending) {
            comparator = comparator.reversed();
        }

        // Sort the users and refresh the lists
        sortAndRefreshUsers(library.getActiveUsers(), activeUsersModel, comparator);
        sortAndRefreshUsers(library.getInactiveUsers(), inactiveUsersModel, comparator);
    }

    private void sortAndRefreshUsers(List<User> users, DefaultListModel<String> model, Comparator<User> comparator) {
        Collections.sort(users, comparator);
        model.clear();
        for (User user : users) {
            model.addElement(user.toString());
        }
    }

    private void searchUsers(String query) {
        activeUsersModel.clear();
        for (User user : library.getActiveUsers()) {
            if (user.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                user.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                user.getLibraryCardNumber().toLowerCase().contains(query.toLowerCase())) {
                activeUsersModel.addElement(user.toString());
            }
        }

        inactiveUsersModel.clear();
        for (User user : library.getInactiveUsers()) {
            if (user.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                user.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                user.getLibraryCardNumber().toLowerCase().contains(query.toLowerCase())) {
                inactiveUsersModel.addElement(user.toString());
            }
        }
    }

    private void activateUser() {
        String selectedUser = inactiveUsersList.getSelectedValue();
        if (selectedUser != null) {
            library.activateUser(selectedUser);
            inactiveUsersModel.removeElement(selectedUser);
            activeUsersModel.addElement(selectedUser);
        } else {
            JOptionPane.showMessageDialog(this, "No user selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inactivateUser() {
        String selectedUser = activeUsersList.getSelectedValue();
        if (selectedUser != null) {
            library.deactivateUser(selectedUser);
            activeUsersModel.removeElement(selectedUser);
            inactiveUsersModel.addElement(selectedUser);
        } else {
            JOptionPane.showMessageDialog(this, "No user selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        new LoginPage(library).setVisible(true);
        dispose();  
    }
}
