#include <ESP8266WiFi.h>
#include <ESP8266mDNS.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WebServer.h>
#include <WiFiUdp.h>
#include <ArduinoOTA.h>
#include <ArduinoJson.h>
#include <DHT.h>
#include <ThreeWire.h>
#include <RtcDateTime.h>
#include <RtcDS1302.h>

//WiFi
const char* ssid = "kate";
const char* password = "kate_023";

//arduino web-server on 80 port:
ESP8266WebServer server(80);

//autowatering web-server url
const char* serverUrl = "http://192.168.1.34:8080/autowatering";

//pins
#define GROUND_HUMIDITY_SENSOR A0
#define PUMP D2
#define AIR_SENSOR D3
#define WHISTLE D4
#define VAPORIZER D5
#define LIGHTING D6
#define FLOAT D7
#define GROUND_HUMIDITY_ELECTRICITY D8

//initialize sensor
DHT dht(AIR_SENSOR, DHT11);

//work with datetime
#define countof(a) (sizeof(a) / sizeof(a[0]))
ThreeWire myWire(D9,D8,D10); // IO, SCLK, CE
RtcDS1302<ThreeWire> Rtc(myWire);
RtcDateTime now;
unsigned long lastCheckTime;

//inverter relay sign
#define RELAY_INVERTED true
#if defined(RELAY_INVERTED)
#define RELAY_OPEN LOW
#define RELAY_CLOSE HIGH
#else
#define RELAY_OPEN HIGH
#define RELAY_CLOSE LOW
#endif

//----------------SETTINGS--------------
//settings for watering process
struct WateringSettings {
  bool enabled;
  int minHumidity;
  int interval;   //in milliseconds
  int duration; //in milliseconds
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
    interval(5*60000) {}
};
//settings for lighting
struct LightingSettings {
  bool enabled;
  int startTime; //hour of day
  int stopTime; //hour of day
  LightingSettings(): enabled(true),
  startTime(7),
  stopTime(23){}
};
//settings for whistling
struct WhistleSettings {
  bool enabled;
  int duration; //in milliseconds
  WhistleSettings(): enabled(true),
      duration(200) {}
};
//all settings
struct Settings {
  struct WateringSettings watering;
  struct VaporizeSettings vaporize;
  struct LightingSettings lighting;
  struct WhistleSettings whistling;
  Settings(): watering(WateringSettings()),
    vaporize(VaporizeSettings()),
    lighting(LightingSettings()),
    whistling(WhistleSettings()){}
};
struct Settings settings;

//---------------STATE-----------------
//last checked air state
struct AirState {
  int humidity;
  int temperature;
  bool needWater;
  unsigned long lastCheck;
  RtcDateTime date;
};
//last checked tanker state
struct TankerState {
  bool isFull;
  unsigned long lastCheck;
  RtcDateTime date;
};
//last checked ground state
struct GroundState {
  int humidity;
  bool needWater;
  unsigned long lastCheck;
  RtcDateTime date;
};
//last checked lighting state
struct LightingState {
  bool turnedOn;
  unsigned long lastCheck;
  RtcDateTime date;
};
//last checked vaporize state
struct VaporizerState {
  bool turnedOn;
  unsigned long lastCheck;
  RtcDateTime date;
};
//all state
struct State {
  struct AirState air;
  struct TankerState tanker;
  struct GroundState ground;
  struct LightingState lighting;
  struct VaporizerState vaporizer;
};
struct State state;

//--------------SETUP-----------------
void setup(){
  Serial.begin(115200);

  pinMode(PUMP, OUTPUT);
  pinMode(WHISTLE, OUTPUT);
  pinMode(VAPORIZER, OUTPUT);
  pinMode(LIGHTING, OUTPUT);
  pinMode(GROUND_HUMIDITY_ELECTRICITY, OUTPUT);
  pinMode(FLOAT, INPUT);

  digitalWrite(PUMP, RELAY_CLOSE);

  dht.begin();

  wifiConnect();
  initOTA();
  wifiServerStart();
  initDateTime();
  loadSettings();
}

//--------------wifi--------------------
void wifiConnect(){
  Serial.println("WIFI: connection to wifi");
  WiFi.mode(WIFI_STA);
  WiFi.setSleepMode(WIFI_NONE_SLEEP);
  WiFi.begin(ssid, password);
  while (WiFi.waitForConnectResult() != WL_CONNECTED) {
    Serial.println("WIFI: Connection Failed! Rebooting...");
    delay(5000);
    ESP.restart();
  }
  Serial.println("WIFI: connected to wifi");
}

void wifiServerStart(){
  server.on("/settings/change", handleChangeSettings);
  server.on("/state/info", handleGetState);
  server.begin();
  Serial.println("WIFI: server listening started");
}

//------------------ota-----------------------------
void initOTA(){
  // Port defaults to 8266
  // ArduinoOTA.setPort(8266);

  // Hostname defaults to esp8266-[ChipID]
  ArduinoOTA.setHostname("board-8266");

  // No authentication by default
  ArduinoOTA.setPassword((const char *)"kate_023");

  ArduinoOTA.onStart([]() {
    String type;
    if (ArduinoOTA.getCommand() == U_FLASH) {
      type = "sketch";
    } else { // U_FS
      type = "filesystem";
    }
    // NOTE: if updating FS this would be the place to unmount FS using FS.end()
    Serial.println("Start updating " + type);
  });
  ArduinoOTA.onEnd([]() {
    Serial.println("\nEnd");
  });
  ArduinoOTA.onProgress([](unsigned int progress, unsigned int total) {
    Serial.printf("Progress: %u%%\r", (progress / (total / 100)));
  });
  ArduinoOTA.onError([](ota_error_t error) {
    Serial.printf("Error[%u]: ", error);
    if (error == OTA_AUTH_ERROR) {
      Serial.println("Auth Failed");
    } else if (error == OTA_BEGIN_ERROR) {
      Serial.println("Begin Failed");
    } else if (error == OTA_CONNECT_ERROR) {
      Serial.println("Connect Failed");
    } else if (error == OTA_RECEIVE_ERROR) {
      Serial.println("Receive Failed");
    } else if (error == OTA_END_ERROR) {
      Serial.println("End Failed");
    }
  });
  ArduinoOTA.begin();
  Serial.println("Ready");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}

//------------------settings-------------------
void loadSettings(){
   Serial.println("loading settings...");
   String result = get("/autowatering/settings/info");
   if(result == "error"){
      Serial.println("loading setting error. Use default values.");
   } else {
      Serial.println("parsing json: " + String(result));
      settings = deserializeSettings(result);
      Serial.println("settings are loaded successfully");
   }
}

//----------------datetime--------------------
void initDateTime(){
  Serial.print("SENSOR: RTC compiled: ");
  Serial.print(__DATE__);
  Serial.println(__TIME__);

  Rtc.Begin();

  RtcDateTime compiled = RtcDateTime(__DATE__, __TIME__);
  Serial.println(prettyDateTime(compiled));

  if (!Rtc.IsDateTimeValid()){
    Serial.println("SENSOR: RTC INIT: lost confidence in the DateTime!");
    Rtc.SetDateTime(compiled);
  }

  if (Rtc.GetIsWriteProtected()){
    Serial.println("SENSOR: RTC was write protected, enabling writing now");
    Rtc.SetIsWriteProtected(false);
  }

  if (!Rtc.GetIsRunning()){
    Serial.println("RTC was not actively running, starting now");
    Rtc.SetIsRunning(true);
  }

  RtcDateTime now = Rtc.GetDateTime();
  if (now < compiled){
    Serial.println("SENSOR: RTC is older than compile time! (updating DateTime)");
    Rtc.SetDateTime(compiled);
  } else if (now > compiled) {
    Serial.println("SENSOR: RTC is newer than compile time. (this is expected)");
  } else if (now == compiled) {
    Serial.println("SENSOR: RTC is the same as compile time! (not expected but all is fine)");
  }
  lastCheckTime = millis();
}

//------------------LOOP-------------------
void loop(){
  ArduinoOTA.handle();
  server.handleClient();

  if(needCheckTanker()){
    boolean tankerIsFull = getTankerIsFull();
    saveTankerState(tankerIsFull);
    Serial.println("--------------------");
  }

  if(needCheckGround()){
    int humidity = getGroundHumidity();
    Serial.println("--------------------");
    bool needWater = needWatering();
    Serial.println("--------------------");
    if(needWater){
      watering();
      Serial.println("--------------------");
    }
    saveGroundState(humidity, needWatering);
    Serial.println("--------------------");
  }

  if(needCheckAir()){
    float humidity = getAirHumidity();
    float temperature = getAirTemperature();
    saveAirState(humidity, temperature);
    Serial.println("--------------------");
    if(needVaporizeOn()){
      vaporizeOn();
      Serial.println("--------------------");
      saveVaporizerState(true);
      Serial.println("--------------------");
    } else if(needVaporizeOff()){
      vaporizeOff();
      Serial.println("--------------------");
      saveVaporizerState(false);
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
    saveLightingState(true);
    Serial.println("--------------------");
  } else if(needLightingOff()){
    lightingOff();
    Serial.println("--------------------");
    saveLightingState(false);
    Serial.println("--------------------");
  }
  delay(5000);
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
  return state.ground.lastCheck == 0 || millis() - state.ground.lastCheck > settings.watering.interval;
}
bool needCheckAir(){
  return state.vaporizer.lastCheck == 0 || millis() - state.vaporizer.lastCheck > settings.vaporize.interval;
}
//check required actions
bool needWhistling(){
  return settings.whistling.enabled && !state.tanker.isFull;
}
bool needWatering(){
  /*
  Serial.println("settings.watering.enabled=" + String(settings.watering.enabled));
  Serial.println("state.tanker.isFull=" + String(state.tanker.isFull));

  Serial.println("millis()=" + millis());
  Serial.println("state.ground.lastCheck=" + String(state.ground.lastCheck));
  Serial.println("settings.watering.interval=" + String(settings.watering.interval));

  Serial.println("settings.watering.minHumidity=" + String(settings.watering.minHumidity));
  Serial.println("state.ground.humidity=" + String(state.ground.humidity));

  Serial.println("-----------------------");
  */
  return settings.watering.enabled && state.tanker.isFull
    && (state.ground.lastCheck == 0 || millis() - state.ground.lastCheck > settings.watering.interval)
    && (state.ground.humidity < settings.watering.minHumidity);
}
bool needLightingOn(){
  Serial.println("need lighting on");
  Serial.println("settings.lighting.enabled=" + String(settings.lighting.enabled));
  Serial.println("state.lighting.turnedOn=" + String(state.lighting.turnedOn));

  Serial.println("settings.lighting.startTime=" + String(settings.lighting.startTime));
  Serial.println("settings.lighting.stopTime=" + String(settings.lighting.stopTime));
  Serial.println("getDateTime().Hour()=" + String(getDateTime().Hour()));
  Serial.println("-----------------------");

  bool turnOn = !state.lighting.turnedOn &&
    (settings.lighting.enabled &&
     getDateTime().Hour() > settings.lighting.startTime &&
     getDateTime().Hour() < settings.lighting.stopTime);
  Serial.println("lighting.turnOn = " + String(turnOn));
  return turnOn;
}
bool needLightingOff(){
  /*
  Serial.println("need lighting off");
  Serial.println("settings.lighting.enabled=" + String(settings.lighting.enabled));
  Serial.println("state.lighting.turnedOn=" + String(state.lighting.turnedOn));

  Serial.println("settings.lighting.startTime=" + String(settings.lighting.startTime));
  Serial.println("settings.lighting.stopTime=" + String(settings.lighting.stopTime));
  Serial.println("getDateTime().Hour()=" + String(getDateTime().Hour()));
  Serial.println("-----------------------");
  */

  return state.lighting.turnedOn &&
    (!settings.lighting.enabled ||
      getDateTime().Hour() < settings.lighting.startTime ||
      getDateTime().Hour() > settings.lighting.stopTime);
}
bool needVaporizeOn(){
  return settings.vaporize.enabled && state.air.humidity < settings.vaporize.minHumidity;
}
bool needVaporizeOff(){
  return !settings.vaporize.enabled || state.air.humidity > settings.vaporize.minHumidity;
}

//------------SAVING STATE--------------
void saveGroundState(int humidity, bool needWatering){
  state.ground.lastCheck = millis();
  state.ground.date = getDateTime();
  state.ground.humidity = humidity;
  state.ground.needWater = needWatering;
}
void saveTankerState(bool isFull){
  state.tanker.lastCheck = millis();
  state.tanker.date = getDateTime();
  state.tanker.isFull = isFull;
}
void saveAirState(int humidity, int temperature){
  state.air.lastCheck = millis();
  state.air.date = getDateTime();
  state.air.humidity = humidity;
  state.air.temperature = temperature;
}
void saveLightingState(bool lightingStatus){
  Serial.println("saving lighting state");
  state.lighting.turnedOn = lightingStatus;
  state.lighting.date = getDateTime();
  Serial.println("state.lighting.turnedOn=" + String(state.lighting.turnedOn));
}
void saveVaporizerState(bool vaporizerStatus){
  state.vaporizer.turnedOn = vaporizerStatus;
  state.vaporizer.date = getDateTime();
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
  StaticJsonDocument<256> doc;
  deserializeJson(doc, settings);

  struct Settings tmp;
  JsonObject watering = doc["watering"];
  tmp.watering.enabled = watering["enabled"]; // false
  tmp.watering.minHumidity = watering["minHumidity"]; // 200
  tmp.watering.interval = watering["interval"]; // 30
  tmp.watering.interval = tmp.watering.interval * 60000;
  tmp.watering.duration = watering["duration"]; // 2
  tmp.watering.duration = tmp.watering.duration * 1000;

  JsonObject vaporize = doc["vaporize"];
  tmp.watering.enabled = vaporize["enabled"]; // false
  tmp.watering.minHumidity = vaporize["minHumidity"]; // 50
  tmp.watering.interval = vaporize["interval"]; // 30

  tmp.lighting.enabled = doc["lighting"]["enabled"]; // false
  tmp.lighting.startTime = doc["lighting"]["startTime"]; //7
  tmp.lighting.stopTime = doc["lighting"]["stopTime"]; //23

  tmp.whistling.enabled = doc["whistling"]["enabled"]; // false
  tmp.whistling.duration = doc["whistling"]["duration"]; // 2

  return tmp;
}

String serializeState(){
  StaticJsonDocument<256> doc;

  JsonObject air = doc.createNestedObject("air");
  air["humidity"] = state.air.humidity;
  air["temp"] = state.air.temperature;
  air["needWater"] = state.air.needWater;
  air["date"] = prettyDateTime(state.air.date);

  JsonObject ground = doc.createNestedObject("ground");
  ground["humidity"] = state.ground.humidity;
  ground["needWater"] = state.ground.needWater;
  ground["date"] = prettyDateTime(state.ground.date);

  JsonObject tanker = doc.createNestedObject("tanker");
  tanker["full"] = state.tanker.isFull;
  tanker["date"] = prettyDateTime(state.tanker.date);

  JsonObject light = doc.createNestedObject("light");
  light["status"] = state.lighting.turnedOn;
  light["date"] = prettyDateTime(state.lighting.date);

  JsonObject vaporizer = doc.createNestedObject("vaporizer");
  vaporizer["status"] = state.vaporizer.turnedOn;
  vaporizer["date"] = prettyDateTime(state.vaporizer.date);

  String output;
  serializeJson(doc, output);
  return output;
}

String prettyDateTime(const RtcDateTime& dt){
  return String(dt.Day()) + "." + String(dt.Month()) + "." + String(dt.Year()) + "T" + String(dt.Hour()) + ":" + String(dt.Minute()) + ":" + String(dt.Second());
}

//-----------SENSOR FUNCTIONS--------------
boolean getTankerIsFull(){
  //Serial.println("getting tanker state...");
  boolean tankerIsFull = !digitalRead(FLOAT);
  //Serial.println("SENSOR: tanker is full =" + String(tankerIsFull));
  //Serial.println("tanker state checked successfully");
  return tankerIsFull;
}

int getGroundHumidity(){
  //Serial.println("getting ground state...");
  digitalWrite(GROUND_HUMIDITY_ELECTRICITY, LOW);
  delay(500);

  int humidity = map(analogRead(GROUND_HUMIDITY_SENSOR), 1023, 0, 0, 1023);
  //Serial.println("SENSOR: ground humidity=" + String(humidity));

  digitalWrite(GROUND_HUMIDITY_ELECTRICITY, HIGH);
  //Serial.println("ground state checked successfully");
  return humidity;
}

float getAirHumidity(){
  //Serial.println("getting air humidity...");
  float value =  dht.readHumidity();
  if(isnan(value)){
    //Serial.println("SENSOR: error during reading air humidity");
    value = -1;
  } else {
    //Serial.println("SENSOR: air humidity " + String(value) + " %");
  }
  return value;
}

float getAirTemperature(){
  //Serial.println("getting air temperature...");
  float value =  dht.readTemperature();
  if(isnan(value)){
    //Serial.println("SENSOR: error during reading air temperature");
    value = -1;
  } else {
    //Serial.println("SENSOR: air temperature " + String(value) + " C");
  }
  return value;
}

void watering(){
  //Serial.println("turning pump on...");
  digitalWrite(PUMP, RELAY_OPEN);

  delay(settings.watering.duration);
  //Serial.println("turning pump off...");

  digitalWrite(PUMP, RELAY_CLOSE);
  //Serial.println("pump is turned of");
}

void whistling(){
  //Serial.println("whistling...");
  digitalWrite(WHISTLE, HIGH);
  delay(settings.whistling.duration);
  digitalWrite(WHISTLE, LOW);
  //Serial.println("whistled...");
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
  //Serial.println("turning vaporize on...");
  digitalWrite(VAPORIZER, HIGH);
  //Serial.println("vaporize is turned on");
}

void vaporizeOff(){
  //Serial.println("turning vaporize off...");
  digitalWrite(VAPORIZER, LOW);
  //Serial.println("vaporize is turned off");
}

RtcDateTime getDateTime(){
  if(millis() - lastCheckTime > 1000){
     now = Rtc.GetDateTime();
     lastCheckTime = millis();
     if (!now.IsValid()){
       Serial.println("SENSOR: RTC CHECK: lost confidence in the DateTime!");
     }
  }
  return now;
}