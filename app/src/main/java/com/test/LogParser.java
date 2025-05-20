package com.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LogParser {
    private final Gson gson = new Gson();

    /**
     * Parses a log file and returns a list of Event objects.
     *
     * @param filePath the path to the log file
     * @return a list of Event objects
     * @throws IOException if an I/O error occurs (including file not found)
     */
    public List<Event> parseLogFile(String filePath) throws IOException {
        List<Event> events = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Event event = gson.fromJson(line, Event.class);
                    events.add(event);
                } catch (JsonSyntaxException e) {
                    System.err.println("Failed to parse line: " + line);
                }
            }
        }
        return events;
    }
}
