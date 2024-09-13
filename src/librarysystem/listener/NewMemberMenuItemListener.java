package librarysystem.listener;

import librarysystem.LibrarySystem;
import librarysystem.NewMemberWindow;
import librarysystem.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewMemberMenuItemListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LibrarySystem.hideAllWindows();
        if (!NewMemberWindow.INSTANCE.isInitialized()) {
            NewMemberWindow.INSTANCE.init();
            NewMemberWindow.INSTANCE.setSize(1200, 630);
            Util.centerFrameOnDesktop(NewMemberWindow.INSTANCE);
            NewMemberWindow.INSTANCE.setTitle("New Member Window");
            NewMemberWindow.INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            NewMemberWindow.INSTANCE.reloadData();
        }
        NewMemberWindow.INSTANCE.setVisible(true);
//        NewMemberWindow.INSTANCE.repaint();
    }
}
