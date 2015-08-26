package com.emptypockets.spacemania.desktop;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.emptypockets.spacemania.network.common.utils.IpManagerInterface;

final class DesktopIpFinder implements IpManagerInterface {
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
}