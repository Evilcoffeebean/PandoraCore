package dev.pandora.core.commands;

import com.google.common.collect.Maps;
import dev.pandora.core.api.handler.command.CommandBase;
import dev.pandora.core.api.networking.database.DatabaseManager;
import dev.pandora.core.utils.ServerUtil;
import dev.pandora.core.utils.StringUtils;
import dev.pandora.core.utils.UtilLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by Zvijer on 2.4.2017..
 */
public class MsgCommand extends CommandBase {

    class MessageCache {

        private Player sender, getter;
        private final Map<UUID, String> dispatcher, receiver;

        public MessageCache(Player sender, Player getter) {
            this.sender = sender;
            this.getter = getter;
            this.dispatcher = Maps.newConcurrentMap();
            this.receiver = Maps.newConcurrentMap();
            if (!dispatcher.isEmpty() || !receiver.isEmpty()) {
                dispatcher.clear();
                receiver.clear();
            }
        }

        public String getDispatchMessage() {
            return dispatcher.get(sender.getUniqueId());
        }

        public String getReceivedMessage() {
            return receiver.get(getter.getUniqueId());
        }

        public void cache(String msg) {
            dispatcher.put(sender.getUniqueId(), msg);
            receiver.put(getter.getUniqueId(), msg);
        }

        public void invalidate() {
            Stream.of(dispatcher).forEach(uuid -> dispatcher.clear());
            Stream.of(receiver).forEach(uuid -> receiver.clear());
        }
    }

    private DatabaseManager manager;

    public MsgCommand(DatabaseManager manager) {
        super("message", "/msg <player> <message>", new String[]{"pm", "msg", "privatemessage", "privatemsg"}, null, 2);
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender caller, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(getCommand())) {
            if (!(caller instanceof Player)) return true;
            Player p = (Player) caller;
            String tag = manager.getRank(p).getTag(true);
            if (args.length < getLength()) {
                UtilLogger.help(p, "Command Center", "Usage: " + getUsage());
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            if (!ServerUtil.find(target)) {
                UtilLogger.help(p, "Online Player Search", "No such player could have been found.");
                return true;
            }
            String targetTag = manager.getRank(target).getTag(true);
            final String message = StringUtils.fromArgs(args, 1);
            final String[] format = {
                    "%sender% &c-> %receiver% &e%msg%", //Sender
                    "%receiver% &c-> %sender% &e%msg%"
            };
            MessageCache cache = new MessageCache(p, target); //Caching if for later uses, dont remove it
            cache.cache(message);
            p.sendMessage(StringUtils.colorize(format[0].replace("%sender%", tag + p.getName()).replace("%receiver%", targetTag + target.getName()).replace("%msg%", cache.getDispatchMessage())));
            target.sendMessage(StringUtils.colorize(format[0].replace("%receiver%", targetTag + target.getName()).replace("%sender%", tag + p.getName()).replace("%msg%", cache.getReceivedMessage())));
            cache.invalidate();
            return true;
        }
        return true;
    }
}
