package src.main.frames;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import src.main.visuals.AddressesTable;
import src.main.visuals.CheckButton;
import src.main.visuals.ClearSelectionsButton;
import src.main.visuals.ManageExecutionButton;


public class AddressesFrame extends JPanel {

    public static JTable table;


    public AddressesFrame(List<String[]> data) throws IOException {

        super(new BorderLayout());
        table = new JTable(new AddressesTable());                       //creating ips and macs table

        table.getColumnModel().getColumn(2).setCellRenderer(new CheckboxRenderer());
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());                   //initializing button panel at the bottom
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.top = 5;
        gbc.insets.bottom = 5;
        gbc.insets.left = 10;
        gbc.anchor = GridBagConstraints.WEST;

        buttonPanel.add(new ClearSelectionsButton("Clear Selections", data), gbc);          //creating clear selections button
        gbc.gridx++;
        gbc.insets.left = 20;
        gbc.anchor = GridBagConstraints.EAST;

        buttonPanel.add(new CheckButton("Check hosts", data), gbc);                          //creating check button
        gbc.gridx++;
        gbc.gridx++;
        gbc.insets.left = 20;
        gbc.anchor = GridBagConstraints.EAST;
        buttonPanel.add(new ManageExecutionButton("Manage Execution"), gbc);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    static class CheckboxRenderer extends JCheckBox implements TableCellRenderer { // third column ("selected")

        public CheckboxRenderer() {
            setHorizontalAlignment(JCheckBox.CENTER);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setSelected((value != null && (Boolean) value));
            return this;
        }
    }
}