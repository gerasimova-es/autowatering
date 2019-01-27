#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>

// WiFi Parameters
const char* ssid = "kate";
const char* password = "kate_023";

// контакт подключения аналогового выхода датчика влажности
#define HUMIDITY A0
// значение минимально допустимой сухости почвы
#define MIN_HUMIDITY 300
// интервал между проверкой на полив растения (10 минут)
#define INTERVAL 60000 * 10
// Реле модуль подключен к цифровому выводу 4
#define RELAY D4
// Поплавок подключен к цифровому входу
#define FLOAT D7
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
  pinMode(RELAY, OUTPUT);
  pinMode(FLOAT, INPUT);
}

void loop(){
     int humidity = map(analogRead(HUMIDITY), 1023, 0, 0, 1023);
     Serial.print("humidity=" + String(humidity));
     sendPotState(humidity);

     boolean floatDown = digitalRead(FLOAT);
     Serial.print("float is down=" + String(floatDown));
     sendTnakState();

  //если прошел заданный интервал времени
  if(timeExpired()){
     // если поплавок плавает и значения датчика влажности меньше допустимой границы
     if (!floatDown && humidity < MIN_HUMIDITY ) {
        watering();
     }
     //сохраняем текущее время проверки
     waitTime = millis();
  }
  delay(10000);
}

boolean timeExpired(){
  return waitTime == 0 || millis() - waitTime > INTERVAL;
}

void sendPotState(int humidity){
  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("sending request...");
    HTTPClient http;
    http.begin("http://192.168.1.35:8080/autowatering/pot/state/save");
    http.addHeader("Content-Type", "application/json");
    String body = String("{\"potCode\": \"ANTHURIUM\", \"humidity\": ") + String(humidity) + String(".0 }");
    int httpCode = http.POST(body);
    Serial.print("request status: " + String(httpCode));
    if (httpCode > 0) {
      // Get the response pay
      String payload = http.getString();
      Serial.print("payload=" + String(payload));
    }
    http.end();
  } else {
      Serial.println("error in WiFi connection");
  }
}

void sendTnakState(){
}

void watering(){
  Serial.println("включаем помпу");
  digitalWrite(RELAY, HIGH);
  delay(2000);
  digitalWrite(RELAY, LOW);
}
