package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;

public class ShowCommand extends Command {
    public ShowCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description =
                "show : print all items of the collection as string output in the standard output";
        this.commandType = CommandType.SHOW;
    }

    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Can't show elements.");
                if (!req.hasArg() && !req.hasElement()){
                    res.setResponseLine(String.format("Elements in this collection:%n%s", this.curCol.show()));
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
