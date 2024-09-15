package librarysystem.components;

import business.Address;
import business.Author;
import librarysystem.PopupCallback;
import librarysystem.Util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class AuthorDialog extends JDialog {
    private JPanel mainPanel;
    private JPanel middlePanel;
    private JButton okButton;
    private JButton cancelButton;
    private PopupCallback callback;

    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTextField telephoneTextField;
    private JTextArea bioTextArea;
    private JTextField streetTextField;
    private JTextField cityTextField;
    private JTextField stateTextField;
    private JTextField zipTextField;

    // Create the red border for invalid input
    private final Border redBorder = new LineBorder(Color.RED, 1);
    // Create the default border to reset later
    private Border defaultBorder;

    public AuthorDialog(JFrame parent, PopupCallback callback) {
        super(parent, "Author Popup", true);
        this.callback = callback;
        setSize(new Dimension(400, 450));
        setLocationRelativeTo(parent);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        defineMiddlePanel();
        mainPanel.add(middlePanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setModal(true);
        getRootPane().setDefaultButton(okButton);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        mainPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void defineMiddlePanel() {
        middlePanel = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.LEFT, 20, 8);
        middlePanel.setLayout(fl);
        middlePanel.setPreferredSize(new Dimension(360, 420));

        // textboxes
        JLabel firstNameLabel = new JLabel("First Name");
        firstNameLabel.setPreferredSize(new Dimension(80, 30));
        firstNameTextField = new JTextField();
        firstNameTextField.setPreferredSize(new Dimension(250, 30));
        middlePanel.add(firstNameLabel);
        middlePanel.add(firstNameTextField);

        defaultBorder = firstNameTextField.getBorder();

        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setPreferredSize(new Dimension(80, 30));
        lastNameTextField = new JTextField();
        lastNameTextField.setPreferredSize(new Dimension(250, 30));
        middlePanel.add(lastNameLabel);
        middlePanel.add(lastNameTextField);

        JLabel telephoneLabel = new JLabel("Telephone");
        telephoneLabel.setPreferredSize(new Dimension(80, 30));
        telephoneTextField = new JTextField();
        telephoneTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                // Allow only digits and minus sign
                if (!Character.isDigit(ch) && ch != '-') {
                    e.consume();  // Ignore invalid input
                }
            }
        });
        telephoneTextField.setPreferredSize(new Dimension(250, 30));
        middlePanel.add(telephoneLabel);
        middlePanel.add(telephoneTextField);

        JLabel bioLabel = new JLabel("Short bio");
        bioLabel.setPreferredSize(new Dimension(80, 30));
        bioTextArea = new JTextArea();
//        bioTextArea.setPreferredSize(new Dimension(250, 50));
        bioTextArea.setRows(5); // Set the number of rows
        bioTextArea.setColumns(25); // Set the number of columns
        bioTextArea.setLineWrap(true); // Enable line wrapping
        bioTextArea.setWrapStyleWord(true); // Wrap at word boundaries
        middlePanel.add(bioLabel);
        middlePanel.add(bioTextArea);

        // Create a horizontal JSeparator (the default is horizontal)
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setPreferredSize(new Dimension(360, 1)); // Set preferred size (width x height)
        middlePanel.add(separator);

        JLabel streetLabel = new JLabel("Street");
        streetLabel.setPreferredSize(new Dimension(80, 30));
        streetTextField = new JTextField();
        streetTextField.setPreferredSize(new Dimension(250, 30));
        middlePanel.add(streetLabel);
        middlePanel.add(streetTextField);

        JLabel cityLabel = new JLabel("City");
        cityLabel.setPreferredSize(new Dimension(80, 30));
        cityTextField = new JTextField();
        cityTextField.setPreferredSize(new Dimension(250, 30));
        middlePanel.add(cityLabel);
        middlePanel.add(cityTextField);

        JLabel stateLabel = new JLabel("State");
        stateLabel.setPreferredSize(new Dimension(80, 30));
        stateTextField = new JTextField();
        stateTextField.setPreferredSize(new Dimension(250, 30));
        middlePanel.add(stateLabel);
        middlePanel.add(stateTextField);

        JLabel zipLabel = new JLabel("Zip");
        zipLabel.setPreferredSize(new Dimension(80, 30));
        zipTextField = new JTextField();
        zipTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                // Allow only digits
                if (!Character.isDigit(ch)) {
                    e.consume();  // Ignore invalid input
                }
            }
        });
        zipTextField.setPreferredSize(new Dimension(250, 30));
        middlePanel.add(zipLabel);
        middlePanel.add(zipTextField);

        // buttons
        okButton = new JButton("OK");
        okButton.addActionListener(e -> onOK());
        middlePanel.add(okButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> onCancel());
        middlePanel.add(cancelButton);
    }

    private boolean validateForm() {
        boolean isValid = true;

        // validate firstName field is required
        String inputFirstName = firstNameTextField.getText().trim();
        if (inputFirstName.isEmpty()) {
            firstNameTextField.setBorder(redBorder);
            isValid = false;
        } else {
            firstNameTextField.setBorder(defaultBorder);
        }

        // validate lastName field is required
        String inputLastName = lastNameTextField.getText().trim();
        if (inputLastName.isEmpty()) {
            lastNameTextField.setBorder(redBorder);
            isValid = false;
        } else {
            lastNameTextField.setBorder(defaultBorder);
        }

        // validate telephone field is required
        String inputTelephone = telephoneTextField.getText().trim();
        if (inputTelephone.isEmpty() || !Util.isValidTelephone(inputTelephone)) {
            telephoneTextField.setBorder(redBorder);
            isValid = false;
        } else {
            telephoneTextField.setBorder(defaultBorder);
        }

        // validate bio field

        // validate street field is required
        String inputStreet = streetTextField.getText().trim();
        if (inputStreet.isEmpty()) {
            streetTextField.setBorder(redBorder);
            isValid = false;
        } else {
            streetTextField.setBorder(defaultBorder);
        }

        // validate city field is required
        String inputCity = cityTextField.getText().trim();
        if (inputCity.isEmpty()) {
            cityTextField.setBorder(redBorder);
            isValid = false;
        } else {
            cityTextField.setBorder(defaultBorder);
        }

        // validate state field is required
        String inputState = stateTextField.getText().trim();
        if (inputState.isEmpty()) {
            stateTextField.setBorder(redBorder);
            isValid = false;
        } else {
            stateTextField.setBorder(defaultBorder);
        }

        // validate zip field is required
        String inputZip = zipTextField.getText().trim();
        if (inputZip.isEmpty() || !Util.isNumeric(inputZip)) {
            zipTextField.setBorder(redBorder);
            isValid = false;
        } else {
            zipTextField.setBorder(defaultBorder);
        }

        return isValid;
    }

    private void onOK() {
        // implement here
        boolean isValid = validateForm();
        if (!isValid) return;

        if (callback != null) {
            String inputFirstName = firstNameTextField.getText().trim();
            String inputLastName = lastNameTextField.getText().trim();
            String inputTelephone = telephoneTextField.getText().trim();
            String inputBio = bioTextArea.getText().trim();
            String inputStreet = streetTextField.getText().trim();
            String inputCity = cityTextField.getText().trim();
            String inputState = stateTextField.getText().trim();
            String inputZip = zipTextField.getText().trim();

            var address = new Address(inputStreet, inputCity, inputState, inputZip);
            var author = new Author(inputFirstName, inputLastName, inputTelephone, address, inputBio);

            callback.onDataReceived(author);
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
