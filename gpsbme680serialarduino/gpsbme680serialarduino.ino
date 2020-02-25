#include <ArduinoJson.h>

#include "TinyGPS++.h"
#include "SoftwareSerial.h"

TinyGPSPlus gps;
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_Sensor.h>
#include "Adafruit_BME680.h"

#include <LSM6.h>

LSM6 imu;

#define BME_SCK 13
#define BME_MISO 12
#define BME_MOSI 11
#define BME_CS 10

#define SEALEVELPRESSURE_HPA (1013.25)

Adafruit_BME680 bme; 

String message = "";
bool messageReady = false;

void setup() {
  Serial.begin(9600);
  //Serial.println(F("BME680 test"));

  if (!bme.begin(0x76)) {
    
    Serial.println("Could not find a valid BME680 sensor, check wiring!");
    while (1);
  }
  if (!imu.init())
  {
    Serial.println("Failed to detect and initialize IMU!");
    while (1);
  }
  imu.enableDefault();

  
  bme.setTemperatureOversampling(BME680_OS_8X);
  bme.setHumidityOversampling(BME680_OS_2X);
  bme.setPressureOversampling(BME680_OS_4X);
  bme.setIIRFilterSize(BME680_FILTER_SIZE_3);
  bme.setGasHeater(320, 150); // 320*C for 150 ms
}

void loop() {
  imu.read();
 
  while(Serial.available()) {
//    gps.encode(Serial1.read());
    message = Serial.readString();
    messageReady = true;
  }
 
  if(messageReady) {
   
    DynamicJsonDocument doc(1024);    
    DeserializationError error = deserializeJson(doc,message);
    if(error) {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.c_str());
      
      messageReady = false;
      return;
    }
    if(doc["type"] == "request") {
      doc["type"] = "response";
     
      doc["Temperature"] = bme.temperature;
        doc["Pressure"] = bme.pressure / 100.0;
      doc["Humidity"] = bme.humidity;
      doc["Gas"] = bme.gas_resistance / 1000.0;
      doc["Approx-Altitude"] = bme.readAltitude(SEALEVELPRESSURE_HPA);
      doc["A_X"] = imu.a.x; 
      doc["A_Y"] = imu.a.y;
      doc["A_Z"] = imu.a.z;
      doc["G_X"] = imu.g.x;
      doc["G_Y"] = imu.g.y;
      doc["G_Z"] = imu.g.z;
//      
//      if(gps.location.isUpdated()){
//        
//      doc["Satellite Count"] = gps.satellites.value();
//      doc["Latitude"] = gps.location.lat();
//      doc["Longitude"] = gps.location.lng();
//      doc["Speed MPH"] = gps.speed.mph();
//      doc["Altitude Feet"] = gps.altitude.feet();
//      
//      }

      

      
      
      serializeJson(doc,Serial);
    }
    messageReady = false;
  }
}
