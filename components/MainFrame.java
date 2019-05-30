package my.fileManager.components;

import my.fileManager.Sockets.ClientSocket;
import my.fileManager.core.Drive;
import my.fileManager.core.File;
import my.fileManager.core.Folder;
import my.fileManager.core.FolderProperties;
import my.fileManager.managers.FileManager;
import my.fileManager.managers.OperationManager;
import sun.nio.cs.ext.EUC_JP_Open;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class MainFrame extends JFrame {
    private JTextField ipAddressTextField, currentTargetPath;
    private JButton connectButton;
    private JLabel errorLabel, enterIpAddressLabel, nothingLabel;
    private JPanel fileSystem;
    private int width, height, rows;

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


    public void redisplayFileSystem()
    {
        ArrayList<File> files = FileManager.getCurrentTarget().getFiles();
        int filesSize = files.size();
        GridLayout layout = (GridLayout) fileSystem.getLayout();
        if ((filesSize+1)/3 != rows)
        {
            rows = (filesSize+1)/3;
            layout.setRows(rows);
        }
        int columns = 3;
        if (filesSize<3 && filesSize > 0)
            columns = filesSize;
        if (layout.getColumns() != columns)
            layout.setColumns(columns);

        fileSystem.removeAll();
        for(File file:files)
            fileSystem.add(file);
        if (filesSize == 0)
            fileSystem.add(nothingLabel);
        Folder path = FileManager.getCurrentTarget();
        currentTargetPath.setText(path.Properties().getPath()+path.Properties().getName()+":"+((FolderProperties)path.Properties()).getSpace());
        revalidate();
    }

    private void setCurrentTargetPath(Folder path)
    {
        FileManager.getCurrentTarget().deselectAll();
        FileManager.setCurrentTarget(path);
        currentTargetPath.setText(path.Properties().getPath()+path.Properties().getName()+":"+((FolderProperties)path.Properties()).getSpace());
        redisplayFileSystem();
        repaint();
    }

    public static String createUser()
    {
        String name = JOptionPane.showInputDialog("Username:");
        String password;
        if (name != null && name.length() > 3)
        {
            password = JOptionPane.showInputDialog("Password:");
            if (password != null && password.length() > 3)
                try
                {
                    ClientSocket.getInstance().createUser(name, password);
                    return name;
                }
            catch (Exception e)
            {
                System.err.println("Connection problems");
            }
        }
        return null;
    }

    private void displayFileSystem()
    {
        FileManager.setCurrentTarget(OperationManager.getConfigatuion());
        JButton addUser = new JButton("Add User");
        int yToDraw = 10;
        addUser.setBounds(width-120, yToDraw, 100, 30);
        yToDraw += 40;
        JButton createButton = new JButton("Create");
        createButton.setBounds(width-120, yToDraw, 100, 30);
        yToDraw += 40;
        currentTargetPath = new JTextField(120);
        currentTargetPath.setBounds(0, 0, width-130, 30);
        currentTargetPath.setEnabled(false);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        fileSystem = new JPanel(new GridLayout(1, 3));
        fileSystem.setBounds(0, 32, width-130, height);
        fileSystem.setBackground(Color.GRAY);
        panel.add(currentTargetPath);
        panel.add(fileSystem);
        JButton openButton = new JButton("Open");
        openButton.setBounds(width-120, yToDraw, 100, 30);
        yToDraw += 40;
        openButton.setEnabled(false);
        JButton backButton = new JButton("Back");
        backButton.setBounds(width-120, yToDraw, 100, 30);
        yToDraw += 40;
        backButton.setEnabled(false);
        JButton cutButton = new JButton("Cut");
        cutButton.setBounds(width-120, yToDraw, 100, 30);
        yToDraw += 40;
        cutButton.setEnabled(false);
        JButton copyButton = new JButton("Copy");
        copyButton.setBounds(width-120, yToDraw, 100, 30);
        copyButton.setEnabled(false);
        yToDraw += 40;
        JButton removeButton = new JButton("Remove");
        removeButton.setBounds(width-120, yToDraw, 100, 30);
        removeButton.setEnabled(false);
        yToDraw += 40;
        JButton pasteButton = new JButton("Paste");
        pasteButton.setBounds(width-120, yToDraw, 100, 30);
        pasteButton.setEnabled(false);
        yToDraw += 40;
        JButton renameButton = new JButton("Rename");
        renameButton.setBounds(width-120, yToDraw, 100, 30);
        renameButton.setEnabled(false);
        ActionListener actionLister = e -> {
            Object source = e.getSource();
            Folder currentTarget = FileManager.getCurrentTarget();
            if (source == copyButton || source == cutButton)
            {
                FileManager.setClipboard(currentTarget.getSelectedFiles().get(0));
                String action = "Copy";
                if (source == cutButton)
                    action = "Cut";
                FileManager.setAction(action);
                pasteButton.setEnabled(true);
            }
            else
            {
                if (source == removeButton)
                {
                    File selectedFile = currentTarget.getSelectedFiles().get(0);
                    currentTarget.remove(selectedFile);
                    ClientSocket.getInstance().removeElement(selectedFile);
                }
                else if (source == renameButton)
                {
                    File selectedFile = currentTarget.getSelectedFiles().get(0);
                    String newName = JOptionPane.showInputDialog("Enter new name:");
                    selectedFile.Rename(newName);
                    ClientSocket.getInstance().renameElement(selectedFile);
                }
                else
                {
                    String Action = FileManager.getAction();
                    File fileToCreate, clipboardFile = FileManager.getClipboardFile();
                    if (Action.equals("Copy"))
                    {
                        if (clipboardFile instanceof Folder)
                            fileToCreate = new Folder((Folder)clipboardFile);
                        else
                            fileToCreate = new File(clipboardFile);
                    }
                    else
                        fileToCreate = clipboardFile;
                    try
                    {
                        currentTarget.addFile(fileToCreate);
                        if (Action.equals("Copy"))
                            ClientSocket.getInstance().createDatabaseElement(fileToCreate);
                        else
                            ClientSocket.getInstance().changeParentForElement(fileToCreate);
                        pasteButton.setEnabled(false);
                        FileManager.setClipboard(null);
                    }
                    catch (Exception exception)
                    {
                        JOptionPane.showMessageDialog(MainFrame.this, "Not enough memory!");
                    }
                }
                redisplayFileSystem();
                repaint();
            }
        };
        cutButton.addActionListener(actionLister);
        copyButton.addActionListener(actionLister);
        removeButton.addActionListener(actionLister);
        pasteButton.addActionListener(actionLister);
        renameButton.addActionListener(actionLister);
        backButton.addActionListener(e -> {
            Folder  currentTarget = FileManager.getCurrentTarget(),
                    parent = currentTarget.getFileParent();
            setCurrentTargetPath(parent);
            if (parent.getFileParent() == null) {
                backButton.setEnabled(false);
                pasteButton.setEnabled(false);
            }
            if (cutButton.isEnabled())
                cutButton.setEnabled(false);
            if (copyButton.isEnabled())
                copyButton.setEnabled(false);
            if (removeButton.isEnabled())
                removeButton.setEnabled(false);
            if(renameButton.isEnabled())
                renameButton.setEnabled(false);
        });
        openButton.addActionListener(e -> {
            setCurrentTargetPath((Folder) (FileManager.getCurrentTarget().getSelectedFiles()).get(0));
            if (!backButton.isEnabled())
                backButton.setEnabled(true);
            if (!pasteButton.isEnabled() && FileManager.getClipboardFile() != null)
                pasteButton.setEnabled(true);
            openButton.setEnabled(false);
            if (cutButton.isEnabled())
                cutButton.setEnabled(false);
            if (copyButton.isEnabled())
                copyButton.setEnabled(false);
            if (removeButton.isEnabled())
                removeButton.setEnabled(false);
            if (renameButton.isEnabled())
                renameButton.setEnabled(false);
        });
        addUser.addActionListener(e -> createUser());
        currentTargetPath.setText(FileManager.getCurrentTarget().Properties().getName());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Folder currentTarget = FileManager.getCurrentTarget();
                currentTarget.deselectAll();
                if (openButton.isEnabled())
                    openButton.setEnabled(false);
                if (cutButton.isEnabled())
                    cutButton.setEnabled(false);
                if (copyButton.isEnabled())
                    copyButton.setEnabled(false);
                if (removeButton.isEnabled())
                    removeButton.setEnabled(false);
                if (renameButton.isEnabled())
                    renameButton.setEnabled(false);
            }
        });
        fileSystem.addMouseListener(new MouseAdapter()
        {
            public void mousePressed (MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    ArrayList<File> selectedFiles = FileManager.getCurrentTarget().getSelectedFiles();
                    if (selectedFiles.size() > 0)
                    {
                        File selectedFile = (FileManager.getCurrentTarget().getSelectedFiles()).get(0);
                        if (selectedFile instanceof Folder)
                        {
                            setCurrentTargetPath((Folder) selectedFile);
                            if (!backButton.isEnabled())
                                backButton.setEnabled(true);
                            if (!pasteButton.isEnabled() && FileManager.getClipboardFile() != null)
                                pasteButton.setEnabled(true);
                            openButton.setEnabled(false);
                            if (cutButton.isEnabled())
                                cutButton.setEnabled(false);
                            if (copyButton.isEnabled())
                                copyButton.setEnabled(false);
                            if (removeButton.isEnabled())
                                removeButton.setEnabled(false);
                            if (renameButton.isEnabled())
                                renameButton.setEnabled(false);
                        }
                    }
                }
                else
                {
                    try
                    {
                        File file = (File) fileSystem.getComponentAt(e.getX(), e.getY());
                        Folder currentTarget = FileManager.getCurrentTarget();
                        currentTarget.deselectAll();
                        currentTarget.selectFile(file);
                        if (!cutButton.isEnabled())
                            cutButton.setEnabled(true);
                        if (!copyButton.isEnabled())
                            copyButton.setEnabled(true);
                        if (!removeButton.isEnabled())
                            removeButton.setEnabled(true);
                        if (!renameButton.isEnabled())
                            renameButton.setEnabled(true);
                        if (file instanceof Folder)
                        {
                            if (!openButton.isEnabled())
                                openButton.setEnabled(true);
                            if (file instanceof Drive)
                            {
                                if (cutButton.isEnabled())
                                    cutButton.setEnabled(false);
                                if (copyButton.isEnabled())
                                    copyButton.setEnabled(false);
                                if (removeButton.isEnabled())
                                    removeButton.setEnabled(false);
                            }
                        }
                        else if (openButton.isEnabled())
                                openButton.setEnabled(false);
                    }
                    catch (Exception e1)
                    {
                        if (openButton.isEnabled())
                            openButton.setEnabled(false);
                        if (cutButton.isEnabled())
                            cutButton.setEnabled(false);
                        if (copyButton.isEnabled())
                            copyButton.setEnabled(false);
                        if (removeButton.isEnabled())
                            removeButton.setEnabled(false);
                        if (removeButton.isEnabled())
                            renameButton.setEnabled(false);
                        FileManager.getCurrentTarget().deselectAll();
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(0, 0, width-130,height);
        createButton.addActionListener(e -> {
            CreateElementDialog.display(MainFrame.this);
        });
        nothingLabel = new JLabel("Nothing");
        nothingLabel.setHorizontalAlignment(JLabel.CENTER);
        add(scrollPane);
        add(createButton);
        add(addUser);
        add(openButton);
        add(backButton);
        add(cutButton);
        add(copyButton);
        add(removeButton);
        add(pasteButton);
        add(renameButton);
        remove(connectButton);
        remove(ipAddressTextField);
        remove(enterIpAddressLabel);
        remove(errorLabel);
        redisplayFileSystem();
    }
}
