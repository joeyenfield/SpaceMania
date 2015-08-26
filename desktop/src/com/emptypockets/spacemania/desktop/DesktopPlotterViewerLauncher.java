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
import com.emptypockets.spacemania.metrics.plotter.PlotterViewer;
import com.emptypockets.spacemania.network.common.utils.IpManager;
import com.emptypockets.spacemania.utils.ErrorUtils;
import com.esotericsoftware.minlog.Log;

public class DesktopPlotterViewerLauncher {

	public static void main(String[] arg) throws Exception {
		try {
			IpManager.setIpFinder(new DesktopIpFinder());
			System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");

			Log.ERROR();
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.width = 800;
			config.height = 600;
			config.x = 0;
			config.y = 0;
			config.foregroundFPS = 60;
			config.fullscreen = false;
			config.allowSoftwareMode = true;

			ApplicationListener app;
			app = new PlotterViewer();
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
	
	public static void startSecondJVM() throws Exception {
		String separator = System.getProperty("file.separator");
		String classpath = System.getProperty("java.class.path");
		String path = System.getProperty("java.home")
	                + separator + "bin" + separator + "java";
		ProcessBuilder processBuilder = 
	                new ProcessBuilder(path, "-cp", 
	                classpath, 
	                DesktopLauncher.class.getName());
		Process process = processBuilder.start();
		process.waitFor();
	}
}
