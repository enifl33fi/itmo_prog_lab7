package network.requests;

import commands.CommandType;
import element.ElementParser;
import element.ElementValidator;
import inputWorkers.InputHandler;

public class AddRequest extends Request {
    private final transient ElementParser elementParser = new ElementParser();
    private final transient InputHandler inputHandler;
    private final transient ElementValidator elementValidator = new ElementValidator();

    public AddRequest(InputHandler inputHandler){
        super(CommandType.ADD);
        this.inputHandler = inputHandler;
    }

    @Override
    public Request get(boolean fromFile){
        if (fromFile){
            this.setElement(elementValidator.validateSpaceMarine(inputHandler.readElem(), this.getUserContainer().getLeft()));
        } else{
            this.setElement(elementParser.parseElement(this.getUserContainer().getLeft()));
        }
        return this;
    }
}
