package managers;

import commands.CommandType;
import network.requests.Request;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashMap;

public class RequestManager {
    private final HashMap<CommandType, Request> data = new HashMap<>();

    public void addRequest(Request req) {
        this.data.put(req.getCommandType(), req);
    }

    public void removeRequest(CommandType key) {
        this.data.remove(key);
    }

    public Request getRequest(CommandType key) {
        return this.data.get(key);
    }

    public void setUserContainer(ImmutablePair<String, String> userContainer) {
        for (Request req:this.data.values()){
            req.setUserContainer(userContainer);
        }
    }
}
