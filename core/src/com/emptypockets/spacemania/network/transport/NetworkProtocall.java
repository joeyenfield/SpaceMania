package com.emptypockets.spacemania.network.transport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.client.input.ClientInput;
import com.emptypockets.spacemania.network.client.payloads.ClientMyPlayerStateUpdatePayload;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginFailedResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginSuccessResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LogoutSuccessPayload;
import com.emptypockets.spacemania.network.client.payloads.engine.ClientEngineEntityManagerSyncPayload;
import com.emptypockets.spacemania.network.client.payloads.rooms.ClientRoomMessagesPayload;
import com.emptypockets.spacemania.network.client.payloads.rooms.JoinRoomSuccessPayload;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.client.player.MyPlayer;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomChatMessage;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomPlayerJoinMessage;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomPlayerLeaveMessage;
import com.emptypockets.spacemania.network.engine.EntityState;
import com.emptypockets.spacemania.network.engine.EntityType;
import com.emptypockets.spacemania.network.engine.sync.EntityCreation;
import com.emptypockets.spacemania.network.engine.sync.EntityManagerSync;
import com.emptypockets.spacemania.network.engine.sync.EntityRemoval;
import com.emptypockets.spacemania.network.server.payloads.ServerClientInputUpdatePayload;
import com.emptypockets.spacemania.network.server.payloads.authentication.LoginRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.authentication.LogoutRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.ChatMessagePayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.CreateRoomRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.JoinLobyRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.JoinRoomRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.RequestRoomListPayload;
import com.esotericsoftware.kryo.Kryo;

public class NetworkProtocall {
    public static void register(Kryo kryo) {
        kryo.register(ArrayList.class);
		kryo.register(HashMap.class);
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
        kryo.register(ClientRoomMessagesPayload.class);
        kryo.register(CreateRoomRequestPayload.class);
        kryo.register(LoginRequestPayload.class);
        kryo.register(LogoutRequestPayload.class);
        kryo.register(ClientEngineEntityManagerSyncPayload.class);
        kryo.register(ClientMyPlayerStateUpdatePayload.class)
        ;
		kryo.register(EntityManagerSync.class);
		kryo.register(EntityState.class);
		kryo.register(EntityRemoval.class);
		kryo.register(EntityCreation.class);
		kryo.register(EntityType.class);
		kryo.register(ServerClientInputUpdatePayload.class);
		kryo.register(ClientInput.class);
		kryo.register(MyPlayer.class);
    }
}
