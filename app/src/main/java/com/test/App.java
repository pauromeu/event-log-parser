package com.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            logger.error("Usage: java -jar app.jar <path-to-logfile.txt>");
            System.exit(1);
        }

        String logFilePath = args[0];
        LogParser parser = new LogParser();

        try {
            List<ProcessedEvent> processedEvents = parser.parseAndProcessEvents(logFilePath);
            logger.info("Processed {} events:", processedEvents.size());
            for (ProcessedEvent pe : processedEvents) {
                logger.info(pe.toString());
            }

            // Save to DB
            EventRepository repo = new EventRepository();
            repo.saveEvents(processedEvents);
            repo.close();

            logger.info("Saved events to database successfully.");

        } catch (IOException e) {
            logger.error("Error reading log file: {}", e.getMessage());
            System.exit(1);
        } catch (SQLException e) {
            logger.error("Database error: {}", e.getMessage());
            System.exit(1);
        }
    }
}
