package de.sintrax.debugAPI.api;

import de.sintrax.debugAPI.config.ConfigManager;
import de.sintrax.debugAPI.config.DebugConfig;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.plugin.Plugin;

public class DebugAPI {
    private static DebugAPI instance;
    private DebugManager debugManager;
    private ConfigManager configManager;
    private DebugConfig debugConfig;
    private boolean isBukkit;

    public DebugAPI(Object plugin) {
        if (instance != null) {
            throw new IllegalStateException("DebugAPI wurde bereits initialisiert!");
        }
        instance = this;

        this.isBukkit = plugin instanceof JavaPlugin;
        this.configManager = new ConfigManager(plugin, isBukkit);
        this.debugConfig = new DebugConfig(configManager);
        this.debugManager = new DebugManager(debugConfig);

        if (isBukkit) {
            setupBukkit((JavaPlugin) plugin);
        } else {
            setupBungee((Plugin) plugin);
        }
    }

    public static DebugAPI init(Object plugin) {
        if (instance == null) {
            new DebugAPI(plugin);
        }
        return instance;
    }

    public static DebugAPI getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DebugAPI wurde nicht initialisiert!");
        }
        return instance;
    }

    private void setupBukkit(JavaPlugin plugin) {
        plugin.getCommand("debug").setExecutor(new DebugCommand(debugManager));
        debugManager.consoleLog(DebugManager.LogLevel.INFO, "DebugAPI für Bukkit/Paper initialisiert");
    }

    private void setupBungee(Plugin plugin) {
        plugin.getProxy().getPluginManager().registerCommand(plugin,
                new BungeeDebugCommand(debugManager));
        debugManager.consoleLog(DebugManager.LogLevel.INFO, "DebugAPI für BungeeCord/Waterfall initialisiert");
    }

    public DebugManager getDebugManager() {
        return debugManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public boolean isBukkit() {
        return isBukkit;
    }
}