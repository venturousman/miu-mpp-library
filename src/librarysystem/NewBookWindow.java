package librarysystem;

import business.Author;
import business.Book;
import business.BookController;

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
import java.util.Collection;
import java.util.List;

public class NewBookWindow extends JFrame implements LibWindow {

    public static final NewBookWindow INSTANCE = new NewBookWindow();
    //    ControllerInterface ci = new SystemController();
    BookController bookController = new BookController();

    private boolean isInitialized = false;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel middlePanel;

    private JTable booksTable;
    private JTextField bookTitleTextField;
    private JTextField bookISBNTextField;
    private JTextField checkoutLengthTextField;

    private JButton addBookButton;
    private JButton updateBookButton;
    private JButton deleteBookButton;
    private JButton clearFormButton;

    // Create the red border for invalid input
    private final Border redBorder = new LineBorder(Color.RED, 1);
    // Create the default border to reset later
    private Border defaultBorder;
    private final String[] columnNames = {
            "ISBN", "Book Title", "Checkout Length", "Copies No.", "Author(s)"
    };

    private NewBookWindow() {
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void init() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
//        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        defineLeftPanel();
        defineMiddlePanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(middlePanel, BorderLayout.CENTER);
//        mainPanel.add(leftPanel);
//        mainPanel.add(middlePanel);

        getContentPane().add(mainPanel);
        isInitialized = true;
    }

    private void defineLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 8));
//        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(300, 600));

        // textboxes
        JLabel titleLabel = new JLabel("Book Title");
        bookTitleTextField = new JTextField();
        bookTitleTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(titleLabel);
        leftPanel.add(bookTitleTextField);

        defaultBorder = bookTitleTextField.getBorder();

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

        JLabel checkoutLengthLabel = new JLabel("Max Checkout Days");
        checkoutLengthTextField = new JTextField("21");
        // Add a KeyListener to restrict input to digits and a minus sign
        checkoutLengthTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if (!Character.isDigit(ch)) {
                    e.consume();  // Ignore invalid input
                }
            }
        });
        checkoutLengthTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(checkoutLengthLabel);
        leftPanel.add(checkoutLengthTextField);

        // buttons
        addBookButton = new JButton("Add");
        registerAddButtonListener(addBookButton);
        leftPanel.add(addBookButton);

        updateBookButton = new JButton("Update");
        registerUpdateButtonListener(updateBookButton);
        leftPanel.add(updateBookButton);

        deleteBookButton = new JButton("Delete");
        registerDeleteButtonListener(deleteBookButton);
        leftPanel.add(deleteBookButton);

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
        scrollPane.setPreferredSize(new Dimension(850, 540));
//        scrollPane.setBounds(6, 154, 582, 287);
        middlePanel.add(scrollPane);

        booksTable = new JTable();
        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = booksTable.getSelectedRow();
                String selectedISBN = booksTable.getValueAt(selectedRow, 0).toString();
                String selectedTitle = booksTable.getValueAt(selectedRow, 1).toString();
                String selectedCheckoutLength = booksTable.getValueAt(selectedRow, 2).toString();
                bookISBNTextField.setText(selectedISBN);
                bookTitleTextField.setText(selectedTitle);
                checkoutLengthTextField.setText(selectedCheckoutLength);
            }
        });
        booksTable.setBackground(new Color(255, 255, 255));
//        booksTable.setPreferredSize(new Dimension(900, 600));
        scrollPane.setViewportView(booksTable);

        // load data
        loadBooksToTable();
    }

    private void loadBooksToTable() {
        // load data
        Collection<Book> books = bookController.getBooks();
//        System.out.println(books);

        Object[][] items = new Object[books.size()][];

        int i = 0;
        for (Book book : books) {
            Object[] item = new Object[]{
                    book.getIsbn(),
                    book.getTitle(),
                    book.getMaxCheckoutLength(),
                    book.getNumCopies(),
                    book.getAuthorNames(),
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

        booksTable.setModel(tableModel);
    }

    private void registerBackButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
        });
    }

    private void resetForm() {
        bookISBNTextField.setText("");
        bookTitleTextField.setText("");
        checkoutLengthTextField.setText(""); // "21"
    }

    private void registerClearButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            this.resetForm();
        });
    }

    private void registerAddButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            boolean isValid = true;

            // validate title field is required
            String inputTitle = bookTitleTextField.getText().trim();
            if (inputTitle.isEmpty()) {
                bookTitleTextField.setBorder(redBorder);
                isValid = false;
            } else {
                bookTitleTextField.setBorder(defaultBorder);
            }

            // validate isbn field is required
            String inputISBN = bookISBNTextField.getText().trim();
            if (inputISBN.isEmpty() || !Util.isValidISBN(inputISBN)) {
                bookISBNTextField.setBorder(redBorder);
                isValid = false;
            } else {
//                bookISBNTextField.setBorder(defaultBorder);
                boolean isExisted = bookController.isExisted(inputISBN);
                if (isExisted) {
                    bookISBNTextField.setBorder(redBorder);
                    isValid = false;
                    JOptionPane.showMessageDialog(null, "This isbn already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    bookISBNTextField.setBorder(defaultBorder);
                }
            }

            // validate checkoutLength field is required and numeric
            String inputCheckoutLength = checkoutLengthTextField.getText().trim();
            int checkoutLength = 0;
            if (inputCheckoutLength.isEmpty() || !Util.isNumeric(inputCheckoutLength)) {
                checkoutLengthTextField.setBorder(redBorder);
                isValid = false;
            } else {
//                checkoutLengthTextField.setBorder(defaultBorder);
                // validate checkoutLength field is less than 21 days
                checkoutLength = Integer.parseInt(inputCheckoutLength);
                if (checkoutLength < 1 || checkoutLength > 21) {
                    checkoutLengthTextField.setBorder(redBorder);
                    isValid = false;
                } else {
                    checkoutLengthTextField.setBorder(defaultBorder);
                }
            }

            if (isValid) {
                // save book
                // TODO input authors
                List<Author> authors = new ArrayList<>();
                bookController.saveNewBook(inputISBN, inputTitle, checkoutLength, authors);
                JOptionPane.showMessageDialog(null, "Added Successfully");
                // reload / re-render
                loadBooksToTable();
            }
        });
    }

    private void registerDeleteButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            boolean isValid = true;
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow < 0) {
                isValid = false;
                JOptionPane.showMessageDialog(null, "Please select a row!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (isValid) {
                String selectedISBN = booksTable.getValueAt(selectedRow, 0).toString();
                if (selectedISBN.isEmpty()) {
                    return;
                }
                bookController.deleteBook(selectedISBN);
                JOptionPane.showMessageDialog(null, "Deleted Successfully");
                // reload / re-render
                loadBooksToTable();
                this.resetForm();
            }
        });
    }

    private void registerUpdateButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            boolean isValid = true;
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow < 0) {
                isValid = false;
                JOptionPane.showMessageDialog(null, "Please select a row!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (isValid) {
                JOptionPane.showMessageDialog(null, "Updated Successfully");
                // reload / re-render
//            loadBooksToTable();
            }
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
