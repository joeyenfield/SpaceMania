package com.emptypockets.spacemania.network.transport;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.client.payloads.LoginFailedResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.LoginSuccessResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.LogoutSuccessPayload;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.server.payloads.LoginRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.LogoutRequestPayload;
import com.esotericsoftware.kryo.Kryo;

public class NetworkProtocall {
	public static void register(Kryo kryo){
		kryo.register(ArrayList.class);
		kryo.register(Vector2.class);

		kryo.register(LoginFailedResponsePayload.class);
		kryo.register(LoginSuccessResponsePayload.class);
		kryo.register(LogoutSuccessPayload.class);
		kryo.register(NotifyClientPayload.class);


		kryo.register(LoginRequestPayload.class);
		kryo.register(LogoutRequestPayload.class);
	}
}
