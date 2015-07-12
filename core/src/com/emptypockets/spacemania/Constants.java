package com.emptypockets.spacemania;

public class Constants {
	public static int DEFAULT_PARTICLES = 2000;
	public static int DEFAULT_PARTICLES_SPHERE = 50;
	public static int DEFAULT_PARTICLES_LIFETIME = 3000;

	public static int PARTICLE_SYTEM_PARTITION_X = 20;
	public static int PARTICLE_SYTEM_PARTITION_Y = 20;

	public static int ENTITY_SYTEM_PARTITION_X = 20;
	public static int ENTITY_SYTEM_PARTITION_Y = 20;
	public static float DEFAULT_ROOM_SIZE = 1000;

	public static boolean ENTITY_SPAWN = true;
	public static int ENTITY_SPAWN_TIME = 200;

	public static int ENTITY_SPAWN_FOLLOW_COUNT = 10;
	public static int ENTITY_SPAWN_RANDOM_COUNT = 10;
	
	
	public static boolean RENDER_DEBUG = true;
	public static boolean RENDER_TEXTURE = false;

	public static final int BUFFER_OBJECT_SERVER = 5*1024*1024;
	public static final int BUFFER_WRITE_SERVER = 5*1024*1024;
	

	public static final int BUFFER_OBJECT_CLIENT = 5*1024*1024;
	public static final int BUFFER_WRITE_CLIENT = 5*1024*1024;
}
