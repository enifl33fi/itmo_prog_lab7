package managers;
import collections.InteractiveCollection;
import collections.UsersCollection;
import commands.*;
import dataBases.DataBasesManager;

public class CommandLoader {
    private final CommandManager commandManager;

    public CommandLoader(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void load(InteractiveCollection curCol, UsersCollection usersCol) {
        Command addCommand = new AddCommand(curCol, usersCol);
        Command authCommand = new AuthCommand(curCol, usersCol);
        Command clearCommand = new ClearCommand(curCol, usersCol);
        Command countByCategoryCommand = new CountByCategoryCommand(curCol, usersCol);
        Command exitCommand = new ExitCommand(curCol, usersCol);
        Command filterContainsNameCommand = new FilterContainsNameCommand(curCol, usersCol);
        Command headCommand = new HeadCommand(curCol, usersCol);
        Command helpCommand = new HelpCommand(curCol, usersCol, this.commandManager);
        Command infoCommand = new InfoCommand(curCol, usersCol);
        Command printFieldAscendingHeartCountCommand = new PrintFieldAscendingHeartCountCommand(curCol, usersCol);
        Command removeByIdCommand = new RemoveByIdCommand(curCol, usersCol);
        Command removeFirstCommand = new RemoveFirstCommand(curCol, usersCol);
        Command removeLowerCommand = new RemoveLowerCommand(curCol, usersCol);
        Command showCommand = new ShowCommand(curCol, usersCol);
        Command updateCommand = new UpdateCommand(curCol, usersCol);

        this.commandManager.addCommand(addCommand);
        this.commandManager.addCommand(authCommand);
        this.commandManager.addCommand(clearCommand);
        this.commandManager.addCommand(countByCategoryCommand);
        this.commandManager.addCommand(exitCommand);
        this.commandManager.addCommand(filterContainsNameCommand);
        this.commandManager.addCommand(headCommand);
        this.commandManager.addCommand(helpCommand);
        this.commandManager.addCommand(infoCommand);
        this.commandManager.addCommand(printFieldAscendingHeartCountCommand);
        this.commandManager.addCommand(removeByIdCommand);
        this.commandManager.addCommand(removeFirstCommand);
        this.commandManager.addCommand(removeLowerCommand);
        this.commandManager.addCommand(showCommand);
        this.commandManager.addCommand(updateCommand);
    }
}