#ifndef IOT_ICE
#define IOT_ICE

module IoT {

	exception EX {
		string text;
	}
	
	enum DeviceState {OFF, ON}
	
	interface Device {
		idempotent void turnOn();
		idempotent void turnOff();
		idempotent string getName();
		idempotent string getState();
	}
	
	interface Fridge extends Device {
		void increaseTemperature(int value) throws EX;
		void decreaseTemperature(int value) throws EX;
		idempotent int getTemperature() throws EX; 
	}
	
	interface Bulbulator extends Device {
		idempotent void doNothing() throws EX;
	}
	
	interface MotionDetector extends Device {
		idempotent bool checkForDetection() throws EX;
	}
	
	interface MotionDetectorButBroken extends Device {
		idempotent bool checkForDetection() throws EX;
	}
	
	sequence<string> deviceList;
	
	interface Devices {
		idempotent deviceList getDeviceList();
	}

}

#endif