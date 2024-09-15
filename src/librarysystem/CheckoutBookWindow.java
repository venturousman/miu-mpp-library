package librarysystem;

import business.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CheckoutBookWindow extends JFrame implements LibWindow {

    public static final CheckoutBookWindow INSTANCE = new CheckoutBookWindow();
    ControllerInterface ci = new SystemController();
    private boolean isInitialized = false;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel middlePanel;

    private JTextField memberIdTextField;
    private JTextField bookISBNTextField;

    private JTable checkoutTable;

    private JButton checkoutButton;

    // Create the red border for invalid input
    private final Border redBorder = new LineBorder(Color.RED, 1);
    // Create the default border to reset later
    private Border defaultBorder;
    private final String[] columnNames = {
            "Checkout Date", "Due Date", "Member Id", "Member Name", "Copy No", "Book ISBN", "Book Title",
    };

    private CheckoutBookWindow() {
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
        leftPanel.setPreferredSize(new Dimension(280, 600));

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Define the desired date format (MM-DD-YYYY)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        // Format the current date
        String formattedDate = currentDate.format(formatter);
        JLabel nowLabel = new JLabel("Today is " + formattedDate);
        leftPanel.add(nowLabel);

        // Create a horizontal JSeparator (the default is horizontal)
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setPreferredSize(new Dimension(250, 1)); // Set preferred size (width x height)
        leftPanel.add(separator);

        // textboxes
        JLabel memberIdLabel = new JLabel("Member ID");
        memberIdTextField = new JTextField();
        memberIdTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(memberIdLabel);
        leftPanel.add(memberIdTextField);

        JLabel isbnLabel = new JLabel("Book ISBN");
        bookISBNTextField = new JTextField();
        bookISBNTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                // Allow only digits and minus sign
                if (!Character.isDigit(ch) && ch != '-') {
                    e.consume();  // Ignore invalid input
                }
            }
        });
        bookISBNTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(isbnLabel);
        leftPanel.add(bookISBNTextField);

        // buttons
        checkoutButton = new JButton("Add");
        registerCheckoutButtonListener(checkoutButton);
        leftPanel.add(checkoutButton);

        JButton backButton = new JButton("Back");
        registerBackButtonListener(backButton);
        leftPanel.add(backButton);
    }

    private void defineMiddlePanel() {
        middlePanel = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 0, 8);
        middlePanel.setLayout(fl);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(880, 540));
        middlePanel.add(scrollPane);

        checkoutTable = new JTable();
//        checkoutTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                int selectedRow = checkoutTable.getSelectedRow();
//                if (selectedRow >= 0) {
////                    String selectedISBN = checkoutTable.getValueAt(selectedRow, 0).toString();
////                    String selectedTitle = checkoutTable.getValueAt(selectedRow, 1).toString();
////                    String selectedCheckoutLength = checkoutTable.getValueAt(selectedRow, 2).toString();
////                    bookISBNTextField.setText(selectedISBN);
////                    bookTitleTextField.setText(selectedTitle);
////                    checkoutLengthTextField.setText(selectedCheckoutLength);
//                }
//            }
//        });
        checkoutTable.setBackground(new Color(255, 255, 255));
//        booksTable.setPreferredSize(new Dimension(900, 600));
        scrollPane.setViewportView(checkoutTable);

        // load data
        loadDataToTable();
    }

    private void resetForm() {
        memberIdTextField.setText("");
        memberIdTextField.setBorder(defaultBorder);
        bookISBNTextField.setText("");
        bookISBNTextField.setBorder(defaultBorder);
        checkoutTable.clearSelection();
    }

    private boolean validateForm() {
        boolean isValid = true;

        // validate member ID is required
        String inputMemberID = memberIdTextField.getText().trim();
        if (inputMemberID.isEmpty()) {
            memberIdTextField.setBorder(redBorder);
            isValid = false;
        } else {
            var memberIds = ci.allMemberIds();
            boolean isExisted = memberIds.contains(inputMemberID);
            if (!isExisted) {
                memberIdTextField.setBorder(redBorder);
                isValid = false;
                JOptionPane.showMessageDialog(null, "This member is not found!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                memberIdTextField.setBorder(defaultBorder);
            }
        }

        // validate isbn field is required
        String inputISBN = bookISBNTextField.getText().trim();
        if (inputISBN.isEmpty() || !Util.isValidISBN(inputISBN)) {
            bookISBNTextField.setBorder(redBorder);
            isValid = false;
        } else {
            var bookIds = ci.allBookIds();
            boolean isExisted = bookIds.contains(inputISBN);
            if (!isExisted) {
                bookISBNTextField.setBorder(redBorder);
                isValid = false;
                JOptionPane.showMessageDialog(null, "This isbn is not found!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // check if any copy is available
                var book = ci.getBookById(inputISBN);
                if (!book.isAvailable()) {
                    bookISBNTextField.setBorder(redBorder);
                    isValid = false;
                    JOptionPane.showMessageDialog(null, "This book is not available!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    bookISBNTextField.setBorder(defaultBorder);
                }
            }
        }

        return isValid;
    }

    private void registerCheckoutButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            boolean isValid = validateForm();
            if (isValid) {
                // save checkout
                String inputMemberID = memberIdTextField.getText().trim();
                String inputISBN = bookISBNTextField.getText().trim();
                ci.saveNewCheckout(inputISBN, inputMemberID);
                JOptionPane.showMessageDialog(null, "Checked-out Successfully");
                // reload / re-render
                loadDataToTable();
                resetForm();
            }
        });
    }

    private void loadDataToTable() {
        var checkouts = ci.allCheckouts();
//        System.out.println(checkouts);

        // sum entries
        int sum = 0;
        for (Checkout checkout : checkouts) {
            sum += checkout.getNumEntries();
        }

        Object[][] items = new Object[sum][];

        int i = 0;
        for (Checkout checkout : checkouts) {
            var entries = checkout.getEntries();
            if (entries != null) {
                var member = checkout.getMember();
                for (CheckoutEntry entry : checkout.getEntries()) {
                    var bookCopy = entry.getBookCopy();
                    var book = bookCopy.getBook();
                    Object[] item = new Object[]{
                            entry.getCheckoutDate(),
                            entry.getDueDate(),
                            member.getMemberId(),
                            member.getFullName(),
                            bookCopy.getCopyNum(),
                            book.getIsbn(),
                            book.getTitle(),
                    };
                    items[i++] = item;
                }
            }
        }

        // Creating a DefaultTableModel with isCellEditable() overridden to return false
        DefaultTableModel tableModel = new DefaultTableModel(items, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // All cells are not editable
                return false;
            }
        };

        checkoutTable.setModel(tableModel);
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
        loadDataToTable();
    }
}
