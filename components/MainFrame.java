package my.fileManager.components;

import my.fileManager.Sockets.ClientSocket;
import my.fileManager.managers.OperationManager;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {
    private JTextField ipAddressTextField;
    private JButton button;
    private JLabel errorLabel, enterIpAddressLabel;

    public MainFrame()
    {
        super("File system");
        setSize(600, 600);
        errorLabel = new JLabel();
        ipAddressTextField = new JTextField("localhost");
        enterIpAddressLabel = new JLabel("Enter IP Address:");
        errorLabel.setBounds(10, 10, 400, 10);
        errorLabel.setForeground(Color.RED);
        enterIpAddressLabel.setBounds(5, 505, 100, 30);
        ipAddressTextField.setBounds(5, 530, 575, 30);
        button = new Button("Connect",this, errorLabel, ipAddressTextField);
        add(errorLabel);
        add(enterIpAddressLabel);
        add(button);
        add(ipAddressTextField);
        setLayout(null);
        setResizable(false);
        setVisible(true);
    }

    public void connectionLost()
    {
        errorLabel.setText("Connection lost!");
        ClientSocket.closeConnection();
    }

    public void displayFileSystem()
    {
        JButton addUser = new JButton("Add User");
        addUser.setBounds(480, 10, 100, 20);
        addUser.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Username:");
            String password;
            if (name != null && name.length() > 3)
            {
                password = JOptionPane.showInputDialog("Password:");
                if (password != null && password.length() > 3)
                    ClientSocket.getInstance().createUser(name, password);
            }

        });
        Tree filesTree = new Tree(OperationManager.getConfigatuion(), this);
        add(addUser);
        remove(button);
        remove(ipAddressTextField);
        remove(enterIpAddressLabel);
        add(filesTree);

    }
}
