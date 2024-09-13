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

public class NewBookCopyWindow extends JFrame implements LibWindow {

    public static final NewBookCopyWindow INSTANCE = new NewBookCopyWindow();
    ControllerInterface ci = new SystemController();
    private boolean isInitialized = false;
    private JPanel mainPanel;
    //    private JPanel topPanel;
    private JPanel middlePanel;

    private JComboBox<Book> bookComboBox;
    private JButton addBookCopyButton;
    private JTable bookCopyTable;

    // Create the red border for invalid input
    private final Border redBorder = new LineBorder(Color.RED, 1);
    // Create the default border to reset later
    private Border defaultBorder;
    private final String[] columnNames = {
            "Copy No", "ISBN", "Book Title", "Available"
    };

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
        middlePanel.setPreferredSize(new Dimension(500, 600));
        FlowLayout fl = new FlowLayout(FlowLayout.LEFT, 16, 8);
        middlePanel.setLayout(fl);

        JLabel isbnLabel = new JLabel("Book");
        var books = ci.allBooks();
        bookComboBox = new JComboBox(books.toArray());
        bookComboBox.setPreferredSize(new Dimension(250, 30));
        // Add action listener to retrieve selected person
//        bookComboBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                var selectedBook = (Book) bookComboBox.getSelectedItem();
//                if (selectedBook != null) {
//                    // Show the name and id of the selected person
//                    System.out.println("Selected: " + selectedPerson.getName() + " (ID: " + selectedPerson.getId() + ")");
//                }
//            }
//        });
        middlePanel.add(isbnLabel);
        middlePanel.add(bookComboBox);

        // buttons
        addBookCopyButton = new JButton("Add");
        registerAddButtonListener(addBookCopyButton);
        middlePanel.add(addBookCopyButton);

        JButton backButton = new JButton("Back");
        registerBackButtonListener(backButton);
        middlePanel.add(backButton);

        // table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(400, 350));
        middlePanel.add(scrollPane);

        bookCopyTable = new JTable();
        bookCopyTable.setBackground(new Color(255, 255, 255));
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
        bookComboBox.setSelectedItem(null);
    }

    private void registerAddButtonListener(JButton btn) {
        btn.addActionListener(evt -> {
            // TODO
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
}
