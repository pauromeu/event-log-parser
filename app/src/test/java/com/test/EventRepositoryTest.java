package com.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collections;

import static org.junit.Assert.*;

public class EventRepositoryTest {

    private EventRepository repo;

    @Before
    public void setUp() throws SQLException {
        repo = new EventRepository();
        repo.clearEvents();  // Clear table using repository method
    }

    @After
    public void tearDown() {
        repo.close();
    }

    @Test
    public void testInsertAndRetrieveEvent() throws SQLException {
        ProcessedEvent event = new ProcessedEvent("test1", 10, "typeA", "hostA");
        repo.saveEvents(Collections.singletonList(event));

        ProcessedEvent retrieved = repo.getEventById("test1");
        assertNotNull(retrieved);
        assertEquals("test1", retrieved.getId());
        assertEquals(10L, retrieved.getDuration());
        assertEquals("typeA", retrieved.getType());
        assertEquals("hostA", retrieved.getHost());
        assertTrue(retrieved.isAlert());  // duration 10 > 4
    }

    @Test
    public void testUpdateEvent() throws SQLException {
        ProcessedEvent event1 = new ProcessedEvent("test2", 3, null, null);
        repo.saveEvents(Collections.singletonList(event1));

        ProcessedEvent event2 = new ProcessedEvent("test2", 7, "newType", "newHost");
        repo.saveEvents(Collections.singletonList(event2));

        ProcessedEvent retrieved = repo.getEventById("test2");
        assertNotNull(retrieved);
        assertEquals("test2", retrieved.getId());
        assertEquals(7L, retrieved.getDuration());
        assertEquals("newType", retrieved.getType());
        assertEquals("newHost", retrieved.getHost());
        assertTrue(retrieved.isAlert());  // duration 7 > 4
    }
}
