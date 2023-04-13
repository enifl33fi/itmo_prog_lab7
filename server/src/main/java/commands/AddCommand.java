package commands;


import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class AddCommand extends Command {
    public AddCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description = "add {element} : add a new element to the collection";
        this.commandType = CommandType.ADD;
    }


    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())){
                res.setResponseLine("Incorrect response. Add failed.");
                if (req.hasElement() && !req.hasArg()){
                    try {
                        this.curCol.add(req.getElement());
                        res.setResponseLine("Element was successfully added.");
                    } catch (SQLException e) {
                        res.setResponseLine("Problems with db.");
                    }
                }
            } else {
                res.setExit(true);
            }
        } catch (NoSuchAlgorithmException e) {
            res.setResponseLine("Server exception while handling login and password");
        }
        return res;
    }
}
