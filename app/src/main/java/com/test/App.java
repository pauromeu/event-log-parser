package com.test;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Error");
            System.exit(1);
        }

        String logFilePath = args[0];
        LogParser parser = new LogParser();

        try {
            List<Event> events = parser.parseLogFile(logFilePath);
            System.out.println("Parsed " + events.size() + " events:");
            for (Event event : events) {
                System.out.println(event);
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
            System.exit(1);
        }
    }
}
