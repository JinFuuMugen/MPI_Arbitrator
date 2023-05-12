package src.main.visuals;

import src.main.processors.ArpProcessor;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.util.List;

public class AddressesTable extends AbstractTableModel {
    private final String[] columnNames = { "IP", "MAC", "Selected" }; // make 3-column table
    private final Class[] columnTypes = { String.class, String.class, Boolean.class };
    private final Object[][] tableData;

    public AddressesTable() throws IOException {
        List<String[]> data = ArpProcessor.getArps();
        int rowCount = data.size();
        this.tableData = new Object[rowCount][3];
        for (int i = 0; i < rowCount; i++) {
            this.tableData[i][0] = data.get(i)[0];
            this.tableData[i][1] = data.get(i)[1];
            this.tableData[i][2] = Boolean.FALSE;
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
        return tableData.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return tableData[row][col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 2;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        this.tableData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}