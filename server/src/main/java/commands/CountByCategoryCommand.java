package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import element.AstartesCategory;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;

public class CountByCategoryCommand extends Command {
    public CountByCategoryCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description =
                "count_by_category category : output the number of elements with a category value equal to the given one";
        this.commandType = CommandType.COUNT_BY_CATEGORY;
    }

    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Can't count by category.");
                if (req.hasArg() && !req.hasElement()) {
                    int count = this.curCol.countByCategory(AstartesCategory.valueOf(req.getArg()));
                    res.setResponseLine(String.format("Count of %s category - %d%n", req.getArg(), count));
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