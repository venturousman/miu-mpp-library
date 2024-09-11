package librarysystem.listener;

import librarysystem.LibrarySystem;
import librarysystem.LoginWindow;
import librarysystem.Util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginMenuItemListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LibrarySystem.hideAllWindows();
        LoginWindow.INSTANCE.init();
//        LoginWindow.INSTANCE.setSize(660, 500);
        Util.centerFrameOnDesktop(LoginWindow.INSTANCE);
        LoginWindow.INSTANCE.setVisible(true);
    }
}
