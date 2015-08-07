package com.emptypockets.spacemania.network.old.client.commands;


import com.emptypockets.spacemania.commandLine.Command;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.old.client.ClientManager;

public abstract class ClientCommand extends Command {

    protected ClientManager client;

    public ClientCommand(String name, ClientManager client) {
        super(name);
        this.client = client;
    }

    public ClientManager getClient() {
        return client;
    }
    
    public Console getConsole(){
    	return client.getConsole();
    }
}
