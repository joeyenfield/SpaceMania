package com.emptypockets.spacemania.utils.event;


public class Event {
    private EventTimerAccuracy accuracy = EventTimerAccuracy.MILLI_TIME;
    private long start;
    private long end;
    private float seconds;
    public Event() {
    }

    public Event(EventTimerAccuracy accuracy) {
        this();
        this.accuracy = accuracy;
    }

    private void updateTime() {
        seconds = end - start;
        switch (accuracy) {
            case MILLI_TIME:
                seconds = seconds / 1e3f;
                break;
            case NANO_TIME:
                seconds = seconds / 1e9f;
                break;
        }
    }

    public void start() {
        start = getTime(accuracy);
    }

    public void end() {
        end = getTime(accuracy);
        updateTime();
    }

    public float getSeconds() {
        return seconds;
    }

    private long getTime(EventTimerAccuracy accuracy) {
        switch (accuracy) {
            case NANO_TIME:
                return System.nanoTime();
            case MILLI_TIME:
                return System.currentTimeMillis();
            default:
                return 0;
        }
    }

    public String toString() {
        return String.format("[%d - %d] : %f", start, end, seconds);
    }

    public enum EventTimerAccuracy {
        NANO_TIME,
        MILLI_TIME
    }
}