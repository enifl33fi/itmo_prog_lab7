package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;

public class PrintFieldAscendingHeartCountCommand extends Command {
    public PrintFieldAscendingHeartCountCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description =
                "print_field_ascending_heart_count : print the heartCount values of all elements in ascending order";
        this.commandType = CommandType.PRINT_FIELD_ASCENDING_HEART_COUNT;
    }

    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Can't get heartCount values");
                if (!req.hasArg() && !req.hasElement()){
                    res.setResponseLine(String.format("HeartCount values of all elements in ascending order:%n%s", this.curCol.printFieldAscendingHeartCount()));
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
