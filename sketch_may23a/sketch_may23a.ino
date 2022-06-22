/*
 * Author:  Owen Gibson
 * Project: CIS350 Embedded System Contribution
 * Date:    5/23/21
 */
#include <Keyboard.h>

int backPin = 9;                  // Set button pins
int pausePin = 10;
int forwardPin = 4;
int toggle = 1;

/**
 * @brief One-time initialization of pins and keyboard drivers.
 * 
 */
void setup()
{
  pinMode(backPin, INPUT);        // Set the button as an input
  digitalWrite(backPin, HIGH);    // Pull the button high

  pinMode(pausePin, INPUT);  
  digitalWrite(pausePin, HIGH);  

  pinMode(forwardPin, INPUT);  
  digitalWrite(forwardPin, HIGH);  

  Keyboard.begin();               //Init keyboard emulation
}

/**
 * @brief Continuous looping body, takes in button input and injects corresponding keystrokes.
 * 
 */
void loop()
{
  if (digitalRead(backPin) == 0)  // if the button goes low
  {
    Keyboard.write('3');

    delay(300);
  }
  if (digitalRead(pausePin) == 0)  
  {
    if(toggle) 
    {
      Keyboard.write('1');
      toggle = 0;
    }
    else
    {
      keyboard.write('2');
      toggle = 0;
    }
    delay(300);
  }
  if (digitalRead(forwardPin) == 0)  
  {
    Keyboard.write('4');

    delay(300);
  }
}
