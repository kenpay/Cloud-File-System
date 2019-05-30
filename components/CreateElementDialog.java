package my.fileManager.components;

import my.fileManager.Sockets.ClientSocket;
import my.fileManager.core.Drive;
import my.fileManager.core.File;
import my.fileManager.core.Folder;
import my.fileManager.managers.FileManager;
import my.fileManager.managers.OperationManager;
import my.fileManager.managers.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateElementDialog extends JDialog
{

    private static JDialog dialog;

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
                ((MainFrame) getParent()).redisplayFileSystem();
                ClientSocket.getInstance().createDatabaseElement(fileToAdd);
            }
            catch(Exception exception)
            {
                if (exception.getMessage().equals("Not enough memory"))
                    JOptionPane.showMessageDialog(this, "Not enough space!");
                else
                    JOptionPane.showMessageDialog(this, "Invalid number!");
            }
        }
        else
            JOptionPane.showMessageDialog(this, "Invalid name!");
        CreateElementDialog.this.setVisible(false);
    }

    private CreateElementDialog(MainFrame parent)
    {
        super(parent, "Create...", true);
        setSize(600, 400);
        setResizable(false);
        GridLayout layout = new GridLayout(2, 1), imagesLayout = new GridLayout(1, 3);
        layout.setVgap(40);
        imagesLayout.setHgap(40);
        JLabel label = new JLabel("Select what you want to create:");
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        JPanel panel = new JPanel(imagesLayout);
        JLabel file = new JLabel("File");
        file.setIcon(ResourceManager.getFileIcon());
        JLabel folder = new JLabel("Folder");
        folder.setIcon(ResourceManager.getFolderIcon());
        JLabel drive = new JLabel("Drive");
        if (FileManager.getCurrentTarget() == OperationManager.getConfigatuion())
        {
            folder.setEnabled(false);
            file.setEnabled(false);
        }
        else
            drive.setEnabled(false);
        drive.setIcon(ResourceManager.getDriveIcon());
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Component object = panel.getComponentAt(e.getX(), e.getY());
                if (object instanceof JLabel)
                    if (object.isEnabled())
                    showCreateElementDialog(((JLabel) object).getText().toLowerCase(), FileManager.getCurrentTarget());
            }
        };
        panel.addMouseListener(mouseAdapter);
        panel.add(file);
        panel.add(folder);
        panel.add(drive);
        setLayout(layout);
        add(label);
        add(panel);
        setVisible(true);
    }

    public static void display(MainFrame parent)
    {
        if (dialog == null || dialog.getParent() != parent)
            dialog = new CreateElementDialog(parent);
        dialog.setVisible(true);
    }
}
