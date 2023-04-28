import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

class CheckboxRenderer extends JCheckBox implements TableCellRenderer { // third column ("selected")

    public CheckboxRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        setSelected((value != null && (Boolean) value));
        return this;
    }
}