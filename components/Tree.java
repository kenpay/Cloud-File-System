package my.fileManager.components;

import my.fileManager.core.File;
import my.fileManager.core.Folder;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tree extends JTree {

    public Tree(File File, MainFrame MainFrame)

    {
        super(File);
        setBounds(0, 0, 470, 600);
        setDragEnabled(true);
        setDropMode(DropMode.ON_OR_INSERT);
        setTransferHandler(new TreeTransferHandler(this, MainFrame));
        JPopupMenu rightClick = new JPopupMenu("Context menu");
        JMenu Add = new JMenu("Add...");
        JMenuItem addNewFile = new JMenuItem("new file");
        JMenuItem remove = new JMenuItem("Remove");
        ActionListener rightClickActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addNewFile)
                {
                    String fileName = JOptionPane.showInputDialog("Enter file name: ");
                    if (fileName != null)
                    {
                        try
                        {
                            double fileSize = Double.parseDouble(JOptionPane.showInputDialog("Enter file size:"));
                            Folder selectedNode = (Folder) Tree.this.getLastSelectedPathComponent();
                            if (selectedNode != null)
                                selectedNode.addFile(new File(fileSize, fileName));
                            else
                                JOptionPane.showMessageDialog(Tree.this, "You must select a node!");
                        }
                        catch(Exception exception)
                        {
                            JOptionPane.showMessageDialog(Tree.this, "Invalid number!");
                        }
                    }
                }
                else if (e.getSource() == remove)
                {
                    File selectedNode = (File) Tree.this.getLastSelectedPathComponent();
                    ((File) (selectedNode.getParent())).remove(selectedNode);
                }
                MainFrame.repaint();
                ((DefaultTreeModel) Tree.this.getModel()).reload();
            }
        };
        ((DefaultTreeModel) treeModel ).setAsksAllowsChildren(true);
        addNewFile.addActionListener(rightClickActionListener);
        remove.addActionListener(rightClickActionListener);
        Add.add(addNewFile);
        rightClick.add(Add);
        rightClick.add(remove);
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {
                Tree.this.setSelectionPath(Tree.this.getPathForLocation(e.getX(), e.getY()));
            }

            public void mouseReleased(MouseEvent e)
            {
                if (e.isPopupTrigger() && !Tree.this.getRowBounds(0).contains(e.getX(), e.getY()))
                    rightClick.show((Component) e.getSource(), e.getX(), e.getY());
            }
        });
    }
}
