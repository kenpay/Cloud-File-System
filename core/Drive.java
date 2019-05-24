package my.fileManager.core;

import java.io.Serializable;

public class Drive extends Folder implements Serializable {
    public Drive(String name, double space)
    {
        super(name, space);
    }
    public Drive(int id, String name, double space) {   super(id, name, space); }
    public Drive()
    {
        super("NoName", 100);
    }
}
