package com.emptypockets.spacemania.desktop;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.utils.ErrorUtils;
import com.emptypockets.spacemania.utils.ipConfig.IpManager;
import com.esotericsoftware.minlog.Log;

public class DesktopLauncher {

	public static void main(String[] arg) throws InterruptedException, FileNotFoundException, IOException {
		
//	}
//	public static void maina(String[] arg) throws InterruptedException, FileNotFoundException, IOException {
		try {
			IpManager.setIpFinder(new DesktopIpFinder());
			System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");

			Log.ERROR();
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.width = 1280;
			config.height = 720;
			config.x = 0;
			config.y = 0;
			config.foregroundFPS = 60;
			config.fullscreen = false;
			config.allowSoftwareMode = true;

			ApplicationListener app;
			app = new MainGame();
			new LwjglApplication(app, config);
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
