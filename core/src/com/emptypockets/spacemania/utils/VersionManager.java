package com.emptypockets.spacemania.utils;

/**
 * Created by jenfield on 15/05/2015.
 */
public class VersionManager {
    public static final int CURRENT_VERSION = 1;

    public static int getCurrentVersion(){
        return CURRENT_VERSION;
    }

    public static boolean supportsVersion(int version){
        return version == getCurrentVersion();
    }
}
