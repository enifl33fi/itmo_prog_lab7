package collections;

import dataBases.DataBasesManager;
import dataBases.UsersTable;
import network.Response;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class UsersCollection {
    private final UsersTable usersTable;
    private final ConcurrentHashMap<String, String> data;

    public UsersCollection(DataBasesManager dbManager) {
        this.usersTable = dbManager.getUsersTable();
        this.data = usersTable.getUsersCollection();
    }

    public void addUser(ImmutablePair<String, String> userContainer) throws SQLException, NoSuchAlgorithmException {
        String login = userContainer.getLeft();
        String password = userContainer.getRight();
        String encPassword = this.encryptMD2(password);
        this.usersTable.addUser(login, encPassword);
        this.data.put(login, encPassword);
    }

    public boolean checkUser(ImmutablePair<String, String> userContainer) throws NoSuchAlgorithmException {
        String login = userContainer.getLeft();
        String password = userContainer.getRight();
        if (data.containsKey(login)){
            String curPassword = this.encryptMD2(password);
            return curPassword.equals(data.get(login));


        }
        return false;
    }

    public Response signIn(ImmutablePair<String, String> userContainer){
        Response res = new Response("Sign in failed.");
        try{
            if (this.checkUser(userContainer)){
                res.setResponseLine("You have successfully signed in.");
                res.setRegistered(true);
            }else {
                res.setResponseLine("Access denied. Incorrect login or password.");
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    public Response signUp(ImmutablePair<String, String> userContainer){
        Response res = new Response("Registration failed.");
        try{
            this.addUser(userContainer);
            res.setRegistered(true);
            res.setResponseLine("You have successfully registered.");
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    public String encryptMD2(String message) throws NoSuchAlgorithmException {
        String salt = "9| XyLg@N4eG";
        message += salt;
        byte[] bytesOfMessage = message.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("MD2");
        byte[] theMD2digest = md.digest(bytesOfMessage);
        String messageMD2 = new BigInteger(1, theMD2digest).toString(16);
        return messageMD2;
    }
}
