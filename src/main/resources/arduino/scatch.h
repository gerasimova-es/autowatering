#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>

// WiFi Parameters
const char* ssid = "kate";
const char* password = "kate_023";

// аналаговый выхода датчика влажности
#define HUMIDITY A0
// значение минимально допустимой влажности почвы
#define MIN_HUMIDITY 300
// интервал между проверкой на полив растения (10 минут)
#define INTERVAL 60000 * 10
// цифровой выход для датчика влажности на реле
#define HUMIDITY_RELAY D2
// цифровой выход для насоса на реле
#define PUMP_RELAY D4
// цифровой выход для поплавка
#define FLOAT D7
// цифровой выход для свистелки-перделки
#define WHISTLE D3

// статическая переменная для хранения времени
unsigned long waitTime = 0;

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
  Serial.println("float is up=" + String(tankIsFull));

  //включаем свистелку-перделку, если резервуар опустошился
  if(!tankIsFull){
     digitalWrite(WHISTLE, HIGH);
     Serial.println("включаем свистелку-перделку");
     delay(100);
     digitalWrite(WHISTLE, LOW);
  }

  //если прошел заданный интервал времени
  if(timeExpired()){
     int humidity = getHumidity();
     Serial.println("humidity=" + String(humidity));

     boolean needWatering = false;
     // если резервуар заполнен и влажность низкая
     if (tankIsFull && humidity < MIN_HUMIDITY) {
        //включаем насос
        needWatering = true;
        watering();
     }
     sendPotState(humidity, needWatering);
     sendTankState(tankIsFull);

     //сохраняем текущее время проверки
     waitTime = millis();
     Serial.println("--------------------");
  }
  delay(2000);
}

boolean timeExpired(){
  return waitTime == 0 || millis() - waitTime > INTERVAL;
}

void sendPotState(int humidity, boolean watering){
  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("sending post state save request...");
    HTTPClient http;
    http.begin("http://192.168.1.35:8080/autowatering/pot/state/save");
    http.addHeader("Content-Type", "application/json");
    String body = String("{\"potCode\": \"AUTHORIUM\", \"humidity\": ") + String(humidity) + ".0, \"watering\": " + String(watering) + " }";
    int httpCode = http.POST(body);
    Serial.println("request status: " + String(httpCode));
    if (httpCode > 0) {
      // Get the response pay
      String payload = http.getString();
      Serial.println("payload=" + String(payload));
    }
    http.end();
  } else {
      Serial.println("error in WiFi connection");
  }
}

void sendTankState(boolean tankIsFull){
  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("sending tank state save request...");
    HTTPClient http;
    http.begin("http://192.168.1.35:8080/autowatering/tank/state/save");
    http.addHeader("Content-Type", "application/json");
    String body = String("{\"name\": \"DEFAULT\", \"volume\": 2.178, \"filled\": ") + String(tankIsFull) + String(".0 }");
    int httpCode = http.POST(body);
    Serial.println("request status: " + String(httpCode));
    if (httpCode > 0) {
      // Get the response pay
      String payload = http.getString();
      Serial.println("payload=" + String(payload));
    }
    http.end();
  } else {
      Serial.println("error in WiFi connection");
  }
}

boolean getTankIsFull(){
  return !digitalRead(FLOAT);
}

int getHumidity(){
  Serial.println("включаем реле датчика влажности");
  digitalWrite(HUMIDITY_RELAY, LOW);
  delay(1000);
  int humidity = map(analogRead(HUMIDITY), 1023, 0, 0, 1023);
  digitalWrite(HUMIDITY_RELAY, HIGH);
  return humidity;
}

void watering(){
  Serial.println("включаем помпу");
  digitalWrite(PUMP_RELAY, LOW);
  delay(2000);
  digitalWrite(PUMP_RELAY, HIGH);
}