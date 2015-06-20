package com.emptypockets.spacemania.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.commandLine.CommandLine;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		final MainGame test = new MainGame();
		initialize(test, config);

		new Thread(new Runnable() {
			@Override
			public void run() {
				// if (arg.length > 0) {
				CommandLine client = null;
				do {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (test != null && test.screen != null && test.screen.getClient() != null && test.screen.getClient().getCommand() != null) {
						client = test.screen.getClient().getCommand();
					}
				} while (client == null);
//				client.processCommand("start;set grid 0; set roomsize 5000;set particles 100;");
			}
		}).start();
	}
}
