package com.test;

public class Event {
    public String id;
    public String state; // "STARTED" or "FINISHED"
    public String type; // Optional
    public String host; // Optional
    public long timestamp;

    // No-args constructor for Gson
    public Event() {
    }

    @Override
    public String toString() {
        return String.format("Event{id='%s', state='%s', timestamp=%d}", id, state, timestamp);
    }
}
