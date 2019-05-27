package my.fileManager.components;

import my.fileManager.Sockets.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;

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

    public UsersPanel(MainFrame MainFrame, String Title, boolean modal)
    {
        super(MainFrame, Title, modal);
        setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Pair dialogSize = new Pair(screenSize.width/2, screenSize.height/2);
        setBounds(screenSize.width/4, screenSize.height/4, screenSize.width/2,dialogSize.Y/2+210);
        setLayout(null);
        JLabel selectUser = new JLabel("Select user:");
        selectUser.setBounds(10, 0, dialogSize.X-30, 32);
        JList<String> listToDisplay = new JList<>(ClientSocket.getInstance().getUsers()); // NoSuchElementException
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
                            MainFrame.displayFileSystem();
                            UsersPanel.this.dispose();
                        }
                        else
                            errorLabel.setText("Login Failed!");
                    }
                    catch (NoSuchElementException ex)
                    {
                        MainFrame.connectionLost();
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

    public static void display(MainFrame Parent)
    {
        new UsersPanel(Parent, "Users Panel", true);
    }
}
