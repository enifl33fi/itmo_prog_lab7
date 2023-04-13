package network.requests;

import commands.CommandType;

public class AuthRequest extends Request{

    public AuthRequest(){
        super(CommandType.AUTH);
    }
    @Override
    public Request get(boolean fromFile){
        return this;
    }
}
