package librarysystem;

import business.Book;
import business.BookCopy;
import business.ControllerInterface;
import business.SystemController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NewBookCopyWindow extends JFrame implements LibWindow {

    public static final NewBookCopyWindow INSTANCE = new NewBookCopyWindow();
    ControllerInterface ci = new SystemController();
    private boolean isInitialized = false;
    private JPanel mainPanel;
    //    private JPanel topPanel;
    private JPanel middlePanel;

    private JTextField bookISBNTextField;
    private JLabel foundBookLabel;
    private JButton addBookCopyButton, clearFormButton;
    private JTable bookCopyTable;

    // Create the red border for invalid input
    private final Border redBorder = new LineBorder(Color.RED, 1);
    // Create the default border to reset later
    private Border defaultBorder;
    private final String[] columnNames = {
            "Copy No", "ISBN", "Book Title", "Available"
    };
    private final String defaultText = "Please enter a book to add a copy!";

    private NewBookCopyWindow() {
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void init() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

//        defineTopPanel();
        defineMiddlePanel();
//        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(middlePanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        isInitialized = true;
    }

    private void defineMiddlePanel() {
        middlePanel = new JPanel();
        middlePanel.setPreferredSize(new Dimension(600, 500));
        FlowLayout fl = new FlowLayout(FlowLayout.LEFT, 16, 8);
        middlePanel.setLayout(fl);

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
        middlePanel.add(isbnLabel);
        middlePanel.add(bookISBNTextField);

        // buttons
        addBookCopyButton = new JButton("Add");
        registerAddButtonListener(addBookCopyButton);
        middlePanel.add(addBookCopyButton);

        clearFormButton = new JButton("Clear");
        registerClearButtonListener(clearFormButton);
        middlePanel.add(clearFormButton);

        JButton backButton = new JButton("Back");
        registerBackButtonListener(backButton);
        middlePanel.add(backButton);

        // label
        foundBookLabel = new JLabel(defaultText);
        middlePanel.add(foundBookLabel);

        // table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(550, 350));
        middlePanel.add(scrollPane);

        bookCopyTable = new JTable();
        bookCopyTable.setBackground(new Color(255, 255, 255));
        // Disable row and column selection
        bookCopyTable.setRowSelectionAllowed(false);
        bookCopyTable.setColumnSelectionAllowed(false);
//        bookCopyTable.setPreferredSize(new Dimension(900, 600));
        scrollPane.setViewportView(bookCopyTable);

        // load data
        loadDataToTable();
    }

    private void loadDataToTable() {
        // load data
        var books = ci.allBooks();
//        System.out.println(books);

        // sum copies
        int sum = 0;
        for (Book book : books) {
            sum += book.getNumCopies();
        }

        Object[][] items = new Object[sum][];

        int i = 0;
        for (Book book : books) {
            var copies = book.getCopies();
            for (BookCopy copy : copies) {
                Object[] item = new Object[]{
                        copy.getCopyNum(),
                        book.getIsbn(),
                        book.getTitle(),
                        copy.isAvailable(),
                };
                items[i++] = item;
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

        bookCopyTable.setModel(tableModel);
    }

    private void resetForm() {
        bookISBNTextField.setText("");
        bookISBNTextField.setBorder(defaultBorder);
        foundBookLabel.setText(defaultText);
    }

    private boolean validateForm() {
        boolean isValid = true;

        // validate isbn field is required
        String inputISBN = bookISBNTextField.getText().trim();
        if (inputISBN.isEmpty() || !Util.isValidISBN(inputISBN)) {
            bookISBNTextField.setBorder(redBorder);
            isValid = false;
        } else {
            var book = ci.getBookById(inputISBN);
            boolean isExisted = book != null;
            if (!isExisted) {
                bookISBNTextField.setBorder(redBorder);
                isValid = false;
                JOptionPane.showMessageDialog(null, "This book is not found!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                bookISBNTextField.setBorder(defaultBorder);
                String displayText = book.getIsbn() + ", " + book.getTitle();
                foundBookLabel.setText(displayText);
            }
        }

        return isValid;
    }

    private void registerAddButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            boolean isValid = validateForm();
            if (isValid) {
                // save book copy
                String inputISBN = bookISBNTextField.getText().trim();
                ci.saveNewCopy(inputISBN);
                JOptionPane.showMessageDialog(null, "Added Successfully");
                // reload / re-render
                loadDataToTable();
//                resetForm();
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
        loadDataToTable();
    }
}
