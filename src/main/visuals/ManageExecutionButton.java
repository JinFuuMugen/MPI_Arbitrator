package src.main.visuals;

import src.main.frames.ExecutionFrame;

import javax.swing.*;

public class ManageExecutionButton extends JButton{

    private void WriteHostsFile(){
        //TODO finish this function
    }


    public ManageExecutionButton(String name){
        super(name);                              // creating manage button
        addActionListener(e -> {
            JFrame frame = ExecutionFrame.GetExecutionFrame();
            frame.setVisible(true);
        });
    }

}
