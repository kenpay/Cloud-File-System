package my.fileManager.components;

import my.fileManager.Sockets.ClientSocket;
import my.fileManager.core.Drive;
import my.fileManager.core.File;
import my.fileManager.core.Folder;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tree extends JTree {

    private void showCreateElementDialog(String type, Folder selectedNode)
    {
        String fileName = JOptionPane.showInputDialog("Enter " + type + " name: ");
        if (fileName != null)
        {
            try
            {
                boolean typeEqualsFile = type.equals("file");
                double fileSize = Double.parseDouble(JOptionPane.showInputDialog("Enter " + type + " " + (typeEqualsFile ? "size":"space") + ":"));
                File fileToAdd;
                if (typeEqualsFile)
                    fileToAdd = new File(fileSize, fileName);
                else if (type.equals("folder"))
                    fileToAdd = new Folder(fileName, fileSize);
                else
                    fileToAdd = new Drive(fileName, fileSize);
                selectedNode.addFile(fileToAdd);
                ClientSocket.getInstance().createDatabaseElemenet(fileToAdd);
            }
            catch(Exception exception)
            {
                if (exception.getMessage().equals("Not enough memory"))
                    JOptionPane.showMessageDialog(Tree.this, "Not enough space!");
                else
                    JOptionPane.showMessageDialog(Tree.this, "Invalid number!");
            }
        }
        else
            JOptionPane.showMessageDialog(Tree.this, "Invalid name!");
    }

    public Tree(File File, MainFrame MainFrame)

    {
        //super(File);
        setBounds(0, 0, 470, 600);
        setDragEnabled(true);
        setDropMode(DropMode.ON_OR_INSERT);
        setTransferHandler(new TreeTransferHandler(this));
        JPopupMenu rightClick = new JPopupMenu("Context Menu");
        JPopupMenu createDrive = new JPopupMenu("Create Drive Menu");
        JMenuItem createDriveItem = new JMenuItem("Create drive");
        JMenu Add = new JMenu("Add...");
        JMenuItem addNewFile = new JMenuItem("new file");
        JMenuItem addNewFolder = new JMenuItem("new folder");
        JMenuItem remove = new JMenuItem("Remove");
        ActionListener rightClickActionListener = e -> {
            Object source = e.getSource();
            File selectedNode = (File) Tree.this.getLastSelectedPathComponent();
            if (selectedNode == null)
            {
                JOptionPane.showMessageDialog(Tree.this, "You must select a node!");
                return;
            }
            Folder parentNode = (Folder) selectedNode.getParent();
            if (source == addNewFile || source == addNewFolder || source == createDriveItem)
            {
                String type = "file";
                if (source == addNewFolder)
                    type = "folder";
                else if(source == createDriveItem)
                    type = "drive";
                showCreateElementDialog(type, (Folder) selectedNode);
            }
            else if (e.getSource() == remove)
                parentNode.remove(selectedNode);
            //((DefaultTreeModel) Tree.this.getModel()).reload(parentNode);
        };
        ((DefaultTreeModel) treeModel ).setAsksAllowsChildren(true);
        addNewFile.addActionListener(rightClickActionListener);
        addNewFolder.addActionListener(rightClickActionListener);
        remove.addActionListener(rightClickActionListener);
        createDriveItem.addActionListener(rightClickActionListener);
        Add.add(addNewFile);
        Add.add(addNewFolder);
        rightClick.add(Add);
        rightClick.add(remove);
        createDrive.add(createDriveItem);
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {
                Tree.this.setSelectionPath(Tree.this.getPathForLocation(e.getX(), e.getY()));
            }

            public void mouseReleased(MouseEvent e)
            {
                if (e.isPopupTrigger())
                    if (Tree.this.getRowBounds(0).contains(e.getX(), e.getY()))
                        createDrive.show((Component) e.getSource(), e.getX(), e.getY());
                    else if ((Tree.this.getLastSelectedPathComponent()) instanceof Folder)
                        rightClick.show((Component) e.getSource(), e.getX(), e.getY());
            }
        });
    }
}
