package my.fileManager;

import my.fileManager.managers.*;
import my.fileManager.utilities.StringUtility;

import java.net.*;

public class Main {

    private static ServerSocket serverSocket = null;
    private static Socket serverClientSocket, clientSocket;
    private static String[] commands = {
            "createFolder", "createFile", "moveTo", "showFiles", "renameFile", "deleteFile", "copyFile", "pasteFile", "createUser", "showUsers"
    };

    //TODO : OutOfMemmoryException <- creatingFiles

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new my.fileManager.components.MainFrame();
            }
        });
        try
        {
            synchronized (Thread.currentThread())
            {
                Thread.currentThread().wait();
            }
        }
        catch(java.lang.InterruptedException e)
        {
            System.err.println(e.getMessage());
        }
        /*
        label:
        while (true) {
            System.out.println("Enter option:");
            String option = OperationManager.reader.next();

            switch (option) {
                case "createFolder":
                    OperationManager.createFolder();
                    break;
                case "createFile":
                    OperationManager.createFile();
                    break;
                case "moveTo":
                    OperationManager.moveTo();
                    break;
                case "showFiles":
                    OperationManager.showFiles();
                    break;
                case "renameFile":
                    OperationManager.renameFile();
                    break;
                case "deleteFile":
                    OperationManager.deleteFile();
                    break;
                case "copyFile":
                    OperationManager.copyFile();
                    break;
                case "pasteFile":
                    OperationManager.pasteFile();
                    break;
                case "createUser":
                    OperationManager.createUser();
                case "showUsers":
                    OperationManager.listUsers();
                    break;
                case "help":
                    listHelpList();
                    break;
                case "exit":
                    break label;
            }
        }*/
    }


    private static void listHelpList()
    {
        System.out.println(StringUtility.strJoin(commands, '\n'));
    }

}
