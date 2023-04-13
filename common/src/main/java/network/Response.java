package network;

import commands.CommandType;

import java.io.Serializable;
import java.util.Arrays;

public class Response implements Serializable {
    private CommandType commandType;
    private String responseLine;

    private boolean isExit;

    private boolean isRegistered;

    public Response(String responseLine){
        this.responseLine = responseLine;
        this.isExit = false;
        this.isRegistered = false;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public String getResponseLine() {
        return responseLine;
    }

    public void setResponseLine(String responseLine) {
        this.responseLine = responseLine;
    }

    public boolean isExit() {
        return this.isExit;
    }

    public void setExit(boolean exit) {
        this.isExit = exit;
    }

    public boolean isRegistered() {
        return this.isRegistered;
    }

    public void setRegistered(boolean registered) {
        this.isRegistered = registered;
    }
}