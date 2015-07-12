package com.emptypockets.spacemania.network.client.commands.settings;

import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

public class ClientSetRenderCommand extends ClientCommand {

	public ClientSetRenderCommand(ClientManager client) {
		super("render", client);
		setDescription("Changes the renderer types : render {texture} {debug}  - 0 off 1 on");
	}

	@Override
	public void exec(String args) {
		String[] data = args.toLowerCase().split(" ");
		if (data.length != 2) {
			getConsole().println("Two values are needed");
		} else {
			try {
				int renderTexture = Integer.parseInt(data[0]);
				int renderDebug = Integer.parseInt(data[1]);
				Constants.RENDER_DEBUG = renderDebug == 1;
				Constants.RENDER_TEXTURE = renderTexture == 1;
			} catch (Throwable t) {
				getConsole().println("Error with command : " + t.getLocalizedMessage());
				getConsole().error(t);
			}
		}
	}
}
