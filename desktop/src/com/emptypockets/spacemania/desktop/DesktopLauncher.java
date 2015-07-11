package com.emptypockets.spacemania.desktop;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.commandLine.CommandLine;
import com.emptypockets.spacemania.network.IpManager;
import com.emptypockets.spacemania.network.IpManagerInterface;
import com.emptypockets.spacemania.plotter.DataLogger;
import com.emptypockets.spacemania.plotter.PlotterViewer;
import com.emptypockets.spacemania.utils.ErrorUtils;
import com.esotericsoftware.minlog.Log;

public class DesktopLauncher {

	public static void main(String[] arg) throws InterruptedException, FileNotFoundException, IOException {
		try {
			IpManager.setIpFinder(new IpManagerInterface() {

				@Override
				public String[] getIpaddress() {
					ArrayList<String> result = new ArrayList<String>();
					InetAddress localhost;
					try {
						localhost = InetAddress.getLocalHost();
						// Just in case this host has multiple IP addresses....
						InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
						if (allMyIps != null && allMyIps.length > 1) {
							for (int i = 0; i < allMyIps.length; i++) {
								result.add(allMyIps[i].getHostAddress());

							}
						}
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return result.toArray(new String[0]);
				}
			});
			System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");

			Log.ERROR();
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.width = 400;
			config.height = 400;
			config.x = 0;
			config.y = 100;

			MainGame game = null;
			ApplicationListener test;

			int option = JOptionPane.showConfirmDialog(null, "Play Game (YES), View Data (NO), Exit (Cancel)", "Run Game", JOptionPane.YES_NO_CANCEL_OPTION);
			if (option == JOptionPane.CANCEL_OPTION) {
				return;
			} else if (option == JOptionPane.OK_OPTION) {
				DataLogger.clean();
				game = new MainGame();
				test = game;
			} else {
				test = new PlotterViewer();
			}
			new LwjglApplication(test, config).addLifecycleListener(new LifecycleListener() {
				@Override
				public void resume() {
				}

				@Override
				public void pause() {
				}

				@Override
				public void dispose() {
					try {
						DataLogger.write();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			;

			// if (arg.length > 0) {
			CommandLine client = null;
			do {
				Thread.sleep(100);
				if (game != null && game.screen != null && game.screen.getClient() != null && game.screen.getClient().getCommand() != null) {
					client = game.screen.getClient().getCommand();
				}
			} while (client == null);
			//
			// client.processCommand("start;set gridsize 128 128; set roomsize 2000;set gridrender 1;set particles 10000;");
			// client.processCommand("connect emptypocketgames.noip.me");
			// client.processCommand("login awsoem");
			// client.processCommand("lobby");
			//
			// client.processCommand("host start; connect;login server; lobby");
			// client.processCommand("connect;login jenfield2; lobby");
			// client.processCommand("lobby");
			// client.processCommand("host rooms");
			// game.screen.getClient().getCommand().processCommand("start; set roomsize 4000;set grid 1; set gridrender 1; set gridsize 2 2");

			while (true) {
				Scanner in = new Scanner(System.in);
				while (true) {
					game.screen.getClient().getCommand().processCommand(in.nextLine());
				}
			}
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
