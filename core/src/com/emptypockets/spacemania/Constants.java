package com.emptypockets.spacemania;

public class Constants {
	public static int DEFAULT_PARTICLES = 1000;
	public static int DEFAULT_PARTICLES_CREATED_IN_SPHERE = 50;
	public static int DEFAULT_PARTICLES_LIFETIME = 3000;

	public static int PARTICLE_SYSTEM_PARTITION_X = 20;
	public static int PARTICLE_SYSTEM_PARTITION_Y = 20;
	public static int ENTITY_SYSTEM_PARTITION_X = 50;
	public static int ENTITY_SYSTEM_PARTITION_Y = 50;
	
	public static float DEFAULT_ROOM_SIZE = 2000;

	public static boolean ENTITY_SPAWN = true;
	public static int ENTITY_SPAWN_TIME = 200;

	public static int ENTITY_SPAWN_FOLLOW_COUNT = 20;
	public static int ENTITY_SPAWN_RANDOM_COUNT = 2;
	public static final float ENTITY_SPAWN_PLAYER_DISTANCE = 200;
	
	
	public static boolean RENDER_DEBUG = false;
	public static boolean RENDER_TEXTURE = true;
	
	public static int GRID_SIZE_X = 2;
	public static int GRID_SIZE_Y = 2;
	public static int GRID_RENDER = 0;
	public static final boolean GRID_DYNAMIC = false;
	
	public static final int SERVER_BUFFER_OBJECT = 5*1024*1024;
	public static final int SERVER_BUFFER_WRITE = 5*1024*1024;
	
	public static final int CLIENT_BUFFER_OBJECT = 5*1024*1024;
	public static final int CLIENT_BUFFER_WRITE = 5*1024*1024;

	public static final long CLIENT_TIME_INPUT_TO_SERVER_PEROID = 50;
	
	public static final long SERVER_TIME_UPDATE_PEROID = 50;
	public static final long SERVER_TIME_PING_UPDATE_PEROID = 5000;
	public static final long SERVER_TIME_PLAYER_STATE_UPDATE_PEROID = 1000;
	public static final long SERVER_TIME_ROOM_BROADCAST_PEROID = 150;
	public static final long SERVER_TIME_ROOM_BROADCAST_PLAYER_PEROID = 2000;
	
}
