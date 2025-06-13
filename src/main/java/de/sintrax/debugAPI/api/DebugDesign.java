package de.sintrax.debugAPI.api;

import org.bukkit.ChatColor;

public class DebugDesign {
    public static final String INFO_PREFIX = "§8[§9INFO§8]§7";
    public static final String WARNING_PREFIX = "§8[§eWARN§8]§7";
    public static final String DEBUG_PREFIX = "§8[§bDEBUG§8]§7";
    public static final String ERROR_PREFIX = "§8[§cERROR§8]§7";

    public static final String INFO_COLOR = ChatColor.GRAY.toString();
    public static final String WARNING_COLOR = ChatColor.YELLOW.toString();
    public static final String DEBUG_COLOR = ChatColor.AQUA.toString();
    public static final String ERROR_COLOR = ChatColor.RED.toString();

    public static final String SEPARATOR = ChatColor.DARK_GRAY + " | ";
    public static final String CALLER_PREFIX = ChatColor.DARK_AQUA.toString();
    public static final String STACKTRACE_PREFIX = ChatColor.DARK_GRAY + "» ";
}