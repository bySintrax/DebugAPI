package de.sintrax.debugAPI.api;

import de.sintrax.debugAPI.config.DebugConfig;
import de.sintrax.debugAPI.utils.DebugUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.logging.Level;

public class DebugManager {
    private final DebugConfig config;
    private final Map<UUID, LogLevel> playerDebugLevels = new HashMap<>();
    private LogLevel consoleDebugLevel;
    private boolean globalDebug;
    private boolean stackTraceEnabled;

    public enum LogLevel {
        INFO(DebugDesign.INFO_PREFIX, DebugDesign.INFO_COLOR, Level.INFO),
        WARN(DebugDesign.WARNING_PREFIX, DebugDesign.WARNING_COLOR, Level.WARNING),
        DEBUG(DebugDesign.DEBUG_PREFIX, DebugDesign.DEBUG_COLOR, Level.CONFIG),
        ERROR(DebugDesign.ERROR_PREFIX, DebugDesign.ERROR_COLOR, Level.SEVERE);

        private final String prefix;
        private final String color;
        private final Level bukkitLevel;

        LogLevel(String prefix, String color, Level bukkitLevel) {
            this.prefix = prefix;
            this.color = color;
            this.bukkitLevel = bukkitLevel;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getColor() {
            return color;
        }

        public Level getBukkitLevel() {
            return bukkitLevel;
        }
    }

    public DebugManager(DebugConfig config) {
        this.config = config;
        loadConfig();
    }

    private void loadConfig() {
        this.globalDebug = config.isGlobalDebugEnabled();
        this.stackTraceEnabled = config.isStackTraceEnabled();
        this.consoleDebugLevel = config.getConsoleLogLevel();
    }

    public void reload() {
        config.reload();
        loadConfig();
        consoleLog(LogLevel.INFO, "Konfiguration wurde neu geladen");
    }

    public boolean isStackTraceEnabled() {
        return stackTraceEnabled;
    }

    public void setStackTraceEnabled(boolean enabled) {
        this.stackTraceEnabled = enabled;
        config.setStackTraceEnabled(enabled);
        consoleLog(LogLevel.INFO, "Stacktrace-Ausgaben " + (enabled ? "§aaktiviert" : "§cdeaktiviert"));
    }

    public boolean isGlobalDebug() {
        return globalDebug;
    }

    public LogLevel getConsoleDebugLevel() {
        return consoleDebugLevel;
    }

    public void setPlayerDebugLevel(Player player, LogLevel level) {
        playerDebugLevels.put(player.getUniqueId(), level);
    }

    public void setPlayerDebugLevel(ProxiedPlayer player, LogLevel level) {
        playerDebugLevels.put(player.getUniqueId(), level);
    }

    public LogLevel getPlayerDebugLevel(Player player) {
        return playerDebugLevels.getOrDefault(player.getUniqueId(), config.getDefaultPlayerLevel());
    }

    public LogLevel getPlayerDebugLevel(ProxiedPlayer player) {
        return playerDebugLevels.getOrDefault(player.getUniqueId(), config.getDefaultPlayerLevel());
    }

    public void setConsoleDebugLevel(LogLevel level) {
        this.consoleDebugLevel = level;
        config.setConsoleLogLevel(level);
    }

    public void setGlobalDebug(boolean enabled) {
        this.globalDebug = enabled;
        config.setGlobalDebugEnabled(enabled);
    }

    public void log(CommandSender sender, LogLevel level, String message) {
        if (DebugAPI.getInstance().isBukkit()) {
            if (sender instanceof Player) {
                playerLog((Player) sender, level, message);
            } else {
                consoleLog(level, message);
            }
        } else {
            if (sender instanceof ProxiedPlayer) {
                proxyPlayerLog((ProxiedPlayer) sender, level, message);
            } else {
                consoleLog(level, message);
            }
        }
    }

    public void playerLog(Player player, LogLevel level, String message) {
        if (shouldLog(player, level)) {
            player.sendMessage(formatMessage(level, message, true));
        }
    }

    public void proxyPlayerLog(ProxiedPlayer player, LogLevel level, String message) {
        if (shouldLog(player, level)) {
            player.sendMessage(formatMessage(level, message, true));
        }
    }

    public void consoleLog(LogLevel level, String message) {
        String formatted = formatMessage(level, message, false);

        if (DebugAPI.getInstance().isBukkit()) {
            Bukkit.getConsoleSender().sendMessage(formatted);
            Bukkit.getLogger().log(level.getBukkitLevel(), ChatColor.stripColor(formatted));
        } else {
            net.md_5.bungee.api.ProxyServer.getInstance().getConsole().sendMessage(formatted);
        }

        if (globalDebug) {
            broadcast(level, formatted);
        }
    }

    private void broadcast(LogLevel level, String formattedMessage) {
        if (DebugAPI.getInstance().isBukkit()) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> shouldLog(p, level))
                    .forEach(p -> p.sendMessage(formattedMessage));
        } else {
            net.md_5.bungee.api.ProxyServer.getInstance().getPlayers().stream()
                    .filter(p -> shouldLog(p, level))
                    .forEach(p -> p.sendMessage(formattedMessage));
        }
    }

    private boolean shouldLog(Player player, LogLevel level) {
        return level.ordinal() >= getPlayerDebugLevel(player).ordinal();
    }

    private boolean shouldLog(ProxiedPlayer player, LogLevel level) {
        return level.ordinal() >= getPlayerDebugLevel(player).ordinal();
    }

    public void sendAvailableLevels(Player player) {
        player.sendMessage(buildLevelList());
    }

    public void sendAvailableLevels(ProxiedPlayer player) {
        player.sendMessage(buildLevelList());
    }

    private String buildLevelList() {
        StringBuilder levels = new StringBuilder("§6Verfügbare Log-Level:\n");
        for (LogLevel level : LogLevel.values()) {
            levels.append(level.getPrefix())
                    .append("§7 - ")
                    .append(level.name().toLowerCase())
                    .append("\n");
        }
        return levels.toString();
    }

    private String formatMessage(LogLevel level, String message, boolean forPlayer) {
        try {
            String formatted = level.getPrefix() + level.getColor() + message;

            if (stackTraceEnabled && !forPlayer) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                StackTraceElement element = stackTrace[Math.min(4, stackTrace.length - 1)];
                String callerInfo = String.format("%s.%s():%d",
                        DebugUtils.simplifyClassName(element.getClassName()),
                        element.getMethodName(),
                        element.getLineNumber());

                formatted += DebugDesign.SEPARATOR + DebugDesign.CALLER_PREFIX + callerInfo;
            }

            return formatted;
        } catch (Exception e) {
            return level.getPrefix() + level.getColor() + message + " §c(Fehler beim Formatieren)";
        }
    }
}