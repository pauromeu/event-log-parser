package com.test;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class LogParser {
    private final Gson gson = new Gson();

    /**
     * Parses a log file and returns a list of ProcessedEvent objects by pairing STARTED and FINISHED events.
     *
     * @param filePath the path to the log file
     * @return a list of processed events with durations and alert flags
     * @throws IOException if an I/O error occurs
     */
    public List<ProcessedEvent> parseAndProcessEvents(String filePath) throws IOException {
        Map<String, Event> eventMap = new HashMap<>();
        List<ProcessedEvent> processedEvents = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                try {
                    Event event = gson.fromJson(line, Event.class);

                    String id = event.id;

                    if (eventMap.containsKey(id)) {
                        // Matching event found, compute duration and create ProcessedEvent
                        Event previous = eventMap.get(id);
                        long duration = Math.abs(event.timestamp - previous.timestamp);

                        // Prefer type and host from either event if present
                        String type = event.type != null ? event.type : previous.type;
                        String host = event.host != null ? event.host : previous.host;

                        ProcessedEvent processedEvent = new ProcessedEvent(id, duration, type, host);
                        processedEvents.add(processedEvent);

                        // Remove paired event
                        eventMap.remove(id);
                    } else {
                        // Store event for later pairing
                        eventMap.put(id, event);
                    }
                } catch (JsonSyntaxException e) {
                    System.err.println("Skipping invalid JSON line: " + line);
                }
            }
        }

        // Handle unmatched events
        if (!eventMap.isEmpty()) {
            System.err.println("Warning: Unmatched events found for IDs: " + eventMap.keySet());
        }

        return processedEvents;
    }
}
