package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;

public class ExitCommand extends Command {
    public ExitCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description = "exit : end the program (without saving it to a file)";
        this.commandType = CommandType.EXIT;
    }

    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Can't exit an app.");
                if (!req.hasArg() && !req.hasElement()){
                    res.setExit(true);
                    res.setResponseLine("bye bye");
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
