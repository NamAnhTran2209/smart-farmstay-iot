package model;

import java.util.HashMap;
import java.util.Map;

public class SensorData {
    private static SensorData instance;
    private Map<String, String> sensorValues;

    private SensorData() {
        sensorValues = new HashMap<>();
    }

    public static SensorData getInstance() {
        if (instance == null) {
            instance = new SensorData();
        }
        return instance;
    }

    public Map<String, String> getSensorValues() {
        return sensorValues;
    }

    public void setSensorValues(Map<String, String> sensorValues) {
        this.sensorValues = sensorValues;
    }
}

