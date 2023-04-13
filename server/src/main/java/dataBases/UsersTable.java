package dataBases;


import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UsersTable {
    private Connection connection;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void init() throws SQLException {
        try (Statement st = this.connection.createStatement()){
            String reqSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "login varchar(128) PRIMARY KEY," +
                    "password varchar(128) NOT NULL UNIQUE);";
            st.executeUpdate(reqSQL);
        }
    }

    public void addUser(String login, String password) throws SQLException {
        lock.writeLock().lock();
        String reqSQL = "INSERT INTO users(login, password) VALUES (?, ?);";
        try (PreparedStatement st = this.connection.prepareStatement(reqSQL)){
            st.setString(1, login);
            st.setString(2, password);
            st.executeUpdate();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public ConcurrentHashMap<String, String> getUsersCollection(){
        lock.readLock().lock();
        ConcurrentHashMap<String, String> usersCollection = new ConcurrentHashMap<>();
        try (Statement st = this.connection.createStatement()) {
            String reqSQL = "SELECT * FROM users";
            ResultSet rs = st.executeQuery(reqSQL);
            while (rs.next()){
                usersCollection.put(rs.getString("login"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
        return usersCollection;
    }
}
