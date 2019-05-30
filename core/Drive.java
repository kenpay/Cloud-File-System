package my.fileManager.core;

import my.fileManager.managers.ResourceManager;
import java.io.Serializable;

public class Drive extends Folder implements Serializable {
    public Drive(String name, double space)
    {
        super(name, space);
        setIcon(ResourceManager.getDriveIcon());
    }
    public Drive(int id, String name, double space) {
        super(id, name, space);
        setIcon(ResourceManager.getDriveIcon()); }
}
