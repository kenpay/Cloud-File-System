package my.fileManager.ServerModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;

public class Main {

    private static ServerSocket serverSocket = null;
    private static Connection conn = null;
    private static Thread acceptConnections = new Thread()
    {
        @Override
        public void run()
        {
            try {
                serverSocket = new ServerSocket(4321);
                //synchronized(serverSocket)
                //{
                    while (serverSocket != null)
                    {
                        Socket serverClientSocket = serverSocket.accept();
                        if (serverClientSocket != null)
                        {
                            System.out.println("Got connection: " + serverClientSocket.getInetAddress());
                            Thread acceptInformations = new Thread(() -> {
                                try
                                {
                                    InputStream serverInputStream = serverClientSocket.getInputStream();
                                    OutputStream serverOutputStream = serverClientSocket.getOutputStream();
                                    Scanner scanner = new Scanner(serverInputStream);
                                    PrintWriter socketWriter = new PrintWriter(serverOutputStream);
                                    String line;
                                    while (scanner.hasNextLine()) {
                                        System.out.println("Server " + serverSocket.getInetAddress() +"(" + InetAddress.getLocalHost().getHostAddress() + ") - from Client" + serverClientSocket.getInetAddress() + ": " + (line = scanner.nextLine()));
                                        if (line.equals("getUsers"))
                                            getUsers(socketWriter);
                                        else if (line.startsWith("getPasswordForUser"))
                                            getPasswordForUser(line.substring(19), socketWriter);
                                        else if (line.startsWith("createUser"))
                                            createUser(line.substring(11), socketWriter);
                                        else if (line.equals("getFileSystem"))
                                            getFileSystem(socketWriter);
                                        socketWriter.flush();
                                    }
                                    System.out.println("Disconnected: " + serverClientSocket.getInetAddress());
                                }
                                catch (IOException e)
                                {
                                    System.err.println(e.getMessage());
                                }
                            });
                            acceptInformations.start();
                        }
                    }
                //}
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }

        private void createUser(String userInformations, PrintWriter socketWriter)
        {
            if (conn != null)
            {
                try(Statement stmt = conn.createStatement())
                {
                    String[] userDetails = userInformations.split(":");
                    ResultSet resultSet = stmt.executeQuery("select password from users where name = '" + userInformations + "'");
                    StringBuilder stringBuilder = new StringBuilder();
                    if(resultSet.next())
                        socketWriter.println(resultSet.getString(1));
                }
                catch(SQLException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }

        private void getPasswordForUser(String user, PrintWriter socketWriter)
        {
            if (conn != null)
            {
                try(Statement stmt = conn.createStatement())
                {
                    ResultSet resultSet = stmt.executeQuery("select password from users where name = '" + user + "'");
                    StringBuilder stringBuilder = new StringBuilder();
                    if(resultSet.next())
                        socketWriter.println(resultSet.getString(1));
                }
                catch(SQLException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }

        private void getUsers(PrintWriter socketWriter)
        {
            if (conn != null)
            {
                try(Statement stmt = conn.createStatement())
                {
                    ResultSet resultSet = stmt.executeQuery("select * from users");
                    StringBuilder stringBuilder = new StringBuilder();
                    while(resultSet.next())
                    {
                        stringBuilder.append(resultSet.getInt(1)).append('.');
                        stringBuilder.append(resultSet.getString(2)).append('.');
                        stringBuilder.append(resultSet.getByte(4)).append(';');
                    }
                    socketWriter.println(stringBuilder.toString());
                }
                catch(SQLException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }

        private void getFileSystem(PrintWriter socketWriter)
        {
            //synchronized (conn)
            //{
                if(conn != null)
                {
                    try(Statement stmt = conn.createStatement())
                    {
                        ResultSet resultSet = stmt.executeQuery("select * from drives;");

                        if (resultSet.next())
                        {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(resultSet.getInt(1)).append(';');
                            stringBuilder.append(resultSet.getString(2)).append(';');
                            stringBuilder.append(resultSet.getFloat(3)).append("-"); // THIS WILL BE THE CONFIGURATION
                            // ALWAYS WILL HAVE ID 0


                            while (resultSet.next()) {
                                stringBuilder.append(resultSet.getInt(1)).append('.');
                                stringBuilder.append(resultSet.getString(2)).append('.');
                                stringBuilder.append(resultSet.getFloat(3)).append(';');
                            }
                            stringBuilder.append('-');
                            resultSet = stmt.executeQuery("select * from folders;");
                            while (resultSet.next()) {
                                stringBuilder.append(resultSet.getInt(1)).append('.');
                                stringBuilder.append(resultSet.getString(2)).append('.');
                                stringBuilder.append(resultSet.getInt(3)).append('.');
                                stringBuilder.append(resultSet.getInt(4)).append(';');
                            }
                            stringBuilder.append('-');
                            resultSet = stmt.executeQuery("select * from files;");
                            while (resultSet.next()) {
                                stringBuilder.append(resultSet.getInt(1)).append('.');
                                stringBuilder.append(resultSet.getString(2)).append('.');
                                stringBuilder.append(resultSet.getInt(3)).append('.');
                                stringBuilder.append(resultSet.getInt(4)).append(';');
                            }

                            socketWriter.println(stringBuilder.toString());
                        }
                    }
                    catch (SQLException e)
                    {
                        System.err.println(e.getMessage());
                    }
                }
            //}
        }
    };

    public static void main(String[] args)
    {
        acceptConnections.start();
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+InetAddress.getLocalHost().getHostAddress()+"/sgf","root","");
            Scanner reader = new Scanner(System.in);
            while (!reader.nextLine().equals("exit")) {
                ;;;;;
            }
            synchronized (serverSocket)
            {
                serverSocket.close();
            }
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
}
