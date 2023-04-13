package managers;

import collections.SpaceMarineCollection;
import dataBases.DataBasesManager;

public class ObjectsKeeper {

    private final DataBasesManager dbManager = new DataBasesManager();

    private final CommandManager commandManager = new CommandManager();
    public CommandManager getCommandManager() {
        return commandManager;
    }


    public DataBasesManager getDbManager() {
        return this.dbManager;
    }
}
