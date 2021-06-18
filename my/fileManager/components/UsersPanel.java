package my.fileManager.components;

import my.fileManager.Sockets.ClientSocket;
import my.fileManager.core.Drive;
import my.fileManager.managers.FileManager;
import my.fileManager.managers.OperationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsersPanel extends JDialog {

    public class Pair
    {
        public  final  int X,  Y;
        public  Pair(int x, int y)
        {
            X = x;
            Y = y;
        }
    }

    public UsersPanel(MainFrame mainFrame, String Title, boolean modal) throws Exception
    {
        super(mainFrame, Title, modal);
        setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Pair dialogSize = new Pair(screenSize.width/4, screenSize.height/4);
        setBounds(screenSize.width/3, screenSize.height/3, screenSize.width/4,dialogSize.Y/2+210);
        setLayout(null);
        JLabel selectUser = new JLabel("Select user:");
        selectUser.setBounds(10, 0, dialogSize.X-30, 32);
        DefaultListModel<String> users = ClientSocket.getInstance().getUsers();
        if (users.size() == 0)
        {
            JOptionPane.showMessageDialog(this, "No users found. Please create one!");
            String user = MainFrame.createUser();
            if (user == null)
                throw new Exception("Invalid user!");
            users.addElement(user);
        }
        JList<String> listToDisplay = new JList<>(users);
        listToDisplay.setBounds(10, 32, dialogSize.X - 33, dialogSize.Y/2);
        JLabel enterPasswordLabel = new JLabel("Enter password:");
        enterPasswordLabel.setBounds(10, 38+dialogSize.Y/2, dialogSize.X-30, 32);
        JPasswordField passwordField = new JPasswordField(32);
        passwordField.setBounds(10, 68+dialogSize.Y/2, dialogSize.X - 30, 40);
        passwordField.setHorizontalAlignment(JPasswordField.CENTER);
        passwordField.setEchoChar('_');
        JButton button = new JButton("Select");
        button.setBounds(10, 118+dialogSize.Y/2, dialogSize.X - 30, 32);
        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(10, 146+dialogSize.Y/2, dialogSize.X - 30, 32);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = listToDisplay.getSelectedValue();
                if (selectedValue != null)
                {
                    try
                    {
                        ClientSocket clientSocket = ClientSocket.getInstance();
                        if (clientSocket.getPasswordForUser(selectedValue).equals(new String(passwordField.getPassword())))
                        {
                            clientSocket.getFileSystem();
                            if (OperationManager.getConfiguration() == null)
                            {
                                JOptionPane.showMessageDialog(UsersPanel.this, "No configuration found. Please configure your system!");

                                    try
                                    {
                                        double configurationSpace = Double.parseDouble(JOptionPane.showInputDialog("Enter configuration space:"));
                                        Drive configuration = new Drive("Total space", configurationSpace);
                                        ClientSocket.getInstance().createDatabaseElement(configuration);
                                        OperationManager.setConfiguration(configuration);
                                        FileManager.setCurrentTarget(configuration);
                                    }
                                    catch(Exception exception)
                                    {
                                            JOptionPane.showMessageDialog(UsersPanel.this, "Invalid number!");
                                    }
                            }
                            UsersPanel.this.dispose();
                        }
                        else
                            errorLabel.setText("Login Failed!");
                    }
                    catch (Exception ex)
                    {
                        mainFrame.connectionLost();
                        UsersPanel.this.dispose();
                    }
                }
                else
                    errorLabel.setText("You must select a user!");
            }
        });

        add(listToDisplay);
        add(enterPasswordLabel);
        add(passwordField);
        add(errorLabel);
        add(button);
        add(selectUser);
        setVisible(true);
    }

    public static void display(MainFrame Parent) throws Exception
    {
        JDialog dialog = new UsersPanel(Parent, "Users Panel", true);
    }
}
