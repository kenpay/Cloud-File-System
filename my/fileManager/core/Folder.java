package my.fileManager.core;

import my.fileManager.managers.ResourceManager;

import java.awt.*;
import java.util.ArrayList;

public class Folder extends File {
    private ArrayList<File> files = new ArrayList<>(), selectedFiles = new ArrayList<>();

    public ArrayList<File> getSelectedFiles() { return selectedFiles;}

    public boolean contains(String name)
    {
        for (File file:files)
            if (file.Properties().getName().equals(name))
                return true;
        return false;
    }

    public Folder(String folderName, double fSpace)
    {
        super(new FolderProperties(0, folderName, fSpace));
        setIcon(ResourceManager.getFolderIcon());
        //allowsChildren = true;
    }

    public Folder(Folder folder)
    {
        super(new FolderProperties(folder.Properties().getSize(), folder.Properties().getName(), ((FolderProperties)folder.Properties()).getSpace()));
        setIcon(ResourceManager.getFolderIcon());
        for(File file:folder.files)
            if (file instanceof Folder)
                files.add(new Folder((Folder) file));
            else
                files.add(new File(file));
    }

    private void deselectFile(File file)
    {
        file.setBackground(Color.GRAY);
        file.setForeground(Color.black);
        file.repaint();
        selectedFiles.remove(file);
    }

    public void selectFile(File file)
    {
        if (selectedFiles.contains(file))
            deselectFile(file);
        else
        {
            file.setForeground(Color.GREEN);
            file.setBackground(Color.CYAN);
            selectedFiles.add(file);
        }
    }

    public void deselectAll()
    {
        int size = selectedFiles.size();
        for (int index=size-1; index >= 0; index--)
            deselectFile(selectedFiles.get(index));
    }


    public Folder(int id, String folderName, double fSpace)
    {
        super(id, new FolderProperties(0, folderName, fSpace));
        setIcon(ResourceManager.getFolderIcon());
    }

    public ArrayList<File> getFiles()
    {
        return files;
    }

    /*public Vector getChildren()
    {
        return children;
    }*/

    private void add(File file)
    {
        //super.add(file);
        if (file.parent != null)
            file.parent.remove(file);
        file.parent = this;
        if (getId() != 0)
            file.Properties().setPath(Properties().getPath()+Properties().getName()+'/');
        int numberOfFiles = ((FolderProperties) Properties()).getContainingXFiles();
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
        files.add(file);
    }

    public void addFile(File file) throws Exception
    {
        if ((file instanceof Folder && ((((FolderProperties) file.Properties()).getSpace() <= ((FolderProperties) Properties()).getSpace()))) || ((((FolderProperties)Properties()).getSpace() - file.Properties().Size() >= 0)))
            add(file);
        else
            throw new Exception("Not enough memory");
    }

    public void remove(File file)
    {
        //super.remove(file);
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
        files.remove(file);
    }

    @Override
    public String toString()
    {
        return super.toString() + " " + ((FolderProperties) Properties()).getSpace();
    }
}
