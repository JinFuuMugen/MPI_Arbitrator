package src.main.visuals;

import src.main.processors.ArpProcessor;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.util.List;


public class AddressesTable extends AbstractTableModel {
    private final String[] columnNames = { "IP", "MAC", "Selected", "Processes" }; // make 4-column table
    private final Class[] columnTypes = { String.class, String.class, Boolean.class, Integer.class};
    private final Object[][] tableData;

    public AddressesTable() throws IOException {
        List<String[]> data = ArpProcessor.getArps();
        int rowCount = data.size();
        this.tableData = new Object[rowCount][4];
        for (int i = 0; i < rowCount; i++) {
            this.tableData[i][0] = data.get(i)[0];
            this.tableData[i][1] = data.get(i)[1];
            this.tableData[i][2] = Boolean.FALSE;
            this.tableData[i][3] = 0; // 4th column ComboBox model
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
        return col == 2 || (col == 3 && getValueAt(row, 2).equals(true)); // make last column editable
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if ((value.equals(false)) && col == 2){
            setValueAt(0, row, 3);
        }
        this.tableData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}