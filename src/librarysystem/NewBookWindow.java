package librarysystem;

import business.*;
import librarysystem.components.AuthorDialog;
import librarysystem.components.ButtonEditor;
import librarysystem.components.ButtonRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class NewBookWindow extends JFrame implements LibWindow, PopupCallback {

    public static final NewBookWindow INSTANCE = new NewBookWindow();
    ControllerInterface ci = new SystemController();

    private boolean isInitialized = false;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel middlePanel;

    private JTable booksTable;
    private JTable authorsTable;
    private JTextField bookTitleTextField;
    private JTextField bookISBNTextField;
    private JTextField checkoutLengthTextField;

    private JButton addBookButton;
    private JButton updateBookButton;
    private JButton deleteBookButton;
    private JButton clearFormButton;
    private JButton addAuthorButton;

    // Create the red border for invalid input
    private final Border redBorder = new LineBorder(Color.RED, 1);
    // Create the default border to reset later
    private Border defaultBorder;
    private final String[] columnNames = {
            "ISBN", "Book Title", "Checkout Length", "Copies No.", "Author(s)"
    };
    private final String[] authorColumnNames = {
            "", "First Name", "Last Name", "Telephone", "Bio", "Street", "City", "State", "Zip",
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

        getContentPane().add(mainPanel);
        isInitialized = true;
    }

    private void defineLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 8));
//        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(360, 600));

        // textboxes
        JLabel titleLabel = new JLabel("Book Title");
        bookTitleTextField = new JTextField();
        bookTitleTextField.setPreferredSize(new Dimension(280, 30));
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
        bookISBNTextField.setPreferredSize(new Dimension(280, 30));
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
        checkoutLengthTextField.setPreferredSize(new Dimension(280, 30));
        leftPanel.add(checkoutLengthLabel);
        leftPanel.add(checkoutLengthTextField);

        // authors button and table
        JLabel authorLabel = new JLabel("Authors");
        leftPanel.add(authorLabel);
        addAuthorButton = new JButton("Add Author");
        registerAddAuthorButtonListener(addAuthorButton);
        leftPanel.add(addAuthorButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(330, 250));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        leftPanel.add(scrollPane);

        // Creating a DefaultTableModel with isCellEditable() overridden to return false
        DefaultTableModel tableModel = new DefaultTableModel(null, authorColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // All cells are not editable
//                return false;
                return column == 0;
            }
        };
        authorsTable = new JTable(tableModel);
        authorsTable.setBackground(new Color(255, 255, 255));
        authorsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disable auto-resizing
//        authorsTable.setPreferredSize(new Dimension(900, 600));
        authorsTable.setRowHeight(20); // Set the row height (e.g., 30 pixels)
        // Set custom renderer and editor for the column containing buttons
        TableColumn firstColumn = authorsTable.getColumnModel().getColumn(0);
        firstColumn.setCellRenderer(new ButtonRenderer());
        ActionListener deleteAction = e -> deleteAuthorRow();
        firstColumn.setCellEditor(new ButtonEditor(deleteAction));
        firstColumn.setPreferredWidth(18); // Set the width of the first column ("Name") to 150 pixels
        scrollPane.setViewportView(authorsTable);

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
        scrollPane.setPreferredSize(new Dimension(800, 540));
//        scrollPane.setBounds(6, 154, 582, 287);
        middlePanel.add(scrollPane);

        booksTable = new JTable();
        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = booksTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String selectedISBN = booksTable.getValueAt(selectedRow, 0).toString();
                    String selectedTitle = booksTable.getValueAt(selectedRow, 1).toString();
                    String selectedCheckoutLength = booksTable.getValueAt(selectedRow, 2).toString();
                    bookISBNTextField.setText(selectedISBN);
//                    bookISBNTextField.setEditable(false);
                    bookTitleTextField.setText(selectedTitle);
                    checkoutLengthTextField.setText(selectedCheckoutLength);
                    // map authors
                    loadAuthorsToTable(selectedISBN);
                }
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
        var books = ci.allBooks();
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

    private void loadAuthorsToTable(String isbn) {
        // load data
        var book = ci.getBookById(isbn);
        var bookAuthors = book.getAuthors();
//        System.out.println(bookAuthors);
//        int len = bookAuthors.size();
//        Object[][] items = new Object[len][];

        DefaultTableModel model = (DefaultTableModel) authorsTable.getModel();
        model.setRowCount(0); // This clears all rows
//        int i = 0;
        for (Author a : bookAuthors) {
            var address = a.getAddress();
            Object[] item = new Object[]{
                    "Delete button",
                    a.getFirstName(),
                    a.getLastName(),
                    a.getTelephone(),
//                    a.getAddress().toString(),
                    a.getBio(),
                    address.getStreet(),
                    address.getCity(),
                    address.getState(),
                    address.getZip()
            };
//            items[i++] = item;
            model.addRow(item);
        }

//        // Creating a DefaultTableModel with isCellEditable() overridden to return false
//        DefaultTableModel tableModel = new DefaultTableModel(items, authorColumnNames) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                // All cells are not editable
//                return false;
//            }
//        };
//
//        authorsTable.setModel(tableModel);
    }

    private List<Author> getAuthorsFromTable() {
        // "First Name", "Last Name", "Telephone", "Bio", "Street", "City", "State", "Zip"
        List<Author> authors = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) authorsTable.getModel();
        int column = 1; // firstName column is 1
        // Loop through all rows of the table
        for (int row = 0; row < model.getRowCount(); row++) {
            String firstName = authorsTable.getValueAt(row, column).toString();
            String lastName = authorsTable.getValueAt(row, column + 1).toString();
            String telephone = authorsTable.getValueAt(row, column + 2).toString();
            String bio = authorsTable.getValueAt(row, column + 3).toString();
            String street = authorsTable.getValueAt(row, column + 4).toString();
            String city = authorsTable.getValueAt(row, column + 5).toString();
            String state = authorsTable.getValueAt(row, column + 6).toString();
            String zip = authorsTable.getValueAt(row, column + 7).toString();
            var address = new Address(street, city, state, zip);
            var author = new Author(firstName, lastName, telephone, address, bio);
            authors.add(author);
        }
        return authors;
    }

    private void deleteAuthorRow() {
//        ButtonEditor editor = (ButtonEditor) authorsTable.getCellEditor();
//        int selectedRow = editor.getCurrentRow();
//        DefaultTableModel model = (DefaultTableModel) authorsTable.getModel();
//        model.removeRow(selectedRow); // Remove the selected row
//        JOptionPane.showMessageDialog(null, "Row " + selectedRow + " deleted");
        if (authorsTable.isEditing()) {
            authorsTable.getCellEditor().stopCellEditing();
        }
        int selectedRow = authorsTable.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) authorsTable.getModel();
        if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
            model.removeRow(selectedRow); // Remove the selected row
        }
        JOptionPane.showMessageDialog(null, "Row " + selectedRow + " deleted");
    }

    private void registerBackButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            this.resetForm();
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
        });
    }

    private void resetForm() {
        bookISBNTextField.setText("");
        bookISBNTextField.setBorder(defaultBorder);
//        bookISBNTextField.setEditable(true);
        bookTitleTextField.setText("");
        bookTitleTextField.setBorder(defaultBorder);
        checkoutLengthTextField.setText(""); // "21"
        checkoutLengthTextField.setBorder(defaultBorder);
        booksTable.clearSelection();

        authorsTable.clearSelection();
        DefaultTableModel model = (DefaultTableModel) authorsTable.getModel();
        model.setRowCount(0); // This clears all rows
    }

    private boolean validateForm(String selectedISBN) {
        boolean isValid = true;

        boolean isAddingNew = selectedISBN == null || selectedISBN.isEmpty();

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
            var bookIds = ci.allBookIds();
            boolean isExisted = bookIds.contains(inputISBN);
            boolean updatingCheck = !isAddingNew && !selectedISBN.equalsIgnoreCase(inputISBN) && isExisted;
            boolean addingCheck = isAddingNew && isExisted;
            if (addingCheck || updatingCheck) {
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
            // validate checkoutLength field is less than 21 days
            checkoutLength = Integer.parseInt(inputCheckoutLength);
            if (checkoutLength < 1 || checkoutLength > 21) {
                checkoutLengthTextField.setBorder(redBorder);
                isValid = false;
            } else {
                checkoutLengthTextField.setBorder(defaultBorder);
            }
        }

        // validate at least one author in the table
        if (authorsTable.getModel().getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Please add at least one author!", "Error", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        return isValid;
    }

    private void registerClearButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            this.resetForm();
        });
    }

    private void registerAddAuthorButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            // open the popup
            var popup = new AuthorDialog(this, this);
            popup.setVisible(true);
        });
    }

    private void registerAddButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            boolean isValid = validateForm(null);

            if (isValid) {
                // save book
                String inputTitle = bookTitleTextField.getText().trim();
                String inputISBN = bookISBNTextField.getText().trim();
                String inputCheckoutLength = checkoutLengthTextField.getText().trim();
                int checkoutLength = Integer.parseInt(inputCheckoutLength);
//                var inputAuthors = authorMultiComboBox.getSelectedValues();
                // get authors from the table
                List<Author> inputAuthors = getAuthorsFromTable();
//                List<Author> inputAuthors = new ArrayList<>();
                ci.saveNewBook(inputISBN, inputTitle, checkoutLength, inputAuthors);
                JOptionPane.showMessageDialog(null, "Added Successfully");
                // reload / re-render
                loadBooksToTable();
                resetForm();
            }
        });
    }

    private void registerDeleteButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(null, "Please select a row!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isValid = true;

            String selectedISBN = booksTable.getValueAt(selectedRow, 0).toString();
            var bookIds = ci.allBookIds();
            boolean isExisted = bookIds.contains(selectedISBN);
            if (selectedISBN.isEmpty() || !isExisted) {
                isValid = false;
            }

            if (isValid) {
                ci.deleteBook(selectedISBN);
                JOptionPane.showMessageDialog(null, "Deleted Successfully");
                // reload / re-render
                loadBooksToTable();
                resetForm();
            }
        });
    }

    private void registerUpdateButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(null, "Please select a row!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedISBN = booksTable.getValueAt(selectedRow, 0).toString();
            var bookIds = ci.allBookIds();
            boolean isExisted = bookIds.contains(selectedISBN);
            if (selectedISBN.isEmpty() || !isExisted) {
                return;
            }

            boolean isValid = validateForm(selectedISBN);

            if (isValid) {
                String updatedISBN = bookISBNTextField.getText().trim();
                String updatedTitle = bookTitleTextField.getText().trim();
                String updatedCheckoutLength = checkoutLengthTextField.getText().trim();
                int checkoutLength = Integer.parseInt(updatedCheckoutLength);
//                var updatedAuthors = authorMultiComboBox.getSelectedValues();
                // get authors from the table and update
                List<Author> updatedAuthors = getAuthorsFromTable();
//                List<Author> updatedAuthors = new ArrayList<>();
                ci.updateBook(selectedISBN, updatedISBN, updatedTitle, checkoutLength, updatedAuthors);
                JOptionPane.showMessageDialog(null, "Updated Successfully");
                // reload / re-render
                loadBooksToTable();
                resetForm();
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

    @Override
    public void reloadData() {
        loadBooksToTable();
    }

    @Override
    public void onDataReceived(Object data) {
//        // Parse the Object to List<Author>
//        if (data instanceof List<?>) {
//            try {
//                @SuppressWarnings("unchecked")
//                List<Author> authorList = (List<Author>) data; // Cast Object to List<User>
//
//                // Use the list
//                for (Author user : authorList) {
//                    System.out.println(user);
//                }
//            } catch (ClassCastException e) {
//                System.out.println("Failed to cast the object to List<User>: " + e.getMessage());
//            }
//        } else {
//            System.out.println("The object is not a List");
//        }
        // Parse the Object to Author
        if (data instanceof Author) {
            try {
                var inputAuthor = (Author) data; // Cast Object to Author
                var address = inputAuthor.getAddress();
//                System.out.println(inputAuthor);

                DefaultTableModel model = (DefaultTableModel) authorsTable.getModel();
                Object[] item = new Object[]{
                        "Delete button",
                        inputAuthor.getFirstName(),
                        inputAuthor.getLastName(),
                        inputAuthor.getTelephone(),
//                        inputAuthor.getAddress().toString(),
                        inputAuthor.getBio(),
                        address.getStreet(),
                        address.getCity(),
                        address.getState(),
                        address.getZip()
                };
                model.addRow(item);
            } catch (ClassCastException e) {
                System.out.println("Failed to cast the object to Author: " + e.getMessage());
            }
        } else {
            System.out.println("The object is not an Author");
        }
    }
}
