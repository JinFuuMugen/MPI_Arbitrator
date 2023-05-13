package src.main.frames;

import src.main.cmd.Arbitrator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class ExecutionFrame extends JFrame {
    public static JFrame GetExecutionFrame() {
        JFrame frame = new JFrame("MPI Parameters");
        frame.setSize(1000, 500);
        String[] paramTableColumns = { "Parameter name", "Description", "Value" };
        Object[][] paramData = {
                { "File", "Name of C/CPP MPI file", Arbitrator.getSelectedFileName() },
                { "Path", "Directory of C/CPP MPI file", Arbitrator.getSelectedFilePath() },
                {"Hosts", "Full path to hosts.txt", Arbitrator.getSelectedFilePath() + "hosts.txt"}
        };
        DefaultTableModel model = new DefaultTableModel(paramData, paramTableColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // only allow editing in the "Value" column
            }
        };
        JTable table = new JTable(model);
        Font font = new Font("TimesNewRoman", Font.PLAIN, 12);
        TableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String val = (String) value;
                if (val.equals("File is not choosen!")) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(Color.BLACK);
                }
                c.setFont(font); // set the font
                return c;
            }
        };
        table.getColumnModel().getColumn(0).setCellRenderer(renderer);
        table.getColumnModel().getColumn(1).setCellRenderer(renderer);
        table.getColumnModel().getColumn(2).setCellRenderer(renderer);

        table.getColumnModel().getColumn(1).setPreferredWidth(400); // set width of Description column
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS); // resize other columns to fit
        table.getColumnModel().getColumn(0).setCellEditor(null); // make the first column uneditable
        table.getColumnModel().getColumn(1).setCellEditor(null); // make the second column uneditable
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);


        JButton runButton = new JButton("Run");
        runButton.addActionListener(e -> {
            String stdMpi = " -l msmpi -L \"C:\\Program Files (x86)\\Microsoft SDKs\\MPI\\Lib\\x64\" -I \"C:\\Program Files (x86)\\Microsoft SDKs\\MPI\\Include\"";
            // String command = "g++ -o " + fileName + " " +
            // ArbitratorApplication.selectedFilePath + stdMpi;
            String command = "cmd /c start cmd /k \"cd /d " + Arbitrator.getSelectedFilePath()
                    + " && echo Working directory changed to: " + Arbitrator.getSelectedFilePath()
                    + " && " + "gcc " + Arbitrator.getSelectedFileName() + stdMpi
                    + " && echo Compiled! && echo Program output:"
                    + " && mpiexec -machinefile hosts.txt a.exe\"";
            // g++ -o hello.exe hello.cpp -l msmpi -L "C:\Program Files (x86)\Microsoft
            // SDKs\MPI\Lib\x64" -I "C:\Program Files (x86)\Microsoft SDKs\MPI\Include"
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
