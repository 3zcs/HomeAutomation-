

char command;
String string;
boolean ledon = false;
boolean fanon = false; 
#define fan 9
#define led 8


void setup(){
    Serial.begin(9600);
    pinMode(fan, OUTPUT);
    pinMode(led, OUTPUT);
  }
  
  
void loop()
{
    if (Serial.available() > 0){
      string = "";
      }
      
      
      while(Serial.available() > 0){
        command = ((byte)Serial.read());
          if(command == ':')
      {
        break;
      }
      else
      {
      string += command;
      }
      delay(1);
    }
    
    if(string =="1")
    {
      fans();
    }
    
    if(string =="2")
    {
      lamp();
    }
    
        if(string =="3"){
          out();
        }
    {
      lamp();
    }

    Serial.println(string); //debug
    delay(10);

}


void fans()
{
  if (fanon == false ) {
    digitalWrite(fan, HIGH);
    fanon = true ; 
    string = "";
    delay(10);
  }else {
        digitalWrite(fan, LOW);
        fanon = false ; 
        string = "";
        delay(10);
  }
}


void lamp()
{
  if (ledon == false ) {
    digitalWrite(led, HIGH);
    ledon = true ; 
    string = "";
    delay(10);
  }else {
        digitalWrite(led, LOW);
        ledon = false ; 
        string = "";
        delay(10);
  }
}

void out()
{ 
        digitalWrite(led, LOW);
        digitalWrite(led, LOW);
        fanon = false ;
        ledon = false ; 
        string = "";
        delay(10);
}

