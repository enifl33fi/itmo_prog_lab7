package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/** Class using for executing remove_lower command.
 * @author Kirill Markov
 * @version 1.0*/
public class RemoveLowerCommand extends Command {
    public RemoveLowerCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description =
                "remove_lower {element} : remove all elements from the collection that are smaller than the given one";
        this.commandType = CommandType.REMOVE_LOWER;
    }

    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Remove lower failed.");
                if (req.hasElement() && !req.hasArg()){
                    try{
                        res.setResponseLine(this.curCol.removeLower(req.getElement(), req.getUserContainer().getLeft()));
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

