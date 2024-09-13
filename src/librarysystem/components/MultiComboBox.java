package librarysystem.components;

import business.Author;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Custom class for MultiSelect ComboBox with objects
public class MultiComboBox<T> extends JComboBox<T> {
    private ArrayList<T> selectedItems;

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
        //if (item == null) return;
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        repaint();  // Refresh the display
    }

    // Get the list of selected items
//    public ArrayList<String> getSelectedValues() {
//        ArrayList<String> values = new ArrayList<>();
//        for (T item : selectedItems) {
//            if (item instanceof Author author) {
//                String selectedValue = author.getFullName();
//                values.add(selectedValue);  // Get the value of the selected item
//            }
//        }
//        return values;
//    }
    public ArrayList<T> getSelectedValues() {
        return selectedItems;
    }

    public void setSelectedItems(ArrayList<T> items) {
        this.selectedItems = items;
        repaint();
    }

    public void clearSelectedItems() {
        this.selectedItems.clear();
        repaint();
    }

    // Custom renderer for checkboxes with names from the object
    private class CheckBoxRenderer<T> implements ListCellRenderer<T> {
        @Override
        public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
            if (index >= 0) {
                // Render the combo box's drop-down (popup) with checkboxes
                JCheckBox checkBox = new JCheckBox(value.toString());  // Display the name
                checkBox.setSelected(selectedItems.contains(value));
                return checkBox;
            } else {
                // If no index, handle rendering for the combo box display (when the dropdown is closed)
                String displayValue;
                if (selectedItems == null || selectedItems.isEmpty()) {
                    displayValue = "Select at least one value";
                } else {
                    List<String> names = selectedItems.stream()
                            .map(Object::toString)  // Extract names
                            .sorted()                  // Sorting alphabetically
                            .toList();                 // Collecting results to a list
                    displayValue = String.join(",", names);
                }
                JLabel label = new JLabel(displayValue);
                label.setOpaque(true);
                return label;
            }
        }
    }
}