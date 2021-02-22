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
#include <Wire.h>
//#include <LiquidCrystal_I2C.h>

//WiFi
const char* ssid = "kate";
const char* password = "kate_023";

//arduino web-server on 80 port:
ESP8266WebServer server(80);
const char* hostName = "board-8266";

//autowatering web-server url
const char* serverUrl = "http://192.168.1.34:8080/autowatering";

//pins
#define GROUND_HUMIDITY_SENSOR A0
#define VAPORIZER D2
#define AIR_SENSOR D3
#define WHISTLE D4
#define PUMP D5
#define LIGHTING D6
#define FLOAT D7
#define GROUND_HUMIDITY_ELECTRICITY D8

//initialize sensor
DHT dht(AIR_SENSOR, DHT11);
//LiquidCrystal_I2C lcd(0x27,2,1,0,4,5,6,7);

//work with datetime
#define countof(a) (sizeof(a) / sizeof(a[0]))
ThreeWire myWire(D9,D8,D10); // IO, SCLK, CE
RtcDS1302<ThreeWire> Rtc(myWire);
RtcDateTime now;
unsigned long lastCheckTime;

//inverter relay sign
#define PUMP_RELAY_OPEN HIGH
#define PUMP_RELAY_CLOSE LOW

#define LIGHT_RELAY_OPEN LOW
#define LIGHT_RELAY_CLOSE HIGH

#define VAPORIZE_RELAY_OPEN LOW
#define VAPORIZE_RELAY_CLOSE HIGH

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
  int startHour; //hour of day
  int startMinute;
  int stopHour; //hour of day
  int stopMinute;
  LightingSettings(): enabled(true),
    startHour(8),
    startMinute(30),
    stopHour(23),
    stopMinute(30){}
};
//settings for whistling
struct WhistleSettings {
  bool enabled;
  int duration; //in milliseconds
  int startHour; //hour of day
  int startMinute;
  int stopHour; //hour of day
  int stopMinute;
  WhistleSettings(): enabled(true),
    duration(200),
    startHour(10),
    startMinute(0),
    stopHour(22),
    stopMinute(47){}
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

  digitalWrite(PUMP, PUMP_RELAY_CLOSE);
  digitalWrite(LIGHTING, LIGHT_RELAY_CLOSE);
  digitalWrite(VAPORIZER, VAPORIZE_RELAY_CLOSE);

  dht.begin();

  //initLcd();
  wifiConnect();
  initOTA();
  wifiServerStart();
  initDateTime();
  loadSettings();
}

//-----------------lcd---------------------
/*void initLcd(){
  lcd.begin(16,2); // for 16 x 2 LCD module
  lcd.setBacklightPin(3, POSITIVE);
  lcd.setBacklight(HIGH);

  lcd.home();  // go home
  lcd.print("Initializing...");
  Serial.println("lcd initialized");
}*/

//--------------wifi--------------------
void wifiConnect(){
  WiFi.mode(WIFI_STA);
  WiFi.setSleepMode(WIFI_NONE_SLEEP);
  WiFi.begin(ssid, password);
  while (WiFi.waitForConnectResult() != WL_CONNECTED) {
    delay(5000);
    ESP.restart();
  }
}

void wifiServerStart(){
  server.on("/settings/change", handleChangeSettings);
  server.on("/state/info", handleGetState);
  server.on("/watering/force", handleForceWatering);
  server.begin();
  Serial.println("WEB SERVER: server listening started");
}

//------------------ota-----------------------------
void initOTA(){
  // Port defaults to 8266
  // ArduinoOTA.setPort(8266);

  // Hostname defaults to esp8266-[ChipID]
  ArduinoOTA.setHostname(hostName);

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
    Serial.println("OTA: Start updating " + type);
  });
  ArduinoOTA.onEnd([]() {
    Serial.println("\nEnd");
  });
  ArduinoOTA.onProgress([](unsigned int progress, unsigned int total) {
    Serial.printf("OTA: Progress: %u%%\r", (progress / (total / 100)));
  });
  ArduinoOTA.onError([](ota_error_t error) {
    Serial.printf("OTA: Error[%u]: ", error);
    if (error == OTA_AUTH_ERROR) {
      Serial.println("OTA: Auth Failed");
    } else if (error == OTA_BEGIN_ERROR) {
      Serial.println("OTA: Begin Failed");
    } else if (error == OTA_CONNECT_ERROR) {
      Serial.println("OTA: Connect Failed");
    } else if (error == OTA_RECEIVE_ERROR) {
      Serial.println("OTA: Receive Failed");
    } else if (error == OTA_END_ERROR) {
      Serial.println("End Failed");
    }
  });
  ArduinoOTA.begin();
  Serial.println("OTA: Ready");
  Serial.print("WIFI: IP address: ");
  Serial.println(WiFi.localIP());
}

//------------------settings-------------------
void loadSettings(){
   Serial.println("loading settings...");
   String result = getRequest("/settings/info");
   if(result == "error"){
      Serial.println("loading setting error. Using default values.");
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
    Serial.println("SENSOR: RTC was not actively running, starting now");
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
    //Serial.println("--------------------");
  }

  if(needCheckGround()){
    int humidity = getGroundHumidity();
    saveGroundState(humidity);
    //Serial.println("--------------------");
    if(needWatering()){
      watering();
      //Serial.println("--------------------");
    }

    //Serial.println("--------------------");
  }

  if(needCheckAir()){
    float humidity = getAirHumidity();
    float temperature = getAirTemperature();
    saveAirState(humidity, temperature);
    //Serial.println("--------------------");
    if(needVaporizeOn()){
      vaporizeOn();
      //Serial.println("--------------------");
      saveVaporizerState(true);
      //Serial.println("--------------------");
    } else if(needVaporizeOff()){
      vaporizeOff();
      //Serial.println("--------------------");
      saveVaporizerState(false);
      //Serial.println("--------------------");
    }
  }

  if(needWhistling()){
    whistling();
    //Serial.println("--------------------");
  }

  if(needLightingOn()){
    lightingOn();
    //Serial.println("--------------------");
      saveLightingState(true);
    // Serial.println("--------------------");
  } else if(needLightingOff()){
    lightingOff();
   // Serial.println("--------------------");
     saveLightingState(false);
    // Serial.println("--------------------");
  }

  delay(2000);
}

//-----------------REQUEST HANDLERS--------------------
void handleChangeSettings() {
  //Serial.println("changing setting request received. Handling...");
  if (server.hasArg("plain") == false){
     server.send(400, "text/plain", "body is empty");
     return;
  }
  settings = deserializeSettings(server.arg("plain"));
  server.send(200, "text/plain", "ok");

  //Serial.println("request handled successfully");
  //Serial.println("--------------------");
}

void handleGetState(){
  //Serial.println("getting state info request received. Handling...");

  String state = serializeState();
  server.send(200, "application/json;charset=UTF-8", state);

  //Serial.println("request handled successfully");
  //Serial.println("--------------------");
}

void handleForceWatering(){
  //Serial.println("watering request received. Handling...");

  watering();
  server.send(200, "text/plain", "ok");

  //Serial.println("request handled successfully");
  //Serial.println("--------------------");
}

//--------------CHECKERS-----------------
//check interval
bool needCheckTanker(){
  //todo choose conditions
  return true;
}
bool needCheckGround(){
  return state.ground.lastCheck == 0 ||
    millis() - state.ground.lastCheck > settings.watering.interval;
}
bool needCheckAir(){
  return state.vaporizer.lastCheck == 0 ||
     millis() - state.vaporizer.lastCheck > settings.vaporize.interval;
}
//check required actions
bool needWhistling(){
  bool needWhistle =  settings.whistling.enabled &&
     greaterOrEqual(getDateTime().Hour(), getDateTime().Minute(), settings.whistling.startHour, settings.whistling.startMinute) &&
     greater(settings.whistling.stopHour, settings.whistling.stopMinute, getDateTime().Hour(), getDateTime().Minute()) &&
     !state.tanker.isFull;
     //Serial.println("whistle = " + String(needWhistle));
     return needWhistle;
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
  /*
  Serial.println("need lighting on calculation:");
  Serial.println("settings.lighting.enabled=" + String(settings.lighting.enabled));
  Serial.println("state.lighting.turnedOn=" + String(state.lighting.turnedOn));

  Serial.println("settings.lighting.startTime=" + String(settings.lighting.startTime) + ":" + String(settings.lighting.startMinute));
  Serial.println("settings.lighting.stopTime=" + String(settings.lighting.stopHour) + ":" + String(settings.lighting.stopMinute));
  Serial.println("getDateTime().Hour()=" + String(getDateTime().Hour()));
  Serial.println("getDateTime().Minute()=" + String(getDateTime().Minute()));
  Serial.println("-----------------------");
  */
  bool turnOn = !state.lighting.turnedOn &&
    (settings.lighting.enabled &&
     greaterOrEqual(getDateTime().Hour(), getDateTime().Minute(), settings.lighting.startHour, settings.lighting.startMinute) &&
     greater(settings.lighting.stopHour, settings.lighting.stopMinute, getDateTime().Hour(), getDateTime().Minute()));
  /*
  Serial.println("lighting.turnOn = " + String(turnOn));
  Serial.println("-----------------------");
  */
  return turnOn;
}
bool needLightingOff(){
  /*
  Serial.println("need lighting off calculation:");
  Serial.println("settings.lighting.enabled=" + String(settings.lighting.enabled));
  Serial.println("state.lighting.turnedOn=" + String(state.lighting.turnedOn));

  Serial.println("settings.lighting.startTime=" + String(settings.lighting.startTime) + ":" + String(settings.lighting.startMinute));
  Serial.println("settings.lighting.stopTime=" + String(settings.lighting.stopHour) + ":" + String(settings.lighting.stopMinute));
  Serial.println("getDateTime().Hour()=" + String(getDateTime().Hour()));
  Serial.println("getDateTime().Minute()=" + String(getDateTime().Minute()));
  Serial.println("-----------------------");
  */
  bool turnOff = state.lighting.turnedOn &&
    (!settings.lighting.enabled ||
      greater(settings.lighting.startHour, settings.lighting.startMinute,   getDateTime().Hour(), getDateTime().Minute()) ||
      greaterOrEqual(getDateTime().Hour(), getDateTime().Minute(), settings.lighting.stopHour, settings.lighting.stopMinute));
  /*
  Serial.println("lighting.turnOff = " + String(turnOff));
  Serial.println("-----------------------");
  */
  return turnOff;
}
bool needVaporizeOn(){
  /*Serial.println("need vaporize on calculation:");
  Serial.println("settings.vaporize.enabled=" + String(settings.vaporize.enabled));
  Serial.println("settings.vaporize.minHumidity:" + String(settings.vaporize.minHumidity));
  Serial.println("state.air.humidity:" + String(state.air.humidity));
  Serial.println("-----------------------");*/
  bool vaporizeOn = !state.vaporizer.turnedOn && settings.vaporize.enabled && state.air.humidity < settings.vaporize.minHumidity;
  /*Serial.println("vaporize.turnOn = " + String(vaporizeOn));
  Serial.println("-----------------------");*/
  return vaporizeOn;
}
bool needVaporizeOff(){
  /*Serial.println("need vaporize on calculation:");
  Serial.println("settings.vaporize.enabled=" + String(settings.vaporize.enabled));
  Serial.println("settings.vaporize.minHumidity:" + String(settings.vaporize.minHumidity));
  Serial.println("state.air.humidity:" + String(state.air.humidity));
  Serial.println("-----------------------");*/
  bool vaporizeOff = state.vaporizer.turnedOn && (!settings.vaporize.enabled || state.air.humidity > settings.vaporize.minHumidity);
  /*Serial.println("vaporize.turnOff = " + String(vaporizeOff));
  Serial.println("-----------------------");*/
  return vaporizeOff;
}

//------------SAVING STATE--------------
void saveGroundState(int humidity){
  state.ground.lastCheck = millis();
  state.ground.date = getDateTime();
  state.ground.humidity = humidity;
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
  state.lighting.turnedOn = lightingStatus;
  state.lighting.date = getDateTime();
}
void saveVaporizerState(bool vaporizeStatus){
  state.vaporizer.turnedOn = vaporizeStatus;
  state.vaporizer.date = getDateTime();
}

//-----------UTILITY FUNCTIONS----------
bool greaterOrEqual(int hour1, int minute1, int hour2, int minute2){
   return hour1 > hour2 || hour1 == hour2 && minute1 >= minute2;
}
bool greater(int hour1, int minute1, int hour2, int minute2){
   return hour1 > hour2 || hour1 == hour2 && minute1 > minute2;
}

String getRequest(String serviceUrl){
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
  StaticJsonDocument<512> doc;
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
  tmp.lighting.startHour = doc["lighting"]["startHour"]; //7
  tmp.lighting.startMinute = doc["lighting"]["startMinute"]; //0
  tmp.lighting.stopHour = doc["lighting"]["stopHour"]; //23
  tmp.lighting.stopMinute = doc["lighting"]["stopMinute"]; //30

  tmp.whistling.enabled = doc["whistling"]["enabled"]; // false
  tmp.whistling.duration = doc["whistling"]["duration"]; // 2
  tmp.whistling.startHour = doc["whistling"]["startHour"]; //7
  tmp.whistling.startMinute = doc["whistling"]["startMinute"]; //0
  tmp.whistling.stopHour = doc["whistling"]["stopHour"]; //23
  tmp.whistling.stopMinute = doc["whistling"]["stopMinute"]; //30

  return tmp;
}

String serializeState(){
  StaticJsonDocument<512> doc;

  JsonObject air = doc.createNestedObject("air");
  air["humidity"] = state.air.humidity;
  air["temperature"] = state.air.temperature;
  air["lastCheck"] = prettyDateTime(state.air.date);

  JsonObject ground = doc.createNestedObject("ground");
  ground["humidity"] = state.ground.humidity;
  ground["lastCheck"] = prettyDateTime(state.ground.date);

  JsonObject tanker = doc.createNestedObject("tanker");
  tanker["full"] = state.tanker.isFull;
  tanker["lastCheck"] = prettyDateTime(state.tanker.date);

  JsonObject light = doc.createNestedObject("light");
  light["status"] = state.lighting.turnedOn;
  light["lastCheck"] = prettyDateTime(state.lighting.date);

  JsonObject vaporizer = doc.createNestedObject("vaporizer");
  vaporizer["status"] = state.vaporizer.turnedOn;
  vaporizer["lastCheck"] = prettyDateTime(state.vaporizer.date);

  String output;
  serializeJson(doc, output);
  return output;
}

String prettyDateTime(const RtcDateTime& dt){
  return String(dt.Day()) + "-" + String(dt.Month()) + "-" + String(dt.Year()) + "T" + String(dt.Hour()) + ":" + String(dt.Minute()) + ":" + String(dt.Second());
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
  digitalWrite(PUMP, PUMP_RELAY_OPEN);
  delay(settings.watering.duration);
  digitalWrite(PUMP, PUMP_RELAY_CLOSE);
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
  //Serial.println("turning lighting on...");
  digitalWrite(LIGHTING, LIGHT_RELAY_OPEN);
  //Serial.println("lighting is turned on");
}

void lightingOff(){
  //Serial.println("turning lighting off...");
  digitalWrite(LIGHTING, LIGHT_RELAY_CLOSE);
  //Serial.println("lighting is turned off");
}

void vaporizeOn(){
  //Serial.println("turning vaporize on...");
  digitalWrite(VAPORIZER, VAPORIZE_RELAY_OPEN);
  //Serial.println("vaporize is turned on");
}

void vaporizeOff(){
 // Serial.println("turning vaporize off...");
  digitalWrite(VAPORIZER, VAPORIZE_RELAY_CLOSE);
 // Serial.println("vaporize is turned off");
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

/*void lcdPrint(String message){
  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print(message);
}*/