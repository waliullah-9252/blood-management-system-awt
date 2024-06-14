import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

class Donor {
    private String name;
    private String bloodType;
    private String contact;

    public Donor(String name, String bloodType, String contact) {
        this.name = name;
        this.bloodType = bloodType;
        this.contact = contact;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Contact: " + contact);
    }

    public String toString() {
        return "Name: " + name + ", Blood Type: " + bloodType + ", Contact: " + contact;
    }
}

class User {
    private String username;
    private String password;
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}

class BloodBank {
    private List<Donor> donors;

    public BloodBank() {
        this.donors = new ArrayList<>();
    }

    public void addDonor(Donor donor) {
        this.donors.add(donor);
        System.out.println("Donor added successfully.");
    }

    public List<Donor> searchDonor(String bloodType) {
        List<Donor> matchingDonors = new ArrayList<>();
        for (Donor donor : donors) {
            if (donor.getBloodType().equalsIgnoreCase(bloodType)) {
                matchingDonors.add(donor);
            }
        }
        return matchingDonors;
    }

    public List<Donor> getAllDonors() {
        return donors;
    }
}

public class Main {
    private static List<User> users = new ArrayList<>();
    private static User loggedInUser = null;
    private static BloodBank bank = new BloodBank();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Blood Bank System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            JTabbedPane tabbedPane = new JTabbedPane();

            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new GridLayout(3, 2));
            JTextField loginUsername = new JTextField();
            JPasswordField loginPassword = new JPasswordField();
            JButton loginButton = new JButton("Login");

            loginPanel.add(new JLabel("Username:"));
            loginPanel.add(loginUsername);
            loginPanel.add(new JLabel("Password:"));
            loginPanel.add(loginPassword);
            loginPanel.add(new JLabel());
            loginPanel.add(loginButton);

            JPanel registerPanel = new JPanel();
            registerPanel.setLayout(new GridLayout(4, 2));
            JTextField registerUsername = new JTextField();
            JPasswordField registerPassword = new JPasswordField();
            JTextField registerEmail = new JTextField();
            JButton registerButton = new JButton("Register");

            registerPanel.add(new JLabel("Username:"));
            registerPanel.add(registerUsername);
            registerPanel.add(new JLabel("Email:"));
            registerPanel.add(registerEmail);
            registerPanel.add(new JLabel("Password:"));
            registerPanel.add(registerPassword);
            registerPanel.add(new JLabel());
            registerPanel.add(registerButton);

            tabbedPane.add("Login", loginPanel);
            tabbedPane.add("Register", registerPanel);

            frame.add(tabbedPane);
            frame.setVisible(true);

            loginButton.addActionListener(e -> {
                String username = loginUsername.getText();
                String password = new String(loginPassword.getPassword());

                for (User user : users) {
                    if (user.getUsername().equals(username) && user.checkPassword(password)) {
                        loggedInUser = user;
                        JOptionPane.showMessageDialog(frame, "Logged in successfully.");
                        showBloodBankPanel(tabbedPane, loginPanel, registerPanel);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "Invalid username or password. Please try again.");
            });

            registerButton.addActionListener(e -> {
                String username = registerUsername.getText();
                String password = new String(registerPassword.getPassword());
                String email = registerEmail.getText();

                users.add(new User(username, password, email));
                JOptionPane.showMessageDialog(frame, "User registered successfully.");
            });
        });
    }

    private static void showBloodBankPanel(JTabbedPane tabbedPane, JPanel loginPanel, JPanel registerPanel) {
        JPanel bloodBankPanel = new JPanel();
        bloodBankPanel.setLayout(new GridLayout(5, 2));
        JButton addDonorButton = new JButton("Add Donor");
        JButton searchDonorButton = new JButton("Search Donor");
        JButton displayAllDonorsButton = new JButton("Display All Donors");
        JButton logoutButton = new JButton("Logout");

        bloodBankPanel.add(new JLabel());
        bloodBankPanel.add(addDonorButton);
        bloodBankPanel.add(searchDonorButton);
        bloodBankPanel.add(displayAllDonorsButton);
        bloodBankPanel.add(logoutButton);

        tabbedPane.add("Blood Bank", bloodBankPanel);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, new CloseButtonTabComponent(tabbedPane));

        tabbedPane.remove(loginPanel);
        tabbedPane.remove(registerPanel);

        addDonorButton.addActionListener(e -> {
            JTextField donorName = new JTextField();
            JTextField donorBloodType = new JTextField();
            JTextField donorContact = new JTextField();

            JPanel donorPanel = new JPanel(new GridLayout(3, 2));
            donorPanel.add(new JLabel("Name:"));
            donorPanel.add(donorName);
            donorPanel.add(new JLabel("Blood Type:"));
            donorPanel.add(donorBloodType);
            donorPanel.add(new JLabel("Contact:"));
            donorPanel.add(donorContact);

            int result = JOptionPane.showConfirmDialog(null, donorPanel, 
                     "Please Enter Donor Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = donorName.getText();
                String bloodType = donorBloodType.getText();
                String contact = donorContact.getText();
                Donor donor = new Donor(name, bloodType, contact);
                bank.addDonor(donor);
                JOptionPane.showMessageDialog(null, "Donor added successfully.");
            }
        });

        searchDonorButton.addActionListener(e -> {
            String bloodType = JOptionPane.showInputDialog("Enter blood type to search:");
            List<Donor> matchingDonors = bank.searchDonor(bloodType);
            if (matchingDonors.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No donors found with matching blood type.");
            } else {
                StringBuilder donorsInfo = new StringBuilder();
                for (Donor donor : matchingDonors) {
                    donorsInfo.append(donor.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(null, donorsInfo.toString());
            }
        });

        displayAllDonorsButton.addActionListener(e -> {
            List<Donor> allDonors = bank.getAllDonors();
            if (allDonors.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No donors found.");
            } else {
                StringBuilder donorsInfo = new StringBuilder();
                for (Donor donor : allDonors) {
                    donorsInfo.append(donor.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(null, donorsInfo.toString());
            }
        });

        logoutButton.addActionListener(e -> {
            loggedInUser = null;
            JOptionPane.showMessageDialog(null, "Logged out successfully.");
            tabbedPane.remove(bloodBankPanel);
            tabbedPane.add("Login", loginPanel);
            tabbedPane.add("Register", registerPanel);
        });
    }
}

class CloseButtonTabComponent extends JPanel {
    public CloseButtonTabComponent(JTabbedPane tabbedPane) {
        setOpaque(false);
        JLabel label = new JLabel(tabbedPane.getTitleAt(tabbedPane.getTabCount() - 1));
        add(label);
        JButton closeButton = new JButton("x");
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.addActionListener(e -> tabbedPane.removeTabAt(tabbedPane.indexOfTabComponent(this)));
        add(closeButton);
    }
}


