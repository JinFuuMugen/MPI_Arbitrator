package src.main.visuals;

import src.main.cmd.Launcher;
import src.main.frames.AddressesFrame;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckButton extends JButton {
    public CheckButton(List<String[]> data){
        super("Check hosts");
        addActionListener((e -> {
            ExecutorService executor = Executors.newFixedThreadPool(data.size());
            for (int i = 0; i < data.size(); i++) {
                if (AddressesFrame.addressTable.getModel().getValueAt(i, 3) == Boolean.TRUE) {
                    final int index = i;
                    executor.execute(() -> {
                        StringBuilder command = new StringBuilder("cmd /c \"cd /d ").append(Launcher.getSelectedFilePath()).append(" && ").append((String) AddressesFrame.pathsTable.getValueAt(2, 2)).append(" -timeout 5 -host ");
                        String ip = (String) AddressesFrame.addressTable.getModel().getValueAt(index, 0);
                        command.append(ip).append(" hostname\"");
                        try {
                            Process process = Runtime.getRuntime().exec(command.toString());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            process.waitFor();
                            System.out.println(process.exitValue());
                            if (process.exitValue() != 0)
                                AddressesFrame.addressTable.setValueAt("Cannot get hostname", index, 2);
                            else {
                                AddressesFrame.addressTable.setValueAt(reader.readLine(), index, 2);
                            }
                        } catch (IOException | InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
            }
            executor.shutdown();
        }));
    }
}
