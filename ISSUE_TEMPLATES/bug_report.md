---
name: 🐛 Bug Report
about: Report reproducible errors
title: "[BUG] "
labels: bug
---

### 🖥 Environment
| Key | Value |
|-----|-------|
| DebugAPI Version | 1.0.0 |
| Server Software | Paper/Bungee |
| Java Version | JDK 17+ |

### 🐞 Problem Description
[What happens]

### 🔧 Steps to Reproduce
1. Call:
```java
debugManager.log(player, null, "test"); // NullPointerException
```

### ✅ Expected Behavior
[What should happen]

### 📜 Error Log
```
[00:00:00 ERROR]: NullPointerException at...
```