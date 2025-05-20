package com.test;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class LogParserTest {
    @Test
    public void testParseSingleEvent() throws IOException {
        String json = "{\"id\":\"1\",\"state\":\"STARTED\",\"timestamp\":1000}";
        File tempFile = createTempFileWithContent(json);

        LogParser parser = new LogParser();
        List<ProcessedEvent> processedEvents = parser.parseAndProcessEvents(tempFile.getAbsolutePath());

        // No pairs yet, so expect 0 processed events
        assertEquals(0, processedEvents.size());

        tempFile.delete();
    }

    // Test pairing of STARTED and FINISHED events with duration calculation
    @Test
    public void testPairingEvents() throws IOException {
        String jsonLines = 
            "{\"id\":\"1\",\"state\":\"STARTED\",\"timestamp\":1000}\n" +
            "{\"id\":\"1\",\"state\":\"FINISHED\",\"timestamp\":1005}\n";

        File tempFile = createTempFileWithContent(jsonLines);

        LogParser parser = new LogParser();
        List<ProcessedEvent> processedEvents = parser.parseAndProcessEvents(tempFile.getAbsolutePath());

        assertEquals(1, processedEvents.size());
        ProcessedEvent event = processedEvents.get(0);
        assertEquals("1", event.getId());
        assertEquals(5, event.getDuration());
        assertTrue(event.isAlert());  // 5 > 4, so alert should be true

        tempFile.delete();
    }

    // Test invalid JSON line is skipped without crashing
    @Test
    public void testInvalidJsonLine() throws IOException {
        String jsonLines = 
            "{\"id\":\"1\",\"state\":\"STARTED\",\"timestamp\":1000}\n" +
            "invalid json\n" +
            "{\"id\":\"1\",\"state\":\"FINISHED\",\"timestamp\":1002}\n";

        File tempFile = createTempFileWithContent(jsonLines);

        LogParser parser = new LogParser();
        List<ProcessedEvent> processedEvents = parser.parseAndProcessEvents(tempFile.getAbsolutePath());

        assertEquals(1, processedEvents.size());
        ProcessedEvent event = processedEvents.get(0);
        assertEquals(2, event.getDuration());

        tempFile.delete();
    }

    private File createTempFileWithContent(String content) throws IOException {
        File tempFile = Files.createTempFile("test-log", ".txt").toFile();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            bw.write(content);
        }
        return tempFile;
    }
}
