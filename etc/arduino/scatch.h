#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WebServer.h>
#include <ArduinoJson.h>

//WiFi
const char* ssid = "kate";
const char* password = "kate_023";
ESP8266WebServer server(80);

//server
const char* serverUrl = "http://192.168.1.34:8080/autowatering";

//pins
//#define GROUND_HUMIDITY_SENSOR A0
#define AIR_CONDITIONS_SENSOR A0
//#define GROUND_HUMIDITY_ELECTRICITY D2
#define PUMP D3
#define WHISTLE D4
#define VAPORIZER D5
#define LIGHTING D6
#define FLOAT D7

//initialize
DHT dht(AIR_CONDITIONS_SENSOR, DHT11);

//----------------SETTINGS--------------
//settings for watering process
struct WateringSettings {
  bool enabled;
  int minHumidity;
  int interval;   //in milliseconds
  int duration; //in seconds
  WateringSettings(): enabled(true),
    minHumidity(200),
    interval(60*60000),
    duration(3*1000){}
};
//settings for air conditions
struct VaporizeSettings {
  bool enabled;
  int minHumidity;
  int interval; //in milliseconds
  VaporizeSettings(): enabled(true),
    minHumidity(50),
    interval(3*60000) {}
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
  int duration; //in milliseconds
  WhistleSettings(): enabled(true),
      duration(200) {}
}
//all settings
struct Settings {
  struct WateringSettings watering;
  struct VaporizeSettings vaporize;
  struct LightingSettings lighting;
  struct WhistleSettings whistle;
  Settings(): watering(WateringSettings())
    vaporize(VaporizeSettings()),
    lighting(LightingSettings()),
    whistle(WhistleSettings()){}
}
struct Settings settings();

//---------------STATE-----------------
//last checked air state
struct AirState {
  int humidity;
  int temperature;
  unsigned long date;
}
//last checked tanker state
struct TankerState {
  bool isFull;
  unsigned long date;
}
//last checked ground state
struct GroundState {
  int humidity;
  unsigned long date;
}
//last checked lighting state
struct LightingState {
  bool status;
  unsigned long date;
}
//last checked vaporize state
struct VaporizeState {
  bool status;
  unsigned long date;
}
//all state
struct State {
  struct AirState air;
  struct TankerState tanker;
  struct GroundState ground;
  struct LightingState lighting;
  struct VaporizeState vaporize;
}
struct State state();

//--------------APPLICATION-----------------
void setup(){
  Serial.begin(115200);
  dht.begin();

  //at first turn off pump
  pinMode(PUMP, OUTPUT);
  digitalWrite(PUMP, HIGH);

  pinMode(WHISTLE, OUTPUT);
  pinMode(VAPORIZER, OUTPUT);
  pinMode(LIGHTING, OUTPUT);
  //pinMode(HUMIDITY_ELECTRICITY, OUTPUT);
  pinMode(FLOAT, INPUT);

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

//--------------WIFI--------------------
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

//------------------INIT SETTINGS-------------------
void loadSettings(){
   Serial.println("loading settings...");
   String result = get("/autowatering/settings/info");
   if(result == "error"){
      Serial.println("loading setting error. Use default values.");
   } else {
      String payload = getPayload(result);
      Serial.println("parsing json: " + String(payload));
      settings = parseSettingsJson(payload);
      Serial.println("settings are loaded successfully");
   }
}

//-----------------REQUEST HANDLERS--------------------
void handleChangeSettings() {
  Serial.println("changing setting request received. Handling...");

  if (server.hasArg("plain") == false){
     server.send(400, "text/plain", "body is empty");
     return;
  }
  settings = deserializeSettings(server.arg("plain"));
  server.send(200, "text/plain", "ok");

  Serial.println("request handled successfully");
  Serial.println("--------------------");
}

void handleGetState(){
  Serial.println("getting state info request received. Handling...");

  if (server.hasArg("plain") == false){
      server.send(400, "text/plain", "body is empty");
      return;
  }
  String state = serializeState();
  server.send(200, "text/plain", state);

  Serial.println("request handled successfully");
  Serial.println("--------------------");
}

//--------------CHECKERS-----------------
//check interval
bool needCheckTanker(){
  //todo choose conditions
  return true;
}
bool needCheckGround(){
  return state.ground.date == 0 || millis() - state.ground.date > settings.ground.interval;
}
bool needCheckAir(){
  return state.vaporize.date == 0 || millis() - state.vaporize.date > settings.vaporize.interval;
}
//check required actions
bool needWhistling(){
  return settings.whistle.enabled && !state.tanker.isFull;
}
bool needWatering(){
  return settings.watering.enabled && tanker.isFull
    && (state.ground.date == 0 || millis() - state.ground.date > settings.watering.interval)
    && state.ground.humidity < settings.watering.minHumidity;
}
bool needLightingOn(){
  return settings.lighting.enabled && !state.lighting.status;
  //todo and check time has come
}
bool needLightingOff(){
  return !settings.light.enabled || state.lighting.status;
  //todo and check time has come
}
bool needVaporizeOn(){
  return settings.vaporize.enabled && state.air.humidity < settings.vaporize.minHumidity
}
bool needVaporizeOff(){
  return !settings.vaporize.enabled || state.air.humidity > settings.vaporize.minHumidity
}

//------------SAVING STATE--------------
void saveGroundState(int humidity){
  state.ground.date = millis();
  state.ground.humidity = humidity;
}
void saveTankerState(bool isFull){
  state.tanker.date = mills();
  state.tanker.isFull = isFull;
}
void saveAirState(int humidity, int temperature){
  state.air.date = mills();
  state.air.humidity = humidity;
  state.air.temperature = temperature;
}

//-----------UTILITY FUNCTIONS----------
String get(String serviceUrl){
   if (WiFi.status() != WL_CONNECTED) {
      Serial.println("WiFi connection error");
      return "error";
   }
   HTTPClient http;
   http.begin(String(serverUrl) + serviceUrl);
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

struct Settings deserializeSettings(String settings){
  StaticJsonDocument<384> doc;
  deserializeJson(doc, payload);

  struct Settings tmp;
  JsonObject watering = doc["watering"];
  tmp.watering.enabled = watering["enabled"]; // false
  tmp.watering.minHumidity = watering["minHumidity"]; // 200
  tmp.watering.interval = watering["interval"]; // 30
  tmp.watering.duration = watering["duration"]; // 2

  JsonObject vaporize = doc["vaporize"];
  tmp.watering.enabled = vaporize["enabled"]; // false
  tmp.watering.minHumidity = vaporize["minHumidity"]; // 50
  tmp.watering.interval = vaporize["interval"]; // 30

  tmp.lighting.enabled = doc["lighting"]["enabled"]; // false

  tmp.whistling.enabled = doc["whistling"]["enabled"]; // false
  tmp.whistling.duration = doc["whistling"]["duration"]; // 2

  return tmp;
}

String serializeState(){
  StaticJsonDocument<256> doc;

  JsonObject air = doc.createNestedObject("air");
  air["humidity"] = state.air.humidity;
  air["temp"] = state.air.temperature;
  air["date"] = state.air.date;

  JsonObject ground = doc.createNestedObject("ground");
  ground["humidity"] = state.ground.humidity;
  ground["date"] = state.ground.date;

  JsonObject tanker = doc.createNestedObject("tanker");
  tanker["full"] = state.tanker.isFull;
  tanker["date"] = state.tanker.date;

  JsonObject light = doc.createNestedObject("light");
  light["status"] = state.lighting.status;
  light["date"] = state.lighting.date;

  JsonObject vaporizer = doc.createNestedObject("vaporizer");
  vaporizer["status"] = state.vaporizer.status;
  vaporizer["date"] = state.vaporizer.date;

  serializeJson(doc, output);
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
  delay(settings.ground.duration);
  Serial.println("turning pump off...");
  digitalWrite(PUMP, HIGH);
  Serial.println("pump is turned of");
}

void whistling(){
  digitalWrite(WHISTLE, HIGH);
  delay(setting.whistle.duration);
  digitalWrite(WHISTLE, LOW);
}

void lightingOn(){
  Serial.println("turning lighting on...");
  digitalWrite(LIGHTING, HIGH);
  Serial.println("lighting is turned on");
}

void lightingOff(){
  Serial.println("turning lighting off...");
  digitalWrite(LIGHTING, LOW);
  Serial.println("lighting is turned off");
}

void vaporizeOn(){
  Serial.println("turning vaporize on...");
  digitalWrite(VAPORIZER, HIGH)
  Serial.println("vaporize is turned on");
}

void vaporizeOff(){
  Serial.println("turning vaporize off...");
  digitalWrite(VAPORIZER, LOW)
  Serial.println("vaporize is turned off");
}