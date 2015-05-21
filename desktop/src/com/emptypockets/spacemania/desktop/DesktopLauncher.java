package com.emptypockets.spacemania.desktop;

import java.util.Scanner;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.commandLine.CommandLine;
import com.esotericsoftware.minlog.Log;

public class DesktopLauncher {
    public static void main(String[] arg) throws InterruptedException {
    	Log.ERROR();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 20;
        config.height = 20;
        config.x = 0;
        config.y = 100;

        MainGame test = new MainGame();

//        ViewportDemo demo = new ViewportDemo();
        new LwjglApplication(test, config);
//
        Thread.sleep(500);
        CommandLine client = test.screen.getClient().getCommand();

//        client.processCommand("connect emptypocketgames.noip.me");
//        client.processCommand("login awsoem");
//        client.processCommand("lobby");
        
        
        client.processCommand("host start; connect;login jenfield; lobby");
//        client.processCommand("connect;login jenfield2; lobby");
//        client.processCommand("lobby");
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
