package src.main.frames;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.*;
import src.main.visuals.*;

public class AddressesFrame extends JPanel {

    public static JTable addressTable;
    public static JTable pathsTable;

    public AddressesFrame(List<String[]> data) throws IOException {
        super(new BorderLayout());

        addressTable = new JTable(new AddressesTable()); // creating ips and macs table
        pathsTable = new PathsTable();                  // creating paths table

        JComboBox<Integer> comboBox = new JComboBox<>(IntStream.rangeClosed(0, 30).boxed().toArray(Integer[]::new)); // set checkbox renderer
        addressTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBox));

        JScrollPane scrollPaneAddress = new JScrollPane(addressTable);
        JScrollPane scrollPanePaths = new JScrollPane(pathsTable);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(scrollPaneAddress, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(scrollPanePaths, BorderLayout.CENTER);


        bottomPanel.setPreferredSize(new Dimension(scrollPanePaths.getPreferredSize().width, scrollPanePaths.getPreferredSize().height));

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new GridBagLayout()); // initializing button panel at the bottom
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.top = 5;
        gbc.insets.bottom = 5;
        gbc.insets.left = 10;
        gbc.anchor = GridBagConstraints.WEST;

        buttonPanel.add(new ClearSelectionsButton("Clear Selections", data), gbc); // creating clear selections button
        gbc.gridx++;
        gbc.insets.left = 20;
        gbc.anchor = GridBagConstraints.EAST;

        buttonPanel.add(new CheckButton(data), gbc); // creating check button
        gbc.gridx++;
        gbc.gridx++;
        gbc.insets.left = 20;
        gbc.anchor = GridBagConstraints.EAST;


        buttonPanel.add(new ManageExecutionButton("Manage Execution"), gbc);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
