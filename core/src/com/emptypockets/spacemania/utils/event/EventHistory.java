package com.emptypockets.spacemania.utils.event;

import java.util.ArrayList;

public class EventHistory {
	ArrayList<Event> history;
	int maxHistory=0;
	float avgDuration=0;
	
	public EventHistory(int maxHistory){
		this.maxHistory = maxHistory;
		history = new ArrayList<Event>(maxHistory+1);
	}

	public synchronized void add(Event event){
		history.add(event);
		if(maxHistory>0 && history.size() > maxHistory){
			history.remove(0);
		}
		updateAverage();
	}

	private void updateAverage(){
		float avg = 0;
		if(history.size()==0){
			avg = 0;
		}else{
			for(Event e : history){
				avg+=e.getSeconds();
			}
			avg/=history.size();
		}
		avgDuration = avg;
	}
	
	public synchronized void printEventDetails(){
		for(Event e :  history){
			System.out.println(e);
		}
	}
	
	public float getAverageDuration() {
		return avgDuration;
	}
	
	public float getAverageDurationMS() {
		return avgDuration*1e3f;
	}
	
	public synchronized int getCount() {
		return history.size();
	}
}
