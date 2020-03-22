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
//float V_0 = 5.0; // supply voltage to the pressure sensor
//float rho = 1.204; // density of air 
//// parameters for averaging and offset
//int offset = 0;
//int offset_size = 10;
//int veloc_mean_size = 20;
//int zero_span = 2;
     
double asOffsetV = 0.0;

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
  
//  for (int ii=0;ii<offset_size;ii++){
//    offset += analogRead(A0)-(1023/2);
//  }
//  offset /= offset_size;
asOffsetV = analogRead(A0) * .0047;
}

void loop() {
// float adc_avg = 0; float veloc = 0.0;
//  
//// average a few ADC readings for stability
//  for (int ii=0;ii<veloc_mean_size;ii++){
//    adc_avg+= analogRead(A0)-offset;
//  }
//  adc_avg/=veloc_mean_size;
//  
//  // make sure if the ADC reads below 512, then we equate it to a negative velocity
//  if (adc_avg>512-zero_span and adc_avg<512+zero_span){
//  } else{
//    if (adc_avg<512){
//      veloc = -sqrt((-10000.0*((adc_avg/1023.0)-0.5))/rho);
//    } else{
//      veloc = sqrt((10000.0*((adc_avg/1023.0)-0.5))/rho);
//    }
//  }
  double asVolts = 0.0;
  double compVOut = 0.0;
  double dynPress = 0.0;
  double airSpeed = 0.0;
  
  asVolts = analogRead(A0) * .0047;  
  compVOut = asVolts - asOffsetV;
  //dynPress = (compVOut / 5.0 - .2) / .2;  // Transfer function with no autozero
  if(compVOut < .005)  {                    // Set noise to 0, min speed is ~8mph
    compVOut = 0.0;
  }  
  //dynPress = compVOut * 1000.0;          // With autozero, dynamic pressure in kPa = Vout, convert kPa to P
 airSpeed = sqrt((2 * compVOut * 1000.0)/1.225);   // Converts pressure to m/s, 1.225 k/m3 is standard air density
//  
   

  

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
      doc["Speed"] = airSpeed;
      
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
