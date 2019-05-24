package my.fileManager.ActionListeners;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import my.fileManager.components.Tree;
import my.fileManager.components.TreeNode;

public class PopupActionListener implements ActionListener {

    private Tree filesTree;
    JMenuItem addNewFile;

    public PopupActionListener(Tree tree, JMenuItem AddNewFile)
    {
        filesTree = tree;
        addNewFile = AddNewFile;
    }

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
}
