package com.emptypockets.spacemania.network.server.commands;

import com.emptypockets.spacemania.network.server.ServerManager;

/**
 * Created by jenfield on 13/05/2015.
 */
public class ServerPingCommand extends ServerCommand {
    public ServerPingCommand(ServerManager server) {
        super("ping", server);
        setDescription("Force the server to update the ping of a user, or all users : ping {username}");
    }

    @Override
    public void exec(String args) {
        if(args == null || args.trim().length() == 0){
            server.updatePings();
        }else{
            server.pingUser(args);
        }
    }
}
