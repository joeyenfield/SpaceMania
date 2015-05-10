package com.emptypockets.spacemania.console;

public interface ConsoleListener {
	public void print(String message);
	public void println(String message);
	public void printf(String message, Object... values);
}
