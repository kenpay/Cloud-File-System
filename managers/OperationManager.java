package my.fileManager.managers;

import my.fileManager.core.*;

import java.util.*;

public class OperationManager {
    //private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Drive> drives = new ArrayList<>();

    private static Drive configuration;

    public static void addToStorage(Drive drive)
    {
        drives.add(drive);
    }

    public static ArrayList<Drive> getDrives()
    {
        return drives;
    }

    public static void setConfiguration(Drive Configuration)
    {
        configuration = Configuration;
    }

    public static Drive getConfigatuion()
    {
        return configuration;
    }

    /*/public static void addUser(User userToAdd)
    {
        users.add(userToAdd);
    }*/

    /* DFS
    static private Folder getRecursivelyFolderById(int id, TreeNode folderIn)
    {
    -- TODO TreeNode Ideology DFS
        for(File folder:folderIn.getFiles()){
            if (*older instanceof Folder)
            {
                if (folder.getId() == id)
                    return (Folder) folder;
                Folder found = getRecursivelyFolderById(id, (Folder) folder);
                if (found != null)
                    return found;
            }
        }
        return null;
    }

    public static TreeNode getFolderById(int id)
    {
        for(TreeNode drive:drives)
        {
            if (((Drive) drive.getUserObject()).getId() == id)
                return drive;
            return getRecursivelyFolderById(id, drive);
        }
        return null;
    } */ // HALFWAY - not finished at all


    //BFS
    public static Folder getFolderById(int id)
    {
        Queue<File> queue = new LinkedList<>(configuration.getFiles());
        while (!queue.isEmpty())
        {
            File folder = queue.remove();
            if (folder instanceof Folder)
            {
                if (folder.getId() == id)
                    return (Folder) folder;
                queue.addAll(((Folder) folder).getFiles());
            }
        }
        return null;
    }

    /*
    public static void createFile()
    {
        System.out.println("File Name");
        String fileName = reader.next();
        System.out.println("File size");
        double fileSize = reader.nextDouble();
        File fileCreated = new File(fileSize, fileName);
        FileManager.addFileToCurrentTarget(fileCreated);
        CSVFileManager.getInstance().add(fileCreated);
        CSVManager.getInstance().stamp("createFile", LocalDateTime.now().toString());
        System.out.println("File successfully created!");
    }

    public static void showFiles()
    {
        System.out.println(FileManager.currentTarget());
    }

    public static void moveTo()
    {
        System.out.println(FileManager.currentTarget() + "Choose .. to go back or write folder name:");
        String choose = reader.next();
        if (choose.equals(".."))
            FileManager.goBack();
        else
            FileManager.moveTo(choose);
    }

    public static void renameFile()
    {
        System.out.println(FileManager.currentTarget() + "Write file name:");
        String choose = reader.next(), newName;
        System.out.println("Enter file new name:");
        newName = reader.next();
        FileManager.renameFile(choose, newName);
    }

    public static void deleteFile()
    {
        System.out.println(FileManager.currentTarget() + "Write file name:");
        String choose = reader.next();
        FileManager.deleteFile(choose);
    }

    public static void copyFile()
    {
        System.out.println(FileManager.currentTarget() + "Write file name:");
        String choose = reader.next();
        FileManager.setClipboard(choose);
    }

    public static void pasteFile()
    {
        FileManager.paste();
    }

    public static void listUsers()
    {
        StringBuilder stringBuilder = new StringBuilder().append("Users:\n");
        for (User user:users)
            stringBuilder.append(user.getName()).append("\n");
        System.out.println(stringBuilder.toString());
    }
    */
}
