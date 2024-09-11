package librarysystem.listener;

import librarysystem.LibrarySystem;
import librarysystem.NewMemberWindow;
import librarysystem.Util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewMemberMenuItemListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LibrarySystem.hideAllWindows();
        NewMemberWindow.INSTANCE.init();
        NewMemberWindow.INSTANCE.setSize(660, 500);
        Util.centerFrameOnDesktop(NewMemberWindow.INSTANCE);
        NewMemberWindow.INSTANCE.setVisible(true);
    }
}
