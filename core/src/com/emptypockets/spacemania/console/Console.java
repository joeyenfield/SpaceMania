package com.emptypockets.spacemania.console;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;


public class Console {
	static Console console;
	
	ArrayList<ConsoleListener> screens = new ArrayList<ConsoleListener>();
	
	static ConsoleListener sysout = new ConsoleListener() {
		@Override
		public void println(String message) {
			System.out.println(message);
		}
		
		@Override
		public void printf(String message, Object... values) {
			System.out.printf(message,values);
		}
		
		@Override
		public void print(String message) {
			System.out.print(message);
		}
	};
	
	private Console(){
		screens.add(sysout);
	}
	
	public static void enableSysOut(){
		register(sysout);
	}
	
	public static void disableSysOut(){
		unregister(sysout);
	}
	
	private static Console getConsole(){
		if(console == null){
			console = new Console();
		}
		return console;
	}
	
	public static void register(ConsoleListener screen){
		Console console = getConsole();
		synchronized (console.screens) {
			console.screens.add(screen);
		}
	}
	
	public static void unregister(ConsoleListener screen){
		Console console = getConsole();
		synchronized (console.screens) {
			console.screens.remove(screen);
		}
	}
	
	public static void print(String msg){
		Console console = getConsole();
		synchronized (console.screens) {
			for(ConsoleListener view : console.screens){
				view.print(msg);
			}
		}
	}
	
	public static void println(String msg){
		Console console = getConsole();
		synchronized (console.screens) {
			for(ConsoleListener view : console.screens){
				view.println(msg);
			}
		}
	}

	public static void printf(String msg, Object... obj){
		Console console = getConsole();
		synchronized (console.screens) {
			for(ConsoleListener view : console.screens){
				view.printf(msg, obj);
			}
		}
	}

	public static void error(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		println(sw.toString()); // stack trace as a string
	}
}
