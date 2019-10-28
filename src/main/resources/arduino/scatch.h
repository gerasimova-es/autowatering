#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WebServer.h>
#include <ArduinoJson.h>

// WiFi
const char* ssid = "kate";
const char* password = "kate_023";
ESP8266WebServer server(80);

// application url
const char* url = "http://192.168.1.34:8080/autowatering";

//pins
#define HUMIDITY A0
#define HUMIDITY_RELAY D2
#define PUMP_RELAY D4
#define FLOAT D7
#define WHISTLE D3

struct Pot{
  const char* code;
  unsigned int minHumidity;
  unsigned int checkInterval;
  unsigned int wateringDuration;
  unsigned long lastCheckTime;
};
struct Pot authorium;

void setup(){
  Serial.begin(115200);
  pinMode(PUMP_RELAY, OUTPUT);
  digitalWrite(PUMP_RELAY, HIGH);
  pinMode(HUMIDITY_RELAY, OUTPUT);
  pinMode(FLOAT, INPUT);
  pinMode(WHISTLE, OUTPUT);

  wifiConnect();
  wifiServerStart();
  loadSettings();
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
     if (tankIsFull && humidity < authorium.minHumidity) {
        needWatering = true;
        watering();
     }
     sendPotState(humidity, needWatering);
     sendTankState(tankIsFull);

     authorium.lastCheckTime = millis();
     Serial.println("--------------------");
  }
  delay(2000);
}

void wifiConnect(){
  WiFi.begin(ssid, password);
  Serial.println("connecting to wifi...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("waiting for connection...");
  }
  Serial.println("connected to wifi");
}

void wifiServerStart(){
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  server.on("/pot/settings/save", handleLoadSettings);
  server.begin();
  Serial.println("server listening started");
  Serial.println("--------------------");
}

void loadSettings(){
   Serial.println("loading settings...");
   //authorium
   String result = sendGetRequest("/pot/info?code=AUTHORIUM");
   if(result == "error"){
      Serial.println("load setting error for pot AUTHORIUM. Setting default values...");
      authorium.minHumidity = 200;
      authorium.checkInterval = 10*60000;
      authorium.wateringDuration = 3*1000;
   } else {
      String payload = getPayload(result);
      authorium = parsePotJson(getPayload(result));
   }
}

void handleLoadSettings() {
  Serial.println("Loading setting request received. Saving...");
  if (server.hasArg("plain")== false){
     server.send(200, "text/plain", "{ \"status\": \"ERROR\", \"message\": \"body is empty\"}");
     return;
  }
  struct Pot pot = parsePotJson(server.arg("plain"));

  if(String(pot.code) == "AUTHORIUM"){
    authorium = pot;
    if(timeExpired()){
      authorium.lastCheckTime = 0;
    }
    server.send(200, "application/json", "{ \"status\": \"SUCCESS\", \"message\": \"message was handled successfully\"}");
  } else {
    Serial.println("The system doesn't know pot with name " + String(pot.code));
      server.send(500, "application/json", "{}");
  }


  Serial.println("--------------------");
}

boolean timeExpired(){
  return authorium.lastCheckTime == 0 || millis() - authorium.lastCheckTime > authorium.checkInterval;
}

void sendPotState(int humidity, boolean watering){
  String body = String("{\"potCode\": \"AUTHORIUM\", \"humidity\": ") + String(humidity) + ".0, \"watering\": " + String(watering) + " }";
  sendPostRequest("/pot/state/save", body);
}

void sendTankState(boolean tankIsFull){
  String body = String("{\"name\": \"DEFAULT\", \"volume\": 2.178, \"filled\": ") + String(tankIsFull) + String(".0 }");
  sendPostRequest("/tank/state/save", body);
}

//вспомогательные функции
String sendGetRequest(String service){
   if (WiFi.status() != WL_CONNECTED) {
      Serial.println("WiFi connection error");
      return "error";
   }
   HTTPClient http;
   http.begin(String(url) + service);
   http.addHeader("Content-Type", "application/json");
   int httpCode = http.GET();

   String result;
   if(httpCode > 0){
      result = http.getString();
   } else {
      result = "error";
      Serial.println("request status: " + String(httpCode));
   }
   http.end();
   return result;
}

String sendPostRequest(String service, String body){
   Serial.println("sending POST body = " + body);
   if (WiFi.status() != WL_CONNECTED) {
      Serial.println("WiFi connection error");
      return "error";
   }
   HTTPClient http;
   http.begin(String(url) + service);
   http.addHeader("Content-Type", "application/json");
   int httpCode = http.POST(body);
   String result;
   if(httpCode > 0){
      result = http.getString();
   } else {
      Serial.println("error response status: " + String(httpCode));
      result = "error";
   }
   http.end();
   return result;
}

String getPayload(String json){
  const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_OBJECT_SIZE(7) + 190;
  DynamicJsonBuffer jsonBuffer(capacity);
  JsonObject& root = jsonBuffer.parseObject(json);
  return root["payload"];
}

struct Pot parsePotJson(String potJson){
  const size_t capacity = JSON_OBJECT_SIZE(7) + 120;
  DynamicJsonBuffer jsonBuffer(capacity);
  JsonObject& root = jsonBuffer.parseObject(potJson);

  struct Pot tmp;
  tmp.code = root["code"];
  tmp.minHumidity = root["minHumidity"];

  //todo cast to int
  tmp.checkInterval = root["checkInterval"];
  tmp.checkInterval = tmp.checkInterval * 60000;

  tmp.wateringDuration = root["wateringDuration"];
  tmp.wateringDuration = tmp.wateringDuration * 1000;

  Serial.println("Pot " + String(tmp.code)+ ": min humidity " + String(tmp.minHumidity) + ", check interval = " + String(tmp.checkInterval) + ", watering duration = " + String(tmp.wateringDuration));
  return tmp;
}

//фунукции работы с устройствами
boolean getTankIsFull(){
  return !digitalRead(FLOAT);
}

int getHumidity(){
  Serial.println("включаем датчик влажности");
  digitalWrite(HUMIDITY_RELAY, LOW);
  delay(1000);
  int humidity = map(analogRead(HUMIDITY), 1023, 0, 0, 1023);
  digitalWrite(HUMIDITY_RELAY, HIGH);
  Serial.println("выключаем датчик влажности");
  return humidity;
}

void watering(){
  Serial.println("включаем помпу");
  digitalWrite(PUMP_RELAY, LOW);
  delay(authorium.wateringDuration);
  digitalWrite(PUMP_RELAY, HIGH);
  Serial.println("выключаем помпу");
}

void whistling(){
   digitalWrite(WHISTLE, HIGH);
   delay(100);
   digitalWrite(WHISTLE, LOW);
}