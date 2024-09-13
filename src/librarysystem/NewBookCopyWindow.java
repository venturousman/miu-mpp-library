package librarysystem;

import business.Book;
import business.ControllerInterface;
import business.SystemController;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class NewBookCopyWindow extends JFrame implements LibWindow {

    public static final NewBookCopyWindow INSTANCE = new NewBookCopyWindow();
    ControllerInterface ci = new SystemController();
    private boolean isInitialized = false;
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel middlePanel;
    private JTextField isbnField;
    private JLabel statusLabel;
    private JTable booksTable;
    private JScrollPane scrollPane;
    private DataAccess dataAccess;

    private NewBookCopyWindow() {
        dataAccess = new DataAccessFacade();
    }

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

        JLabel titleLabel = new JLabel("New Book Copy Window");
        Util.adjustLabelFont(titleLabel, Util.DARK_BLUE, true);
        topPanel.add(titleLabel);

        JButton backButton = new JButton("Back");
        addBackButtonListener(backButton);
        topPanel.add(backButton);
    }

    private void defineMiddlePanel() {
        middlePanel = new JPanel(new BorderLayout(10, 10));

        // Top section for ISBN input and Add Copy button
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel isbnLabel = new JLabel("Enter ISBN:");
        isbnField = new JTextField(15);
        JButton addButton = new JButton("Add Copy");
        statusLabel = new JLabel("");

        addButton.addActionListener(new AddCopyActionListener());

        inputPanel.add(isbnLabel);
        inputPanel.add(isbnField);
        inputPanel.add(addButton);
        inputPanel.add(statusLabel);

        middlePanel.add(inputPanel, BorderLayout.NORTH);

        // Table to display books and their number of copies
        booksTable = new JTable();
        scrollPane = new JScrollPane(booksTable);

        middlePanel.add(scrollPane, BorderLayout.CENTER);
        refreshTableData();  // Load initial data into the table
    }

    // Fetch book data from controller and populate the table
    private void refreshTableData() {
        var books = ci.allBooks();
        String[] columnNames = {"ISBN", "Title", "Number of Copies"};
        Object[][] data = new Object[books.size()][3];

        int i = 0;
        for (Book book : books) {
            data[i][0] = book.getIsbn();
            data[i][1] = book.getTitle();
            data[i][2] = book.getNumCopies();
            i++;
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        booksTable.setModel(tableModel);
    }

    private class AddCopyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String isbn = isbnField.getText();

            // Validate input
            if (isbn.isEmpty()) {
                statusLabel.setText("ISBN cannot be empty!");
                return;
            }

            // Look up book by ISBN
            HashMap<String, Book> books = dataAccess.readBooksMap();
            if (books == null || !books.containsKey(isbn)) {
                statusLabel.setText("Book with ISBN " + isbn + " not found!");
                return;
            }

            // Add a copy to the book
            Book book = books.get(isbn);
            book.addCopy();

            // Persist the updated book
            dataAccess.saveNewBook(book);

            // Refresh only the table
            refreshTableData();

            statusLabel.setText("New copy added for ISBN: " + isbn);
            isbnField.setText("");
        }
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
