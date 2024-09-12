package librarysystem.listener;

import librarysystem.LibrarySystem;
import librarysystem.LoginWindow;
import librarysystem.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginMenuItemListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LibrarySystem.hideAllWindows();
        LoginWindow.INSTANCE.init();
        LoginWindow.INSTANCE.setSize(360, 300);
        Util.centerFrameOnDesktop(LoginWindow.INSTANCE);
        LoginWindow.INSTANCE.setTitle("Login Window");
        LoginWindow.INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoginWindow.INSTANCE.setVisible(true);
        LoginWindow.INSTANCE.repaint();  // Refresh the display
    }
}
