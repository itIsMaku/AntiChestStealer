package cz.maku.anticheststealer.command;

import cz.maku.anticheststealer.AntiChestStealer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DetectionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("anticheststealer.detection")) {
            if (args.length < 1) {
                sender.sendMessage("Provide player, please.");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("Player is offline.");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage("Provide ticks, please.");
                return true;
            }
            int ticks = Integer.parseInt(args[1]);
            AntiChestStealer.detect(target, ticks);
        } else {
            sender.sendMessage("No permissions.");
        }
        return false;
    }
}
