package com.emptypockets.spacemania.utils.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EventRecorder {

    int historyCount = 0;

    HashMap<String, Event> currentEvents = new HashMap<String, Event>();
    HashMap<String, EventHistory> historicEvents = new HashMap<String, EventHistory>();

    Event.EventTimerAccuracy accuracy = Event.EventTimerAccuracy.NANO_TIME;

    boolean enabled = false;

    public EventRecorder() {
    }

    public EventRecorder(int historyCount) {
        this.historyCount = historyCount;
    }

    public EventRecorder(Event.EventTimerAccuracy accur) {
        this.accuracy = accur;
    }

    private void ensureKey(String key) {
        if (!currentEvents.containsKey(key)) {
            currentEvents.put(key, new Event(accuracy));
        }

        if (!historicEvents.containsKey(key)) {
            historicEvents.put(key, new EventHistory(historyCount));
        }
    }

    public float getAverageEventDuration(String key) {
        if (!historicEvents.containsKey(key)) {
            return 0;
        }
        EventHistory history = historicEvents.get(key);
        return history.getAverageDuration();
    }

    public synchronized void begin(String key) {
        if (!enabled) {
            return;
        }
        ensureKey(key);

        Event event = currentEvents.get(key);
        event.start();
    }

    public synchronized int getEventCount(String key) {
        EventHistory hist = historicEvents.get(key);
        if (hist == null) {
            return 0;
        }
        return hist.getCount();
    }

    public synchronized void end(String key) {
        if (!enabled) {
            return;
        }
        Event event = currentEvents.get(key);
        event.end();
        currentEvents.remove(key);
        historicEvents.get(key).add(event);
    }

    public Event.EventTimerAccuracy getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Event.EventTimerAccuracy accuracy) {
        this.accuracy = accuracy;
    }
    
    public void logData(){
    	  ArrayList<String> event = new ArrayList<String>(historicEvents.keySet());
          Collections.sort(event, Collections.reverseOrder());
          int length = 0;
          for (String key : event) {
              if (length < key.length()) {
                  length = key.length();
              }
          }
          length = 1;
          for (String key : event) {
              System.out.println(String.format("%" + length + "s : %1.2f", key, historicEvents.get(key).getAverageDurationMS()));
          }
    }

    public void draw(SpriteBatch textBatch, BitmapFont font, float x, float y, float yOff) {

        ArrayList<String> event = new ArrayList<String>(historicEvents.keySet());
        Collections.sort(event, Collections.reverseOrder());

        font.setColor(Color.RED);
        int count = 1;

        int length = 0;
        for (String key : event) {
            if (length < key.length()) {
                length = key.length();
            }
        }
        length = 1;
        for (String key : event) {
            font.draw(textBatch, String.format("%" + length + "s : %1.2f", key, historicEvents.get(key).getAverageDurationMS()), x, y + (count++ * yOff));
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}
