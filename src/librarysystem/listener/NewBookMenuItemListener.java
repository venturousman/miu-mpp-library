package librarysystem.listener;

import librarysystem.LibrarySystem;
import librarysystem.NewBookWindow;
import librarysystem.Util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewBookMenuItemListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LibrarySystem.hideAllWindows();
        NewBookWindow.INSTANCE.init();
        NewBookWindow.INSTANCE.setSize(660, 500);
        Util.centerFrameOnDesktop(NewBookWindow.INSTANCE);
        NewBookWindow.INSTANCE.setVisible(true);
    }
}
