package my.fileManager.Sockets;

import my.fileManager.components.MainFrame;
import my.fileManager.core.*;
import my.fileManager.managers.FileManager;
import my.fileManager.managers.OperationManager;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocket extends Socket {

    private static ClientSocket clientSocket;

    private Scanner socketReader;
    private PrintWriter socketWriter;

    public static ClientSocket getInstance()
    {
        return clientSocket;
    }

    public void renameElement(File file)
    {
        socketWriter.println("rename"+file.getClass().getSimpleName()+":"+file.getId()+","+file.Properties().getName());
        socketWriter.flush();
    }


    public void removeElement(File file)
    {
        socketWriter.println("remove"+file.getClass().getSimpleName()+":"+file.getId());
        socketWriter.flush();
    }

    public void createDatabaseElement(File file)
    {
        socketWriter.println("create"+file.getClass().getSimpleName()+":"+file.getId()+','+file.Properties().getName()+','+file.Properties().Size()+','+((file.Properties() instanceof FolderProperties)?((FolderProperties) file.Properties()).getSpace():"")+','+
                (file instanceof Drive? -1 :file.getFileParent().getId()));
        socketWriter.flush();
    }

    public void changeParentForElement(File file)
    {
        socketWriter.println("changeParentFor"+file.getClass().getSimpleName()+":"+file.getId()+','+file.getFileParent().getId());
        socketWriter.flush();
    }

    //Create only it doesn't exists....
    public static void createInstance(InetAddress address, int port) throws IOException
    {
        if (clientSocket == null)
            clientSocket = new ClientSocket(address, port);
    }

    public static void closeConnection()
    {
        clientSocket = null;
    }

    private ClientSocket(InetAddress ipAddress, int port) throws IOException
    {
        super(ipAddress, port);
        socketReader = new Scanner(getInputStream());
        socketWriter = new PrintWriter(getOutputStream());
    }

    public DefaultListModel<String> getUsers()
    {
        socketWriter.println("getUsers");
        socketWriter.flush();
        String[] users = socketReader.nextLine().split(";");
        DefaultListModel<String> usersList = new DefaultListModel<>();
        if (!users[0].equals(""))
            for (String user : users)
            {
                String[] userProperties = user.split("\\.");
                usersList.addElement(userProperties[1]);
            }
        return usersList;
    }

    public String getPasswordForUser(String User)
    {
        socketWriter.println("getPasswordForUser:"+User);
        socketWriter.flush();
        return socketReader.nextLine();
    }

    public void createUser(String User, String Password)
    {
        socketWriter.println("createUser:"+User+":"+Password);
        socketWriter.flush();
    }

    public void getFileSystem() throws Exception
    {
        socketWriter.println("getFileSystem");
        socketWriter.flush();
        String nextLine = socketReader.nextLine();
        String[] fileSystem = nextLine.split("-"), configuration, drives, folders, files;
        Drive configurationDrive;
        if (fileSystem.length > 0)
        {
            configuration = fileSystem[0].split(";");
            configurationDrive = new Drive(Integer.parseInt(configuration[0]), configuration[1], Double.parseDouble(configuration[2]));
            OperationManager.setConfiguration(configurationDrive);
            FileManager.setCurrentTarget(configurationDrive);
            drives = fileSystem[1].split(";");
            if (!drives[0].equals("")) {
                for (String drive : drives) {
                    String[] driveProperties = drive.split("\\.");
                    Drive driveToCreate = new Drive(Integer.parseInt(driveProperties[0]), driveProperties[1], Double.parseDouble(driveProperties[2]));
                    configurationDrive.addFile(driveToCreate);
                }

                if (fileSystem.length>2) {
                    folders = fileSystem[2].split(";");
                    if (!folders[0].equals(""))
                        for (String folder : folders) {
                            String[] folderProperties = folder.split("\\.");
                            Folder folderIn = OperationManager.getFolderById(Integer.parseInt(folderProperties[2]));
                            if (folderIn != null)
                                folderIn.addFile(new Folder(Integer.parseInt(folderProperties[0]), folderProperties[1], Double.parseDouble(folderProperties[3])));

                        }
                    if (fileSystem.length > 3) {
                        files = fileSystem[3].split(";");
                        if (!files[0].equals(""))
                            for (String file : files) {
                                String[] fileProperties = file.split("\\.");
                                Folder folderIn = OperationManager.getFolderById(Integer.parseInt(fileProperties[3]));
                                if (folderIn != null)
                                    folderIn.addFile(new my.fileManager.core.File(Integer.parseInt(fileProperties[0]), Double.parseDouble(fileProperties[2]), fileProperties[1]));
                            }
                    }
                }
            }
        }

    }
}
