package src.main.visuals;

import src.main.frames.AddressesFrame;

import javax.swing.*;
import java.util.List;

public class ClearSelectionsButton extends JButton {
    public ClearSelectionsButton(String name, List<String[]> data) {
        super(name);
        addActionListener(e -> {
            for (int i = 0; i < data.size(); i++) {
                AddressesFrame.addressTable.getModel().setValueAt(false, i, 2);
            }
        });
    }
}
