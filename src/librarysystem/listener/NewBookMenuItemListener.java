package librarysystem.listener;

import librarysystem.LibrarySystem;
import librarysystem.NewBookCopyWindow;
import librarysystem.NewBookWindow;
import librarysystem.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewBookMenuItemListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LibrarySystem.hideAllWindows();
        if (!NewBookWindow.INSTANCE.isInitialized()) {
            NewBookWindow.INSTANCE.init();
            NewBookWindow.INSTANCE.setSize(1200, 600);
            Util.centerFrameOnDesktop(NewBookWindow.INSTANCE);
            NewBookWindow.INSTANCE.setTitle("New Book Window");
            NewBookWindow.INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            NewBookWindow.INSTANCE.reloadData();
        }
        NewBookWindow.INSTANCE.setVisible(true);
//        NewBookWindow.INSTANCE.repaint();  // Refresh the display
    }
}
