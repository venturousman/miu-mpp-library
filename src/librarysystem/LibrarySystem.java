package librarysystem;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import business.ControllerInterface;
import business.SystemController;
import dataaccess.Auth;
import librarysystem.listener.*;

public class LibrarySystem extends JFrame implements LibWindow {
    ControllerInterface ci = new SystemController();
    public final static LibrarySystem INSTANCE = new LibrarySystem();
    JPanel mainPanel;
    JMenuBar menuBar;
    JMenu accountMenu;
    JMenuItem loginMenuItem, logoutMenuItem;
    JMenu bookMenu, memberMenu, helpMenu;
    JMenuItem newBookMenuItem, checkoutBookMenuItem, newBookCopyMenuItem;
    JMenuItem newMemberMenuItem;
    JMenuItem aboutMenuItem;
    String pathToImage, pathToIcon;
    JLabel authLabel;
    private boolean isInitialized = false;

    private static final LibWindow[] allWindows = {
            LibrarySystem.INSTANCE,
            LoginWindow.INSTANCE,
            NewMemberWindow.INSTANCE,
            NewBookWindow.INSTANCE,
            NewBookCopyWindow.INSTANCE,
            CheckoutBookWindow.INSTANCE,
    };

    public static void hideAllWindows() {
        for (LibWindow frame : allWindows) {
            frame.setVisible(false);
        }
    }

    private LibrarySystem() {
    }

    public void init() {
        formatContentPane();
        setImagePaths();
        setImages();

        createMenus();
        //pack();
        setSize(660, 500);
        isInitialized = true;
    }

    public void toggleMenus() {
//        System.out.println(SystemController.currentAuth);
        Auth currentAuth = SystemController.currentAuth;
        if (currentAuth == null) {
            authLabel.setVisible(false);
            loginMenuItem.setEnabled(true);
            logoutMenuItem.setEnabled(false);

            bookMenu.setEnabled(false);
            memberMenu.setEnabled(false);
        } else {
            authLabel.setText("I am " + currentAuth);
            authLabel.setVisible(true);
            loginMenuItem.setEnabled(false);
            logoutMenuItem.setEnabled(true);

            bookMenu.setEnabled(true);
            memberMenu.setEnabled(true);

            if (currentAuth == Auth.BOTH) {
                checkoutBookMenuItem.setEnabled(true);
                newBookMenuItem.setEnabled(true);
                newBookCopyMenuItem.setEnabled(true);
                newMemberMenuItem.setEnabled(true);
            } else if (currentAuth == Auth.ADMIN) {
                checkoutBookMenuItem.setEnabled(false);
                newBookMenuItem.setEnabled(true);
                newBookCopyMenuItem.setEnabled(true);
                newMemberMenuItem.setEnabled(true);
            } else if (currentAuth == Auth.LIBRARIAN) {
                checkoutBookMenuItem.setEnabled(true);
                newBookMenuItem.setEnabled(false);
                newBookCopyMenuItem.setEnabled(false);
                newMemberMenuItem.setEnabled(false);
            }
        }
    }

    private void formatContentPane() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 1));
        getContentPane().add(mainPanel);
    }

    private void setImagePaths() {
        String currDirectory = System.getProperty("user.dir");
        pathToImage = currDirectory + "\\src\\librarysystem\\library.jpg";
        pathToIcon = currDirectory + "\\src\\librarysystem\\icon.png";
    }

    private void setImages() {
        ImageIcon image = new ImageIcon(pathToImage);
        mainPanel.add(new JLabel(image));

        try {
            setIconImage(ImageIO.read(new File(pathToIcon)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
    }

    private void createMenus() {
        menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createRaisedBevelBorder());
        addAccountMenu();
        addBookMenu();
        addMemberMenu();
        addHelpMenu();
        setJMenuBar(menuBar);
    }

    private void addAccountMenu() {
        accountMenu = new JMenu("Account");
        menuBar.add(accountMenu);
        // items
        authLabel = new JLabel();
        accountMenu.add(authLabel);

        loginMenuItem = new JMenuItem("Login");
        loginMenuItem.addActionListener(new LoginMenuItemListener());
        accountMenu.add(loginMenuItem);

        logoutMenuItem = new JMenuItem("Logout");
        registerLogoutMenuItemListener(logoutMenuItem);
//        logoutMenuItem.addActionListener(new LogoutMenuItemListener());
        accountMenu.add(logoutMenuItem);

        if (SystemController.currentAuth == null) {
            authLabel.setVisible(false);
            loginMenuItem.setEnabled(true);
            logoutMenuItem.setEnabled(false);
        }
//        else {
//            loginMenuItem.setEnabled(false);
//            logoutMenuItem.setEnabled(true);
//        }
    }

    private void addBookMenu() {
        bookMenu = new JMenu("Book");
        menuBar.add(bookMenu);
        // items
        newBookMenuItem = new JMenuItem("New Book");
        newBookMenuItem.addActionListener(new NewBookMenuItemListener());
        bookMenu.add(newBookMenuItem);

        newBookCopyMenuItem = new JMenuItem("New Book Copy");
        newBookCopyMenuItem.addActionListener(new NewBookCopyMenuItemListener());
        bookMenu.add(newBookCopyMenuItem);

        checkoutBookMenuItem = new JMenuItem("Checkout Book(s)");
        checkoutBookMenuItem.addActionListener(new CheckoutBookMenuItemListener());
        bookMenu.add(checkoutBookMenuItem);

        if (SystemController.currentAuth == null) {
            bookMenu.setEnabled(false);
        }
    }

    private void addMemberMenu() {
        memberMenu = new JMenu("Member");
        menuBar.add(memberMenu);
        // items
        newMemberMenuItem = new JMenuItem("New Member");
        newMemberMenuItem.addActionListener(new NewMemberMenuItemListener());
        memberMenu.add(newMemberMenuItem);

        if (SystemController.currentAuth == null) {
            memberMenu.setEnabled(false);
        }
    }

    private void addHelpMenu() {
        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        // items
        aboutMenuItem = new JMenuItem("About");
//        aboutMenuItem.addActionListener(new );
        helpMenu.add(aboutMenuItem);
    }

    private void registerLogoutMenuItemListener(JMenuItem menuItem) {
        menuItem.addActionListener(evt -> {
            ci.logout();
            this.toggleMenus();
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
        // empty
    }
}
