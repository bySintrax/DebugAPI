package de.sintrax.debugAPI.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class DebugCommand implements CommandExecutor, TabCompleter {
    private final DebugManager debugManager;

    public DebugCommand(DebugManager debugManager) {
        this.debugManager = debugManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "toggle":
                handleToggleCommand(sender);
                break;
            case "stacktrace":
                handleStackTraceCommand(sender);
                break;
            case "level":
                handleLevelCommand(sender, args);
                break;
            case "global":
                handleGlobalCommand(sender, args);
                break;
            case "console":
                handleConsoleCommand(sender, args);
                break;
            case "reload":
                handleReloadCommand(sender);
                break;
            default:
                sendUsage(sender);
        }

        return true;
    }

    private void handleToggleCommand(CommandSender sender) {
        if (!checkPermission(sender, "debugapi.command.toggle")) return;

        boolean newState = !debugManager.isGlobalDebug();
        debugManager.setGlobalDebug(newState);

        String status = newState ? "§aaktiviert" : "§cdeaktiviert";
        String message = "Globaler Debug-Modus wurde " + status;

        debugManager.log(null, DebugManager.LogLevel.INFO, message);
        if (sender instanceof Player) {
            sender.sendMessage("§7" + message);
        }
    }

    private void handleStackTraceCommand(CommandSender sender) {
        if (!checkPermission(sender, "debugapi.command.stacktrace")) return;

        boolean newState = !debugManager.isStackTraceEnabled();
        debugManager.setStackTraceEnabled(newState);

        String status = newState ? "§aaktiviert" : "§cdeaktiviert";
        debugManager.log(null, DebugManager.LogLevel.INFO, "Stacktraces " + status);

        if (sender instanceof Player) {
            sender.sendMessage("§7Stacktraces sind jetzt " + status);
        }
    }

    private void handleLevelCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            debugManager.log(sender, DebugManager.LogLevel.ERROR, "Nur Spieler können ihr Level setzen");
            return;
        }

        if (!checkPermission(sender, "debugapi.command.level")) return;

        if (args.length < 2) {
            debugManager.sendAvailableLevels((Player) sender);
            debugManager.log((Player) sender, DebugManager.LogLevel.INFO,
                    "Aktuelles Level: " + debugManager.getPlayerDebugLevel((Player) sender));
            return;
        }

        try {
            DebugManager.LogLevel level = DebugManager.LogLevel.valueOf(args[1].toUpperCase());
            debugManager.setPlayerDebugLevel((Player) sender, level);
            debugManager.log((Player) sender, DebugManager.LogLevel.INFO,
                    "Level gesetzt auf: " + level.name());
        } catch (IllegalArgumentException e) {
            debugManager.log((Player) sender, DebugManager.LogLevel.ERROR,
                    "Ungültiges Level: " + args[1]);
            debugManager.sendAvailableLevels((Player) sender);
        }
    }

    private void handleGlobalCommand(CommandSender sender, String[] args) {
        if (!checkPermission(sender, "debugapi.command.global")) return;

        if (args.length < 2) {
            debugManager.log(null, DebugManager.LogLevel.INFO,
                    "Globaler Debug: " + (debugManager.isGlobalDebug() ? "§aAN" : "§cAUS"));
            return;
        }

        try {
            boolean enable = Boolean.parseBoolean(args[1]);
            debugManager.setGlobalDebug(enable);
            debugManager.log(null, DebugManager.LogLevel.INFO,
                    "Globaler Debug wurde " + (enable ? "§aaktiviert" : "§cdeaktiviert"));
        } catch (IllegalArgumentException e) {
            debugManager.log(null, DebugManager.LogLevel.ERROR,
                    "Ungültiger Wert: " + args[1] + " (erwartet true/false)");
        }
    }

    private void handleConsoleCommand(CommandSender sender, String[] args) {
        if (!checkPermission(sender, "debugapi.command.console")) return;

        if (args.length < 2) {
            debugManager.log(null, DebugManager.LogLevel.INFO,
                    "Konsolen-Level: " + debugManager.getConsoleDebugLevel());
            return;
        }

        try {
            DebugManager.LogLevel level = DebugManager.LogLevel.valueOf(args[1].toUpperCase());
            debugManager.setConsoleDebugLevel(level);
            debugManager.log(null, DebugManager.LogLevel.INFO,
                    "Konsolen-Level gesetzt auf: " + level.name());
        } catch (IllegalArgumentException e) {
            debugManager.log(null, DebugManager.LogLevel.ERROR,
                    "Ungültiges Level: " + args[1]);
        }
    }

    private void handleReloadCommand(CommandSender sender) {
        if (!checkPermission(sender, "debugapi.command.reload")) return;

        debugManager.reload();
        debugManager.log(sender instanceof Player ? (Player) sender : null,
                DebugManager.LogLevel.INFO, "Konfiguration neu geladen");
    }

    private boolean checkPermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission)) {
            return true;
        }
        sendNoPermission(sender);
        return false;
    }

    private void sendNoPermission(CommandSender sender) {
        debugManager.log(sender instanceof Player ? (Player) sender : null,
                DebugManager.LogLevel.ERROR, "§cKeine Berechtigung!");
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage("§8§m               §r §6§lDebug Help §8§m               ");
        sender.sendMessage("§8§m       §r §7created and designed by Sintrax §8§m       ");
        sender.sendMessage("§7Verwende §e/debug <Befehl> [Optionen]§7:");
        sender.sendMessage("");

        if (sender.hasPermission("debugapi.command.level")) {
            sender.sendMessage("§6§lGrundlegende Befehle:");
            sender.sendMessage(" §8▪ §e/debug toggle §7- Debug umschalten");
            sender.sendMessage(" §8▪ §e/debug level [level] §7- Level setzen");
            sender.sendMessage(" §8▪ §7Level: §aINFO§8, §eWARN§8, §bDEBUG§8, §cERROR");
            sender.sendMessage("");
        }

        if (sender.hasPermission("debugapi.command.global")) {
            sender.sendMessage("§6§lStaff-Befehle:");
            sender.sendMessage(" §8▪ §e/debug global [true|false] §7- Globalen Debug setzen");
            sender.sendMessage("");
        }

        if (sender.hasPermission("debugapi.command.admin")) {
            sender.sendMessage("§6§lAdmin-Befehle:");
            sender.sendMessage(" §8▪ §e/debug stacktrace §7- Stacktraces umschalten");
            sender.sendMessage(" §8▪ §e/debug console [level] §7- Konsolen-Level setzen");
            sender.sendMessage(" §8▪ §e/debug reload §7- Konfiguration neu laden");
            sender.sendMessage("");
        }

        sender.sendMessage("§6§lStatus:");
        sender.sendMessage(" §8▪ §7Global: " + (debugManager.isGlobalDebug() ? "§aAN" : "§cAUS"));
        sender.sendMessage(" §8▪ §7Stacktraces: " + (debugManager.isStackTraceEnabled() ? "§aAN" : "§cAUS"));

        if (sender instanceof Player) {
            Player p = (Player) sender;
            sender.sendMessage(" §8▪ §7Dein Level: §f" + debugManager.getPlayerDebugLevel(p));
        }

        sender.sendMessage("§8§m                                        ");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> commands = new ArrayList<>();

            if (sender.hasPermission("debugapi.command.level")) {
                commands.add("toggle");
                commands.add("level");
            }

            if (sender.hasPermission("debugapi.command.global")) {
                commands.add("global");
            }

            if (sender.hasPermission("debugapi.command.admin")) {
                commands.addAll(Arrays.asList("stacktrace", "console", "reload"));
            }

            return StringUtil.copyPartialMatches(args[0], commands, completions);
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "level":
                case "console":
                    if (sender.hasPermission("debugapi.command." + args[0].toLowerCase())) {
                        return Arrays.stream(DebugManager.LogLevel.values())
                                .map(Enum::name)
                                .map(String::toLowerCase)
                                .filter(l -> l.startsWith(args[1].toLowerCase()))
                                .collect(Collectors.toList());
                    }
                    break;
                case "global":
                    if (sender.hasPermission("debugapi.command.global")) {
                        return Arrays.asList("true", "false").stream()
                                .filter(b -> b.startsWith(args[1].toLowerCase()))
                                .collect(Collectors.toList());
                    }
                    break;
            }
        }

        return completions;
    }
}