package my.fileManager.core;

import java.io.Serializable;

public class FolderProperties extends Properties implements Serializable {
    private int containingXFiles;
    private double space;

    FolderProperties(double fSize, String fPath, double fSpace)
    {
        super(fSize, fPath);
        this.space = fSpace;
    }

    int getContainingXFiles()
    {
        return this.containingXFiles;
    }

    void setContainingXFiles(int containingNoFiles)
    {
        this.containingXFiles = containingNoFiles;
    }

    public double getSpace()
    {
        return this.space;
    }

    void setSpace(double newSpace)
    {
        this.space = newSpace;
    }
}
