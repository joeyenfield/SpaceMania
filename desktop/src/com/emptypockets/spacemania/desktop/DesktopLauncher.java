package com.emptypockets.spacemania.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emptypockets.spacemania.SpaceMania;
import com.emptypockets.spacemania.commandLine.CommandLine;
import com.emptypockets.spacemania.network.server.ServerManager;

import java.util.Scanner;

public class DesktopLauncher {
    public static void main(String[] arg) throws InterruptedException {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 600;
        config.x = 0;
        config.y = 100;

        SpaceMania test = new SpaceMania();

        new LwjglApplication(test, config);

        Thread.sleep(2000);
        CommandLine client = test.screen.getClient().getCommand();

        client.processCommand("host start; host status;");
        client.processCommand("connect localhost,8080,9090;login user1,password;");
        client.processCommand("lobby");
//        client.processCommand("host rooms");
//        test.screen.getClient().getCommand().processCommand("connect 109.77.88.13,8080,9090;login user2;");

        while(true){
            Scanner in = new Scanner(System.in);
            while(true){
                test.screen.getClient().getCommand().processCommand(in.nextLine());
            }
        }
    }
}
