#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WebServer.h>
#include <ArduinoJson.h>

// WiFi Parameters
const char* ssid = "kate";
const char* password = "kate_023";
ESP8266WebServer server(80);

// URL приложения
const char* url = "http://192.168.1.35:8080/autowatering";

// аналаговый выхода датчика влажности
#define HUMIDITY A0
// цифровой выход для датчика влажности на реле
#define HUMIDITY_RELAY D2
// цифровой выход для насоса на реле
#define PUMP_RELAY D4
// цифровой выход для поплавка
#define FLOAT D7
// цифровой выход для свистелки-перделки
#define WHISTLE D3

// статическая переменная для хранения времени проверки
unsigned long lastCheckTime = 0;
// минимально допустимая влажность почвы
unsigned long minHumidity = 350;
// интервал между проверкой на полив растения в миллисекундах
unsigned long checkInterval = 600000;
//длительность работы насоса при поливе в миллисекундах
unsigned long wateringDuration = 2000;


void setup(){
  Serial.begin(115200);
  //set pin mode
  pinMode(PUMP_RELAY, OUTPUT);
  pinMode(HUMIDITY_RELAY, OUTPUT);
  pinMode(FLOAT, INPUT);
  pinMode(WHISTLE, OUTPUT);
  //set default value
  digitalWrite(PUMP_RELAY, HIGH);

  //wifi
  WiFi.begin(ssid, password);
  Serial.println("connecting to wifi...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("waiting for connection...");
  }
  Serial.println("connected to wifi");

  //start wifi server
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  server.on("/pot/settings/save", changePotSettings);
  server.begin();
  Serial.println("servier listening started");
  Serial.println("--------------------");

  //добавить считывание настроек с сервера при старте
}

void loop(){
  server.handleClient();

  boolean tankIsFull = getTankIsFull();
  if(!tankIsFull){
     whistling();
  }
  if(timeExpired()){
     int humidity = getHumidity();
     boolean needWatering = false;
     if (tankIsFull && humidity < minHumidity) {
        needWatering = true;
        watering();
     }
     sendPotState(humidity, needWatering);
     sendTankState(tankIsFull);

     lastCheckTime = millis();
     Serial.println("--------------------");
  }
  delay(2000);
}

void sendPotState(int humidity, boolean watering){
  Serial.println("sending post state save request...");
  String body = String("{\"potCode\": \"AUTHORIUM\", \"humidity\": ") + String(humidity) + ".0, \"watering\": " + String(watering) + " }";
  sendToServer("/pot/state/save", body);
}

void sendTankState(boolean tankIsFull){
    Serial.println("sending tank state save request...");
    String body = String("{\"name\": \"DEFAULT\", \"volume\": 2.178, \"filled\": ") + String(tankIsFull) + String(".0 }");
    sendToServer("/tank/state/save", body);
}

void sendToServer(String service, String body){
   if (WiFi.status() == WL_CONNECTED) {
      Serial.println("sending request...");
      HTTPClient http;
      http.begin(String(url) + service);
      http.addHeader("Content-Type", "application/json");
      int httpCode = http.POST(body);
      Serial.println("request status: " + String(httpCode));
      if (httpCode > 0) {
        Serial.println("payload=" + String(http.getString()));
      }
      http.end();
  } else {
      Serial.println("WiFi connection error. Request was not sent");
  }
}

void changePotSettings() {
  if (server.hasArg("plain")== false){
     Serial.println("body is empty");
     server.send(200, "text/plain", "{ \"status\": \"ERROR\", \"message\": \"body is empty\"}");
     return;
  }
  String message = server.arg("plain");
  Serial.println("got message " + message);

  const size_t capacity = JSON_OBJECT_SIZE(4) + 80;
  DynamicJsonBuffer jsonBuffer(capacity);
  JsonObject& root = jsonBuffer.parseObject(message);

  const char* potCode = root["code"];
  Serial.println("potCode=" + String(potCode));

  minHumidity = root["minHumidity"];
  Serial.println("minHumidity=" + String(minHumidity));

  checkInterval = root["checkInterval"];
  checkInterval = checkInterval * 60000;
  Serial.println("checkInterval=" + String(checkInterval));

  wateringDuration = root["wateringDuration"];
  wateringDuration = wateringDuration * 1000;
  Serial.println("wateringDuration=" + String(wateringDuration));

  lastCheckTime = 0;

  server.send(200, "text/plain", "{ \"status\": \"SUCCESS\", \"message\": \"message was handled successfully\"}");
  Serial.println("--------------------");
}

boolean timeExpired(){
  return lastCheckTime == 0 || millis() - lastCheckTime > checkInterval;
}

boolean getTankIsFull(){
  boolean floatIsUp = !digitalRead(FLOAT);
//  Serial.println("float is up=" + String(floatIsUp));
  return floatIsUp;
}

int getHumidity(){
  Serial.println("turning humidity sensor ON...");
  digitalWrite(HUMIDITY_RELAY, LOW);
  delay(1000);
  int humidity = map(analogRead(HUMIDITY), 1023, 0, 0, 1023);
  digitalWrite(HUMIDITY_RELAY, HIGH);
  Serial.println("humidity=" + String(humidity));
  return humidity;
}

void watering(){
  Serial.println("turning pump ON...");
  digitalWrite(PUMP_RELAY, LOW);
  delay(wateringDuration);
  digitalWrite(PUMP_RELAY, HIGH);
}

void whistling(){
   Serial.println("turning whistle ON...");
   digitalWrite(WHISTLE, HIGH);
   delay(100);
   digitalWrite(WHISTLE, LOW);
}
