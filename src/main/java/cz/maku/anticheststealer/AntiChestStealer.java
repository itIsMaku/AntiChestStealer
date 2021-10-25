package cz.maku.anticheststealer;

import cz.maku.anticheststealer.command.DetectionCommand;
import cz.maku.anticheststealer.gui.ProtectionGUI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiChestStealer extends JavaPlugin {

    @Getter
    private static AntiChestStealer plugin;

    public static void detect(JavaPlugin plugin, Player player, int closeTicks) {
        ProtectionGUI protectionGUI = new ProtectionGUI();
        Bukkit.getPluginManager().registerEvents(protectionGUI, plugin);
        protectionGUI.create();
        protectionGUI.open(player);
        Bukkit.getScheduler().runTaskLater(plugin, player::closeInventory, closeTicks);
    }

    public static void detect(Player player, int closeTicks) {
        detect(AntiChestStealer.getPlugin(), player, closeTicks);
    }

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        getCommand("detection").setExecutor(new DetectionCommand());
    }
}