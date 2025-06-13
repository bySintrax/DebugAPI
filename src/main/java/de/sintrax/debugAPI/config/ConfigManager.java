package de.sintrax.debugAPI.config;

import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.plugin.Plugin;

public class ConfigManager {
    private final Object plugin;
    private final boolean isBukkit;
    private DebugConfig debugConfig;

    public ConfigManager(Object plugin, boolean isBukkit) {
        this.plugin = plugin;
        this.isBukkit = isBukkit;
        this.debugConfig = new DebugConfig(this);
    }

    public void reload() {
        debugConfig.reload();
    }

    public DebugConfig getDebugConfig() {
        return debugConfig;
    }

    public Object getPlugin() {
        return plugin;
    }

    public boolean isBukkit() {
        return isBukkit;
    }
}