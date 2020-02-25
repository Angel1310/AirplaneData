
#include <ESP8266WiFi.h>

#include <ArduinoJson.h>
#include "FirebaseESP8266.h"


#define FIREBASE_HOST "https://airplanedata-e5733.firebaseio.com/" //Without http:// or https:// schemes
#define FIREBASE_AUTH "94V5eRP7vVc7I7DhAZkTOrDyDdgFsHwNMKCQapoa"




FirebaseData firebaseData;

char* ssid = "HUAWEI P smart 2019";
char* password = "12345678";

void setup()
{
  WiFi.begin(ssid,password);
  Serial.begin(9600);
  
  while(WiFi.status()!=WL_CONNECTED)
  {
    Serial.print(".");
    delay(500);
  }
  Serial.println("");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true); 
  firebaseData.setBSSLBufferSize(1024, 1024);  
  firebaseData.setResponseSize(1024);
  
  Firebase.setReadTimeout(firebaseData, 1000 * 60);

  Firebase.setwriteSizeLimit(firebaseData, "tiny");


  
}

void loop()
{
  delay(1000);
  handleIndex();
  delay(1000);
}

void handleIndex(){
 
  DynamicJsonDocument doc(1024);
  double gas = 0, pressure = 0, temperature=0, humidity=0, Altitude=0 ;
  double satelliteCount = 0, Latitude = 0, Longitude = 0, MPH=0,Feet=0;
  double A_X=0, A_Y=0, A_Z=0,G_X=0, G_Y=0, G_Z=0;
  // Sending the request
  doc["type"] = "request";
  
  serializeJson(doc,Serial);
  // Reading the response
  boolean messageReady = false;
  String message = "";
  while(messageReady == false) { 
    if(Serial.available()) {
      message = Serial.readString();
      messageReady = true;
    }
  }
  
  DeserializationError error = deserializeJson(doc,message);
  if(error) {
    Serial.print(F("deserializeJson() failed: "));
    Serial.println(error.c_str());
   
    return;
  }
  pressure = doc["Pressure"];
  gas = doc["Gas"];
  humidity = doc["Humidity"];
  Altitude = doc["Approx-Altitude"];
  temperature = doc["Temperature"];

  A_X = doc["A_X"];
  A_Y = doc["A_Y"];
  A_Z = doc["A_Z"];
  G_X = doc["G_X"];
  G_Y = doc["G_Y"];
  G_Z = doc["G_Z"];
  
//  satelliteCount = doc["Satellite Count"];
//  Latitude = doc["Latitude"];
//  Longitude = doc["Longitude"];
//  MPH = doc["Speed MPH"];
//  Feet = doc["Altitude Feet"];
  
  
  Firebase.setDouble(firebaseData, "/Test/BME680/Pressure", pressure);
  Firebase.setDouble(firebaseData, "/Test/BME680/GAS", gas);
  Firebase.setDouble(firebaseData, "/Test/BME680/Humidity", humidity);
  Firebase.setDouble(firebaseData, "/Test/BME680/Approx-Altitude", Altitude);
  Firebase.setDouble(firebaseData, "/Test/BME680/Temperature", temperature);

  Firebase.setDouble(firebaseData, "/Test/MinIMU-9/Accelerometer/X", A_X );
  Firebase.setDouble(firebaseData, "/Test/MinIMU-9/Accelerometer/Y", A_Y );
  Firebase.setDouble(firebaseData, "/Test/MinIMU-9/Accelerometer/Z", A_Z );
  Firebase.setDouble(firebaseData, "/Test/MinIMU-9/Gyroscope/X", G_X );
  Firebase.setDouble(firebaseData, "/Test/MinIMU-9/Gyroscope/Y", G_Y );
  Firebase.setDouble(firebaseData, "/Test/MinIMU-9/Gyroscope/Z", G_Z );
  
//
//  Firebase.setDouble(firebaseData, "/Test/GPS/Satellite Count", satelliteCount);
//  Firebase.setDouble(firebaseData, "/Test/GPS/Latitude", Latitude);
//  Firebase.setDouble(firebaseData, "/Test/GPS/Longitude", Longitude);
//  Firebase.setDouble(firebaseData, "/Test/GPS/Speed MPH", MPH);
//  Firebase.setDouble(firebaseData, "/Test/GPS/Altitude Feet", MPH);
  
  
  
 

}
