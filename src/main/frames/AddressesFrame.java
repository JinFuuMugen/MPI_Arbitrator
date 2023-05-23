package src.main.frames;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import src.main.cmd.Launcher;
import src.main.visuals.AddressesTable;
import src.main.visuals.ClearSelectionsButton;
import src.main.visuals.ManageExecutionButton;


public class AddressesFrame extends JPanel {

    public static JTable addressTable;

    public static JTable pathsTable;


    public AddressesFrame(List<String[]> data) throws IOException {
        super(new BorderLayout());
        setSize(1000, 500);

        addressTable = new JTable(new AddressesTable());                       //creating ips and macs table

        JComboBox<Integer> comboBox = new JComboBox<>(IntStream.rangeClosed(0, 30).boxed().toArray(Integer[]::new));            //set checkbox renderer
        addressTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBox));

        JScrollPane scrollPaneAddress = new JScrollPane(addressTable);


        DefaultTableModel model = new DefaultTableModel(new Object[][]{{ "File", "Name of C/CPP MPI file", Launcher.getSelectedFileName() },
                { "Path", "Directory of C/CPP MPI file", Launcher.getSelectedFilePath() },
                {"Mpiexec path", "Full path to mpiexec.exe", "\"C:\\Program Files\\Microsoft MPI\\Bin\\mpiexec.exe\""},
                {"MPI Lib path", "Full path to mpi/lib folder", "\"C:\\Program Files (x86)\\Microsoft SDKs\\MPI\\Lib\\x64\""},
                {"MPI Include path", "Full path to mpi/Include folder", "\"C:\\Program Files (x86)\\Microsoft SDKs\\MPI\\Include\""},
                {"C/C++ compiler path", "Full path to compiler", "\"C:\\MinGW\\bin\\g++.exe\""},
                {"Libs names", "Names of all libraries connected to program(space sepparated)", "msmpi"}}, new String[]{ "Path name", "Description", "Value" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // only allow editing in the "Value" column
            }
        };
        pathsTable = new JTable(model);

        pathsTable.getColumnModel().getColumn(0).setCellEditor(null); // make the first column uneditable
        pathsTable.getColumnModel().getColumn(1).setCellEditor(null); // make the second column uneditable
        JScrollPane scrollPanePaths = new JScrollPane(pathsTable);
        pathsTable.getColumnModel().getColumn(1).setPreferredWidth(pathsTable.getColumnModel().getColumn(1).getPreferredWidth());
        pathsTable.getColumnModel().getColumn(2).setPreferredWidth(pathsTable.getColumnModel().getColumn(2).getPreferredWidth());
        pathsTable.getColumnModel().getColumn(2).setPreferredWidth(pathsTable.getColumnModel().getColumn(2).getPreferredWidth());

        JPanel tablesPanel = new JPanel(new GridLayout(2, 1));
        tablesPanel.add(scrollPaneAddress);
        tablesPanel.add(scrollPanePaths);

        add(tablesPanel, BorderLayout.CENTER);


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

        JButton checkButton = new JButton("Check hosts");
        checkButton.addActionListener((e -> {
            ExecutorService executor = Executors.newFixedThreadPool(data.size());
            for (int i = 0; i < data.size(); i++) {
                if (AddressesFrame.addressTable.getModel().getValueAt(i, 3) == Boolean.TRUE) {
                    final int index = i;
                    executor.execute(() -> {
                        StringBuilder command = new StringBuilder("cmd /c \"cd /d ").append(Launcher.getSelectedFilePath()).append(" && ").append((String) AddressesFrame.pathsTable.getValueAt(2, 2)).append(" -timeout 5 -host ");
                        String ip = (String) AddressesFrame.addressTable.getModel().getValueAt(index, 0);
                        command.append(ip).append(" hostname\"");
                        try {
                            Process process = Runtime.getRuntime().exec(command.toString());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            process.waitFor();
                            System.out.println(process.exitValue());
                            if (process.exitValue() != 0)
                                addressTable.setValueAt("Cannot get hostname", index, 2);
                            else {
                                addressTable.setValueAt(reader.readLine(), index, 2);
                            }
                        } catch (IOException | InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
            }
            executor.shutdown();
        }));
        buttonPanel.add(checkButton, gbc);                          //creating check button
        gbc.gridx++;
        gbc.gridx++;
        gbc.insets.left = 20;
        gbc.anchor = GridBagConstraints.EAST;
        buttonPanel.add(new ManageExecutionButton("Manage Execution"), gbc);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}