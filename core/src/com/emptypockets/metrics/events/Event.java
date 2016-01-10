package com.emptypockets.metrics.events;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.utils.PoolsManager;

public class Event implements Poolable {
	String name;
	long startTime;
	long endTime;

	Event parent;
	ArrayList<Event> children = new ArrayList<Event>();

	private long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public void start(String name) {
		this.name = name;
		startTime = getCurrentTime();
		endTime = 0;
	}

	public void stop(String name) {
		if (!name.equals(name)) {
			throw new RuntimeException("Invalid Event");
		}
		endTime = getCurrentTime();
	}

	public void addChild(Event event) {
		synchronized (children) {
			children.add(event);
		}
	}

	public void clearOldEvents(long time) {
		synchronized (children) {
			Iterator<Event> events = children.iterator();
			while (events.hasNext()) {
				Event event = events.next();
				if (event.endTime < time) {
					events.remove();
				}
			}
		}
	}

	@Override
	public void reset() {
		for (Event event : children) {
			PoolsManager.free(event);
		}
		children.clear();
		parent = null;
		name = null;
		startTime = 0;
		endTime = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public Event getParent() {
		return parent;
	}

	public void setParent(Event parent) {
		this.parent = parent;
	}

	public ArrayList<Event> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Event> children) {
		this.children = children;
	}

}
