package librarysystem.listener;

import librarysystem.LibrarySystem;
import librarysystem.CheckoutBookWindow;
import librarysystem.Util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckoutBookMenuItemListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LibrarySystem.hideAllWindows();
        CheckoutBookWindow.INSTANCE.init();
        CheckoutBookWindow.INSTANCE.setSize(660, 500);
        Util.centerFrameOnDesktop(CheckoutBookWindow.INSTANCE);
        CheckoutBookWindow.INSTANCE.setVisible(true);
    }
}
