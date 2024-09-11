package librarysystem.listener;

import librarysystem.LibrarySystem;
import librarysystem.NewBookCopyWindow;
import librarysystem.Util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewBookCopyMenuItemListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LibrarySystem.hideAllWindows();
        NewBookCopyWindow.INSTANCE.init();
        NewBookCopyWindow.INSTANCE.setSize(660, 500);
        Util.centerFrameOnDesktop(NewBookCopyWindow.INSTANCE);
        NewBookCopyWindow.INSTANCE.setVisible(true);
    }
}
