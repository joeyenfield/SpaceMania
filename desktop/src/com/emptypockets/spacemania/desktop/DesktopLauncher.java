package com.emptypockets.spacemania.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emptypockets.spacemania.SpaceMania;
import com.emptypockets.spacemania.network.server.ServerManager;

import java.util.Scanner;

public class DesktopLauncher {
    public static void main(String[] arg) throws InterruptedException {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 20;
        config.height = 20;
        config.x = 0;
        config.y = 1070;

        SpaceMania test = new SpaceMania();

        new LwjglApplication(test, config);

        Thread.sleep(2000);
        test.screen.getClient().getCommand().processCommand("server setup; server start; server status; connect localhost,8080,9090;login jenfield,password;server ping jenfield; server ping jenfield; server users");

//        test.screen.getClient().getCommand().processCommand("connect 109.77.88.13,8080,9090;login user2;");

        while(true){
            Scanner in = new Scanner(System.in);
            while(true){
                test.screen.getClient().getCommand().processCommand(in.nextLine());
            }
        }
    }
}
