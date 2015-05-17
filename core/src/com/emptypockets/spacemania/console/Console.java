package com.emptypockets.spacemania.console;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;


public class Console {
	String consoleKey;
    static ConsoleListener sysout = new ConsoleListener() {
        @Override
        public void println(Console console, String message) {
            System.out.println(console.consoleKey+message);
        }

        @Override
        public void printf(Console console, String message, Object... values) {
            System.out.printf(console.consoleKey+message, values);
        }

        @Override
        public void print(Console console, String message) {
            System.out.print(console.consoleKey+message);
        }
    };
    ArrayList<ConsoleListener> screens = new ArrayList<ConsoleListener>();

    public Console() {
        this("");
    }

    
    public void setConsoleKey(String consoleKey) {
		this.consoleKey = consoleKey;
	}


	public Console(String string) {
    	consoleKey = string;
    	screens.add(sysout);
    }

	public void enableSysOut() {
        register(sysout);
    }

    public void disableSysOut() {
        unregister(sysout);
    }


    public void register(ConsoleListener screen) {
        synchronized (screens) {
            screens.add(screen);
        }
    }

    public void unregister(ConsoleListener screen) {
        synchronized (screens) {
            screens.remove(screen);
        }
    }

    public void print(String msg) {
        synchronized (screens) {
            for (ConsoleListener view : screens) {
                view.print(this, msg);
            }
        }
    }

    public void println(String msg) {
        synchronized (screens) {
            for (ConsoleListener view : screens) {
                view.println(this, msg);
            }
        }
    }

    public void printf(String msg, Object... obj) {
        synchronized (screens) {
            for (ConsoleListener view : screens) {
                view.printf(this, msg, obj);
            }
        }
    }

    public void error(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        println(sw.toString()); // stack trace as a string
    }
}
