package com.emptypockets.spacemania.network.old;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.emptypockets.spacemania.RootTest;
import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.server.ServerManager;

public class ServerTesting extends RootTest {

	@Test
	public void testConnectToServer() throws IOException, InterruptedException {
		ServerManager server = new ServerManager();
		server.start();
		pause();

		assertEquals(0, server.getConnectedCount());
		assertEquals(0, server.getLoggedInCount());
		assertEquals(1, server.getRoomCount());
		
		ClientManager clientA = new ClientManager(null);
		Thread clientAUpdater = createUpdater(clientA);
		pause();
		assertEquals(0, server.getConnectedCount());
		assertEquals(0, server.getLoggedInCount());
		assertEquals(1, server.getRoomCount());


		clientA.connect("localhost", server.getTcpPort(), server.getUdpPort());
		pause();
		assertEquals(1, server.getConnectedCount());
		assertEquals(0, server.getLoggedInCount());
		assertEquals(1, server.getRoomCount());
		assertEquals(0, server.getLobbyRoom().getPlayerCount());
		
		clientA.serverLogin("userA", "password");
		pause();
		assertEquals(1, server.getConnectedCount());
		assertEquals(1, server.getLoggedInCount());
		assertEquals(1, server.getRoomCount());
		assertEquals(0, server.getLobbyRoom().getPlayerCount());
		assertNull(clientA.getCurrentRoom());
		
		clientA.joinLobby();
		pause();
		assertEquals(1, server.getConnectedCount());
		assertEquals(1, server.getLoggedInCount());
		assertEquals(1, server.getRoomCount());
		assertEquals(1, server.getLobbyRoom().getPlayerCount());
		assertEquals("Lobby", clientA.getCurrentRoom().getName());
		
		
		clientA.createRoom("AwsomeORoom");
		pause();
		assertEquals(1, server.getConnectedCount());
		assertEquals(1, server.getLoggedInCount());
		assertEquals(2, server.getRoomCount());
		assertEquals(0, server.getLobbyRoom().getPlayerCount());
		assertEquals(1, server.getRoomManager().findRoomByName("AwsomeORoom").getPlayerCount());
		assertEquals("AwsomeORoom", clientA.getCurrentRoom().getName());
		
		
		clientA.joinLobby();
		pause();
		assertEquals(1, server.getConnectedCount());
		assertEquals(1, server.getLoggedInCount());
		assertEquals(1, server.getRoomCount());
		assertEquals(1, server.getLobbyRoom().getPlayerCount());
		assertEquals("Lobby", clientA.getCurrentRoom().getName());
	
		/**
		 * Second player connected
		 */
		ClientManager clientB = new ClientManager(null);
		Thread t = createUpdater(clientB);
		clientB.connect("localhost", server.getTcpPort(), server.getUdpPort());
		pause();
		
		assertEquals(2, server.getConnectedCount());
		assertEquals(1, server.getLoggedInCount());
		assertEquals(1, server.getRoomCount());
		assertEquals(1, server.getLobbyRoom().getPlayerCount());
		assertFalse(clientB.isLoggedIn());
		
		//Try to login with a username thats already in use
		clientB.serverLogin("userA", "password");
		pause();
		assertEquals(2, server.getConnectedCount());
		assertEquals(1, server.getLoggedInCount());
		assertEquals(1, server.getRoomCount());
		assertEquals(1, server.getLobbyRoom().getPlayerCount());
		assertFalse(clientB.isLoggedIn());
		
		clientB.serverLogin("userB", "password");
		pause();
		assertEquals(2, server.getConnectedCount());
		assertEquals(2, server.getLoggedInCount());
		assertEquals(1, server.getRoomCount());
		assertEquals(1, server.getLobbyRoom().getPlayerCount());
		assertTrue(clientB.isLoggedIn());

		clientB.joinLobby();
		pause();
		assertEquals(2, server.getConnectedCount());
		assertEquals(2, server.getLoggedInCount());
		assertEquals(1, server.getRoomCount());
		assertEquals(2, server.getLobbyRoom().getPlayerCount());
		assertEquals("Lobby", clientB.getCurrentRoom().getName());
		
		//Create a room
		clientB.createRoom("room");
		pause();
		clientA.joinRoom("room");
		pause();
		
		assertEquals(2, server.getConnectedCount());
		assertEquals(2, server.getLoggedInCount());
		assertEquals(2, server.getRoomCount());
		assertEquals(0, server.getLobbyRoom().getPlayerCount());
		assertEquals(2, server.getRoomByName("room").getPlayerCount());
		
		
		assertEquals("room", clientA.getCurrentRoom().getName());
		assertEquals("room", clientB.getCurrentRoom().getName());
		assertEquals(2, clientA.getCurrentRoom().getPlayerCount());
		assertEquals(2, clientB.getCurrentRoom().getPlayerCount());
		
		//Return A to lobby
		clientA.joinLobby();
		pause();
		assertEquals(1, server.getRoomByName("room").getPlayerCount());
		assertEquals("Lobby", clientA.getCurrentRoom().getName());
		assertEquals("room", clientB.getCurrentRoom().getName());
		
		assertEquals(1, clientA.getCurrentRoom().getPlayerCount());
		assertEquals(1, clientB.getCurrentRoom().getPlayerCount());
		
		//Join back to room
		clientA.joinRoom("room");
		pause();
		assertEquals("room", clientA.getCurrentRoom().getName());
		assertEquals("room", clientB.getCurrentRoom().getName());
		assertEquals(2, clientA.getCurrentRoom().getPlayerCount());
		assertEquals(2, clientB.getCurrentRoom().getPlayerCount());
		
		clientB.joinLobby();
		pause();
		assertEquals(2, server.getConnectedCount());
		assertEquals(2, server.getLoggedInCount());
		assertEquals(1, server.getRoomCount());
		assertEquals(2, server.getLobbyRoom().getPlayerCount());
		assertNull(server.getRoomByName("room"));
		
		
		assertEquals("Lobby", clientA.getCurrentRoom().getName());
		assertEquals("Lobby", clientB.getCurrentRoom().getName());
		assertEquals(2, clientA.getCurrentRoom().getPlayerCount());
		assertEquals(2, clientB.getCurrentRoom().getPlayerCount());
		
	}
	
	

	public void pause() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("####################################################################");
	}

	public Thread createUpdater(final ClientManager manager) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					manager.update();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		t.start();
		return t;

	}
}
