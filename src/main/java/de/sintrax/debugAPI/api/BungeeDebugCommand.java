package de.sintrax.debugAPI.api;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BungeeDebugCommand extends Command implements TabExecutor {
    private final DebugManager debugManager;

    public BungeeDebugCommand(DebugManager debugManager) {
        super("debug", "debugapi.command.use", "dbg");
        this.debugManager = debugManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }

        switch (args[0].toLowerCase()) {
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
    }

    private void handleToggleCommand(CommandSender sender) {
        if (!checkPermission(sender, "debugapi.command.toggle")) return;

        boolean newState = !debugManager.isGlobalDebug();
        debugManager.setGlobalDebug(newState);

        String status = newState ? "§aaktiviert" : "§cdeaktiviert";
        String message = "Globaler Debug-Modus wurde " + status;

        debugManager.consoleLog(DebugManager.LogLevel.INFO, message);
        if (sender instanceof ProxiedPlayer) {
            debugManager.proxyPlayerLog((ProxiedPlayer) sender, DebugManager.LogLevel.INFO, message);
        }
    }

    private void handleStackTraceCommand(CommandSender sender) {
        if (!checkPermission(sender, "debugapi.command.stacktrace")) return;

        boolean newState = !debugManager.isStackTraceEnabled();
        debugManager.setStackTraceEnabled(newState);

        String status = newState ? "§aaktiviert" : "§cdeaktiviert";
        String message = "Stacktrace-Ausgaben wurden " + status;

        debugManager.consoleLog(DebugManager.LogLevel.INFO, message);
        if (sender instanceof ProxiedPlayer) {
            debugManager.proxyPlayerLog((ProxiedPlayer) sender, DebugManager.LogLevel.INFO, message);
        }
    }

    private void handleLevelCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            debugManager.consoleLog(DebugManager.LogLevel.ERROR, "Nur Spieler können ihr persönliches Debug-Level setzen.");
            return;
        }

        if (!checkPermission(sender, "debugapi.command.level")) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length < 2) {
            debugManager.sendAvailableLevels(player);
            debugManager.proxyPlayerLog(player, DebugManager.LogLevel.INFO,
                    "Aktuelles Level: " + debugManager.getPlayerDebugLevel(player));
            return;
        }

        try {
            DebugManager.LogLevel level = DebugManager.LogLevel.valueOf(args[1].toUpperCase());
            debugManager.setPlayerDebugLevel(player, level);
            debugManager.proxyPlayerLog(player, DebugManager.LogLevel.INFO,
                    "Debug-Level gesetzt auf: " + level.name());
        } catch (IllegalArgumentException e) {
            debugManager.proxyPlayerLog(player, DebugManager.LogLevel.ERROR,
                    "Ungültiges Level: " + args[1]);
            debugManager.sendAvailableLevels(player);
        }
    }

    private void handleGlobalCommand(CommandSender sender, String[] args) {
        if (!checkPermission(sender, "debugapi.command.global")) return;

        if (args.length < 2) {
            debugManager.consoleLog(DebugManager.LogLevel.INFO,
                    "Globaler Debug-Modus ist " + (debugManager.isGlobalDebug() ? "§aaktiviert" : "§cdeaktiviert"));
            return;
        }

        try {
            boolean enable = Boolean.parseBoolean(args[1]);
            debugManager.setGlobalDebug(enable);
            debugManager.consoleLog(DebugManager.LogLevel.INFO,
                    "Globaler Debug-Modus wurde " + (enable ? "§aaktiviert" : "§cdeaktiviert"));
        } catch (IllegalArgumentException e) {
            debugManager.consoleLog(DebugManager.LogLevel.ERROR,
                    "Ungültiger Wert: " + args[1] + " (erwartet true/false)");
        }
    }

    private void handleConsoleCommand(CommandSender sender, String[] args) {
        if (!checkPermission(sender, "debugapi.command.console")) return;

        if (args.length < 2) {
            debugManager.consoleLog(DebugManager.LogLevel.INFO,
                    "Console Debug-Level ist: " + debugManager.getConsoleDebugLevel());
            return;
        }

        try {
            DebugManager.LogLevel level = DebugManager.LogLevel.valueOf(args[1].toUpperCase());
            debugManager.setConsoleDebugLevel(level);
            debugManager.consoleLog(DebugManager.LogLevel.INFO,
                    "Console Debug-Level gesetzt auf: " + level.name());
        } catch (IllegalArgumentException e) {
            debugManager.consoleLog(DebugManager.LogLevel.ERROR,
                    "Ungültiges Level: " + args[1]);
        }
    }

    private void handleReloadCommand(CommandSender sender) {
        if (!checkPermission(sender, "debugapi.command.reload")) return;

        debugManager.reload();
        if (sender instanceof ProxiedPlayer) {
            debugManager.proxyPlayerLog((ProxiedPlayer) sender,
                    DebugManager.LogLevel.INFO, "Konfiguration debug.yml wurde neu geladen");
        } else {
            debugManager.consoleLog(DebugManager.LogLevel.INFO, "Konfiguration debug.yml wurde neu geladen");
        }
    }

    private boolean checkPermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission)) {
            return true;
        }
        sendNoPermission(sender);
        return false;
    }

    private void sendNoPermission(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            debugManager.proxyPlayerLog((ProxiedPlayer) sender,
                    DebugManager.LogLevel.ERROR, "§cKeine Berechtigung!");
        } else {
            debugManager.consoleLog(DebugManager.LogLevel.ERROR, "§cKeine Berechtigung!");
        }
    }

    private void sendUsage(CommandSender sender) {
        // Header
        sender.sendMessage(new TextComponent("§8§m               §r §6§lDebug Help §8§m               "));
        sender.sendMessage(new TextComponent("§8§m       §r §7created and designed by Sintrax §8§m       "));
        sender.sendMessage(new TextComponent("§7Verwende §e/debug <Befehl> [Optionen]§7:"));
        sender.sendMessage(new TextComponent(""));

        // Grundlegende Befehle
        if (sender.hasPermission("debugapi.command.level")) {
            sender.sendMessage(new TextComponent("§6§lGrundlegende Befehle:"));
            sender.sendMessage(new TextComponent(" §8▪ §e/debug toggle §7- Debug umschalten"));
            sender.sendMessage(new TextComponent(" §8▪ §e/debug level [level] §7- Level setzen"));
            sender.sendMessage(new TextComponent(" §8▪ §7Level: §aINFO§8, §eWARN§8, §bDEBUG§8, §cERROR"));
            sender.sendMessage(new TextComponent(""));
        }

        // Staff-Befehle
        if (sender.hasPermission("debugapi.command.global")) {
            sender.sendMessage(new TextComponent("§6§lStaff-Befehle:"));
            sender.sendMessage(new TextComponent(" §8▪ §e/debug global [true|false] §7- Globalen Debug setzen"));
            sender.sendMessage(new TextComponent(""));
        }

        // Admin-Befehle
        if (sender.hasPermission("debugapi.command.admin")) {
            sender.sendMessage(new TextComponent("§6§lAdmin-Befehle:"));
            sender.sendMessage(new TextComponent(" §8▪ §e/debug stacktrace §7- Stacktraces umschalten"));
            sender.sendMessage(new TextComponent(" §8▪ §e/debug console [level] §7- Konsolen-Level setzen"));
            sender.sendMessage(new TextComponent(" §8▪ §e/debug reload §7- Konfiguration neu laden"));
            sender.sendMessage(new TextComponent(""));
        }

        // Status
        sender.sendMessage(new TextComponent("§6§lStatus:"));
        sender.sendMessage(new TextComponent(" §8▪ §7Global: " + (debugManager.isGlobalDebug() ? "§aAN" : "§cAUS")));
        sender.sendMessage(new TextComponent(" §8▪ §7Stacktraces: " + (debugManager.isStackTraceEnabled() ? "§aAN" : "§cAUS")));

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            sender.sendMessage(new TextComponent(" §8▪ §7Dein Level: §f" + debugManager.getPlayerDebugLevel(p)));
        }

        // Footer
        sender.sendMessage(new TextComponent("§8§m                                        "));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("toggle", "stacktrace", "level", "global", "console", "reload").stream()
                    .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                    .filter(cmd -> {
                        switch (cmd) {
                            case "toggle":
                            case "level":
                                return sender.hasPermission("debugapi.command.level");
                            case "global":
                                return sender.hasPermission("debugapi.command.global");
                            case "stacktrace":
                            case "console":
                            case "reload":
                                return sender.hasPermission("debugapi.command.admin");
                            default:
                                return false;
                        }
                    })
                    .collect(Collectors.toList());
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

        return List.of();
    }
}