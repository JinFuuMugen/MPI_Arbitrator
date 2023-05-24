package src.main.frames;

import src.main.cmd.Launcher;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ExecutionFrame extends JFrame {
    private static DefaultTableModel flagsModel;
    private JTable flagsTable;

    public static JFrame GetExecutionFrame() {
        ExecutionFrame frame = new ExecutionFrame();
        frame.setTitle("MPI Parameters");
        frame.setSize(1000, 500);

        flagsModel = new DefaultTableModel(new Object[][]{}, new String[]{"Flag name", "Value"});
        frame.flagsTable = new JTable(flagsModel);
        JScrollPane scrollPaneFlags = new JScrollPane(frame.flagsTable);

        frame.add(scrollPaneFlags, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Flags");
        addButton.addActionListener(e -> frame.showAddFlagsDialog());

        JButton removeButton = new JButton("Remove Flags");
        removeButton.addActionListener(e -> frame.removeSelectedFlags());

        JButton runButton = new JButton("Run");
        runButton.addActionListener(e -> {

            String execPath = (String) AddressesFrame.pathsTable.getValueAt(0, 2);
            String mpiLib = (String) AddressesFrame.pathsTable.getValueAt(1, 2);
            String mpiInclude = (String) AddressesFrame.pathsTable.getValueAt(2, 2);
            String compilPath = (String) AddressesFrame.pathsTable.getValueAt(3, 2);
            String libsNames = (String)AddressesFrame.pathsTable.getValueAt(4,2);
            String[] libs = libsNames.split(" ");
            StringBuilder stdMpi = new StringBuilder();
            for (String s : libs){
                stdMpi.append(" -l").append(s);
            }

            stdMpi.append(" -L ").append(mpiLib).append(" -I ").append(mpiInclude);
            String command = "cmd /c start cmd /k \"cd /d " + Launcher.getSelectedFilePath()
                    + " && echo Working directory changed to: " + Launcher.getSelectedFilePath()
                    + " && " + compilPath + " " + Launcher.getSelectedFileName() + stdMpi
                    + " && echo Compiled! && echo Program output:"
                    + " && " + execPath + " " + generateFlagsString() + " -machinefile hosts.txt a.exe\"";
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(runButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        return frame;
    }
    private void showAddFlagsDialog() {
        String flagName = JOptionPane.showInputDialog(this, "Enter flag name:");
        if (flagName != null && !flagName.trim().isEmpty()) {
            String value = JOptionPane.showInputDialog(this, "Enter flag value:");
            flagsModel.addRow(new Object[]{flagName, value});
        }
    }

    private void removeSelectedFlags() {
        int[] selectedRows = flagsTable.getSelectedRows();
        if (selectedRows.length > 0) {
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                flagsModel.removeRow(selectedRows[i]);
            }
        }
    }

    private static String generateFlagsString(){
        StringBuilder flagsString = new StringBuilder();
        for (int i = 0; i < flagsModel.getRowCount(); i++) {
            String flagName = (String) flagsModel.getValueAt(i, 0);
            String value = (String) flagsModel.getValueAt(i, 1);
            flagsString.append(" ").append(flagName).append(" ").append(value);
        }
        System.out.println(flagsString.toString().trim());
        return flagsString.toString().trim();
    }
}
