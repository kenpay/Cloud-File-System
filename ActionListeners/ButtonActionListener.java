package my.fileManager.ActionListeners;

import my.fileManager.Sockets.ClientSocket;
import my.fileManager.components.*;
import my.fileManager.components.Button;
import my.fileManager.core.Drive;
import my.fileManager.core.Folder;
import my.fileManager.managers.OperationManager;

import javax.swing.*;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ButtonActionListener implements ActionListener {

    private MainFrame mainFrame;
    private JLabel errorLabel;
    private JTextField ipAddressTextField;

    public ButtonActionListener(MainFrame MainFrame, JLabel ErrorLabel, JTextField IPAddressTextField)
    {
        mainFrame = MainFrame;
        errorLabel = ErrorLabel;
        ipAddressTextField = IPAddressTextField;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Button button = (Button) e.getSource();
        if (button.getText().equals("Connect"))
        {
            if (!errorLabel.getText().equals(""))
                errorLabel.setText("");
            try {
                InetAddress ipAddress = InetAddress.getByName(ipAddressTextField.getText());
                try
                {
                    ClientSocket.createInstance(ipAddress, 4321);
                    UsersPanel.display(mainFrame);
                } catch (IOException ex) {
                    errorLabel.setText("Connection refused!");
                }
            } catch (UnknownHostException exception) {
                errorLabel.setText("Server IP not reliable!");
            }
        }
        mainFrame.repaint();
    }

}
