package my.fileManager.core;

import my.fileManager.managers.OperationManager;

public class User {
    private static int generatorId = 0;
    private int id;
    private String name;
    private byte permissions;

    public User(String userName)
    {
        this.id = generatorId++;
        this.name = userName;
    }

    private User(int id, String username)
    {
        this.id = id;
        this.name = username;
    }

    public String getName()
    {
        return this.name;
    }

    /*public int getId()
    {
        return  this.id;
    }*/

/*
    //Aceaste este metoda Factory <= de observat ca daca un user se afla deja in lista va intoarce o referinta catre acesta
    //Folosita numai pentru a genera useri din fisier
    public static User createUser(int id, String userName)
    {
        User user = new User(id, userName);
        OperationManager.addUser(user);
        generatorId = id+1;
        return user;
    }
    */
}
