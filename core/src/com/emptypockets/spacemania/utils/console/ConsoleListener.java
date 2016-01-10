package com.emptypockets.spacemania.utils.console;

public interface ConsoleListener {
    void print(Console console, String message);

    void println(Console console, String message);

    void printf(Console console, String message, Object... values);
}
