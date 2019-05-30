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
                                            createUser(line.substring(11));
                                        else if (line.equals("getFileSystem"))
                                            getFileSystem(socketWriter);
                                        else if (line.startsWith("create"))
                                            createElement(line.substring(6));
                                        else if (line.startsWith("changeParentFor"))
                                            changeParentFor(line.substring(15));
                                        else if (line.startsWith("remove"))
                                            remove(line.substring(6));

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

        private void remove(String table, int id)
        {
            if (conn != null)
            {
                try(Statement stmt = conn.createStatement())
                {
                    stmt.executeUpdate("DELETE FROM " + table + " WHERE Id="+id);
                }
                catch(SQLException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }

        private void remove(String type)
        {
            String[] what = type.split(":");
            remove(what[0]+"s", Integer.parseInt(what[1]));
        }

        private void update(String table, int id, int parentId)
        {
            if (conn != null)
            {
                try(Statement stmt = conn.createStatement())
                {
                    stmt.executeUpdate("UPDATE " + table + " SET ParentFolderId="+parentId+" WHERE Id="+id);
                }
                catch(SQLException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }

        private void changeParentFor(String element)
        {
            String[] what = element.split(":");
            what[0] = what[0].toLowerCase();
            String[] properties = what[1].split(",");
            int id = Integer.parseInt(properties[0]), parentId = Integer.parseInt(properties[1]);
            update(what[0]+"s", id, parentId);
        }

        private void createFile(int id, String name, double size, int parentIdFolder)
        {
            if (conn != null)
            {
                try(Statement stmt = conn.createStatement())
                {
                    stmt.executeUpdate("INSERT INTO files(Id, Name, Size, ParentFolderId) VALUES ("+id+",'"+name+"',"+size+","+parentIdFolder+");");
                }
                catch(SQLException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }

        private void createFolder(int id, String name, double space, int parentIdFolder)
        {
            if (conn != null)
            {
                try(Statement stmt = conn.createStatement())
                {
                    stmt.executeUpdate("INSERT INTO folders(Id, Name, ParentFolderId, Space) VALUES ("+id+",'"+name+"',"+parentIdFolder+"," +space+");");
                }
                catch(SQLException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }

        private void createDrive(int id, String name, double space)
        {
            if (conn != null)
            {
                try(Statement stmt = conn.createStatement())
                {
                    stmt.executeUpdate("INSERT INTO drives(Id, Name, Space) VALUES ("+id+",'"+name+"'," +space+");");
                }
                catch(SQLException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }

        private void createElement(String element)
        {
            String[] what = element.split(":");
            what[0] = what[0].toLowerCase();
            String[] properties = what[1].split(",");
            int id = Integer.parseInt(properties[0]), parentId = Integer.parseInt(properties[4]);
            String name = properties[1];
            if (what[0].equals("file"))
                createFile(id, name, Double.parseDouble(properties[2]), parentId);
            else
            {
                double space = Double.parseDouble(properties[3]);
                if(what[0].equals("folder"))
                    createFolder(id, name, space, parentId);
                else
                    createDrive(id, name, space);
            }
        }

        private void createUser(String userInformations)
        {
            if (conn != null)
            {
                try(Statement stmt = conn.createStatement())
                {
                    String[] userDetails = userInformations.split(":");
                    stmt.executeUpdate("INSERT INTO users(Name, Password) values ('" + userDetails[0] + "', '" + userDetails[1] + "')");
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
        {//Not supported
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
                        StringBuilder stringBuilder = new StringBuilder();

                        if (resultSet.next()) {
                            stringBuilder.append(resultSet.getInt(1)).append(';');
                            stringBuilder.append(resultSet.getString(2)).append(';');
                            stringBuilder.append(resultSet.getFloat(3)); // THIS WILL BE THE CONFIGURATION
                        }
                            // ALWAYS WILL HAVE ID 0
                        stringBuilder.append('-');


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
