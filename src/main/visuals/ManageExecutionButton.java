package src.main.visuals;

import src.main.cmd.Launcher;
import src.main.frames.AddressesFrame;
import src.main.frames.ExecutionFrame;

import javax.swing.*;
import java.io.*;

public class ManageExecutionButton extends JButton{

    private void WriteHostsFile() throws IOException {
        File file = new File(Launcher.getSelectedFilePath() + "hosts.txt");
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print("");
        }

        for (int i = 0; i < AddressesFrame.addressTable.getRowCount(); i++){
            if (AddressesFrame.addressTable.getValueAt(i, 3).equals(true)){
                if (!AddressesFrame.addressTable.getValueAt(i, 4).equals(0)){
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
                        writer.write(AddressesFrame.addressTable.getValueAt(i, 0) + " " + AddressesFrame.addressTable.getValueAt(i, 4));
                        writer.write(System.lineSeparator());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }


    public ManageExecutionButton(String name){
        super(name);                              // creating manage button
        addActionListener(e -> {
            try {
                WriteHostsFile();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JFrame frame = ExecutionFrame.GetExecutionFrame();
            frame.setVisible(true);
        });
    }

}
