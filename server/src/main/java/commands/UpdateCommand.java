package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import dataBases.DataBasesManager;
import network.Response;
import network.requests.Request;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UpdateCommand extends Command {

    public UpdateCommand(InteractiveCollection curCol, UsersCollection usersCol) {
        super(curCol, usersCol);
        this.description =
                "update id {element} : update the value of an element in the collection whose id is equal to the given one";
        this.commandType = CommandType.UPDATE;
    }

    @Override
    public Response execute(Request req) {
        Response res = new Response("Incorrect login or password");
        try {
            if (this.usersCol.checkUser(req.getUserContainer())) {
                res.setResponseLine("Incorrect response. Update failed.");
                if (req.hasElement() && req.hasArg()){
                    try{
                        res.setResponseLine(this.curCol.update(Long.parseLong(req.getArg()), req.getElement(), req.getUserContainer().getLeft()));
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