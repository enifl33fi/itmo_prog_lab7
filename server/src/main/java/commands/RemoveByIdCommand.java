package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class RemoveByIdCommand extends Command {
    public RemoveByIdCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description = "remove_by_id id : remove an item from the collection by its id";
        this.commandType = CommandType.REMOVE_BY_ID;
    }

    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Can't remove by id.");
                if (req.hasArg() && !req.hasElement()){
                    try {
                        res.setResponseLine(this.curCol.removeById(Long.parseLong(req.getArg()), req.getUserContainer().getLeft()));
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
