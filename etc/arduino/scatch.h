#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WebServer.h>
#include <ArduinoJson.h>

//WiFi
const char* ssid = "kate";
const char* password = "kate_023";
ESP8266WebServer server(80);

//server
const char* url = "http://192.168.1.34:8080/autowatering";

//pins
//#define GROUND_HUMIDITY_SENSOR A0
#define AIR_CONDITIONS_SENSOR A0
//#define GROUND_HUMIDITY_ELECTRICITY D2
#define WHISTLE D3
#define PUMP D4
//todo #define HUMIDIFIER D5
//todo #define LIGHTING D6
#define FLOAT D7

//initialize
DHT dht(AIR_CONDITIONS_SENSOR, DHT11);

//----------------SETTINGS--------------
//settings for watering process
struct WateringSettings {
  bool enabled;
  int minHumidity;
  int checkInterval;   //in milliseconds
  int wateringDuration; //in seconds
  WateringSettings(): enabled(true),
    minHumidity(200),
    checkInterval(60*60000),
    wateringDuration(3*1000){}
};
//settings for air conditions
struct VaporizeSettings {
  bool enabled;
  int minHumidity;
  int checkInterval; //in milliseconds
  VaporizeSettings(): enabled(true),
    minHumidity(50),
    checkInterval(3*60000) {}
}
//settings for lighting
struct LightingSettings {
  bool enabled;
  //todo startTime;
  //todo stopType;
  LightingSettings(): enabled(true){}
}
//settings for whistling
struct WhistleSettings {
  bool enabled;
  int whistleDuration; //in milliseconds
  WhistleSettings(): enabled(true),
      whistleDuration(200) {}
}
//all settings
struct Settings {
  struct WateringSettings wateringSettings;
  struct VaporizeSettings vaporizeSettings;
  struct LightingSettings lightingSettings;
  struct WhistleSettings whistleSettings;
  Settings(): wateringSettings(WateringSettings())
    vaporizeSettings(VaporizeSettings()),
    lightingSettings(LightingSettings()),
    whistleSettings(WhistleSettings()){}
}
struct Settings settings();

//---------------STATE-----------------
//last checked air state
struct AirState {
  int humidity;
  int temperature;
  unsigned long lastCheckTime;
}
//last checked tanker state
struct TankerState {
  bool isFull;
  unsigned long lastCheckTime;
}
//last checked ground state
struct GroundState {
  int humidity;
  unsigned long lastCheckTime;
}
//last checked lighting state
struct LightingState {
  bool lightingStatus;
  unsigned long lastChangeDate;
}
//last checked vaporize state
struct VaporizeState {
  bool vaporizeStatus;
  unsigned long lastChangeDate;
}
//all state
struct State {
  struct AirState airState;
  struct TankerState tankerState;
  struct GroundState groundState;
  struct LightingState lightingState;
  struct VaporizeState vaporizeState;
}
struct State state();

//--------------APPLICATION-----------------
void setup(){
  Serial.begin(115200);
  dht.begin();

  //at first turn off pump
  pinMode(PUMP, OUTPUT);
  digitalWrite(PUMP, HIGH);

  //set mode for digital pins
  //pinMode(HUMIDITY_ELECTRICITY, OUTPUT);
  pinMode(FLOAT, INPUT);
  pinMode(WHISTLE, OUTPUT);

  wifiConnect();
  Serial.println("--------------------");

  wifiServerStart();
  Serial.println("--------------------");

  loadSettings();
  Serial.println("--------------------");
}

void loop(){
  server.handleClient();

  if(needCheckTanker()){
    boolean tankIsFull = getTankIsFull();
    saveTankerState(tankIsFull);
    Serial.println("--------------------");
  }

  if(needCheckGround()){
    int currentHumidity = getHumidity();
    saveGroundState(currentHumidity);
    Serial.println("--------------------");
    if(needWatering()){
      watering();
      Serial.println("--------------------");
    }
  }

  if(needCheckAir()){
    AirState currentState = getAirState();
    saveAirState(currentState.humidity, currentState.temperature)
    Serial.println("--------------------");
    if(needVaporizeOn()){
      vaporizeOn();
      Serial.println("--------------------");
    } else if(needVaporizeOff()){
      vaporizeOff();
      Serial.println("--------------------");
    }
  }

  if(needWhistling()){
    whistling();
    Serial.println("--------------------");
  }

  if(needLightingOn()){
    lightingOn();
    Serial.println("--------------------");
  } else if(needLightingOff()){
    lightingOff();
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
  server.on("/settings/change", handleChangeSettings);
  server.on("/state/info", handleGetState);
  server.begin();
  Serial.println("server listening started");
}

void loadSettings(){
   Serial.println("loading settings...");
   String result = get("/autowatering/settings/info");
   if(result == "error"){
      Serial.println("Loading setting error. Use default values.");
   } else {
      String payload = getPayload(result);
      settings = parseSettingsJson(getPayload(result));
      Serial.println("Settings are loaded successfully");
   }
}

void handleChangeSettings() {
  Serial.println("Changing setting request received. Handling...");
  if (server.hasArg("plain") == false){
     server.send(200, "text/plain", "{ \"status\": \"ERROR\", \"message\": \"body is empty\"}");
     return;
  }
  settings = parseSettingsJson(server.arg("plain"));
  //todo check errors
  Serial.println("Request handled successfully.");
  Serial.println("--------------------");
}

void handleGetState(){
  Serial.println("Getting state info request received. Handling...");
  if (server.hasArg("plain") == false){
      server.send(200, "text/plain", "{ \"status\": \"ERROR\", \"message\": \"body is empty\"}");
      return;
  }
  String info = serializeStateInfo();
  //todo check errors
  Serial.println("Request handled successfully.");
  Serial.println("--------------------");
}

String serializeStateInfo(){
  //todo
  return "state";
}

//--------------CHECKERS-----------------
//check interval
bool needCheckTanker(){
  //todo choose conditions
  return true;
}
bool needCheckGround(){
  return state.groundState.lastCheckTime == 0 || millis() - state.groundState.lastCheckTime > settings.groundSettings.checkInterval;
}
bool needCheckAir(){
  return state.vaporizeState.lastCheckTime == 0 || millis() - state.vaporizeState.lastCheckTime > settings.vaporizeSetting.checkInterval;
}
//check required actions
bool needWhistling(){
  return settings.whistleSettings.enabled && !state.tankerState.isFull;
}
bool needWatering(){
  return settings.wateringSettings.enabled && tankerState.isFull
    && (state.groundState.lastCheckTime == 0 || millis() - state.groundState.lastCheckTime > settings.wateringSettings.checkInterval)
    && state.groundState.humidity < settings.wateringSettings.minHumidity;
}
bool needLightingOn(){
  return settings.lightSettings.enabled && !state.lightingState.lightingStatus;
  //todo and check time has come
}
bool needLightingOff(){
  return !settings.lightSettings.enabled || state.lightingState.lightingStatus;
  //todo and check time has come
}
bool needVaporizeOn(){
  return settings.vaporizeSettings.enabled && state.airState.humidity < settings.vaporizeSettings.minHumidity
}
bool needVaporizeOff(){
  return !settings.vaporizeSettings.enabled || state.airState.humidity > settings.vaporizeSettings.minHumidity
}

//------------SAVING STATE--------------
void saveGroundState(int humidity){
  state.groundState.lastCheckTime = millis();
  state.groundState.humidity = humidity;
}
void saveTankerState(bool isFull){
  state.tankerState.lastCheckTime = mills();
  state.tankerState.isFull = isFull;
}
void saveAirState(int humidity, int temperature){
  state.airState.lastCheckTime = mills();
  state.airState.humidity = humidity;
  state.airState.temperature = temperature;
}

//-----------UTILITY FUNCTIONS----------
String get(String service){
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

String getPayload(String json){
  const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_OBJECT_SIZE(7) + 190;
  DynamicJsonBuffer jsonBuffer(capacity);
  JsonObject& root = jsonBuffer.parseObject(json);
  return root["payload"];
}

struct Settings parseSettingsJson(String json){
  const size_t capacity = JSON_OBJECT_SIZE(7) + 120;
  DynamicJsonBuffer jsonBuffer(capacity);
  JsonObject& root = jsonBuffer.parseObject(json);

  struct Settings tmp;
  //todo
  tmp.code = root["code"];
  tmp.minHumidity = root["minHumidity"];

  tmp.checkInterval = root["checkInterval"];
  tmp.checkInterval = tmp.checkInterval * 60000;

  tmp.wateringDuration = root["wateringDuration"];
  tmp.wateringDuration = tmp.wateringDuration * 1000;

  Serial.println("Pot " + String(tmp.code) + ": min humidity " + String(tmp.minHumidity) + ", check interval = " + String(tmp.checkInterval) + ", watering duration = " + String(tmp.wateringDuration));
  return tmp;
}

//-----------SENSOR FUNCTIONS--------------
boolean getTankerIsFull(){
  return !digitalRead(FLOAT);
}

int getGroundHumidity(){
  Serial.println("turning humidity sensor on...");
  //todo return when need
//  digitalWrite(GROUND_HUMIDITY_ELECTRICITY, LOW);
//  delay(1000);
//  int humidity = map(analogRead(GROUND_HUMIDITY_SENSOR), 1023, 0, 0, 1023);
//  Serial.println("turning humidity sensor off...");
//  digitalWrite(GROUND_HUMIDITY_ELECTRICITY, HIGH);
  Serial.println("humidity sensor turned off");
  return humidity;
}

AirState getAirState(){
  float humidity = dht.readHumidity();
  float temperature = dht.readTemperature();
  if (isnan(humidity) || isnan(temperature)) {
    Serial.println("error during reading air humidity and temperature");
    return AirState(-1, -1);
  }
  return AirState(humidity, temperature);
}

void watering(){
  Serial.println("turning pump on...");
  digitalWrite(PUMP, LOW);
  delay(groundSettings.wateringDuration);
  Serial.println("turning pump off...");
  digitalWrite(PUMP, HIGH);
  Serial.println("pump is turned of");
}

void whistling(){
  digitalWrite(WHISTLE, HIGH);
  delay(setting.whistleSettings.whistleDuration);
  digitalWrite(WHISTLE, LOW);
}

void lightingOn(){
  Serial.println("turning lighting on...");
  //todo
  Serial.println("lighting is turned on");
}

void lightingOff(){
  Serial.println("turning lighting off...");
  //todo
  Serial.println("lighting is turned off");
}

void vaporizeOn(){
  Serial.println("turning vaporize on...");
  //todo
  Serial.println("vaporize is turned on");
}

void vaporizeOff(){
  Serial.println("turning vaporize off...");
  //todo
  Serial.println("vaporize is turned off");
}