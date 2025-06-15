# 🔍 DebugAPI - Minecraft Debugging Toolkit

![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)

[🐛 Issues](/ISSUE_TEMPLATES/bug_report.md) 
[️❓ Features](/ISSUE_TEMPLATES/feature_request.md)

**Cross-platform debugging for Paper & BungeeCord plugins**

# 🚀 5-Sekunden-Start
```java
// 1. Initialize
DebugAPI.init(plugin);

// 2. Log example
DebugAPI.getInstance().getDebugManager()
    .consoleLog(DebugManager.LogLevel.INFO, "Hello World!");
```

# 📦 Installation
Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.bySintrax</groupId>
        <artifactId>DebugAPI</artifactId>
        <version>v1.0.0</version>
    </dependency>
</dependencies>
```

# ✨ Kernfeatures
- ✅ **Multi-Plattform**: Einheitliche API für Bukkit & Bungee
- 📊 **Einheitliches Logging**: Verschiedene Log-Level und Klassenausgaben
- 🎨 **Farbige Logs**: Unterstützt Minecraft-Farbcodes
- ⚙️ **Konfigurierbar**: `debug.yml` pro Plugin

# 🛠️ Permissions
```java
commands:
    debug:
        description: Hauptbefehl für die DebugAPI
        usage: /debug <Befehl> [Optionen]
        permission: debugapi.command.use
        permission-message: §cKeine Berechtigung!
```

## Basisberechtigungen
```java
debugapi.command.use:
    description: Erlaubt die grundsätzliche Nutzung des Debug-Befehls
    default: false
```

## Moderatoren-Berechtigungen
```java
debugapi.group.mod:
    description: Moderator-Berechtigungen
    default: false
    children:
        debugapi.command.toggle: true
        debugapi.command.level: true
        debugapi.command.global: true
```

## Entwickler-Berechtigungen
```java
debugapi.group.dev:
    description: Entwickler-Berechtigungen
    default: false
    children:
        debugapi.group.mod: true
        debugapi.command.stacktrace: true
        debugapi.command.console: true
```

## Admin-Berechtigungen
```java
debugapi.group.admin:
    description: Administrator-Berechtigungen
    default: op
    children:
        debugapi.group.dev: true
        debugapi.command.reload: true
```

## Einzelberechtigungen
```java
debugapi.command.toggle:
    description: Erlaubt das Umschalten des persönlichen Debug-Modus
    default: false

debugapi.command.level:
    description: Erlaubt das Ändern des persönlichen Debug-Levels
    default: false

debugapi.command.global:
    description: Erlaubt die Steuerung des globalen Debug-Modus
    default: false

debugapi.command.stacktrace:
    description: Erlaubt das Aktivieren/Deaktivieren von Stacktraces
    default: false

debugapi.command.console:
    description: Erlaubt das Ändern des Konsolen-Debug-Levels
    default: false

debugapi.command.reload:
    description: Erlaubt das Neuladen der Konfiguration
    default: false
```
## 📝 Nutzungsbeispiele
Grundlegende Logging-Funktionen
```java
DebugManager debug = DebugAPI.getInstance().getDebugManager();

// Einfache Logs
debug.consoleLog(DebugManager.LogLevel.INFO, "Information");
debug.consoleLog(DebugManager.LogLevel.WARN, "Warnung");
debug.consoleLog(DebugManager.LogLevel.DEBUG, "Debug-Nachricht");
debug.consoleLog(DebugManager.LogLevel.ERROR, "Fehlermeldung");

// Spieler-spezifische Logs
Player player = Bukkit.getPlayer("Sintrax");
debug.playerLog(player, DebugManager.LogLevel.INFO, "Hallo Spieler!");

// Mit Stacktrace (wenn aktiviert)
debug.consoleLog(DebugManager.LogLevel.DEBUG, "Mit Stacktrace");
```
## Beispielausgaben:
```java
[INFO]      ->  §b[INFO] §fInformation
[WARNING]   ->  §e[WARN] §fWarnung
[CONFIG]    ->  §3[DEBUG] §fDebug-Nachricht §8| §7[Caller] ExampleClass.test():42
[SEVERE]    ->  §c[ERROR] §fFehlermeldung
```
## Debug-Konfiguration:
```java
// Debug-Level für Spieler setzen
debug.setPlayerDebugLevel(player, DebugManager.LogLevel.DEBUG);

// Globalen Debug-Modus umschalten
debug.setGlobalDebug(true);

// Stacktrace-Ausgaben aktivieren
debug.setStackTraceEnabled(true);

// Konfiguration neu laden
debug.reload();
```

## BungeeCord-spezifische Nutzung
```java
ProxiedPlayer player = ProxyServer.getInstance().getPlayer("Sintrax");
debug.proxyPlayerLog(player, DebugManager.LogLevel.INFO, "Bungee-Nachricht");


//Erweiterte Features
// Logging mit automatischem Caller-Info (wenn stackTraceEnabled=true)
debug.consoleLog(DebugManager.LogLevel.DEBUG, "Automatische Caller-Info");

// Broadcast an alle Spieler mit entsprechendem Debug-Level
debug.broadcast(DebugManager.LogLevel.INFO, "Wichtige Server-Info");
```