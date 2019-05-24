package my.fileManager.components;

import my.fileManager.core.File;

import javax.swing.*;

public class Tree extends JTree {

    public Tree(TreeNode treeNode, MainFrame MainFrame)

    {
        super(treeNode);
        setTransferHandler(new TreeTransferHandler(this, MainFrame));
    }
}
