/*
 * Author:  Owen Gibson
 * Project: CIS350 Embedded System Contribution
 * Date:    5/23/21
 */
#include <Keyboard.h>

int backPin = 9;                  // Set button pins
int pausePin = 10;
int forwardPin = 4;

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

void loop()
{
  if (digitalRead(backPin) == 0)  // if the button goes low
  {
    Keyboard.press(KEY_LEFT_CTRL);
    Keyboard.press(KEY_LEFT_ARROW);

    Keyboard.release(KEY_LEFT_CTRL);
    Keyboard.release(KEY_LEFT_ARROW);

    delay(300);
  }
  if (digitalRead(pausePin) == 0)  
  {
    Keyboard.write(' ');

    delay(300);
  }
  if (digitalRead(forwardPin) == 0)  
  {
    Keyboard.press(KEY_LEFT_CTRL);
    Keyboard.press(KEY_RIGHT_ARROW);

    Keyboard.release(KEY_LEFT_CTRL);
    Keyboard.release(KEY_RIGHT_ARROW);

    delay(300);
  }
}
