package com.emptypockets.spacemania.desktop;

import java.awt.BorderLayout;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.commandLine.CommandLine;
import com.emptypockets.spacemania.utils.ErrorUtils;
import com.esotericsoftware.minlog.Log;

public class DesktopLauncher {

	public static void main(String[] arg) throws InterruptedException {
		try {
			System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");

			Log.ERROR();
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.width = 1280;
			config.height = 800;
			config.x = 0;
			config.y = 100;

			MainGame test = new MainGame();
			// ViewportDemo demo = new ViewportDemo();
			new LwjglApplication(test, config);

			// if (arg.length > 0) {
			CommandLine client = null;
			do {
				Thread.sleep(100);
				if (test != null && test.screen != null && test.screen.getClient() != null && test.screen.getClient().getCommand() != null) {
					client = test.screen.getClient().getCommand();
				}
			} while (client == null);
			client.processCommand("start");
			// client.processCommand("connect emptypocketgames.noip.me");
			// client.processCommand("login awsoem");
			// client.processCommand("lobby");
			// client.processCommand("host start; connect;login server; lobby");
			// client.processCommand("connect;login jenfield2; lobby");
			// client.processCommand("lobby");
			// client.processCommand("host rooms");
			// test.screen.getClient().getCommand().processCommand("connect 109.77.88.13,8080,9090;login user2;");

			while (true) {
				Scanner in = new Scanner(System.in);
				while (true) {
					test.screen.getClient().getCommand().processCommand(in.nextLine());
				}
			}
			// }
		} catch (Throwable t) {
			JTextArea textArea = new JTextArea();
			textArea.append(ErrorUtils.getErrorMessage(t));
			JScrollPane scroll = new JScrollPane(textArea);

			JFrame frame = new JFrame("Error occoured while launching");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(scroll, BorderLayout.CENTER);
			frame.setSize(600, 480);
			frame.setVisible(true);
			t.printStackTrace();
		}
	}
}
