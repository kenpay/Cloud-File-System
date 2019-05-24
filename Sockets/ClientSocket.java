package my.fileManager.Sockets;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocket extends Socket {

    private Scanner socketReader;
    private PrintWriter socketWriter;

    public ClientSocket(InetAddress ipAddress, int port) throws IOException
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

    public String getFileSystem()
    {

        socketWriter.println("getFileSystem");
        socketWriter.flush();
        return socketReader.nextLine();
    }
}
