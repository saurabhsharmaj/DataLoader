package com.ebit.scheduler.models;

import java.util.HashMap;
import java.util.Map;

public class ManualSchedulerArgument {

    private final Map<String, String> arguments = new HashMap<>();

    public void add(String name, String value) {
        arguments.put(name, value);
    }

    public String get(String name) {
        return arguments.get(name);
    }

    public String get(String name, String defaultValue) {
        return arguments.getOrDefault(name, defaultValue);
    }

    public boolean contains(String name) {
        return arguments.containsKey(name);
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public int getInt(String name, int defaultValue) {
        String value = arguments.get(name);

        if (value == null) {
            return defaultValue;
        }

        return Integer.parseInt(value);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        String value = arguments.get(name);

        if (value == null) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value);
    }

    @Override
    public String toString() {
        return arguments.toString();
    }
}
