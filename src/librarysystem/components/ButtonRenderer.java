package librarysystem.components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// Custom cell renderer for the column containing buttons
public class ButtonRenderer extends InTableActionButtonPanel implements TableCellRenderer {
    public ButtonRenderer() {
        // You can customize the look of the buttons here if needed
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }
}

