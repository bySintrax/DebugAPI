package de.sintrax.debugAPI.config;

import de.sintrax.debugAPI.api.DebugManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class DebugConfig {
    private final ConfigManager configManager;
    private Object config;
    private File configFile;

    public DebugConfig(ConfigManager configManager) {
        this.configManager = configManager;
        load();
    }

    public void load() {
        try {
            if (configManager.isBukkit()) {
                loadBukkitConfig();
            } else {
                loadBungeeConfig();
            }
        } catch (IOException e) {
            throw new RuntimeException("Konfiguration konnte nicht geladen werden", e);
        }
    }

    private void loadBukkitConfig() throws IOException {
        JavaPlugin plugin = (JavaPlugin) configManager.getPlugin();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        configFile = new File(plugin.getDataFolder(), "debug.yml");
        if (!configFile.exists()) {
            try (InputStream in = plugin.getResource("debug.yml")) {
                Files.copy(in, configFile.toPath());
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        setDefaults();
    }

    private void loadBungeeConfig() throws IOException {
        net.md_5.bungee.api.plugin.Plugin plugin =
                (net.md_5.bungee.api.plugin.Plugin) configManager.getPlugin();

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        configFile = new File(plugin.getDataFolder(), "debug.yml");

        if (!configFile.exists()) {
            try (InputStream in = plugin.getResourceAsStream("debug.yml")) {
                Files.copy(in, configFile.toPath());
            }
        }

        // Korrekte Verwendung der BungeeCord Configuration API
        config = net.md_5.bungee.config.ConfigurationProvider
                .getProvider(net.md_5.bungee.config.YamlConfiguration.class)
                .load(configFile);
        setDefaults();
    }

    private void setDefaults() {
        if (configManager.isBukkit()) {
            FileConfiguration cfg = (FileConfiguration) config;
            cfg.addDefault("global-debug", false);
            cfg.addDefault("stacktrace-enabled", true);
            cfg.addDefault("console-log-level", "INFO");
            cfg.addDefault("default-player-level", "INFO");
            cfg.options().copyDefaults(true);
        } else {
            Configuration cfg = (Configuration) config;
            cfg.set("global-debug", cfg.getBoolean("global-debug", false));
            cfg.set("stacktrace-enabled", cfg.getBoolean("stacktrace-enabled", true));
            cfg.set("console-log-level", cfg.getString("console-log-level", "INFO"));
            cfg.set("default-player-level", cfg.getString("default-player-level", "INFO"));
        }
        save();
    }

    public void reload() {
        load();
    }

    public void save() {
        try {
            if (configManager.isBukkit()) {
                ((org.bukkit.configuration.file.FileConfiguration) config).save(configFile);
            } else {
                // Korrekte Speichermethode für BungeeCord
                net.md_5.bungee.config.ConfigurationProvider
                        .getProvider(net.md_5.bungee.config.YamlConfiguration.class)
                        .save((net.md_5.bungee.config.Configuration) config, configFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Konfiguration konnte nicht gespeichert werden", e);
        }
    }

    public boolean isGlobalDebugEnabled() {
        if (configManager.isBukkit()) {
            return ((FileConfiguration) config).getBoolean("global-debug");
        }
        return ((Configuration) config).getBoolean("global-debug");
    }

    public void setGlobalDebugEnabled(boolean enabled) {
        if (configManager.isBukkit()) {
            ((FileConfiguration) config).set("global-debug", enabled);
        } else {
            ((Configuration) config).set("global-debug", enabled);
        }
        save();
    }

    public boolean isStackTraceEnabled() {
        if (configManager.isBukkit()) {
            return ((FileConfiguration) config).getBoolean("stacktrace-enabled");
        }
        return ((Configuration) config).getBoolean("stacktrace-enabled");
    }

    public void setStackTraceEnabled(boolean enabled) {
        if (configManager.isBukkit()) {
            ((FileConfiguration) config).set("stacktrace-enabled", enabled);
        } else {
            ((Configuration) config).set("stacktrace-enabled", enabled);
        }
        save();
    }

    public DebugManager.LogLevel getConsoleLogLevel() {
        String level;
        if (configManager.isBukkit()) {
            level = ((FileConfiguration) config).getString("console-log-level", "INFO");
        } else {
            level = ((Configuration) config).getString("console-log-level", "INFO");
        }

        try {
            return DebugManager.LogLevel.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DebugManager.LogLevel.INFO;
        }
    }

    public void setConsoleLogLevel(DebugManager.LogLevel level) {
        if (configManager.isBukkit()) {
            ((FileConfiguration) config).set("console-log-level", level.name());
        } else {
            ((Configuration) config).set("console-log-level", level.name());
        }
        save();
    }

    public DebugManager.LogLevel getDefaultPlayerLevel() {
        String level;
        if (configManager.isBukkit()) {
            level = ((FileConfiguration) config).getString("default-player-level", "INFO");
        } else {
            level = ((Configuration) config).getString("default-player-level", "INFO");
        }

        try {
            return DebugManager.LogLevel.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DebugManager.LogLevel.INFO;
        }
    }
}