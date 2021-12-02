package dev.pandora.core.api.handler.command;

import dev.pandora.core.Main;
import dev.pandora.core.api.enumeration.Rank;
import org.bukkit.command.CommandExecutor;

/**
 * Created by Zvijer on 15.3.2017..
 */
public abstract class CommandBase implements CommandExecutor {

    private String command, usage;
    private String[] aliases;
    private Rank[] required;
    private int length;
    private boolean player, console;

    public CommandBase(String cmd, String usage, Rank[] required, int length) {
        this(cmd, usage, null, required, length, true, true);
    }

    public CommandBase(String cmd, Rank[] required, int length) {
        this(cmd, null, null, required, length, true, true);
    }

    //Console only
    public CommandBase(String cmd, String usage, String[] aliases, int length) {
        this(cmd, usage, aliases, null, length, false, true);
    }

    //Player only
    public CommandBase(String cmd, String usage, String[] aliases, Rank[] required, int length) {
        this(cmd, usage, aliases, required, length, true, false);
    }

    //No need to externally register the commands, instantiation will handle that for us
    public CommandBase(String cmd, String usage, String[] aliases, Rank[] required, int length, boolean player, boolean console) {
        this.command = cmd;
        this.usage = usage;
        this.aliases = (aliases != null) ? aliases : new String[]{"No provided aliases."};
        this.required = (required != null) ? required : new Rank[]{Rank.ALL};   //No rank needed
        this.length = length;
        this.player = player;
        this.console = console;
        register();
    }

    final void register() {
        Main.get().getCommand(command).setExecutor(this);
    }

    protected String getCommand() {
        return command;
    }

    protected String getUsage() {
        return usage;
    }

    protected String[] getAliases() {
        return aliases;
    }

    protected Rank[] getRequired() {
        return required;
    }

    protected int getLength() {
        return length;
    }

    protected boolean isPlayer() {
        return player;
    }

    protected boolean isConsole() {
        return console;
    }

    protected boolean isBoth() {
        return player && console;
    }
}
