package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import element.CollectionPart;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.function.Predicate;

public class ClearCommand extends Command {
    public ClearCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description = "clear : clear the collection";
        this.commandType = CommandType.CLEAR;
    }

    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Can't clear.");
                if (!req.hasArg() && !req.hasElement()) {
                    try {
                        this.curCol.clear(req.getUserContainer().getLeft());
                        res.setResponseLine("Collection cleared successfully.");
                    }catch (SQLException e){
                        System.out.println(e.getMessage());
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