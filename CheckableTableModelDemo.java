import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class CheckableTableModelDemo extends JPanel {
    private final String[] columnNames = { "IP", "MAC", "Selected" };
    private final List<String[]> data;

    public CheckableTableModelDemo(List<String[]> data) {
        super(new BorderLayout());
        this.data = data;
        JTable table = new JTable(new CheckableTableModel());
        table.getColumnModel().getColumn(2).setCellRenderer(new CheckboxRenderer());
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.top = 5;
        gbc.insets.bottom = 5;
        gbc.insets.left = 10;
        gbc.anchor = GridBagConstraints.WEST;
        JButton clearBtn = new JButton("Clear Selection");
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < data.size(); i++) {
                    table.getModel().setValueAt(false, i, 2);
                }
            }
        });
        buttonPanel.add(clearBtn, gbc);
        gbc.gridx++;
        gbc.insets.left = 20;
        gbc.anchor = GridBagConstraints.EAST;
        JButton checkBtn = new JButton("Check IPS");
        checkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder output = new StringBuilder();
                for (int i = 0; i < data.size(); i++) {
                    if (table.getModel().getValueAt(i, 2) == Boolean.TRUE) {
                        String ip = (String) table.getModel().getValueAt(i, 0);
                        try {
                            Process process = Runtime.getRuntime().exec("ping " + ip + " -n 2 -l en"); // run ping
                                                                                                       // command
                            try (BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(process.getInputStream(), "CP866"))) {
                                String line;
                                output.append("Output for ").append(ip).append(":\n");
                                while ((line = reader.readLine()) != null) {
                                    output.append(line).append("\n");
                                }
                                output.append("\n");
                            }
                        } catch (IOException ex) {
                            output.append("Error running ping command for ").append(ip).append(": ")
                                    .append(ex.getMessage()).append("\n\n");
                        }
                    }
                }
                JFrame outputFrame = new JFrame("Ping Output");
                JTextArea outputArea = new JTextArea(output.toString());
                JScrollPane scrollPane = new JScrollPane(outputArea);
                outputFrame.add(scrollPane);
                outputFrame.setSize(500, 500);
                outputFrame.setVisible(true);
            }
        });
        buttonPanel.add(checkBtn, gbc);
        gbc.gridx++;
        gbc.insets.left = 20;
        gbc.anchor = GridBagConstraints.EAST;
        JComboBox<Integer> comboBox = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            comboBox.addItem(i);
        }
        buttonPanel.add(new JLabel("Number of cores: "), gbc);
        gbc.gridx++;
        gbc.insets.left = 20;
        gbc.anchor = GridBagConstraints.EAST;
        buttonPanel.add(comboBox, gbc);
        add(buttonPanel, BorderLayout.SOUTH);
        JButton openBtn = new JButton("Open File");
        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("."));
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "C/C++ files", "c", "cpp");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(getParent());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    // TODO: Prase the file
                }
            }
        });
        gbc.gridx++;
        gbc.insets.left = 20;
        gbc.anchor = GridBagConstraints.EAST;
        buttonPanel.add(openBtn, gbc);
        JButton runBtn = new JButton("Run");
        runBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Make run command
            }
        });
        gbc.gridx++;
        gbc.insets.left = 20;
        gbc.anchor = GridBagConstraints.EAST;
        buttonPanel.add(runBtn, gbc);
    }

    class CheckableTableModel extends AbstractTableModel {

        private final Class[] columnTypes = { String.class, String.class, Boolean.class };
        private final Object[][] data;

        public CheckableTableModel() {
            int rowCount = CheckableTableModelDemo.this.data.size();
            data = new Object[rowCount][3];
            for (int i = 0; i < rowCount; i++) {
                data[i][0] = CheckableTableModelDemo.this.data.get(i)[0];
                data[i][1] = CheckableTableModelDemo.this.data.get(i)[1];
                data[i][2] = Boolean.FALSE;
            }
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Class getColumnClass(int col) {
            return columnTypes[col];
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col == 2;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }

    }

    class CheckboxRenderer extends JCheckBox implements TableCellRenderer {

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

    public static void main(String[] args) throws IOException {
        List<String[]> data = ArpTable.getArps();
        CheckableTableModelDemo panel = new CheckableTableModelDemo(data);
        JFrame frame = new JFrame("Checkable Table Model Demo");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}