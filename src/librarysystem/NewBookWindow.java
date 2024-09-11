package librarysystem;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

public class NewBookWindow extends JFrame implements LibWindow {

    public static final NewBookWindow INSTANCE = new NewBookWindow();
    //    ControllerInterface ci = new SystemController();
    private boolean isInitialized = false;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel middlePanel;

    private JTable booksTable;
    private JTextField bookTitleTextField;
    private JTextField bookISBNTextField;
    private JTextField checkoutLengthTextField;
//    private JFormattedTextField checkoutLengthFormattedTextField;

    private JButton addBookButton;
    private JButton clearFormButton;

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
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(300, 600));

        // textboxes
        JLabel titleLabel = new JLabel("Book Title");
        bookTitleTextField = new JTextField();
        bookTitleTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(titleLabel);
        leftPanel.add(bookTitleTextField);

        JLabel isbnLabel = new JLabel("Book ISBN");
        bookISBNTextField = new JTextField();
        bookISBNTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(isbnLabel);
        leftPanel.add(bookISBNTextField);

//        // Create a NumberFormat instance to ensure numeric input
//        NumberFormat format = NumberFormat.getIntegerInstance();
//        format.setGroupingUsed(false); // Disable grouping (e.g., no commas in large numbers)
//
//        // Create a NumberFormatter that will only allow numbers
//        NumberFormatter formatter = new NumberFormatter(format);
//        formatter.setValueClass(Integer.class); // Only allow integers
//        formatter.setAllowsInvalid(false);      // Prevent invalid input
//        formatter.setMinimum(1);                // Optionally set a minimum value

        JLabel checkoutLengthLabel = new JLabel("Checkout Length");
//        checkoutLengthFormattedTextField = new JFormattedTextField(formatter);
//        checkoutLengthFormattedTextField.setPreferredSize(new Dimension(250, 30));
        checkoutLengthTextField = new JTextField();
        checkoutLengthTextField.setPreferredSize(new Dimension(250, 30));
        leftPanel.add(checkoutLengthLabel);
        leftPanel.add(checkoutLengthTextField);

        // buttons
        addBookButton = new JButton("Add");
        leftPanel.add(addBookButton);

        clearFormButton = new JButton("Clear");
        leftPanel.add(clearFormButton);

        JButton backButton = new JButton("Back");
        addBackButtonListener(backButton);
        leftPanel.add(backButton);
    }

    private void defineMiddlePanel() {
        middlePanel = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 25, 25);
        middlePanel.setLayout(fl);
//        textArea = new TextArea(8, 20);
//        middlePanel.add(textArea);

        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setBounds(6, 154, 582, 287);
        middlePanel.add(scrollPane);

        booksTable = new JTable();
        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = booksTable.getSelectedRow();
//                idtf.setText(model.getValueAt(r, 0).toString());
//                nametf.setText(model.getValueAt(r, 1).toString());
//                authtf.setText(model.getValueAt(r, 2).toString());
            }
        });
        booksTable.setBackground(new Color(255, 240, 245));
//        model = new DefaultTableModel();
//        String[] column = {"ID","Book Name","Author"};
//        String[] row = new String[3];
//        model.setColumnIdentifiers(column);
//        table.setModel(model);
        scrollPane.setViewportView(booksTable);
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
