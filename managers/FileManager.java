package my.fileManager.managers;

import my.fileManager.core.File;
import my.fileManager.core.Folder;

public class FileManager {
    private static Folder currentTarget;

    private static File clipboardFile;

    static String currentTarget()
    {
        return currentTarget.toString();
    }

    public static void currentTarget(Folder target)
    {
        currentTarget = target;
    }

    /*
    static void addFileToCurrentTarget(File file)
    {
        file.setParentFolder(currentTarget);
        currentTarget.addFile(file);
    }

    static void goBack()
    {
        Folder parentFolder = currentTarget.getParentFolder();
        if (parentFolder != null)
            currentTarget = parentFolder;
        else
            System.out.println("Couldn't go back!");
    }

    static void moveTo(String choose)
    {
        Folder chosenFolder = currentTarget.searchFolder(choose);
        if (chosenFolder != null)
            currentTarget = chosenFolder;
        else
            System.out.println("Couldn't move!");
    }
    */
    /*
    static void renameFile(String choose, String newName)
    {
        File chosenFile = currentTarget.searchFile(choose);
        if (chosenFile != null)
            chosenFile.rename(newName);
        else
            System.out.println("File not found!");
    }

    private static void removeFile(File file)
    {
        file.getParentFolder().remove(file);
        CSVFileManager.getInstance().delete(file);
    }

    private static void deleteFolder(Folder folder)
    {
        for(File file:folder.getFiles())
        {
            if (file instanceof Folder) {
                deleteFolder((Folder) file);
                CSVFolderManager.getInstance().delete(folder);
            }
            else
                removeFile(file);
        }
    }

    static void deleteFile(String choose)
    {
        File chosenFile = currentTarget.searchFile(choose);
        if (chosenFile!= null)
        {
            CSVManager.getInstance().stamp("delete", LocalDateTime.now().toString());
            if (chosenFile instanceof Folder)
                deleteFolder((Folder) chosenFile);
            else
                removeFile(chosenFile);
            currentTarget.remove(chosenFile);
        }
        else
            System.out.println("File not found!");
    }

    static void setClipboard(String choose)
    {
        File chosenFile = currentTarget.searchFile(choose);
        if (chosenFile != null)
            clipboardFile = new File(chosenFile);
        else
            System.out.println("File not found!");
    }

    public static void setCutClipboard(String choose)
    {
        File chosenFile = currentTarget.searchFile(choose);
        //Pair<Folder>
        if (chosenFile != null)
            clipboardFile = chosenFile;
        else
            System.out.println("File not found!");
    }

    static void paste()
    {
        //addFileToCurrentTarget(clipboardFile);
        currentTarget.addFile(clipboardFile);
    }

    //Review
    public static void setCipblordbyFullPath(String fullPath)
    {
        String[] path = fullPath.split("/");
        Folder folderPointer = OperationManager.getDrive(path[0]);
        for (int index=1;index < path.length-1 && folderPointer != null; index++)
            folderPointer = folderPointer.searchFolder(path[index]);
        if (folderPointer!=null)
        {
            File chosen = folderPointer.searchFile(path[path.length-1]);
            if (chosen != null)
                clipboardFile = new File(chosen);
            else
                System.out.println("Path is invalid!");
        }
        else
            System.out.println("Path is invalid!");
    }
    */
}
