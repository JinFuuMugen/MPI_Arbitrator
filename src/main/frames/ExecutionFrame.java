package src.main.frames;

import java.awt.*;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ExecutionFrame extends JFrame {
    public static JFrame GetExecutionFrame() {
        JFrame frame = new JFrame("MPI Parameters");
        frame.setSize(1000, 500);


        DefaultTableModel flagsModel = new DefaultTableModel(new Object[][]{}, new String[]{"Flag name", "Description", "Value"});
        JTable flagsTable = new JTable(flagsModel);
        JScrollPane scrollPaneFlags = new JScrollPane(flagsTable);

        JPanel tablePanel = new JPanel(new GridLayout(2, 1));
        tablePanel.add(scrollPaneFlags);

        frame.add(tablePanel, BorderLayout.CENTER);

        JButton runButton = new JButton("Run");
        runButton.addActionListener(e -> {

            String fileName = (String) AddressesFrame.pathsTable.getValueAt(0, 2);
            String filePath = (String) AddressesFrame.pathsTable.getValueAt(1, 2);
            String execPath = (String) AddressesFrame.pathsTable.getValueAt(2, 2);
            String mpiLib = (String) AddressesFrame.pathsTable.getValueAt(3, 2);
            String mpiInclude = (String) AddressesFrame.pathsTable.getValueAt(4, 2);
            String compilPath = (String) AddressesFrame.pathsTable.getValueAt(5, 2);
            String libsNames = (String)AddressesFrame.pathsTable.getValueAt(6,2);
            String[] libs = libsNames.split(" ");
            StringBuilder stdMpi = new StringBuilder();
            for (String s : libs){
                stdMpi.append(" -l").append(s);
            }

            stdMpi.append(" -L ").append(mpiLib).append(" -I ").append(mpiInclude);
            String command = "cmd /c start cmd /k \"cd /d " + filePath
                    + " && echo Working directory changed to: " + filePath
                    + " && " + compilPath + " " + fileName + stdMpi
                    + " && echo Compiled! && echo Program output:"
                    + " && " + execPath + " -machinefile hosts.txt a.exe\"";
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(runButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        return frame;
    }
}
