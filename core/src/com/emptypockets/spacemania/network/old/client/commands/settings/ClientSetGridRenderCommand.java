package com.emptypockets.spacemania.network.old.client.commands.settings;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;
import com.emptypockets.spacemania.network.old.engine.grid.GridSystem;

public class ClientSetGridRenderCommand extends ClientCommand {

	public ClientSetGridRenderCommand(ClientManager client) {
		super("gridrender", client);
		setDescription("Changes the renderer of the grid - 0 (NONE), 1 (Texture), 2 (Path)");
	}

	@Override
	public void exec(String args) {
		String[] data = args.toLowerCase().split(" ");
		if (data.length != 1) {
			getConsole().println("A number is needed");
		} else {
			try {
				int type = Integer.parseInt(data[0]);
				if (type == 0) {
					client.getEngine().getGridData().setRenderType(GridSystem.RENDER_TEXTURE);
				} else {
					client.getEngine().getGridData().setRenderType(GridSystem.RENDER_PATH);
				}
			} catch (Throwable t) {
				getConsole().println("Error with command : " + t.getLocalizedMessage());
				getConsole().error(t);
			}
		}
	}
}
