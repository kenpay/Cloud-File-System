package my.fileManager.core;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;


interface FileInterface
{
    void Rename(String Name);
    Properties Properties();
    double Size();
}

public class File implements FileInterface, Serializable {
    public File()
    {

    }

    static int generatorId = 0;
    private int id;
    private Properties properties;

    File(Properties properties)
    {
        this.id = generatorId++;
        this.properties = properties;
    }

    /*public void setParentFolder(Folder folder)
    {
        this.parentFolder = folder;
    }

    public Folder getParentFolder()
    {
        return this.parentFolder;
    }
    */

    public File(double size, String Name)
    {
        this(new Properties(size, Name));
    }

    public Properties Properties() { return this.properties; }

    public File(int id, double size, String Name)
    {
        this.id = id;
        this.properties = new Properties(size, Name);
    }


    File(int id, Properties properties)
    {
        this.id = id;
        generatorId = id+1;
        this.properties = properties;
    }

    public void Rename(String Name)
    {
        properties.Rename(Name);
    }

    public int getId()
    {
        return this.id;
    }

    public double Size()
    {
        return properties.Size();
    }

    @Override
    public String toString()
    {
        return properties.getName();
    }

}
