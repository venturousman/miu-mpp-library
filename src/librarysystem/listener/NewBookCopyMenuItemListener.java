package librarysystem.listener;

import librarysystem.LibrarySystem;
import librarysystem.NewBookCopyWindow;
import librarysystem.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewBookCopyMenuItemListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LibrarySystem.hideAllWindows();
        if (!NewBookCopyWindow.INSTANCE.isInitialized()) {
            NewBookCopyWindow.INSTANCE.init();
            NewBookCopyWindow.INSTANCE.setSize(660, 500);
            Util.centerFrameOnDesktop(NewBookCopyWindow.INSTANCE);
            NewBookCopyWindow.INSTANCE.setTitle("New Book Copy Window");
            NewBookCopyWindow.INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        NewBookCopyWindow.INSTANCE.setVisible(true);
//        NewBookCopyWindow.INSTANCE.repaint();  // Refresh the display
    }
}
