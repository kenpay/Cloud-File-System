package my.fileManager.components;

import my.fileManager.core.File;
import my.fileManager.core.Folder;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class TreeTransferHandler extends TransferHandler
{
    private Tree filesTree;

    public TreeTransferHandler(Tree Tree)
    {
        filesTree = Tree;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if (!support.isDrop())
            return false;
        JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
        return dropLocation.getPath() != null;
    }

    @Override
    public boolean importData(TransferSupport support)
    {
        if (!canImport(support))
            return false;
        JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
        TreePath treePath = dropLocation.getPath();
        Transferable dropped = support.getTransferable();
        File node;
        try{
            node = (File) dropped.getTransferData(DataFlavor.stringFlavor);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
            return false;
        }
        catch (UnsupportedFlavorException e)
        {
            System.err.println("Can't drop!");
            return false;
        }
        ((Folder) node.getParent()).remove(node);
        File selectedNode = (File) treePath.getLastPathComponent();
        selectedNode.add(node);
        return true;

    }

    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    public Transferable createTransferable(JComponent something)
    {
        return new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[0];
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return false;
            }

            @Override
            public Object getTransferData(DataFlavor flavor) {
                return filesTree.getLastSelectedPathComponent();
            }
        };
    }

}
