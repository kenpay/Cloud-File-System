package my.fileManager.core;

import java.util.Vector;

public class Folder extends File {
    //private TreeNode[] files;
    //private Folder parentFolder;

    /*
    public Folder(FolderProperties properties, File[] fil)
    {
        super(properties);
        this.files = fil;
        ((FolderProperties) Properties()).containingXFiles(fil.length);
    }

    private Folder(FolderProperties properties)
    {
        super(properties);
        ((FolderProperties) Properties()).containingXFiles (0);
    }

    public Folder(double fSize, String fPath, double fSpace, File[] fil)
    {
        super(new FolderProperties(fSize, fPath, fSpace));
        this.files = fil;
    }*/

    public Folder(String folderName, double fSpace)
    {
        super(new FolderProperties(0, folderName, fSpace));
        //this.files = new TreeNode[0];
        //((FolderProperties) Properties()).setContainingXFiles(0);
    }


    public Folder(int id, String folderName, double fSpace)
    {
        super(id, new FolderProperties(0, folderName, fSpace));
        //this.files = new TreeNode[0];
        //((FolderProperties) Properties()).setContainingXFiles(0);
    }

    /*
    public Folder searchFolder(String folderName)
    {
        for(File file:this.files)
            if (file instanceof Folder)
                if (file.Properties().getName().equals(folderName))
                    return (Folder) file;
        return null;
    }

    public File searchFile(String fileName)
    {
        for(File file:this.files)
            if(file.Properties().getName().equals(fileName))
                return file;
        return null;
    }

    public String getFilesId()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(File file: files)
            stringBuilder.append(file.getId()).append(';');
        return stringBuilder.toString();
    }
    */

    /*public TreeNode[] getFiles()
    {
        return this.files;
    }

    public Folder()
    {
        super();
    }*/

    public Vector getChildren()
    {
        return children;
    }

    private void add(File file)
    {
        super.add(file);
        int numberOfFiles = ((FolderProperties) Properties()).getContainingXFiles();
        //file.Properties().setPath(Properties().getPath()+Properties().getName()+'/'); TreeSelectionPath ?
        Properties().setSize(Properties().Size() + file.Properties().Size());
        Folder parent = (Folder) super.parent;
        while (parent != null)
        {
            parent.Properties().setSize(parent.Properties().Size() + file.Properties().Size());
            ((FolderProperties) parent.Properties()).setSpace(((FolderProperties) parent.Properties()).getSpace() - file.Properties().Size());
            if (file instanceof Folder)
                ((FolderProperties) parent.Properties()).setSpace(((FolderProperties) parent.Properties()).getSpace() - ((FolderProperties)file.Properties()).getSpace());
            parent = (Folder) parent.parent;
        }
        ((FolderProperties) Properties()).setSpace(((FolderProperties) Properties()).getSpace() - file.Properties().Size());
        if (file instanceof Folder)
            ((FolderProperties) Properties()).setSpace(((FolderProperties) Properties()).getSpace() - ((FolderProperties)file.Properties()).getSpace());
        ((FolderProperties) Properties()).setContainingXFiles(numberOfFiles+1);
    }

    public void addFile(File file)
    {
        if (file instanceof Folder) {
            if ((((FolderProperties) file.Properties()).getSpace() <= ((FolderProperties) Properties()).getSpace()))
                add(file);
            else
                System.out.println("Not enough space.");
        }
        else if (((FolderProperties)Properties()).getSpace() - file.Properties().Size() >= 0)
            add(file);
        else
            System.out.println("Not enough space.");
    }

    public void remove(File file)
    {
        super.remove(file);
        int numberOfFiles = ((FolderProperties) Properties()).getContainingXFiles(), altIndex=0;
        Properties().setSize(Properties().Size() - file.Properties().Size());
        Folder parent = (Folder) super.parent;
        while (parent != null)
        {
            parent.Properties().setSize(parent.Properties().Size() - file.Properties().Size());
            ((FolderProperties) parent.Properties()).setSpace(((FolderProperties) parent.Properties()).getSpace() + file.Properties().Size());
            if (file instanceof Folder)
                ((FolderProperties) parent.Properties()).setSpace(((FolderProperties) parent.Properties()).getSpace() + ((FolderProperties)file.Properties()).getSpace());
            parent = (Folder) parent.parent;
        }
        ((FolderProperties) Properties()).setSpace(((FolderProperties) Properties()).getSpace() + file.Properties().Size());
        if (file instanceof Folder)
            ((FolderProperties) Properties()).setSpace(((FolderProperties) Properties()).getSpace() + ((FolderProperties)file.Properties()).getSpace());
        ((FolderProperties) Properties()).setContainingXFiles(numberOfFiles+1);
    }

    /*public void newFolder(String folderName, double freeSpace)
    {
        add(new Folder(0, folderName, freeSpace));
    }

    public void newFile(String fileName, double fileSize)
    {
        add(new File(fileSize,fileName));
    }*/
    /*
    @Override
    public String toString()
    {
        StringBuilder newStringBuilder = new StringBuilder();
        newStringBuilder.append("Folder path: ");
        newStringBuilder.append(Properties().getPath());
        newStringBuilder.append("\nContaining ");
        newStringBuilder.append(((FolderProperties) Properties()).getContainingXFiles());
        newStringBuilder.append("\n");
        for (TreeNode fileT:this.files) {
            File file = (File) fileT.getUserObject();
            newStringBuilder.append("Path: ");
            newStringBuilder.append(file.Properties().getPath());
            newStringBuilder.append(file.Properties().getName());
            newStringBuilder.append("\t");
            newStringBuilder.append((file instanceof Folder ? "Folder":"File"));
            newStringBuilder.append('\n');
        }
        return newStringBuilder.toString();
    }
    */
    @Override
    public String toString()
    {
        return super.toString() + " " + ((FolderProperties) Properties()).getSpace();
    }
}
