package my.fileManager.components;

import my.fileManager.ActionListeners.ButtonActionListener;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Button extends JButton {

    private ActionListener buttonActionListener;

    public Button(String Name, MainFrame MainFrame, JLabel ErrorLabel, JTextField IPAddressTextFiles)
    {
        super(Name);
        buttonActionListener = new ButtonActionListener(MainFrame, this, ErrorLabel, IPAddressTextFiles);
        setBounds(460, 485, 120, 40);
        addActionListener(buttonActionListener);
    }

}
