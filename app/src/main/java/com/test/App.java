package com.test;

import com.google.gson.Gson;

public class App {
    public static void main(String[] args) {
        String sample = "{\"id\":\"123\",\"state\":\"STARTED\",\"type\":\"event\",\"host\":\"localhost\",\"timestamp\":1491377495210}";
        Gson gson = new Gson();
        Event event = gson.fromJson(sample, Event.class);
        System.out.println("Parsed event: " + event);
    }
}
