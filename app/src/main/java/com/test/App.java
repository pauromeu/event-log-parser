package com.test;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar app.jar <path-to-logfile.txt>");
            System.exit(1);
        }

        String logFilePath = args[0];
        LogParser parser = new LogParser();

        try {
            List<ProcessedEvent> processedEvents = parser.parseAndProcessEvents(logFilePath);
            System.out.println("Processed " + processedEvents.size() + " events:");
            for (ProcessedEvent pe : processedEvents) {
                System.out.println(pe);
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
            System.exit(1);
        }
    }
}

