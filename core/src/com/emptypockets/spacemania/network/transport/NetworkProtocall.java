package com.emptypockets.spacemania.network.transport;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginFailedResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginSuccessResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LogoutSuccessPayload;
import com.emptypockets.spacemania.network.client.payloads.rooms.ClientRoomPayload;
import com.emptypockets.spacemania.network.client.payloads.rooms.JoinRoomSuccessPayload;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomChatMessage;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomPlayerJoinMessage;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomPlayerLeaveMessage;
import com.emptypockets.spacemania.network.server.payloads.rooms.ChatMessagePayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.CreateRoomRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.JoinRoomRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.RequestRoomListPayload;
import com.emptypockets.spacemania.network.server.player.PlayerManager;
import com.emptypockets.spacemania.network.server.payloads.authentication.LoginRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.authentication.LogoutRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.JoinLobyRequestPayload;
import com.esotericsoftware.kryo.Kryo;

import java.util.ArrayList;
import java.util.HashSet;

public class NetworkProtocall {
    public static void register(Kryo kryo) {
        kryo.register(ArrayList.class);
        kryo.register(HashSet.class);
        kryo.register(Vector2.class);
        kryo.register(Rectangle.class);
        kryo.register(ComsType.class);
        kryo.register(ClientPlayer.class);
        kryo.register(ClientRoom.class);

        kryo.register(ClientRoomPlayerJoinMessage.class);
        kryo.register(ClientRoomPlayerLeaveMessage.class);
        kryo.register(ClientRoomChatMessage.class);

        kryo.register(RequestRoomListPayload.class);
        kryo.register(ChatMessagePayload.class);
        kryo.register(JoinRoomRequestPayload.class);
        kryo.register(LoginFailedResponsePayload.class);
        kryo.register(LoginSuccessResponsePayload.class);
        kryo.register(LogoutSuccessPayload.class);
        kryo.register(NotifyClientPayload.class);
        kryo.register(JoinLobyRequestPayload.class);
        kryo.register(JoinRoomSuccessPayload.class);
        kryo.register(ClientRoomPayload.class);
        kryo.register(CreateRoomRequestPayload.class);
        kryo.register(LoginRequestPayload.class);
        kryo.register(LogoutRequestPayload.class);

    }
}
