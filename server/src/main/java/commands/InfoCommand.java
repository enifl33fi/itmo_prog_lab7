package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;

public class InfoCommand extends Command {
    public InfoCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description =
                "info : print information about the collection (type, initialization date, number of items, etc.) in the standard output.";
        this.commandType = CommandType.INFO;
    }

    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Can't get information about the collection");
                if (!req.hasArg() && !req.hasElement()){
                    res.setResponseLine(String.format("Information about the collection:%n%s", this.curCol.info()));
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