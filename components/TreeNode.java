package my.fileManager.components;

import my.fileManager.core.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeNode extends DefaultMutableTreeNode {

    public TreeNode(String item)
    {
        super(item);
    }

    public TreeNode(File item)
    {
        super(item);
    }
}
