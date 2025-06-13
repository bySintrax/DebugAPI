# 🔍 DebugAPI - Minecraft Debugging Toolkit

![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)

**Cross-platform debugging for Paper & BungeeCord plugins**

## 🚀 5-Sekunden-Start
```java
// 1. Initialize
DebugAPI.init(plugin);

// 2. Log example
DebugAPI.getInstance().getDebugManager()
    .consoleLog(DebugManager.LogLevel.INFO, "Hello World!");
```

## 📦 Installation
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
        <groupId>de.sintrax</groupId>
        <artifactId>DebugAPI</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## ✨ Kernfeatures
- ✅ **Multi-Plattform**: Einheitliche API für Bukkit & Bungee
- 📊 **Einheitliches Logging**: Verschiedene Log-Level und Klassenausgaben
- 🎨 **Farbige Logs**: Unterstützt Minecraft-Farbcodes
- ⚙️ **Konfigurierbar**: `debug.yml` pro Plugin

[🐛 Issues](/ISSUE_TEMPLATES/bug_report.md)