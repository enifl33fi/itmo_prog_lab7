package commands;

import collections.UsersCollection;
import dataBases.DataBasesManager;
import managers.CommandManager;
import collections.InteractiveCollection;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;

public class HelpCommand extends Command {
    private final CommandManager commandManager;

    public HelpCommand(InteractiveCollection curCol, UsersCollection usersCol, CommandManager commandManager) {
        super(curCol, usersCol);
        this.description = "help : print help for available commands";
        this.commandType = CommandType.HELP;
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Can't show help");
                if (!req.hasArg() && !req.hasElement()){
                    res.setResponseLine(this.commandManager.getCommandsDescr() + "\n");
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