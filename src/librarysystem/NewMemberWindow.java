package librarysystem;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import business.Address;
import business.Author;
import business.Book;
import business.LibraryMember;
import dataaccess.DataAccessFacade;

public class NewMemberWindow extends JFrame implements LibWindow {

    public static final NewMemberWindow INSTANCE = new NewMemberWindow();
    private boolean isInitialized = false;
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel middlePanel;

    // Text fields for member data
    private JTextField memberIdField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField zipField;
    private JTextField telephoneField;

    private NewMemberWindow() { }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void init() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        defineTopPanel();
        defineMiddlePanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(middlePanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        isInitialized = true;
    }

    private void defineTopPanel() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titleLabel = new JLabel("New Member Form");
        Util.adjustLabelFont(titleLabel, Util.DARK_BLUE, true);
        topPanel.add(titleLabel);

        JButton backButton = new JButton("Back");
        addBackButtonListener(backButton);
        topPanel.add(backButton);
    }

    private void defineMiddlePanel() {
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(9, 2, 10, 10)); // 9 rows, 2 columns

        // Add labels and text fields for each field
        memberIdField = createInputRow("Member ID:");
        firstNameField = createInputRow("First Name:");
        lastNameField = createInputRow("Last Name:");
        streetField = createInputRow("Street:");
        cityField = createInputRow("City:");
        stateField = createInputRow("State:");
        zipField = createInputRow("ZIP Code:");
        telephoneField = createInputRow("Telephone:");

        // Add Submit button
        JButton submitButton = new JButton("Submit");
        addSubmitButtonListener(submitButton);
        middlePanel.add(submitButton);
    }

    private JTextField createInputRow(String label) {
        JLabel lbl = new JLabel(label);
        JTextField textField = new JTextField(15);
        middlePanel.add(lbl);
        middlePanel.add(textField);
        return textField;
    }

    private void addSubmitButtonListener(JButton submitButton) {
        submitButton.addActionListener(evt -> {
            String memberId = memberIdField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String street = streetField.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String zip = zipField.getText();
            String telephone = telephoneField.getText();


            librarysystem.Member member = new librarysystem.Member(memberId, firstName, lastName, street, city, state, zip, telephone);


            saveMemberData();

            JOptionPane.showMessageDialog(this, "Member added successfully!");
            clearFields();
        });
    }

    private void saveMemberData() {

        try {
            Address address = new Address(streetField.getText(), cityField.getText(), stateField.getText(), zipField.getText());

            LibraryMember libraryMember = new LibraryMember(memberIdField.getText(), firstNameField.getText(), lastNameField.getText(), telephoneField.getText(), address);
            DataAccessFacade da = new DataAccessFacade();
            da.saveNewMember(libraryMember);

        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, "Error saving member data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        memberIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        streetField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipField.setText("");
        telephoneField.setText("");
    }

    private void addBackButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
        });
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void isInitialized(boolean val) {
        isInitialized = val;
    }
}
