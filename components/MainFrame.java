package my.fileManager.components;

import my.fileManager.Sockets.ClientSocket;
import my.fileManager.core.Drive;
import my.fileManager.core.File;
import my.fileManager.core.Folder;
import my.fileManager.managers.FileManager;
import my.fileManager.managers.OperationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class MainFrame extends JFrame {
    private JTextField ipAddressTextField, currentTargetPath;
    private JButton connectButton;
    private JLabel errorLabel, enterIpAddressLabel;
    private JPanel fileSystem;
    private int width, height;

    public MainFrame()
    {
        super("File system");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (screenSize.width*2)/3;
        height = (screenSize.height*2)/3;
        setBounds(width>>2,height>>2, width, height);
        errorLabel = new JLabel();
        errorLabel.setBounds(10, 10, 200, 10);
        errorLabel.setForeground(Color.RED);
        enterIpAddressLabel = new JLabel("Enter IP Address:");
        enterIpAddressLabel.setBounds(10, height-110, 200, 34);
        ipAddressTextField = new JTextField("localhost");
        ipAddressTextField.setBounds(10, height-80, width-30, 34);
        connectButton = new JButton("Connect");
        connectButton.setBounds(width-120, height-120, 100, 34);
        connectButton.addActionListener(e ->
        {
            if (!errorLabel.getText().equals(""))
                errorLabel.setText("");
            try
            {
                InetAddress ipAddress = InetAddress.getByName(ipAddressTextField.getText());
                ClientSocket.createInstance(ipAddress, 4321);
                UsersPanel.display(MainFrame.this);
                displayFileSystem();
            }
            catch (UnknownHostException exception)
            {
                errorLabel.setText("Server IP not reliable!");
            }
            catch (Exception ex)
            {
                errorLabel.setText("Connection refused!");
            }
            MainFrame.this.repaint();
        });
        add(errorLabel);
        add(enterIpAddressLabel);
        add(connectButton);
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


    private int rows=0;

    public void redisplayFileSystem()
    {
        ArrayList<File> files = FileManager.getCurrentTarget().getFiles();
        int filesSize = files.size();
        if ((filesSize+1)/3 != rows)
        {
            rows = (filesSize+1)/3;
            GridLayout layout = (GridLayout) fileSystem.getLayout();
            layout.setRows(rows);
        }
        fileSystem.removeAll();
        for(File file:files)
            fileSystem.add(file);
        if (filesSize == 0)
            fileSystem.add(new JLabel("Nothing"));
        revalidate();
    }

    private void setCurrentTargetPath(Folder path)
    {
        FileManager.getCurrentTarget().deselectAll();
        FileManager.setCurrentTarget(path);
        currentTargetPath.setText(path.Properties().getPath()+path.Properties().getName());
        redisplayFileSystem();
        repaint();
    }

    public void displayFileSystem()
    {
        FileManager.setCurrentTarget(OperationManager.getConfigatuion());
        JButton addUser = new JButton("Add User");
        int yToDraw = 10;
        addUser.setBounds(width-120, yToDraw, 100, 20);
        yToDraw += 30;
        JButton createButton = new JButton("Create");
        createButton.setBounds(width-120, yToDraw, 100, 20);
        yToDraw += 30;
        currentTargetPath = new JTextField(120);
        currentTargetPath.setBounds(0, 0, width-130, 32);
        currentTargetPath.setEnabled(false);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        fileSystem = new JPanel(new GridLayout(1, 3));
        fileSystem.setBounds(0, 32, width-130, height);
        panel.add(currentTargetPath);
        panel.add(fileSystem);
        JButton openButton = new JButton("Open");
        openButton.setBounds(width-120, yToDraw, 100, 20);
        yToDraw += 30;
        openButton.setEnabled(false);
        JButton backButton = new JButton("Back");
        backButton.setBounds(width-120, yToDraw, 100, 20);
        backButton.setEnabled(false);
        backButton.addActionListener(e -> {
            Folder  currentTarget = FileManager.getCurrentTarget(),
                    parent = currentTarget.getFileParent();
            setCurrentTargetPath(parent);
            if (parent.getFileParent() == null)
                backButton.setEnabled(false);
        });
        openButton.addActionListener(e -> {
            setCurrentTargetPath((Folder) (FileManager.getCurrentTarget().getSelectedFiles()).get(0));
            if (!backButton.isEnabled())
                backButton.setEnabled(true);
            openButton.setEnabled(false);
        });
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
        currentTargetPath.setText(FileManager.getCurrentTarget().Properties().getName());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Folder currentTarget = FileManager.getCurrentTarget();
                currentTarget.deselectAll();
                if (openButton.isEnabled())
                    openButton.setEnabled(false);
            }
        });
        fileSystem.addMouseListener(new MouseAdapter()
        {
            public void mousePressed (MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    setCurrentTargetPath((Folder) (FileManager.getCurrentTarget().getSelectedFiles()).get(0));
                    if (!backButton.isEnabled())
                        backButton.setEnabled(true);
                    openButton.setEnabled(false);
                }
                else
                {
                    try
                    {
                        File file = (File) fileSystem.getComponentAt(e.getX(), e.getY());
                        Folder currentTarget = FileManager.getCurrentTarget();
                        currentTarget.deselectAll();
                        currentTarget.selectFile(file);
                        if (file instanceof Folder)
                        {
                            if (!openButton.isEnabled())
                                openButton.setEnabled(true);
                        }
                        else if (openButton.isEnabled())
                            openButton.setEnabled(false);
                    }
                    catch (Exception e1)
                    {
                        openButton.setEnabled(false);
                    }
                }
            }
            public void mouseReleased(MouseEvent e)
            {
                /*
                if (e.isPopupTrigger())
                    rightClick.show((Component) e.getSource(), e.getX(), e.getY());
              */
            }
        });
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(0, 0, width-130,height);
        createButton.addActionListener(e -> {
            CreateElementDialog.display(MainFrame.this);
        });
        add(scrollPane);
        add(createButton);
        add(addUser);
        add(openButton);
        add(backButton);
        remove(connectButton);
        remove(ipAddressTextField);
        remove(enterIpAddressLabel);
        remove(errorLabel);
        redisplayFileSystem();

    }
}
