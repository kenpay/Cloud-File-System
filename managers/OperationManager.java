package my.fileManager.managers;

import my.fileManager.components.TreeNode;

import my.fileManager.core.*;

import java.util.*;

public class OperationManager {
    public static Scanner reader = new Scanner(System.in);

    //private static ArrayList<Drive> drives = new ArrayList<>(); // TODO : STORAGE
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<TreeNode> drives = new ArrayList<>();


    public static void addToStorage(TreeNode drive)
    {
        drives.add(drive);
    }

    public static ArrayList<TreeNode> getDrives()
    {
        return drives;
    }

    /*
    public static Drive getCoreDrive()
    {
        if (drives.size() > 0)
            return drives.get(0);
        return null;
    }*/

    public static User getUserByName(String name)
    {
        for (User user:users)
            if (user.getName() == name)
                return user;
        return null;
    }

    public static void addUser(User userToAdd)
    {
        users.add(userToAdd);
    }

    public static User getCoreUser()
    {
        if (users.size() > 0)
            return users.get(0);
        return null;
    }

    /*
    public static void createUser()
    {
        System.out.println("Please enter user name:");
        String userName = reader.next();
        User user = getUserByName(userName);
        if (user == null)
        {
            user = new User(userName);
            addUser(user);
            CSVUserManager.getInstance().add(user);
            CSVManager.getInstance().stamp("createFolder", LocalDateTime.now().toString());
            System.out.println("User successfully created!");
        }
        else
            System.out.println("User already exists");
    }
    */

    /* DFS
    static private Folder getRecursivelyFolderById(int id, TreeNode folderIn)
    {
        for(File folder:folderIn.getFiles()){
            if (folder instanceof Folder)
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

    public static TreeNode getFolderById(int id)
    {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.addAll(drives);
        while (!queue.isEmpty())
        {
            TreeNode node = queue.remove();
            File nodeUserObject = (File) node.getUserObject();
            if (nodeUserObject instanceof Folder)
            {
                if (nodeUserObject.getId() == id)
                    return node;
                queue.addAll(Arrays.asList(((Folder) nodeUserObject).getFiles()));
            }
        }
        return null;
    }

    /*static Drive getDrive(String driveName)
    {
        for(Drive drive:drives)
            if(drive.getProperties().getName().equals(driveName))
                return drive;
        return null;
    }*/
    /*
    public static void createFolder()
    {
        System.out.println("Folder Name");
        String folderName = reader.next();
        System.out.println("Folder space");
        double folderSpace = reader.nextDouble();
        Folder folderToAdd = new Folder(folderName, folderSpace);
        FileManager.addFileToCurrentTarget(folderToAdd);
        CSVFolderManager.getInstance().add(folderToAdd);
        CSVManager.getInstance().stamp("createFolder", LocalDateTime.now().toString());
        System.out.println("Folder successfully created!");
    }

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
