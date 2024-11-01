sudo apt install sqlite3

Setting Up SQLite in IntelliJ IDEA Ultimate

    Open Database Tool Window: IntelliJ IDEA Ultimate has a Database tool window where you can connect to SQLite and manage your databases.

    Add SQLite Database:
        Open Database Tool Window by going to View > Tool Windows > Database.
        Click on the + icon and choose Data Source > SQLite.
        In the Data Source and Drivers window, locate the SQLite driver (IntelliJ might automatically download it for you).
        Browse to the path of your .db file (e.g., mydatabase.db) and select it.
        Test the connection to make sure IntelliJ can access the SQLite database.

    Running SQL Queries: Once the connection is established, you can open a SQL console by right-clicking on the database and selecting Open Console. Here, you can execute SQL commands directly on the database file.