# 🛠 DebugAPI - Universal Minecraft Debugging Toolkit

[![CI Status](https://github.com/bySintrax/DebugAPI/actions/workflows/ci.yml/badge.svg)](https://github.com/bySintrax/DebugAPI/actions)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Cross-platform debugging utility for Minecraft plugins (Paper & BungeeCord).

## 🚀 Quick Start

```java
// 1. Initialize
DebugAPI.init(this);

// 2. Get Manager
DebugManager debug = DebugAPI.getInstance().getDebugManager();

// 3. Log messages
debug.consoleLog(LogLevel.INFO, "Plugin loaded!");
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

Gradle:
```groovy
implementation 'de.sintrax:debugapi:1.0.0'
```

## 📚 Documentation
- [API Guide](docs/api-guide.md)
- [Javadoc](https://yourname.github.io/DebugAPI/javadoc)
- [Examples](/examples)

## 🌟 Features
- ✅ Cross-platform support
- 📊 Performance monitoring
- 🔍 Stack trace analysis
- 🎨 Color-coded logging
- ⚙️ Configurable levels

## 🛠 Development
```bash
# Build
mvn clean package

# Generate docs
mvn javadoc:javadoc
```