package com.emptypockets.spacemania.console;

public interface ConsoleListener {
    void print(String message);

    void println(String message);

    void printf(String message, Object... values);
}
