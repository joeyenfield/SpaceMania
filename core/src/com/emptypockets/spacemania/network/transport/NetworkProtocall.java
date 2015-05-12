package com.emptypockets.spacemania.network.transport;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginFailedResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginSuccessResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LogoutSuccessPayload;
import com.emptypockets.spacemania.network.client.payloads.engine.EngineStatePayload;
import com.emptypockets.spacemania.network.client.payloads.engine.GameJoinPayload;
import com.emptypockets.spacemania.network.server.payloads.authentication.LoginRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.authentication.LogoutRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.engine.PlayerStatePayload;
import com.esotericsoftware.kryo.Kryo;

import java.util.ArrayList;

public class NetworkProtocall {
    public static void register(Kryo kryo) {
        kryo.register(ArrayList.class);
        kryo.register(Vector2.class);
        kryo.register(EntityState.class);
        kryo.register(Rectangle.class);

        kryo.register(LoginFailedResponsePayload.class);
        kryo.register(LoginSuccessResponsePayload.class);
        kryo.register(LogoutSuccessPayload.class);
        kryo.register(NotifyClientPayload.class);
        kryo.register(GameJoinPayload.class);
        kryo.register(EngineStatePayload.class);
        kryo.register(PlayerStatePayload.class);

        kryo.register(LoginRequestPayload.class);
        kryo.register(LogoutRequestPayload.class);

    }
}
