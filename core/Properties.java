package my.fileManager.core;

import my.fileManager.core.Permissions;

import java.io.Serializable;

interface PropertiesInterface
{
    void Rename(String Name);
    void ChangeSize(double value);
    Permissions Permissions();
    double Size();
}

public class Properties implements PropertiesInterface, Serializable {
    private double size;
    private String name, path;
    private Permissions permissions;

    Properties(double fSize, String fName)
    {
        this.size = fSize;
        this.name = fName;
        this.path = "";
    }

    public String getName()
    {
        return this.name;
    }

    String getPath()
    {
        return this.path;
    }

    void setPath(String newPath)
    {
        this.path = newPath;
    }

    public void ChangeSize(double value)
    {
        size = value;
    }

   public double Size()
    {
        return size;
    }

    void setSize(double newSize)
    {
        this.size = newSize;
    }

    public void Rename(String Name) { this.name = Name; }

    public Permissions Permissions() { return permissions; }
}
