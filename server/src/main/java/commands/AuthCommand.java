package commands;

import collections.InteractiveCollection;
import collections.UsersCollection;
import network.Response;
import network.requests.Request;

public class AuthCommand extends Command{
    public AuthCommand(InteractiveCollection curCol, UsersCollection usersCol){
        super(curCol, usersCol);
        this.commandType = CommandType.AUTH;
    }

    @Override
    public Response execute(Request req) {
        if (!req.hasArg() && !req.hasElement()){
            if (req.isRegistration()){
                return usersCol.signUp(req.getUserContainer());
            }
            else {
                return usersCol.signIn(req.getUserContainer());
            }
        }
        return new Response("Incorrect request.");
    }

}
