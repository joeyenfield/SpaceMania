package com.emptypockets.spacemania.network.old.client.commands.authentication;


import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;

public class ClientLoginCommand extends ClientCommand {

    public ClientLoginCommand(ClientManager client) {
        super("login", client);
        setDescription("Attempts to login to a remote server : login {username},{password}");
    }

    @Override
    public void exec(String data) {
        try {
            String[] args = data.split(",");
            String username = "";
            String password = "";
            username = args[0];
            if (args.length > 1) {
                password = args[1];
            }
            client.serverLogin(username, password);
        } catch (Throwable t) {
        	client.getConsole().printf("Invlaid Arguments");
        	client.getConsole().error(t);
        }
    }
}
