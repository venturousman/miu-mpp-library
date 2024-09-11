package librarysystem.listener;

import librarysystem.LibrarySystem;
import librarysystem.NewBookWindow;
import librarysystem.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewBookMenuItemListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LibrarySystem.hideAllWindows();
        NewBookWindow.INSTANCE.init();
        NewBookWindow.INSTANCE.setSize(800, 600);
        Util.centerFrameOnDesktop(NewBookWindow.INSTANCE);
        NewBookWindow.INSTANCE.setVisible(true);
        NewBookWindow.INSTANCE.setTitle("New Book Window");
        NewBookWindow.INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
