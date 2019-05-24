package my.fileManager.components;
import my.fileManager.core.Drive;
import my.fileManager.core.Folder;
import my.fileManager.managers.OperationManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
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
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class MainFrame extends JFrame {

    private JTextField ipAddressTextField;
    private JButton button;
    private JLabel errorLabel;

    public MainFrame()
    {
        super("File system");
        setSize(600, 600);
        errorLabel = new JLabel();
        ipAddressTextField = new JTextField("localhost");
        button = new Button("Connect",this, errorLabel, ipAddressTextField);
        JLabel enterIpAddressLabel = new JLabel("Enter IP Address:");
        errorLabel.setBounds(10, 10, 400, 10);
        errorLabel.setForeground(Color.RED);
        enterIpAddressLabel.setBounds(5, 505, 100, 30);
        ipAddressTextField.setBounds(5, 530, 575, 30);
        add(errorLabel);
        add(enterIpAddressLabel);
        add(button);
        add(ipAddressTextField);
        setLayout(null);
        setVisible(true);
    }
}
