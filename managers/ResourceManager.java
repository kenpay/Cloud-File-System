package my.fileManager.managers;

import javax.swing.*;

public class ResourceManager {
    private static ImageIcon    filesIcon = new ImageIcon("D:\\Facultate\\PAO\\SGF\\src\\my\\fileManager\\core\\file.png"),
                                foldersIcon = new ImageIcon("D:\\Facultate\\PAO\\SGF\\src\\my\\fileManager\\core\\folder.png"),
                                drivesIcon = new ImageIcon("D:\\Facultate\\PAO\\SGF\\src\\my\\fileManager\\core\\drive.png");

    public static ImageIcon getFileIcon() { return filesIcon; }
    public static ImageIcon getDriveIcon() { return drivesIcon; }
    public static ImageIcon getFolderIcon() { return foldersIcon; }
}
