package librarysystem;

import business.Book;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class NewMemberWindow extends JFrame implements LibWindow {

    public static final NewMemberWindow INSTANCE = new NewMemberWindow();
    ControllerInterface ci = new SystemController();
    private boolean isInitialized = false;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel middlePanel;

    private JTable membersTable;

    private JTextField memberIdTextField;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTextField telephoneTextField;
    private JTextField streetTextField;
    private JTextField cityTextField;
    private JTextField stateTextField;
    private JTextField zipTextField;

    private JButton addMemberButton, updateMemberButton, deleteMemberButton, clearFormButton;

    // Create the red border for invalid input
    private final Border redBorder = new LineBorder(Color.RED, 1);
    // Create the default border to reset later
    private Border defaultBorder;
    private final String[] columnNames = {
            "Id", "First Name", "Last Name", "Telephone", "Address",
    };

    private NewMemberWindow() {
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void init() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        defineLeftPanel();
        defineMiddlePanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(middlePanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        isInitialized = true;
    }

    private void defineLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 8));
        leftPanel.setPreferredSize(new Dimension(300, 600));

        // textboxes
        JLabel memberIdLabel = new JLabel("Member Id");
        memberIdTextField = new JTextField();
        memberIdTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                // Allow only digits
                if (!Character.isDigit(ch)) {
                    e.consume();  // Ignore invalid input
                }
            }
        });
        memberIdTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(memberIdLabel);
        leftPanel.add(memberIdTextField);

        defaultBorder = memberIdTextField.getBorder();

        JLabel firstNameLabel = new JLabel("First Name");
        firstNameTextField = new JTextField();
        firstNameTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(firstNameLabel);
        leftPanel.add(firstNameTextField);

        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameTextField = new JTextField();
        lastNameTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(lastNameLabel);
        leftPanel.add(lastNameTextField);

        JLabel telephoneLabel = new JLabel("Telephone");
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
        leftPanel.add(telephoneLabel);
        leftPanel.add(telephoneTextField);

        // Create a horizontal JSeparator (the default is horizontal)
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setPreferredSize(new Dimension(250, 1)); // Set preferred size (width x height)
        leftPanel.add(separator);

        JLabel streetLabel = new JLabel("Street");
        streetTextField = new JTextField();
        streetTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(streetLabel);
        leftPanel.add(streetTextField);

        JLabel cityLabel = new JLabel("City");
        cityTextField = new JTextField();
        cityTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(cityLabel);
        leftPanel.add(cityTextField);

        JLabel stateLabel = new JLabel("State");
        stateTextField = new JTextField();
        stateTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(stateLabel);
        leftPanel.add(stateTextField);

        JLabel zipLabel = new JLabel("Zip");
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
        leftPanel.add(zipLabel);
        leftPanel.add(zipTextField);

        // buttons
        addMemberButton = new JButton("Add");
        registerAddButtonListener(addMemberButton);
        leftPanel.add(addMemberButton);

        updateMemberButton = new JButton("Update");
        registerUpdateButtonListener(updateMemberButton);
        leftPanel.add(updateMemberButton);

        deleteMemberButton = new JButton("Delete");
        registerDeleteButtonListener(deleteMemberButton);
        leftPanel.add(deleteMemberButton);

        clearFormButton = new JButton("Clear");
        registerClearButtonListener(clearFormButton);
        leftPanel.add(clearFormButton);

        JButton backButton = new JButton("Back");
        registerBackButtonListener(backButton);
        leftPanel.add(backButton);
    }

    private void defineMiddlePanel() {
        middlePanel = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 0, 8);
        middlePanel.setLayout(fl);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(850, 570));
        middlePanel.add(scrollPane);

        membersTable = new JTable();
        membersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = membersTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String selectedMemberId = membersTable.getValueAt(selectedRow, 0).toString();
                    String selectedFirstName = membersTable.getValueAt(selectedRow, 1).toString();
                    String selectedLastName = membersTable.getValueAt(selectedRow, 2).toString();
                    String selectedTelephone = membersTable.getValueAt(selectedRow, 3).toString();

                    memberIdTextField.setText(selectedMemberId);
                    firstNameTextField.setText(selectedFirstName);
                    lastNameTextField.setText(selectedLastName);
                    telephoneTextField.setText(selectedTelephone);
                    // map address
                    var member = ci.getMemberById(selectedMemberId);
                    var address = member.getAddress();
                    streetTextField.setText(address.getStreet());
                    cityTextField.setText(address.getCity());
                    stateTextField.setText(address.getState());
                    zipTextField.setText(address.getZip());
                }
            }
        });
        membersTable.setBackground(new Color(255, 255, 255));
//        membersTable.setPreferredSize(new Dimension(900, 600));
        scrollPane.setViewportView(membersTable);

        // load data
        loadMembersToTable();
    }

    private void loadMembersToTable() {
        // load data
        var members = ci.allMembers();
//        System.out.println(members);

        Object[][] items = new Object[members.size()][];

        int i = 0;
        for (LibraryMember member : members) {
            Object[] item = new Object[]{
                    member.getMemberId(),
                    member.getFirstName(),
                    member.getLastName(),
                    member.getTelephone(),
                    member.getAddress().toString()
            };
            items[i++] = item;
        }

        // Creating a DefaultTableModel with isCellEditable() overridden to return false
        DefaultTableModel tableModel = new DefaultTableModel(items, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // All cells are not editable
                return false;
            }
        };

        membersTable.setModel(tableModel);
    }

    private boolean validateForm(String selectedMemberId) {
        boolean isValid = true;

        boolean isAddingNew = selectedMemberId == null || selectedMemberId.isEmpty();

        // validate member id field is required
        String inputMemberId = memberIdTextField.getText().trim();
        if (inputMemberId.isEmpty()) {
            memberIdTextField.setBorder(redBorder);
            isValid = false;
        } else {
            var memberIds = ci.allMemberIds();
            boolean isExisted = memberIds.contains(inputMemberId);
            boolean updatingCheck = !isAddingNew && !selectedMemberId.equalsIgnoreCase(inputMemberId) && isExisted;
            boolean addingCheck = isAddingNew && isExisted;
            if (addingCheck || updatingCheck) {
                memberIdTextField.setBorder(redBorder);
                isValid = false;
                JOptionPane.showMessageDialog(null, "This member id already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                memberIdTextField.setBorder(defaultBorder);
            }
        }

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

    private void resetForm() {
        memberIdTextField.setText("");
        memberIdTextField.setBorder(defaultBorder);
        firstNameTextField.setText("");
        firstNameTextField.setBorder(defaultBorder);
        lastNameTextField.setText("");
        lastNameTextField.setBorder(defaultBorder);
        telephoneTextField.setText("");
        telephoneTextField.setBorder(defaultBorder);
        streetTextField.setText("");
        streetTextField.setBorder(defaultBorder);
        cityTextField.setText("");
        cityTextField.setBorder(defaultBorder);
        stateTextField.setText("");
        stateTextField.setBorder(defaultBorder);
        zipTextField.setText("");
        zipTextField.setBorder(defaultBorder);
        membersTable.clearSelection();
    }

    private void registerAddButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            boolean isValid = validateForm(null);
            if (isValid) {
                // save member
                String inputMemberId = memberIdTextField.getText().trim();
                String inputFirstName = firstNameTextField.getText().trim();
                String inputLastName = lastNameTextField.getText().trim();
                String inputTelephone = telephoneTextField.getText().trim();
                String inputStreet = streetTextField.getText().trim();
                String inputCity = cityTextField.getText().trim();
                String inputState = stateTextField.getText().trim();
                String inputZip = zipTextField.getText().trim();

                ci.saveNewMember(inputMemberId, inputFirstName, inputLastName, inputTelephone, inputStreet, inputCity, inputState, inputZip);

                JOptionPane.showMessageDialog(null, "Added Successfully");
                // reload / re-render
                loadMembersToTable();
                resetForm();
            }
        });
    }

    private void registerDeleteButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            int selectedRow = membersTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(null, "Please select a row!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isValid = true;

            String selectedMemberId = membersTable.getValueAt(selectedRow, 0).toString();
            var memberIds = ci.allMemberIds();
            boolean isExisted = memberIds.contains(selectedMemberId);
            if (selectedMemberId.isEmpty() || !isExisted) {
                isValid = false;
            }

            if (isValid) {
                ci.deleteMember(selectedMemberId);
                JOptionPane.showMessageDialog(null, "Deleted Successfully");
                // reload / re-render
                loadMembersToTable();
                resetForm();
            }
        });
    }

    private void registerUpdateButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            int selectedRow = membersTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(null, "Please select a row!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedMemberId = membersTable.getValueAt(selectedRow, 0).toString();
            var memberIds = ci.allMemberIds();
            boolean isExisted = memberIds.contains(selectedMemberId);
            if (selectedMemberId.isEmpty() || !isExisted) {
                return;
            }

            boolean isValid = validateForm(selectedMemberId);

            if (isValid) {
                String updatedMemberId = memberIdTextField.getText().trim();
                String updatedFirstName = firstNameTextField.getText().trim();
                String updatedLastName = lastNameTextField.getText().trim();
                String updatedTelephone = telephoneTextField.getText().trim();
                String updatedStreet = streetTextField.getText().trim();
                String updatedCity = cityTextField.getText().trim();
                String updatedState = stateTextField.getText().trim();
                String updatedZip = zipTextField.getText().trim();
                ci.updateMember(selectedMemberId, updatedMemberId, updatedFirstName, updatedLastName, updatedTelephone, updatedStreet, updatedCity, updatedState, updatedZip);
                JOptionPane.showMessageDialog(null, "Updated Successfully");
                // reload / re-render
                loadMembersToTable();
                resetForm();
            }
        });
    }

    private void registerClearButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            this.resetForm();
        });
    }

    private void registerBackButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            this.resetForm();
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

    @Override
    public void reloadData() {
        loadMembersToTable();
    }
}
