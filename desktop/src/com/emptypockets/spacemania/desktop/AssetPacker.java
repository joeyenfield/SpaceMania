package com.emptypockets.spacemania.desktop;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class AssetPacker {

	    public static void main (String[] args) throws IOException {
	        Settings settings = new Settings();
	        settings.maxWidth = 512;
	        settings.maxHeight = 512;
	        
	        File resource = new File("../resources");
	        for(File file : resource.listFiles()){
	        	if(file.isDirectory()){
	        		String folderName = file.getName();
	        		 TexturePacker.process(settings, "../resources/"+folderName, "../android/assets/"+folderName, folderName);
	        	}
	        }
	        
//	       

	}
}
