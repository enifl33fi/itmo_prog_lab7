package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;

public class FilterContainsNameCommand extends Command {

    public FilterContainsNameCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description =
                "filter_contains_name name : output the elements whose name field value contains the specified substring";
        this.commandType = CommandType.FILTER_CONTAINS_NAME;
    }


    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Can't filter elements whose name field value contains the specified substring.");
                if (req.hasArg() && !req.hasElement()){
                    res.setResponseLine(String.format("Elements whose name field value contains substring %s:%n%s", req.getArg(), this.curCol.filterContainsName(req.getArg())));
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
