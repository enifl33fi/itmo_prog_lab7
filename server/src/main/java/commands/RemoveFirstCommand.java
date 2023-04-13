package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class RemoveFirstCommand extends Command {
    public RemoveFirstCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description = "remove_first : remove the first item in the collection";
        this.commandType = CommandType.REMOVE_FIRST;
    }

    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Can't remove first element from the collection");
                if (!req.hasArg() && !req.hasElement()){
                    try {
                        res.setResponseLine(this.curCol.removeFirst(req.getUserContainer().getLeft()) + "\n");
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
