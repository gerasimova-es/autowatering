#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>

// WiFi Parameters
const char* ssid = "kate";
const char* password = "kate_023";
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

// значение минимально допустимой влажности почвы
#define MIN_HUMIDITY 350
// интервал между проверкой на полив растения (10 минут)
#define INTERVAL 60000 * 10

// статическая переменная для хранения времени
unsigned long lastCheckTime = 0;


void setup(){
  Serial.begin(115200);
  //wifi
  WiFi.begin(ssid, password);
  Serial.println("connecting to wifi...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("waiting for connection...");
  }
  Serial.println("connected to wifi");
  //other
  pinMode(PUMP_RELAY, OUTPUT);
  digitalWrite(PUMP_RELAY, HIGH);
  pinMode(HUMIDITY_RELAY, OUTPUT);
  pinMode(FLOAT, INPUT);
  pinMode(WHISTLE, OUTPUT);
}

void loop(){
  boolean tankIsFull = getTankIsFull();
  if(!tankIsFull){
     whistling();
  }
  if(timeExpired()){
     int humidity = getHumidity();
     boolean needWatering = false;
     if (tankIsFull && humidity < MIN_HUMIDITY) {
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

boolean timeExpired(){
  return lastCheckTime == 0 || millis() - lastCheckTime > INTERVAL;
}

boolean getTankIsFull(){
  boolean floatIsUp = !digitalRead(FLOAT);
  Serial.println("float is up=" + String(floatIsUp));
  return floatIsUp;
}

int getHumidity(){
  Serial.println("включаем реле датчика влажности");
  digitalWrite(HUMIDITY_RELAY, LOW);
  delay(1000);
  int humidity = map(analogRead(HUMIDITY), 1023, 0, 0, 1023);
  digitalWrite(HUMIDITY_RELAY, HIGH);
  Serial.println("humidity=" + String(humidity));
  return humidity;
}

void watering(){
  Serial.println("включаем помпу");
  digitalWrite(PUMP_RELAY, LOW);
  delay(3000);
  digitalWrite(PUMP_RELAY, HIGH);
}

void whistling(){
   Serial.println("включаем свистелку-перделку");
   digitalWrite(WHISTLE, HIGH);
   delay(100);
   digitalWrite(WHISTLE, LOW);
}
