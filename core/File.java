package my.fileManager.core;


import my.fileManager.managers.ResourceManager;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public class File extends JLabel implements Serializable {

    private static int generatorId = 0;
    private Properties properties;
    private int id;
    protected Folder parent;


    public Folder getFileParent()
    {
        return this.parent;
    }

    File(Properties properties)
    {
        this(generatorId, properties);
    }

    public File(double size, String Name)
    {
        this(new Properties(size, Name));
    }

    public Properties Properties() { return this.properties; }

    public File(int id, double size, String Name)
    {
        this(id, new Properties(size, Name));
    }


    File(int id, Properties properties)
    {
        super(properties.getName());
        this.properties = properties;
        this.id = id;
        generatorId = id+1;
        setIcon(ResourceManager.getFileIcon());
        setOpaque(true);
        setHorizontalTextPosition(File.CENTER);
        setVerticalTextPosition(File.BOTTOM);
        setHorizontalAlignment(File.CENTER);
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
        return properties.getName() + " " + properties.getSize();
    }

}
