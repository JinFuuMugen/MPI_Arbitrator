package src.main.frames;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.*;

import src.main.visuals.AddressesTable;
import src.main.visuals.CheckButton;
import src.main.visuals.ClearSelectionsButton;
import src.main.visuals.ManageExecutionButton;


public class AddressesFrame extends JPanel {

    public static JTable table;


    public AddressesFrame(List<String[]> data) throws IOException {

        super(new BorderLayout());
        table = new JTable(new AddressesTable());                       //creating ips and macs table


        JComboBox<Integer> comboBox = new JComboBox<>(IntStream.rangeClosed(0, 30).boxed().toArray(Integer[]::new));            //set checkbox renderer
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBox));

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
}