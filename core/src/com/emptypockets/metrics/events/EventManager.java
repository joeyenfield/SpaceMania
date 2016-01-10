package com.emptypockets.metrics.events;

import com.emptypockets.spacemania.utils.PoolsManager;

public class EventManager {
	boolean enabled = true;
	Event rootEvent;
	Event currentEvent;

	public EventManager(String name) {
		start(name);
		rootEvent = currentEvent;
	}

	public void start(String name) {
		if (!enabled) {
			return;
		}
		Event event = PoolsManager.obtain(Event.class);
		if (currentEvent != null) {
			currentEvent.addChild(event);
			event.parent = currentEvent;
		}
		currentEvent = event;
		currentEvent.start(name);
	}

	public void stop(String name) {
		if (!enabled) {
			return;
		}
		if (currentEvent != null) {
			if (currentEvent == rootEvent) {
				throw new RuntimeException("Cant Stop root Event");
			}
			currentEvent.stop(name);
			currentEvent = currentEvent.parent;
		} else {
			throw new RuntimeException("No Current Event");
		}
	}

	public Event getRootEvent() {
		return rootEvent;
	}
}
