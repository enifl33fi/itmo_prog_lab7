package dataBases;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBasesManager {
    private final UsersTable usersTable = new UsersTable();
    private final SpaceMarineTable spaceMarineTable = new SpaceMarineTable();

    public void setConnection(Connection connection){
        usersTable.setConnection(connection);
        spaceMarineTable.setConnection(connection);
    }

    public void init(){
        try {
            usersTable.init();
            spaceMarineTable.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UsersTable getUsersTable(){
        return this.usersTable;
    }

    public SpaceMarineTable getSpaceMarineTable(){
        return this.spaceMarineTable;
    }

}
