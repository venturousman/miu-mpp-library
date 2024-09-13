package librarysystem;

import business.ControllerInterface;
import business.LoginException;
import business.SystemController;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class LoginWindow extends JFrame implements LibWindow {
    public static final LoginWindow INSTANCE = new LoginWindow();
    ControllerInterface ci = new SystemController();
    private boolean isInitialized = false;

    private JPanel mainPanel;
    private JPanel upperPanel;
    private JPanel middlePanel;

    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton loginButton, backButton;
//    private JButton logoutButton;

    // Create the red border for invalid input
    private final Border redBorder = new LineBorder(Color.RED, 1);
    // Create the default border to reset later
    private Border defaultBorder;

    public boolean isInitialized() {
        return isInitialized;
    }

    public void isInitialized(boolean val) {
        isInitialized = val;
    }

    /* This class is a singleton */
    private LoginWindow() {
    }

    public void init() {
        mainPanel = new JPanel();
        BorderLayout bl = new BorderLayout();
        bl.setVgap(30);
        mainPanel.setLayout(bl);

        defineUpperPanel();
        defineMiddlePanel();

        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(middlePanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        isInitialized(true);
        pack();
        //setSize(660, 500);
    }

    private void defineUpperPanel() {
        upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 8));

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        upperPanel.add(loginLabel);
    }

    private void defineMiddlePanel() {
        middlePanel = new JPanel();
        middlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 8));

        JLabel usernameLabel = new JLabel("Username");
        usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(new Dimension(250, 30));
        middlePanel.add(usernameLabel);
        middlePanel.add(usernameTextField);

        defaultBorder = usernameTextField.getBorder();

        JLabel passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 30));
        middlePanel.add(passwordLabel);
        middlePanel.add(passwordField);

        loginButton = new JButton("Login");
        registerLoginButtonListener(loginButton);
        middlePanel.add(loginButton);

        backButton = new JButton("Back");
        registerBackButtonListener(backButton);
        middlePanel.add(backButton);
    }

    private boolean validateForm() {
        boolean isValid = true;
        // validate username field is required
        String inputUsername = usernameTextField.getText().trim();
        if (inputUsername.isEmpty()) {
            usernameTextField.setBorder(redBorder);
            isValid = false;
        } else {
            usernameTextField.setBorder(defaultBorder);
        }

        // validate password field is required
        char[] password = passwordField.getPassword();
        // Convert the password to a String (optional)
        String inputPassword = new String(password);
        if (inputPassword.isEmpty()) {
            passwordField.setBorder(redBorder);
            isValid = false;
        } else {
            passwordField.setBorder(defaultBorder);
        }

        return isValid;
    }

    private void resetForm() {
        usernameTextField.setText("");
        usernameTextField.setBorder(defaultBorder);
        passwordField.setText("");
        passwordField.setBorder(defaultBorder);
    }

    private void registerBackButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            this.resetForm();
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
            LibrarySystem.INSTANCE.toggleMenus();
        });
    }

    private void registerLoginButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            boolean isValid = validateForm();
            if (isValid) {
                String username = usernameTextField.getText().trim();

                // validate password field is required
                char[] inputPassword = passwordField.getPassword();
                // Convert the password to a String (optional)
                String password = new String(inputPassword);

                try {
                    ci.login(username, password);
                    JOptionPane.showMessageDialog(this, "Successful Login");
//                    resetForm();
                    backButton.doClick();
                } catch (LoginException e) {
//                    throw new RuntimeException(e);
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    @Override
    public void reloadData() {
        // empty
    }
}
