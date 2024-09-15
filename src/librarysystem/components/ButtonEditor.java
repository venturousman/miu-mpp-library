package librarysystem.components;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionListener;

// Custom cell editor for the column containing buttons
public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final InTableActionButtonPanel buttonPanel = new InTableActionButtonPanel();
//    private JTable table;
//    private int row;

    public ButtonEditor(ActionListener deleteActionListener) {
        // Add the provided action listeners to the buttons
//        buttonPanel.deleteButton.addActionListener(deleteActionListener); // 1st way

        // Add action listeners to the buttons
        buttonPanel.deleteButton.addActionListener(e -> {
//            JOptionPane.showMessageDialog(buttonPanel, "Action 1 performed");
            deleteActionListener.actionPerformed(e); // 2nd way
            fireEditingStopped(); // Notify the editor to stop editing
        });

//        buttonPanel.editButton.addActionListener(e -> {
//            JOptionPane.showMessageDialog(buttonPanel, "Action 2 performed");
//            fireEditingStopped(); // Notify the editor to stop editing
//        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        this.table = table;
//        this.row = row; // Save the current row
        if (isSelected) {
            buttonPanel.setBackground(table.getSelectionBackground());
        } else {
            buttonPanel.setBackground(table.getBackground());
        }
        return buttonPanel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

//    public int getCurrentRow() {
//        return row;
//    }
}
