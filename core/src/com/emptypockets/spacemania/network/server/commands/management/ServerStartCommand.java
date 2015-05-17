package com.emptypockets.spacemania.network.server.commands.management;

import com.emptypockets.spacemania.logging.ServerLogger;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.commands.ServerCommand;

import java.io.IOException;


public class ServerStartCommand extends ServerCommand {

    public ServerStartCommand(ServerManager server) {
        super("start", server);
    }

    @Override
    public void exec(String data) {
        try {
            if (data != null) {
                String args[] = data.split(",");

                if (args.length == 3) {
                    int tcpPort = Integer.parseInt(args[0]);
                    int udpPort = Integer.parseInt(args[1]);
                    String name = args[3];
                    server.setPorts(tcpPort,udpPort);
                    server.setName(name);
                }
                if (args.length == 2) {
                    int tcpPort = Integer.parseInt(args[0]);
                    int udpPort = Integer.parseInt(args[1]);
                    server.setPorts(tcpPort,udpPort);
                }

            }
        } catch (Exception e) {
            ServerLogger.error("Invalid Arguments", e);
        }

        try {
            server.start();
        } catch (IOException e) {
            ServerLogger.error("Failed to start server", e);
            e.printStackTrace();
        }
    }
}
