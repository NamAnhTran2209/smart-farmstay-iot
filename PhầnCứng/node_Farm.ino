#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "DHT.h"

#define MQTT_VERSION MQTT_VERSION_3_1_1
#define DHTPIN D1
#define DHTTYPE DHT11
#define sensor_pin A0

// Wifi: SSID and password
const char *WIFI_SSID = "Redmi7A";
const char *WIFI_PASSWORD = "123123123";

// MQTT: ID, server IP, port, username and password
const char *MQTT_CLIENT_ID = "farm";
const char *MQTT_SERVER_IP = "143.198.80.181";
const uint16_t MQTT_SERVER_PORT = 1883;
const char *MQTT_USER = "farmstay";
const char *MQTT_PASSWORD = "farmstay";

// MQTT: topic
const char *MQTT_SENSOR_TOPIC_MOISTURE = "farmstay/093838eb-8ddc-4f06-ad63-21d3ab7fda3e/soil_moisture_sensor_0/soil_moisture_sensor_0_data_0";
const char *MQTT_SENSOR_TOPIC_HUMIDITY = "farmstay/093838eb-8ddc-4f06-ad63-21d3ab7fda3e/humidity_temperature_sensor_0/humidity_temperature_sensor_0_data_1";
const char *MQTT_SENSOR_TOPIC_TEMPERATURE = "farmstay/093838eb-8ddc-4f06-ad63-21d3ab7fda3e/humidity_temperature_sensor_0/humidity_temperature_sensor_0_data_0";

// Define variables to keep track of the last time sensor data were sent
unsigned long lastSensorTime = 0;
const unsigned long sensorInterval = 5000; // Send sensor data every 5 seconds

WiFiClient wifiClient;
PubSubClient client(wifiClient);
DHT dht(DHTPIN, DHTTYPE);

void publishData(int p_moisture)
{
    DynamicJsonDocument doc(1024);
    doc["from"] = "hardware";
    doc["hardware_id"] = "9fdr1v9a69lnk8iu";
    doc["value"] = int(p_moisture);
    char jsonBuffer[1024];
    serializeJson(doc, jsonBuffer);
    Serial.println(jsonBuffer);
    client.publish(MQTT_SENSOR_TOPIC_MOISTURE, jsonBuffer);
}
void publishData2(float p_humidity)
{
    DynamicJsonDocument doc(1024);
    doc["from"] = "hardware";
    doc["hardware_id"] = "ob7bl3tmp1ec89sd";
    doc["value"] = float(p_humidity);
    char jsonBuffer[1024];
    serializeJson(doc, jsonBuffer);
    Serial.println(jsonBuffer);
    client.publish(MQTT_SENSOR_TOPIC_HUMIDITY, jsonBuffer);
}
void publishData3(float p_temperature)
{
    DynamicJsonDocument doc(1024);
    doc["from"] = "hardware";
    doc["hardware_id"] = "ehb88zfzo7cx8mcq";
    doc["value"] = float(p_temperature);
    char jsonBuffer[1024];
    serializeJson(doc, jsonBuffer);
    Serial.println(jsonBuffer);
    client.publish(MQTT_SENSOR_TOPIC_TEMPERATURE, jsonBuffer);
}
void setup_wifi()
{
    delay(10);
    // We start by connecting to a WiFi network
    Serial.println();
    Serial.print("INFO: Connecting to ");
    Serial.println(WIFI_SSID);
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

    while (WiFi.status() != WL_CONNECTED)
    {
        delay(500);
        Serial.print(".");
    }
    Serial.println("");
    Serial.println("INFO: WiFi connected");
    Serial.println("INFO: IP address: ");
    Serial.println(WiFi.localIP());
}
void reconnect()
{
    // Loop until we're reconnected
    while (!client.connected())
    {
        Serial.print("INFO: Attempting MQTT connection...");
        // Attempt to connect
        if (client.connect(MQTT_CLIENT_ID, MQTT_USER, MQTT_PASSWORD))
        {
            Serial.println("INFO: connected");
        }
        else
        {
            Serial.print("ERROR: failed, rc=");
            Serial.print(client.state());
            Serial.println("DEBUG: try again in 5 seconds");
            // Wait 5 seconds before retrying
            delay(5000);
        }
    }
}

void setup()
{
    // init the serial
    Serial.begin(115200);
    setup_wifi();
    dht.begin();
    // init the MQTT connection
    client.setServer(MQTT_SERVER_IP, MQTT_SERVER_PORT);
}

void loop()
{
    if (!client.connected())
    {
        reconnect();
    }
    client.loop();
    unsigned long currentTime = millis();

    // Check if it's time to send sensor data
    if (currentTime - lastSensorTime >= sensorInterval)
    {
        float temperature = dht.readTemperature();
        float humidity = dht.readHumidity();

        if (isnan(humidity) || isnan(temperature))
        {
            Serial.println("ERROR: Failed to read from DHT sensor!");
            return;
        }
        else
        {
            publishData2(humidity);
            publishData3(temperature);
        }
        int mois = analogRead(sensor_pin);
        float moisture_percentage = (100.00 - ((mois / 1024.00) * 100.00));
        publishData(moisture_percentage);

        lastSensorTime = currentTime; // Update the last sensor time
    }
}
