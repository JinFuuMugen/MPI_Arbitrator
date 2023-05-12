package src.main.frames;

import src.main.cmd.Arbitrator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
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
                { "-localonly", "Specifies that MPI processes may only be launched on the local machine", "" },
                { "-envall", "Propagates all environment variables from the launching machine to the MPI processes",
                        "" },
                { "-exitcodes", "Specifies that MPI processes should return an integer exit code upon completion", "" },
                { "-gdb", "Runs the debugger using MPI processes", "" },
                { "-verbose", "Displays verbose output for MPI processes", "" },
                { "-timeout", "Specifies the amount of time (in milliseconds) to wait for an MPI process to start",
                        "" },
                { "-np", "Specifies the number of MPI processes to start", "" },
                { "-map", "Specifies how to map the MPI processes to nodes and processors", "" },
                { "-mapping", "Specifies how to map the MPI processes to nodes and processors (same as -map)", "" },
                { "-file", "Specifies the location of the MPI configuration file", "" },
                { "-wdir", "Specifies the working directory for MPI processes", "" },
                { "-env", "Propagates a specific environment variable from the launching machine to the MPI processes",
                        "" },
                { "-envlist",
                        "Propagates a list of environment variables from the launching machine to the MPI processes",
                        "" },
                { "-genv",
                        "Propagates a specific environment variable to the MPI processes, but does not include it in the launching machine's environment",
                        "" },
                { "-genvlist",
                        "Propagates a list of environment variables to the MPI processes, but does not include them in the launching machine's environment",
                        "" },
                { "-stdin", "Specifies the standard input file for MPI processes", "" },
                { "-launcher", "Specifies the launcher to use for MPI processes", "" },
                { "-affinity", "Specifies the processor affinity for each MPI process", "" },
                { "-wait", "Waits for MPI processes to complete before exiting", "" },
                { "-enablex", "Enables X11 forwarding for MPI processes", "" },
                { "-noprompt", "Suppresses the MPI process prompt", "" },
                { "-priority", "Specifies the priority of MPI processes", "" },
                { "-pools", "Specifies the number of process pools to use", "" },
                { "-machines", "Specifies a list of machines to use for MPI processes", "" },
                { "-gdbattach", "Attaches the debugger to running MPI processes", "" }
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
            String stdMpi = " -l msmpi -l stdc++ -L \"C:\\Program Files (x86)\\Microsoft SDKs\\MPI\\Lib\\x64\" -I \"C:\\Program Files (x86)\\Microsoft SDKs\\MPI\\Include\"";
            // String command = "g++ -o " + fileName + " " +
            // ArbitratorApplication.selectedFilePath + stdMpi;
            String command = "cmd /c cd /d " + Arbitrator.getSelectedFilePath()
                    + " && echo Working directory changed to: " + Arbitrator.getSelectedFilePath()
                    + " && " + "g++ " + Arbitrator.getSelectedFileName() + stdMpi
                    + " && echo Compiled!  && echo Program output:"
                    + " && mpiexec -host 192.168.1.101 -np 3 a.exe";
            // g++ -o hello.exe hello.cpp -l msmpi -L "C:\Program Files (x86)\Microsoft
            // SDKs\MPI\Lib\x64" -I "C:\Program Files (x86)\Microsoft SDKs\MPI\Include"
            try {
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append(System.getProperty("line.separator"));
                }
                String output = builder.toString();

                JFrame frame1 = new JFrame("Command Result");
                JTextArea textArea = new JTextArea(output);
                textArea.setFont(new Font("Arial", Font.PLAIN, 15));
                textArea.setEditable(false);
                JScrollPane scrollPane1 = new JScrollPane(textArea);
                frame1.add(scrollPane1);
                frame1.setSize(400, 300);
                frame1.setVisible(true);
                frame1.setAlwaysOnTop(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            frame.setSize(500, 500);
            frame.setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(runButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        return frame;
    }
}
