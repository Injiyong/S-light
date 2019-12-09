#include <Adafruit_NeoPixel.h>  // 네오픽셀 LED 사용하기 위해 헤더파일 선언
#include<SoftwareSerial.h> //블루투스
int RGB_PIN = 3;  // RGB 핀번호 (D3)
int RGB_PIN2=5;
int RGB_PIN3 = 6;
int RGB_PIN4 = 10;
int RGB_PIN5 = 11;
int NUMPIXELS = 8;  // 네오픽셀 개수
int b=120;  //0~255 밝기제어
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, RGB_PIN, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel pixels2 = Adafruit_NeoPixel(NUMPIXELS, RGB_PIN2, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel pixels3 = Adafruit_NeoPixel(NUMPIXELS, RGB_PIN3, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel pixels4 = Adafruit_NeoPixel(NUMPIXELS, RGB_PIN4, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel pixels5 = Adafruit_NeoPixel(NUMPIXELS, RGB_PIN5, NEO_GRB + NEO_KHZ800);

int TxPin=8;
int RxPin=7;
SoftwareSerial SL(TxPin,RxPin);
boolean sb=false;
boolean kbk = false;
int Button=7;
boolean RGB_Complete=false;
String s;

void setup() {
  Serial.begin(9600);
  SL.begin(9600);
  pixels.begin();  // 네오픽셀 제어 시작
  pixels.setBrightness(b);
  pixels.show();  // 네오픽셀 초기화
  pixels2.begin();  
  pixels2.setBrightness(b);
  pixels2.show(); 
  pixels3.begin();  // 네오픽셀 제어 시작
  pixels3.setBrightness(b);
  pixels3.show();
  pixels4.begin();  // 네오픽셀 제어 시작
  pixels4.setBrightness(b);
  pixels4.show();
  pixels5.begin();  // 네오픽셀 제어 시작
  pixels5.setBrightness(b);
  pixels5.show();
  //mode = 0;
  s = "";
  boolean kbk = false;
}

void loop() { 
  Serial.print("b = ");
  Serial.println(b);
  if(SL.available()){
    int x = SL.read();
    
    if(x == 10){
      s += ".";
    }
    else{
      char bk = (char)x;
      s += bk;
    }
  }
  else{
    if(!s.equals("")){
      //Serial.print("s = ");
      //Serial.println(s);
    }
    int RGB[3];
    int count = 0;
    int mode = 0;
    String aa = "";
    for(int i = 0; i < s.length(); i++){
      if(s.length() <= 2){
        mode = s.toInt();
        break;
      }
      if(s.charAt(i) != 46){
        aa += s.charAt(i);
      }
      else{
        switch(count){
          case 0:
            mode = aa.toInt();
            //Serial.print("mode = ");
            //Serial.println(mode);
            aa = "";
            count++;
            break;
          case 1:
            RGB[0] = aa.toInt();
            //Serial.print("R = ");
            //Serial.println(RGB[0]);
            aa = "";
            count++;
            break;
          case 2:
            RGB[1] = aa.toInt();
            //Serial.print("G = ");
            //Serial.println(RGB[1]);
            aa = "";
            count++;
            break;
          case 3:
            RGB[2] = aa.toInt();
            //Serial.print("B = ");
            //Serial.println(RGB[2]);
            aa = "";
            count++;
            break;
        }
      }
    }
    switch(mode){
      case 1 :
        //Serial.println("[LOG] x = 1");
        for(int i=0;i<NUMPIXELS;i++){ //네오픽셀 동작 (1~8까지)
          pixels.setPixelColor(i,RGB[0],RGB[1],RGB[2]); // 색 지정
          pixels2.setPixelColor(i,RGB[0],RGB[1],RGB[2]);
          pixels3.setPixelColor(i,RGB[0],RGB[1],RGB[2]);
          pixels4.setPixelColor(i,RGB[0],RGB[1],RGB[2]);
          pixels5.setPixelColor(i,RGB[0],RGB[1],RGB[2]);
          pixels.show(); //on
          pixels2.show();
          pixels3.show();
          pixels4.show();
          pixels5.show();
        }
        break;
      case 2 :
        //Serial.println("[LOG] x = 2");
        for(int i=0;i<NUMPIXELS;i++){ //네오픽셀 동작 (1~8까지)
          pixels.setPixelColor(i,0,0,0); // 색 지정
          pixels2.setPixelColor(i,0,0,0);
          pixels3.setPixelColor(i,0,0,0);
          pixels4.setPixelColor(i,0,0,0);
          pixels5.setPixelColor(i,0,0,0);
          
          pixels.show(); //on
          pixels2.show();
          pixels3.show();
          pixels4.show();
          pixels5.show(); 
        }
        break;
      case 3 :
        for(int i=0;i<NUMPIXELS;i++){
          pixels.setPixelColor(i,RGB[0],RGB[1],RGB[2]); // 색 지정
          pixels2.setPixelColor(i,RGB[0],RGB[1],RGB[2]);
          pixels3.setPixelColor(i,RGB[0],RGB[1],RGB[2]);
          pixels4.setPixelColor(i,RGB[0],RGB[1],RGB[2]);
          pixels5.setPixelColor(i,RGB[0],RGB[1],RGB[2]);
          pixels.show(); //on
          pixels2.show();
          pixels3.show();
          pixels4.show();
          pixels5.show();
        }
        break;
      case 4 :
        b = RGB[0];
        if(b == 0)
          b = 1;
        //Serial.print("b = ");
        //Serial.println(b);
        //b = map(b,12,100,0,255);
        pixels.setBrightness(b);
        pixels2.setBrightness(b);
        pixels3.setBrightness(b);
        pixels4.setBrightness(b);
        pixels5.setBrightness(b);
        pixels.show(); //on
        pixels2.show();
        pixels3.show();
        pixels4.show();
        pixels5.show();
        
    }
    s = "";
  }
}
