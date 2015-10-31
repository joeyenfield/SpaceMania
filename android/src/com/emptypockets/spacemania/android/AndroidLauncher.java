package com.emptypockets.spacemania.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.network.common.utils.IpManager;
import com.emptypockets.spacemania.network.common.utils.IpManagerInterface;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		IpManager.setIpFinder(new IpManagerInterface() {
			@Override
			public String[] getIpaddress() {
				return new String[]{Utils.getIPAddress(true)};
			}
		});
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		final MainGame test = new MainGame();
		initialize(test, config);
	}
}
