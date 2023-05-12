package src.main.cmd;

import src.main.frames.AddressesFrame;
import src.main.processors.ArpProcessor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Arbitrator {

    private static String selectedFilePath;

    public static String getSelectedFilePath() {
        return selectedFilePath;
    }

    private static String selectedFileName;

    public static String getSelectedFileName() {
        return selectedFileName;
    }


    public static void main(String[] args) throws IOException {
        try {

            JFileChooser chooser = new JFileChooser();                                                                              //get c/cpp file
            FileNameExtensionFilter filter = new FileNameExtensionFilter("C/C++ files", "c", "cpp");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                // exit program if user cancels file chooser dialog
                return;
            }
            File file = chooser.getSelectedFile();
            selectedFilePath = file.getParent();
            selectedFileName = file.getName();

            List<String[]> data = ArpProcessor.getArps();                                       //get arp table
            AddressesFrame panel = new AddressesFrame(data);                                    //write it to table
            JFrame frame = new JFrame("MPI Arbitrator Application");                       //show table
                                                                                                //____________//
            frame.setSize(800, 600);                                               // show frame //
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                             //             //
            frame.add(panel);                                                                //             //
            frame.pack();                                                                   //             //
            frame.setVisible(true);                                                        //_____________//


            Thread smpdThread = new Thread(() -> {                                              //run smpd
                try {
                    Runtime.getRuntime().exec("cmd.exe /c smpd -d 3");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            smpdThread.start();


        } catch (IOException e) {                                                                       //exception catcher
            // display error message
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            // re-run program if user chooses to try again
            int response = JOptionPane.showConfirmDialog(null, "Would you like to try again?", "Error",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                main(args);
            }
        }
    }
}
