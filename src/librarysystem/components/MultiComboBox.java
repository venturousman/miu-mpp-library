package librarysystem.components;

import business.Author;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Custom class for MultiSelect ComboBox with objects
public class MultiComboBox<T> extends JComboBox<T> {
    private final ArrayList<T> selectedItems;

    public MultiComboBox(T[] items) {
        super(items);
        selectedItems = new ArrayList<>();
        setRenderer(new CheckBoxRenderer<>());
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                T selectedItem = (T) comboBox.getSelectedItem();
                toggleSelection(selectedItem);
                comboBox.setSelectedIndex(-1);  // Prevents showing the last selected item in the field
            }
        });
    }

    // Toggle selection for an item
    private void toggleSelection(T item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        repaint();  // Refresh the display
    }

    // Get the list of selected items
    public ArrayList<String> getSelectedValues() {
        ArrayList<String> values = new ArrayList<>();
        for (T item : selectedItems) {
            if (item instanceof Author author) {
                String selectedValue = author.getFullName();
                values.add(selectedValue);  // Get the value of the selected item
            }
        }
        return values;
    }

    // Custom renderer for checkboxes with names from the object
    private class CheckBoxRenderer<T> implements ListCellRenderer<T> {
        @Override
        public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
            JCheckBox checkBox = new JCheckBox(value.toString());  // Display the name
            checkBox.setSelected(selectedItems.contains(value));
            return checkBox;
        }
    }
}