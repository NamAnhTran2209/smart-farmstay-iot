 #include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>

#define channel1_pin D0
#define channel2_pin D1
#define channel3_pin D2
#define channel4_pin D3
#define button_pin1 D5
#define button_pin2 D6
#define button_pin3 D7
#define button_pin4 D8
#define FirePin D4
#define waterLevelPin A0

// Wifi: SSID and password
const char *WIFI_SSID = "Redmi7A";
const char *WIFI_PASSWORD = "123123123";

// MQTT: ID, server IP, port, username and password
const char *MQTT_CLIENT_ID = "home";
const char *MQTT_SERVER_IP = "143.198.80.181";
const uint16_t MQTT_SERVER_PORT = 1883;
const char *MQTT_USER = "farmstay";
const char *MQTT_PASSWORD = "farmstay";

// MQTT: topic
const char *MQTT_RELAY_TOPIC_CHANNEL1 = "farmstay/093838eb-8ddc-4f06-ad63-21d3ab7fda3e/relay_0/relay_0_data_0";
const char *MQTT_RELAY_TOPIC_CHANNEL2 = "farmstay/093838eb-8ddc-4f06-ad63-21d3ab7fda3e/relay_0/relay_0_data_1";
const char *MQTT_RELAY_TOPIC_CHANNEL3 = "farmstay/093838eb-8ddc-4f06-ad63-21d3ab7fda3e/relay_0/relay_0_data_2";
const char *MQTT_RELAY_TOPIC_CHANNEL4 = "farmstay/093838eb-8ddc-4f06-ad63-21d3ab7fda3e/relay_0/relay_0_data_3";
const char *MQTT_SENSOR_TOPIC_FIRE = "farmstay/093838eb-8ddc-4f06-ad63-21d3ab7fda3e/fire_sensor_0/fire_sensor_0_data_0";
const char *MQTT_SENSOR_TOPIC_WATERLEVEL = "farmstay/093838eb-8ddc-4f06-ad63-21d3ab7fda3e/water_level_sensor_0/water_level_sensor_0_data_0";

// Define variables to keep track of the last time sensor data and relay control were sent
const unsigned long sensorInterval = 5000; // Send sensor data every 5 seconds
unsigned long lastSensorTime = 0;

WiFiClient espClient;
PubSubClient client(espClient);

// Publish data sensor fire
void publishData(int p_fire)
{
    DynamicJsonDocument doc(1024);
    doc["from"] = "hardware";
    doc["hardware_id"] = "rvuum33o7b2v8hqt";
    doc["value"] = int(p_fire);
    char jsonBuffer[1024];
    serializeJson(doc, jsonBuffer);
    Serial.println(jsonBuffer);
    client.publish(MQTT_SENSOR_TOPIC_FIRE, jsonBuffer);
}

// Publish data sensor waterlevel
void publishData1(int p_waterlevel)
{
    DynamicJsonDocument doc(1024);
    doc["from"] = "hardware";
    doc["hardware_id"] = "u8uisarqw3vkxogo";
    doc["value"] = int(p_waterlevel);
    char jsonBuffer[1024];
    serializeJson(doc, jsonBuffer);
    Serial.println(jsonBuffer);
    client.publish(MQTT_SENSOR_TOPIC_WATERLEVEL, jsonBuffer);
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

void callback(char *topic, byte *payload, unsigned int length)
{
    // Handle messages on subscribed topics
    if (strcmp(topic, MQTT_RELAY_TOPIC_CHANNEL1) == 0)
    {
        // Convert payload to a null-terminated string
        char payloadStr[length + 1];
        memcpy(payloadStr, payload, length);
        payloadStr[length] = '\0';

        // Parse the JSON string
        DynamicJsonDocument doc(256);
        DeserializationError error = deserializeJson(doc, payloadStr);

        if (error)
        {
            Serial.print("JSON parsing error: ");
            Serial.println(error.c_str());
            return;
        }

        // Check if the message ID matches
        if (strcmp(doc["hardware_id"], "x8ql1jra24gzjjtr") == 0 && strcmp(doc["from"], "client") == 0)
        {
            // Check the value field
            int value = doc["value"];
            if (value == 1)
            {
                digitalWrite(channel1_pin, LOW);
                DynamicJsonDocument doc(1024);
                doc["from"] = "hardware";
                doc["hardware_id"] = "x8ql1jra24gzjjtr";
                doc["value"] = 1;
                char jsonBuffer[1024];
                serializeJson(doc, jsonBuffer);
                Serial.println(jsonBuffer);
                client.publish(MQTT_RELAY_TOPIC_CHANNEL1, jsonBuffer);
            }
            else
            {
                digitalWrite(channel1_pin, HIGH);
                DynamicJsonDocument doc(1024);
                doc["from"] = "hardware";
                doc["hardware_id"] = "x8ql1jra24gzjjtr";
                doc["value"] = 0;
                char jsonBuffer[1024];
                serializeJson(doc, jsonBuffer);
                Serial.println(jsonBuffer);
                client.publish(MQTT_RELAY_TOPIC_CHANNEL1, jsonBuffer);
            }
        }
    }
    if (strcmp(topic, MQTT_RELAY_TOPIC_CHANNEL2) == 0)
    {
        // Convert payload to a null-terminated string
        char payloadStr[length + 1];
        memcpy(payloadStr, payload, length);
        payloadStr[length] = '\0';

        // Parse the JSON string
        DynamicJsonDocument doc(256);
        DeserializationError error = deserializeJson(doc, payloadStr);

        if (error)
        {
            Serial.print("JSON parsing error: ");
            Serial.println(error.c_str());
            return;
        }

        // Check if the message ID matches
        if (strcmp(doc["hardware_id"], "wrfw2lfsriiu9wj3") == 0 && strcmp(doc["from"], "client") == 0)
        {
            doc["from"] = "hardware";
            // Check the value field
            int value = doc["value"];
            if (value == 1)
            {
                digitalWrite(channel2_pin, LOW);
                DynamicJsonDocument doc(1024);
                doc["from"] = "hardware";
                doc["hardware_id"] = "wrfw2lfsriiu9wj3";
                doc["value"] = 1;
                char jsonBuffer[1024];
                serializeJson(doc, jsonBuffer);
                Serial.println(jsonBuffer);
                client.publish(MQTT_RELAY_TOPIC_CHANNEL2, jsonBuffer);
            }
            else
            {
                digitalWrite(channel2_pin, HIGH);
                DynamicJsonDocument doc(1024);
                doc["from"] = "hardware";
                doc["hardware_id"] = "wrfw2lfsriiu9wj3";
                doc["value"] = 0;
                char jsonBuffer[1024];
                serializeJson(doc, jsonBuffer);
                Serial.println(jsonBuffer);
                client.publish(MQTT_RELAY_TOPIC_CHANNEL2, jsonBuffer);
            }
        }
    }
    if (strcmp(topic, MQTT_RELAY_TOPIC_CHANNEL3) == 0)
    {
        // Convert payload to a null-terminated string
        char payloadStr[length + 1];
        memcpy(payloadStr, payload, length);
        payloadStr[length] = '\0';

        // Parse the JSON string
        DynamicJsonDocument doc(256);
        DeserializationError error = deserializeJson(doc, payloadStr);

        if (error)
        {
            Serial.print("JSON parsing error: ");
            Serial.println(error.c_str());
            return;
        }

        // Check if the message ID matches
        if (strcmp(doc["hardware_id"], "s9ec6ijqr9jlo010") == 0 && strcmp(doc["from"], "client") == 0)
        {
            doc["from"] = "hardware";
            // Check the value field
            int value = doc["value"];
            if (value == 1)
            {
                digitalWrite(channel3_pin, LOW);
                DynamicJsonDocument doc(1024);
                doc["from"] = "hardware";
                doc["hardware_id"] = "s9ec6ijqr9jlo010";
                doc["value"] = 1;
                char jsonBuffer[1024];
                serializeJson(doc, jsonBuffer);
                Serial.println(jsonBuffer);
                client.publish(MQTT_RELAY_TOPIC_CHANNEL3, jsonBuffer);
            }
            else
            {
                digitalWrite(channel3_pin, HIGH);
                DynamicJsonDocument doc(1024);
                doc["from"] = "hardware";
                doc["hardware_id"] = "s9ec6ijqr9jlo010";
                doc["value"] = 0;
                char jsonBuffer[1024];
                serializeJson(doc, jsonBuffer);
                Serial.println(jsonBuffer);
                client.publish(MQTT_RELAY_TOPIC_CHANNEL3, jsonBuffer);
            }
        }
    }
    if (strcmp(topic, MQTT_RELAY_TOPIC_CHANNEL4) == 0)
    {
        // Convert payload to a null-terminated string
        char payloadStr[length + 1];
        memcpy(payloadStr, payload, length);
        payloadStr[length] = '\0';

        // Parse the JSON string
        DynamicJsonDocument doc(256);
        DeserializationError error = deserializeJson(doc, payloadStr);

        if (error)
        {
            Serial.print("JSON parsing error: ");
            Serial.println(error.c_str());
            return;
        }

        // Check if the message ID matches
        if (strcmp(doc["hardware_id"], "fjfczaegxjhzqe8i") == 0 && strcmp(doc["from"], "client") == 0)
        {
            doc["from"] = "hardware";
            // Check the value field
            int value = doc["value"];
            if (value == 1)
            {
                digitalWrite(channel4_pin, LOW);
                DynamicJsonDocument doc(1024);
                doc["from"] = "hardware";
                doc["hardware_id"] = "fjfczaegxjhzqe8i";
                doc["value"] = 1;
                char jsonBuffer[1024];
                serializeJson(doc, jsonBuffer);
                Serial.println(jsonBuffer);
                client.publish(MQTT_RELAY_TOPIC_CHANNEL4, jsonBuffer);
            }
            else
            {
                digitalWrite(channel4_pin, HIGH);
                DynamicJsonDocument doc(1024);
                doc["from"] = "hardware";
                doc["hardware_id"] = "fjfczaegxjhzqe8i";
                doc["value"] = 0;
                char jsonBuffer[1024];
                serializeJson(doc, jsonBuffer);
                Serial.println(jsonBuffer);
                client.publish(MQTT_RELAY_TOPIC_CHANNEL4, jsonBuffer);
            }
        }
    }
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
            // Once connected, subscribe to the channels for each relay
            client.subscribe(MQTT_RELAY_TOPIC_CHANNEL1);
            client.subscribe(MQTT_RELAY_TOPIC_CHANNEL2);
            client.subscribe(MQTT_RELAY_TOPIC_CHANNEL3);
            client.subscribe(MQTT_RELAY_TOPIC_CHANNEL4);
        }
        else
        {
            Serial.print("ERROR: failed, rc=");
            Serial.print(client.state());
            Serial.println(" try again in 5 seconds");
            // Wait 5 seconds before retrying
            delay(5000);
        }
    }
}

void setup()
{
    Serial.begin(115200);
    setup_wifi();
    client.setServer(MQTT_SERVER_IP, MQTT_SERVER_PORT);
    client.setCallback(callback);
    // Set Relay pins as outputs
    pinMode(channel1_pin, OUTPUT);
    pinMode(channel2_pin, OUTPUT);
    pinMode(channel3_pin, OUTPUT);
    pinMode(channel4_pin, OUTPUT);
    digitalWrite(channel1_pin, HIGH);
    digitalWrite(channel2_pin, HIGH);
    digitalWrite(channel3_pin, HIGH);
    digitalWrite(channel4_pin, HIGH);
    pinMode(waterLevelPin, INPUT);
}

void loop()
{
    if (!client.connected())
    {
        reconnect();
    }
    client.loop();

    // Get the current time
    unsigned long currentTime = millis();

    // Check if it's time to send sensor data
    if (currentTime - lastSensorTime >= sensorInterval)
    {
        int flameValue = digitalRead(FirePin);
        if (digitalRead(FirePin) == LOW)
        {
            flameValue = 1; // Có lửa
        }
        else
        {
            flameValue = 0; // Không có lửa
        }
        publishData(flameValue);
        int waterLevel = analogRead(waterLevelPin);
        publishData1(waterLevel);
        lastSensorTime = currentTime; // Update the last sensor time
    }
}
