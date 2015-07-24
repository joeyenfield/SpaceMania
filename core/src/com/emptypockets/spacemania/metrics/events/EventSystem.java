package com.emptypockets.spacemania.metrics.events;

public class EventSystem {

	EventManager manager;
	
	static EventSystem eventSystem;
	private EventSystem(){
		manager = new EventManager("Root");
	}
	
	public static EventSystem getEventSystem(){
		if(eventSystem == null){
			synchronized (EventSystem.class) {
				if(eventSystem == null){
					eventSystem = new EventSystem();
				}
			}
		}
		return eventSystem;
	}
	
	public static Event getRoot(){
		return getEventSystem().manager.rootEvent;
	}
	
	public static void start(String name){
		getEventSystem().manager.start(name);
	}
	
	public static void stop(String name){
		getEventSystem().manager.stop(name);
	}
	
	public static void clean(long time) {
		getEventSystem().manager.getRootEvent().clearOldEvents(time);
	}

}
