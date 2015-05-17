package com.emptypockets.spacemania.network.client.commands.connection;

import com.emptypockets.spacemania.logging.ServerLogger;
import com.emptypockets.spacemania.network.NetworkProperties;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

import java.io.IOException;


public class ClientConnectCommand extends ClientCommand {

    public ClientConnectCommand(ClientManager client) {
        super("connect", client);
        setDescription("Connects to a remote server : connect [tcpPort,udpPort]");
    }

    @Override
    public void exec(String data) {
        try {
            if (data == null) {
                data = "";
            }

            String arg[] = data.split(",");
            String address = "localhost";
            int tcpPort = NetworkProperties.tcpPort;
            int udpPort = NetworkProperties.udpPort;

            if (arg.length > 1 && !arg[0].isEmpty()) {
                address = arg[0];
            }
            if (arg.length > 2 && !arg[1].isEmpty()) {
                tcpPort = Integer.parseInt(arg[1]);
            }
            if (arg.length > 3 && !arg[2].isEmpty()) {
                tcpPort = Integer.parseInt(arg[2]);
            }


            try {
                client.connect(address, tcpPort, udpPort);
            } catch (IOException e) {
                ServerLogger.error("Failed to start server", e);
                e.printStackTrace();
            }

        } catch (Exception e) {
            ServerLogger.error("Invalid Arguments", e);
        }
    }


}
