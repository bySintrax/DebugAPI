package de.sintrax.debugAPI.utils;

public class DebugUtils {
    public static String simplifyClassName(String fullName) {
        String[] parts = fullName.split("\\.");
        return parts[parts.length - 1];
    }

    public static String getFullStackTrace(int depth) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();

        for (int i = 2; i < Math.min(stack.length, depth + 2); i++) {
            sb.append("  at ")
                    .append(simplifyClassName(stack[i].getClassName()))
                    .append(".")
                    .append(stack[i].getMethodName())
                    .append("(")
                    .append(stack[i].getFileName())
                    .append(":")
                    .append(stack[i].getLineNumber())
                    .append(")\n");
        }
        return sb.toString();
    }
}