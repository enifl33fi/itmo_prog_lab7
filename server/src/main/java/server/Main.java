package server;

import collections.CollectionGenerator;
import collections.InteractiveCollection;
import collections.SpaceMarineCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import managers.CommandLoader;
import managers.CommandManager;
import managers.ObjectsKeeper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 2){
            String username = args[0];
            String password = args[1];
            ObjectsKeeper objectsKeeper = new ObjectsKeeper();
            try {
                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", username, password);
                DataBasesManager dbManager = objectsKeeper.getDbManager();
                dbManager.setConnection(connection);
                dbManager.init();
                CommandManager commandManager = objectsKeeper.getCommandManager();
                CommandLoader commandLoader = new CommandLoader(commandManager);
                InteractiveCollection curCol = new SpaceMarineCollection(dbManager);
                UsersCollection usersCol = new UsersCollection(dbManager);
                commandLoader.load(curCol, usersCol);
                new CollectionServer(4569, commandManager, curCol, dbManager).start(username, password);
            }catch(SQLException e){
                System.out.println("Can't connect to data base.");
            }
        } else {
            System.out.println("Don't have required username and password");
        }


    }
}
