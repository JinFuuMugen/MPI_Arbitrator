package src.main.cmd;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import src.main.frames.AddressesFrame;
import src.main.processors.ArpProcessor;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Launcher {

    private static final ImageIcon icon = new ImageIcon("src/main/img/logo.png");
    private static String selectedFilePath;

    public static String getSelectedFilePath() {
        return selectedFilePath;
    }

    private static String selectedFileName;

    public static String getSelectedFileName() {
        return selectedFileName;
    }

    public static void renderMainFrame(JFrame frame) throws IOException {
        List<String[]> data = ArpProcessor.getArps();                                       //get arp table
        AddressesFrame panel = new AddressesFrame(data);                                    //write it to table

        Image image = icon.getImage();
        frame.setIconImage(image);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setPreferredSize(new Dimension(frame.getPreferredSize().width, frame.getPreferredSize().height));
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {

        Thread smpdThread = new Thread(() -> {                                              //run smpd
            try {
                Runtime.getRuntime().exec("cmd.exe /c smpd -d 3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        smpdThread.start();

        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
            UIManager.put("Table.cellNoFocusBorder", true);
            UIManager.put("Table.gridColor", new Color(80, 80, 80));
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", true);

            JFileChooser chooser = new JFileChooser();                                                                              //get c/cpp file
            FileNameExtensionFilter filter = new FileNameExtensionFilter("C/C++ files", "c", "cpp");

            chooser.setFileFilter(filter);
            if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = chooser.getSelectedFile();
            selectedFilePath = file.getParent();
            selectedFileName = file.getName();

            JFrame frame = new JFrame("MPI Arbitrator Application");                       //show table
            renderMainFrame(frame);
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
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }
}
