package com.test;

public class ProcessedEvent {
    private final String id;
    private final long duration; // in ms
    private final String type; // Optional
    private final String host; // Optional
    private final boolean alert;  // true if duration > 4ms

    public ProcessedEvent(String id, long duration, String type, String host) {
        this.id = id;
        this.duration = duration;
        this.type = type;
        this.host = host;
        this.alert = duration > 4; // Alert if duration is greater than 4ms
    }

    public String getId() {
        return id;
    }

    public long getDuration() {
        return duration;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public boolean isAlert() {
        return alert;
    }

    @Override
    public String toString() {
        return String.format("ProcessedEvent{id='%s', duration=%d, type='%s', host='%s', alert=%s}",
                id, duration, type, host, alert); 
    }
}
