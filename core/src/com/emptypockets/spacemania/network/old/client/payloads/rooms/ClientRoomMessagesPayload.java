package com.emptypockets.spacemania.network.old.client.payloads.rooms;

import java.util.ArrayList;

import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.old.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.old.client.rooms.messages.ClientRoomMessage;
import com.emptypockets.spacemania.network.old.server.rooms.messages.ServerRoomMessage;
import com.emptypockets.spacemania.utils.PoolsManager;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ClientRoomMessagesPayload extends ClientPayload implements SingleProcessor<ServerRoomMessage> {

	int roomId;
	ArrayList<ClientRoomMessage> messages;

	public ClientRoomMessagesPayload() {
		messages = new ArrayList<ClientRoomMessage>();
	}

	@Override
	public void process(ServerRoomMessage entity) {
		addMessage(entity.createClientMessage());
	}

	public void addMessage(ClientRoomMessage message) {
		messages.add(message);
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	@Override
	public void executePayload(ClientManager clientManager) {
		ClientRoom room = clientManager.getCurrentRoom();
		if (room != null) {
			if (room.getId() == roomId) {
				room.processMessages(clientManager, messages);
			}
		}
	}

	@Override
	public void reset() {
		super.reset();
		roomId = -1;
		for (ClientRoomMessage message : messages) {
			Class<? extends ClientRoomMessage> messageClass = message.getClass();
			PoolsManager.free(message);
		}
		messages.clear();
	}
}
