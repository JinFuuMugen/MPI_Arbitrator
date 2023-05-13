package src.main.visuals;

import src.main.cmd.Arbitrator;
import src.main.frames.AddressesFrame;
import src.main.frames.ExecutionFrame;

import javax.swing.*;
import java.io.*;

public class ManageExecutionButton extends JButton{

    private void WriteHostsFile() throws IOException {
        //TODO finish this function
        File file = new File(Arbitrator.getSelectedFilePath() + "hosts.txt");
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print("");
        }

        for (int i = 0; i < AddressesFrame.table.getRowCount(); i++){
            if (AddressesFrame.table.getValueAt(i, 2).equals(true)){
                if (!AddressesFrame.table.getValueAt(i, 3).equals(0)){
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
                        writer.write(AddressesFrame.table.getValueAt(i, 0) + " " + AddressesFrame.table.getValueAt(i, 3)); // Записываем строку в файл
                        writer.write(System.lineSeparator()); // Записываем перевод строки
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
