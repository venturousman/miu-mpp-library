package librarysystem;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import business.ControllerInterface;
import business.SystemController;
import librarysystem.listener.*;

public class LibrarySystem extends JFrame implements LibWindow {
    ControllerInterface ci = new SystemController();
    public final static LibrarySystem INSTANCE = new LibrarySystem();
    JPanel mainPanel;
    JMenuBar menuBar;
    JMenu accountMenu;
    JMenuItem loginMenuItem, allBookIds, allMemberIds;
    JMenu bookMenu, memberMenu, helpMenu;
    JMenuItem newBookMenuItem, checkoutBookMenuItem, newBookCopyMenuItem;
    JMenuItem newMemberMenuItem;
    JMenuItem aboutMenuItem;
    String pathToImage, pathToIcon;
    private boolean isInitialized = false;

    private static final LibWindow[] allWindows = {
            LibrarySystem.INSTANCE,
            LoginWindow.INSTANCE,
            AllMemberIdsWindow.INSTANCE,
            AllBookIdsWindow.INSTANCE,
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
        loginMenuItem = new JMenuItem("Login");
        loginMenuItem.addActionListener(new LoginMenuItemListener());
        accountMenu.add(loginMenuItem);
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
    }

    private void addMemberMenu() {
        memberMenu = new JMenu("Member");
        menuBar.add(memberMenu);
        // items
        newMemberMenuItem = new JMenuItem("New Member");
        newMemberMenuItem.addActionListener(new NewMemberMenuItemListener());
        memberMenu.add(newMemberMenuItem);
    }

    private void addHelpMenu() {
        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        // items
        aboutMenuItem = new JMenuItem("About");
//        aboutMenuItem.addActionListener(new );
        helpMenu.add(aboutMenuItem);
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
