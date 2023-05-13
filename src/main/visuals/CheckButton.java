package src.main.visuals;

import src.main.cmd.Arbitrator;
import src.main.frames.AddressesFrame;
import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class CheckButton extends JButton {

    public CheckButton(String name, List<String[]> data){
        super(name);
        addActionListener(e -> {
            StringBuilder command = new StringBuilder("cmd /c start cmd /k \" cd /d" + Arbitrator.getSelectedFilePath());
            for (int i = 0; i < data.size(); i++) {
                if (AddressesFrame.table.getModel().getValueAt(i, 2) == Boolean.TRUE) {
                    String ip = (String) AddressesFrame.table.getModel().getValueAt(i, 0);
                    command.append(" && echo Checking hostname of ").append(ip).append(": ");
                    command.append(" && mpiexec -timeout 15 -host ").append(ip).append(" hostname");
                    command.append(" && echo __________________");
                }
            }
            command.append("\"");
            try {
                Runtime.getRuntime().exec(String.valueOf(command));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}