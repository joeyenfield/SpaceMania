package com.emptypockets.spacemania.network.transport;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;

public class NetworkProtocall {
	public static void register(Kryo kryo){
		kryo.register(ArrayList.class);
		kryo.register(Vector2.class);
		kryo.register(ClientLoginRequest.class);
		kryo.register(ClientLogoutRequest.class);
		kryo.register(ClientStateTransferObject.class);
		kryo.register(LoginFailedResponse.class);
		kryo.register(LoginSucessfulResponse.class);
	}
}
