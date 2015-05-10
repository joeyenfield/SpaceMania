package com.emptypockets.spacemania.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emptypockets.spacemania.SpaceMania;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.esotericsoftware.kryonet.Server;

public class DesktopLauncher {
	public static void main (String[] arg) {
		ServerManager manager = new ServerManager(ServerManager.DEFAULT_SERVER_UPDATE);


		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new SpaceMania(), config);
	}
}
