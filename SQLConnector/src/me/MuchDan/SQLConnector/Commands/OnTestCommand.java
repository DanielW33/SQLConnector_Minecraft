package me.MuchDan.SQLConnector.Commands;

import com.google.common.util.concurrent.Service;
import me.MuchDan.SQLConnector.SQLConnector;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class OnTestCommand implements CommandExecutor {
    private SQLConnector plugin;
    public OnTestCommand(SQLConnector plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("SQLTest")) {
            if (!(Sender instanceof Player)) {
                Sender.sendMessage("Only Players can run this command.");
                return false;
            }
            Player player = (Player) Sender;
            if(args.length < 1) {
                try {
                    plugin.getSQLData().CreatePlayer(player);
                    plugin.getServer().getLogger().log(Level.INFO, "Successfully created player.");
                } catch (SQLException throwables) {
                    plugin.getServer().getLogger().log(Level.SEVERE, "Failed to create player.");
                }
                return true;
            }else if(args[0].equalsIgnoreCase("delete")) {
                try {
                    if (plugin.getSQLData().exists(player.getUniqueId())) {
                        try {
                            plugin.getSQLData().DeletePlayer(player);
                            plugin.getServer().getLogger().log(Level.INFO, "Successfully deleted player from database!");
                        } catch (SQLException throwables) {
                            plugin.getServer().getLogger().log(Level.SEVERE, "Failed to delete player from database.");
                        }
                        return true;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

}
