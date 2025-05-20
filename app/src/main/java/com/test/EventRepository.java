package com.test;

import java.sql.*;
import java.util.List;

/**
 * Handles connection and operations with HSQLDB to store ProcessedEvents.
 */
public class EventRepository {
    private static final String JDBC_URL = "jdbc:hsqldb:file:../data/eventsdb;shutdown=true";
    private static final String USER = "SA";
    private static final String PASSWORD = "";

    private Connection connection;

    public EventRepository() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        createTableIfNotExists();
    }

    /**
     * Creates the EVENTS table if it does not exist.
     */
    private void createTableIfNotExists() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS EVENTS (" +
                "ID VARCHAR(255) PRIMARY KEY," +
                "DURATION BIGINT," +
                "TYPE VARCHAR(255)," +
                "HOST VARCHAR(255)," +
                "ALERT BOOLEAN" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        }
    }

    /**
     * Inserts a list of ProcessedEvent records into the database.
     */
    public void saveEvents(List<ProcessedEvent> events) throws SQLException {
        String deleteSQL = "DELETE FROM EVENTS WHERE ID = ?";
        String insertSQL = "INSERT INTO EVENTS (ID, DURATION, TYPE, HOST, ALERT) VALUES (?, ?, ?, ?, ?)";
    
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL);
             PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
    
            for (ProcessedEvent event : events) {
                /**
                 * NOTE: We currently delete existing records before inserting to avoid duplicates.
                 * TODO: Replace with proper UPSERT using HSQLDB MERGE for atomic update/insert.
                 */
                
                // Delete if exists
                deleteStmt.setString(1, event.getId());
                deleteStmt.executeUpdate();
    
                // Insert new record
                insertStmt.setString(1, event.getId());
                insertStmt.setLong(2, event.getDuration());
                insertStmt.setString(3, event.getType());
                insertStmt.setString(4, event.getHost());
                insertStmt.setBoolean(5, event.isAlert());
                insertStmt.executeUpdate();
            }
        }
    }
    

    /**
     * Closes the database connection.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                // Shutdown DB cleanly
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("SHUTDOWN");
                }
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing DB connection: " + e.getMessage());
        }
    }

    public void clearEvents() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM EVENTS");
        }
    }
    
    public ProcessedEvent getEventById(String id) throws SQLException {
        String sql = "SELECT * FROM EVENTS WHERE ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProcessedEvent(
                        rs.getString("ID"),
                        rs.getLong("DURATION"),
                        rs.getString("TYPE"),
                        rs.getString("HOST")
                    );
                }
                return null;
            }
        }
    }
    
}
