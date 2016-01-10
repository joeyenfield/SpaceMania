package com.emptypockets.spacemania.utils.ipConfig;

public class IpManager {
	static IpManagerInterface ipFinder;
	private IpManager(){
		
	}
	public static IpManagerInterface getIpFinder() {
		return ipFinder;
	}
	public static void setIpFinder(IpManagerInterface ipFinder) {
		IpManager.ipFinder = ipFinder;
	}
	
	public static String[] getIP(){
		return ipFinder.getIpaddress();
	}
	
}