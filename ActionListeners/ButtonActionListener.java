package my.fileManager.ActionListeners;

import my.fileManager.Sockets.ClientSocket;
import my.fileManager.components.Button;
import my.fileManager.components.MainFrame;
import my.fileManager.components.Tree;
import my.fileManager.components.TreeNode;
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
    private Button button;
    private JLabel errorLabel;
    private JTextField ipAddressTextField;
    private ClientSocket clientSocket;
    private JList<String> listToDisplay;
    private JTree filesTree;

    public ButtonActionListener(MainFrame MainFrame, Button Button, JLabel ErrorLabel, JTextField IPAddressTextField)
    {
        mainFrame = MainFrame;
        button = Button;
        errorLabel = ErrorLabel;
        ipAddressTextField = IPAddressTextField;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (button.getText().equals("Connect")) {
            if (!errorLabel.getText().equals(""))
                errorLabel.setText("");
            try {
                InetAddress ipAddress = InetAddress.getByName(ipAddressTextField.getText());
                try
                {
                    clientSocket = new ClientSocket(ipAddress, 4321);
                    listToDisplay = new JList<>(clientSocket.getUsers());
                    listToDisplay.setBounds(10, 10, 200, 400);
                    mainFrame.add(listToDisplay);
                    button.setText("Select");
                } catch (IOException ex) {
                    errorLabel.setText("Connection refused!");
                }
            } catch (UnknownHostException exception) {
                errorLabel.setText("Server IP not reliable!");
            }
        }
        else
        {
            String selectedValue = listToDisplay.getSelectedValue();
            if (selectedValue != null)
            {
                ;
                try{
                    if (clientSocket.getPasswordForUser(selectedValue).equals(ipAddressTextField.getText()))
                    {
                        mainFrame.remove(listToDisplay);
                        JButton addUser = new JButton("Add User");
                        addUser.setBounds(480, 10, 100, 20);
                        JTextField label = new JTextField("47");
                        label.setBounds(470, 50, 70, 20);
                        label.setDragEnabled(true);
                        mainFrame.add(label);
                        mainFrame.add(addUser);
                        addUser.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String name = JOptionPane.showInputDialog("Username:");
                                String password;
                                if (name != null && name.length() > 3)
                                {
                                    password = JOptionPane.showInputDialog("Password:");
                                    if (password != null && password.length() > 3)
                                        clientSocket.createUser(name, password);
                                }

                            }
                        });

                        String nextLine = clientSocket.getFileSystem();

                        String[] fileSystem = nextLine.split("-");
                        String[] drives = fileSystem[0].split(";");
                        ArrayList<TreeNode> drivesArray = new ArrayList<>();
                        if (fileSystem.length > 0)
                        {
                            for (String drive:drives)
                            {
                                String[] driveProperties = drive.split("\\.");
                                Drive driveToCreate = new Drive(Integer.parseInt(driveProperties[0]), driveProperties[1], Double.parseDouble(driveProperties[2]));
                                TreeNode treeNode = new TreeNode(driveToCreate);
                                driveToCreate.setTreeNode(treeNode);
                                OperationManager.addToStorage(treeNode);
                            }
                            String[] folders, files;
                            if (fileSystem.length > 1)
                            {folders = fileSystem[1].split(";");
                                for (String folder:folders)
                                {
                                    String[] folderProperties = folder.split("\\.");
                                    TreeNode folderIn = OperationManager.getFolderById(Integer.parseInt(folderProperties[2]));
                                    if (folderIn != null)
                                        ((Folder) folderIn.getUserObject()).addFile(new Folder(Integer.parseInt(folderProperties[0]), folderProperties[1], Double.parseDouble(folderProperties[3])));

                                }
                                if (fileSystem.length>2){
                                    files = fileSystem[2].split(";");
                                    for (String file:files)
                                    {
                                        String[] fileProperties = file.split("\\.");
                                        TreeNode folderIn = OperationManager.getFolderById(Integer.parseInt(fileProperties[3]));
                                        if (folderIn != null)
                                            ((Folder) folderIn.getUserObject()).addFile(new my.fileManager.core.File(Integer.parseInt(fileProperties[0]), Double.parseDouble(fileProperties[2]), fileProperties[1]));
                                    }}}
                            TreeNode treeNode = new TreeNode("SGF");
                            for (TreeNode node:OperationManager.getDrives())
                                treeNode.add(node);
                            filesTree = new Tree(treeNode, mainFrame);
                            filesTree.setBounds(10, 10, 450, 450);
                            JPopupMenu rightClick = new JPopupMenu("Context menu");
                            JMenu Add = new JMenu("Add...");
                            JMenuItem addNewFile = new JMenuItem("new file");
                            JMenuItem remove = new JMenuItem("Remove");
                            ActionListener popupActionListener = new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (e.getSource() == addNewFile)
                                    {
                                        String fileName = JOptionPane.showInputDialog("Enter file name: ");
                                        if (fileName != null)
                                        {
                                            TreeNode selectedNode = (TreeNode) filesTree.getLastSelectedPathComponent();
                                            if (selectedNode != null)
                                                selectedNode.add(new TreeNode(fileName));
                                            else
                                                JOptionPane.showMessageDialog(filesTree, "You must select a node!");
                                        }
                                    }
                                }
                            };
                            filesTree.setDragEnabled(true);
                            filesTree.setDropMode(DropMode.ON_OR_INSERT);
                            addNewFile.addActionListener(popupActionListener);
                            Add.add(addNewFile);
                            rightClick.add(Add);
                            rightClick.add(remove);
                            button.setEnabled(false);
                            filesTree.addMouseListener(new MouseAdapter(){
                                public void mousePressed(MouseEvent e)
                                {
                                    filesTree.setSelectionPath(filesTree.getPathForLocation(e.getX(), e.getY()));
                                }

                                public void mouseReleased(MouseEvent e)
                                {
                                    if (e.isPopupTrigger() && !filesTree.getRowBounds(0).contains(e.getX(), e.getY()))
                                        rightClick.show((Component) e.getSource(), e.getX(), e.getY());
                                }
                            });
                            mainFrame.add(filesTree);}
                    }
                    else
                        errorLabel.setText("Login Failed!");
                }
                catch (NoSuchElementException ex)
                {
                    errorLabel.setText("Connection lost!");
                    button.setText("Connect");
                    mainFrame.remove(listToDisplay);
                }
            }
        }
        mainFrame.repaint();
    }

}
