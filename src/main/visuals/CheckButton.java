package src.main.visuals;

import src.main.cmd.Arbitrator;
import src.main.frames.AddressesFrame;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CheckButton extends JButton {

    public CheckButton(String name, List<String[]> data){
        super(name);
        addActionListener(e -> {
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < data.size(); i++) {
                if (AddressesFrame.table.getModel().getValueAt(i, 2) == Boolean.TRUE) {
                    String ip = (String) AddressesFrame.table.getModel().getValueAt(i, 0);
                    try {
                        String command = "cmd /c cd /d " + Arbitrator.getSelectedFilePath()
                                + " && mpiexec -timeout 5 -host " + ip + " hostname";
                        Process process = Runtime.getRuntime()
                                .exec(command);
                        try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(process.getInputStream(), "CP866"))) {
                            String line;
                            output.append("Output for ").append(ip).append(":\n");
                            while ((line = reader.readLine()) != null) {
                                output.append(line).append("\n");
                            }
                            output.append("\n");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } catch (IOException ex) {
                        output.append("Error running ping command for ").append(ip).append(": ")
                                .append(ex.getMessage()).append("\n\n");
                    }
                }
            }
            JFrame outputFrame = new JFrame("Ping Output");
            JTextArea outputArea = new JTextArea(output.toString());
            outputArea.setEditable(false);
            JScrollPane scrollPane1 = new JScrollPane(outputArea);
            outputFrame.add(scrollPane1);
            outputFrame.setSize(500, 500);
            outputFrame.setVisible(true);
        });
    }
}