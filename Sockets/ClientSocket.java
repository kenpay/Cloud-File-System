package my.fileManager.Sockets;

import my.fileManager.core.Drive;
import my.fileManager.core.Folder;
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
        for (String user : users) {
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

    public void getFileSystem()
    {

        socketWriter.println("getFileSystem");
        socketWriter.flush();
        String nextLine = socketReader.nextLine();
        String[] fileSystem = nextLine.split("-"), configuration, drives, folders, files;
        if (fileSystem.length > 0)
        {
            configuration = fileSystem[0].split(";");
            Drive configurationDrive = new Drive(Integer.parseInt(configuration[0]), configuration[1], Double.parseDouble(configuration[2]));
            OperationManager.setConfiguration(configurationDrive);

            if (fileSystem.length > 1)
            {
                drives = fileSystem[1].split(";");
                for (String drive : drives) {
                    String[] driveProperties = drive.split("\\.");
                    Drive driveToCreate = new Drive(Integer.parseInt(driveProperties[0]), driveProperties[1], Double.parseDouble(driveProperties[2]));
                    configurationDrive.addFile(driveToCreate);
                    OperationManager.addToStorage(driveToCreate);
                }
                if (fileSystem.length > 2)
                {
                    folders = fileSystem[1].split(";");
                    for (String folder : folders) {
                        String[] folderProperties = folder.split("\\.");
                        Folder folderIn = OperationManager.getFolderById(Integer.parseInt(folderProperties[2]));
                        if (folderIn != null)
                            folderIn.addFile(new Folder(Integer.parseInt(folderProperties[0]), folderProperties[1], Double.parseDouble(folderProperties[3])));

                    }
                    if (fileSystem.length > 3)
                    {
                        files = fileSystem[2].split(";");
                        for (String file : files) {
                            String[] fileProperties = file.split("\\.");
                            Folder folderIn = OperationManager.getFolderById(Integer.parseInt(fileProperties[3]));
                            if (folderIn != null)
                                ((Folder) folderIn.getUserObject()).addFile(new my.fileManager.core.File(Integer.parseInt(fileProperties[0]), Double.parseDouble(fileProperties[2]), fileProperties[1]));
                        }
                    }
                }
            }
        }
    }
}
