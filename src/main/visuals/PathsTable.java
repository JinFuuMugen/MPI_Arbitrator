package src.main.visuals;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;

public class PathsTable extends JTable {

    private static final DefaultTableModel model = new DefaultTableModel(
            new Object[][]{
                    {"Mpiexec path", "Full path to mpiexec.exe", "\"C:\\Program Files\\Microsoft MPI\\Bin\\mpiexec.exe\""},
                    {"MPI Lib path", "Full path to mpi/lib folder", "\"C:\\Program Files (x86)\\Microsoft SDKs\\MPI\\Lib\\x64\""},
                    {"MPI Include path", "Full path to mpi/Include folder", "\"C:\\Program Files (x86)\\Microsoft SDKs\\MPI\\Include\""},
                    {"C/C++ compiler path", "Full path to compiler", "\"C:\\MinGW\\bin\\g++.exe\""},
                    {"Libs names", "Names of all libraries connected to program (space separated)", "msmpi"}
            },
            new String[]{"Path name", "Description", "Value", "Configure path"}
    ){
        @Override
        public boolean isCellEditable(int row, int column) {
            // Make columns 0, 1, and 2 non-editable
            return column != 0 && column != 1 && column != 2;
        }
    };

    public PathsTable() {
        super(model);
        getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        getColumnModel().getColumn(3).setCellEditor(new ButtonEditor());
        getColumnModel().getColumn(3).setPreferredWidth(100);

    }

    // Custom renderer for rendering buttons
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Configure");
            return this;
        }
    }

    // Custom editor for handling button clicks
    private class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String path;

        public ButtonEditor() {
            super(new JTextField());
            setClickCountToStart(1);

            button = new JButton();
            button.addActionListener(e -> {
                int row = getEditingRow();
                switch (row) {
                    case 0, 3 -> {
                        JFileChooser cppFileChooser = new JFileChooser();
                        cppFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        int cppResult = cppFileChooser.showOpenDialog(button);
                        if (cppResult == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = cppFileChooser.getSelectedFile();
                            path = selectedFile.getAbsolutePath();
                            model.setValueAt(path, row, 2); // Update the corresponding cell value in the model
                        }
                    }
                    case 1, 2 -> {
                        JFileChooser folderChooser = new JFileChooser();
                        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int folderResult = folderChooser.showOpenDialog(button);
                        if (folderResult == JFileChooser.APPROVE_OPTION) {
                            File selectedFolder = folderChooser.getSelectedFile();
                            path = selectedFolder.getAbsolutePath();
                            model.setValueAt(path, row, 2); // Update the corresponding cell value in the model
                        }
                    }
                    case 4 -> {
                        JFrame frame = new JFrame("Text Editor");
                        JTextArea textArea = new JTextArea(5, 50);
                        textArea.setFont(new Font("Arial", Font.PLAIN, 16)); // Установка нового шрифта и размера
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        JButton saveButton = new JButton("Save");
                        saveButton.addActionListener(e1 -> {
                            String text = textArea.getText();
                            model.setValueAt(text, row, 2); // Update the corresponding cell value in the model
                            frame.dispose();
                        });
                        JPanel panel = new JPanel(new BorderLayout());
                        panel.add(scrollPane, BorderLayout.CENTER);
                        panel.add(saveButton, BorderLayout.SOUTH);
                        frame.getContentPane().add(panel);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
                    }
                }
                stopCellEditing();
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        public Object getCellEditorValue() {
            return path;
        }
    }
}
