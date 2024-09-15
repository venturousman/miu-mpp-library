package librarysystem.components;

import javax.swing.*;
import java.awt.*;

// Custom panel that contains two buttons
public class InTableActionButtonPanel extends JPanel {
    public JButton deleteButton = new JButton("X"); // Delete - Action 1
//    public JButton editButton = new JButton("Edit"); // Action 2

    public InTableActionButtonPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));

        // Remove the button padding by setting the margin to zero
        deleteButton.setMargin(new Insets(0, 0, 0, 0));

        // Remove the button borders and background to make it look like an icon
        deleteButton.setFocusPainted(false); // Remove the focus painted around the text
        deleteButton.setContentAreaFilled(false); // Remove the button background
        deleteButton.setBorderPainted(false); // Remove the button border
        deleteButton.setOpaque(false); // Make the button non-opaque

        // Optionally: change the font and color to make it more icon-like
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setForeground(Color.RED);

        add(deleteButton);
//        add(editButton);
    }
}

