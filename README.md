# 🔍 DebugAPI - Minecraft Debugging Toolkit
![CI Status](https://github.com/bySintrax/DebugAPI/actions/workflows/ci.yml/badge.svg)
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
<dependency>
    <groupId>de.sintrax</groupId>
    <artifactId>debugapi</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

## ✨ Kernfeatures
- ✅ **Multi-Plattform**: Einheitliche API für Bukkit & Bungee
- 📊 **Performance-Tracking**: `debugManager.startTracker("DB-Query")`
- 🎨 **Farbige Logs**: Unterstützt Minecraft-Farbcodes
- ⚙️ **Konfigurierbar**: `debug.yml` pro Plugin

[📚 Javadoc](https://bySintrax.github.io/DebugAPI/) | [🐛 Issues](.github/ISSUE_TEMPLATE/bug_report.md)