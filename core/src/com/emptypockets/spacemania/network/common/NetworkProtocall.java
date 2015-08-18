package com.emptypockets.spacemania.network.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.objenesis.instantiator.ObjectInstantiator;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementData;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.transform.LinearTransformData;
import com.emptypockets.spacemania.network.common.data.engine.GameEngineState;
import com.emptypockets.spacemania.network.common.data.engine.entity.EntitySystemState;
import com.emptypockets.spacemania.network.common.data.engine.entity.GameEntityAdded;
import com.emptypockets.spacemania.network.common.data.engine.entity.GameEntityNetworkSync;
import com.emptypockets.spacemania.network.common.data.engine.entity.GameEntityRemoved;
import com.emptypockets.spacemania.network.old.client.input.ClientInput;
import com.emptypockets.spacemania.network.old.client.payloads.ClientMyPlayerStateUpdatePayload;
import com.emptypockets.spacemania.network.old.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.old.client.payloads.authentication.LoginFailedResponsePayload;
import com.emptypockets.spacemania.network.old.client.payloads.authentication.LoginSuccessResponsePayload;
import com.emptypockets.spacemania.network.old.client.payloads.authentication.LogoutSuccessPayload;
import com.emptypockets.spacemania.network.old.client.payloads.engine.ClientEngineEntityManagerSyncPayload;
import com.emptypockets.spacemania.network.old.client.payloads.engine.ClientEnginePlayerManagerSyncPayload;
import com.emptypockets.spacemania.network.old.client.payloads.engine.ClientRoomEngineRegionStatePayload;
import com.emptypockets.spacemania.network.old.client.payloads.rooms.ClientRoomMessagesPayload;
import com.emptypockets.spacemania.network.old.client.payloads.rooms.JoinRoomSuccessPayload;
import com.emptypockets.spacemania.network.old.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.old.client.player.MyPlayer;
import com.emptypockets.spacemania.network.old.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.old.client.rooms.messages.ClientRoomChatMessage;
import com.emptypockets.spacemania.network.old.client.rooms.messages.ClientRoomPlayerJoinMessage;
import com.emptypockets.spacemania.network.old.client.rooms.messages.ClientRoomPlayerLeaveMessage;
import com.emptypockets.spacemania.network.old.engine.EngineRegionSync;
import com.emptypockets.spacemania.network.old.engine.entities.EntityType;
import com.emptypockets.spacemania.network.old.engine.entities.states.EntityState;
import com.emptypockets.spacemania.network.old.engine.entities.states.MovingEntityState;
import com.emptypockets.spacemania.network.old.engine.sync.EntityManagerSync;
import com.emptypockets.spacemania.network.old.engine.sync.PlayerManagerSync;
import com.emptypockets.spacemania.network.old.engine.sync.events.EntityAdd;
import com.emptypockets.spacemania.network.old.engine.sync.events.EntityRemoval;
import com.emptypockets.spacemania.network.old.server.payloads.ServerClientInputUpdatePayload;
import com.emptypockets.spacemania.network.old.server.payloads.authentication.LoginRequestPayload;
import com.emptypockets.spacemania.network.old.server.payloads.authentication.LogoutRequestPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.ChatMessagePayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.CreateRoomRequestPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.JoinLobyRequestPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.JoinRoomRequestPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.RequestRoomListPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.ResizeRoomPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.SpawnPlayerRequestPayload;
import com.emptypockets.spacemania.utils.PoolsManager;
import com.esotericsoftware.kryo.Kryo;

public class NetworkProtocall {
	public static void register(Kryo kryo) {
		register(kryo, ArrayList.class);
		register(kryo, HashMap.class);
		register(kryo, HashSet.class);
		register(kryo, Vector2.class);
		register(kryo, Rectangle.class);
		register(kryo, ComsType.class);
		register(kryo, ClientPlayer.class);
		register(kryo, ClientRoom.class);

		register(kryo, ClientRoomPlayerJoinMessage.class);
		register(kryo, ClientRoomPlayerLeaveMessage.class);
		register(kryo, ClientRoomChatMessage.class);

		register(kryo, RequestRoomListPayload.class);
		register(kryo, ChatMessagePayload.class);
		register(kryo, JoinRoomRequestPayload.class);
		register(kryo, LoginFailedResponsePayload.class);
		register(kryo, LoginSuccessResponsePayload.class);
		register(kryo, LogoutSuccessPayload.class);
		register(kryo, NotifyClientPayload.class);
		register(kryo, JoinLobyRequestPayload.class);
		register(kryo, JoinRoomSuccessPayload.class);
		register(kryo, ClientRoomMessagesPayload.class);
		register(kryo, CreateRoomRequestPayload.class);
		register(kryo, LoginRequestPayload.class);
		register(kryo, LogoutRequestPayload.class);
		register(kryo, ClientEngineEntityManagerSyncPayload.class);
		register(kryo, ClientMyPlayerStateUpdatePayload.class);
		register(kryo, ClientRoomEngineRegionStatePayload.class);
		register(kryo, EngineRegionSync.class);
		register(kryo, EntityManagerSync.class);
		register(kryo, EntityState.class);
		register(kryo, EntityRemoval.class);
		register(kryo, EntityAdd.class);
		register(kryo, EntityType.class);
		register(kryo, ServerClientInputUpdatePayload.class);
		register(kryo, ClientInput.class);

		register(kryo, ResizeRoomPayload.class);
		register(kryo, PlayerManagerSync.class);
		register(kryo, ClientEnginePlayerManagerSyncPayload.class);
		register(kryo, SpawnPlayerRequestPayload.class);
		register(kryo, MyPlayer.class);
		register(kryo, MovingEntityState.class);
		
		
		
		
		
		/**
		 * NEW CLASS TYPES
		 */
		
		register(kryo, GameEngineState.class);
		register(kryo, EntitySystemState.class);
		register(kryo, GameEntityAdded.class);
		register(kryo, GameEntityRemoved.class);
		register(kryo, GameEntityNetworkSync.class);
		register(kryo, ComponentType.class);
		register(kryo, LinearMovementData.class);
		register(kryo, LinearTransformData.class);
		register(kryo, GameEntityType.class);
	}

	public static <T> void register(Kryo kryo, final Class<T> classType) {
		kryo.register(classType).setInstantiator(new ObjectInstantiator<T>() {

			@Override
			public T newInstance() {
				return PoolsManager.obtain(classType);
			}
		});
	}
}
