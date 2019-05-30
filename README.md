# Cloud-File-System
Computer Science Project

Cloud File System is a file system with client-server communication and a database connection.

Server -> databaseConnection
Client -> Server

Server processes informations from and to database and eventually transfer them to client and is used as storage.
Future update : ClientsSynchronization

Client has a graphical representation of file system and can do several action.

File actions permitted:
-Copy
-Cut
-Paste
-Create
Future:
-Rename
-Permissions

File subdivisions : File, Folder, Drive.

NEW: A folder has space property, which means that a folder can storage up to a maximum value.

Structure exemple:
              Configuration ( Total space: <- Actually a Drive )
           Drive        Drive
      Folder    File  Folder  Folder
                     File       File
                     
                     
