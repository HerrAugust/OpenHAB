package Monitoring;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Takes data from sensors, that communicate them through MQTT.
 * These data are then written into DB
 * @author agost
 */
public class Monitoring {
	public static void main(String[] args) throws MqttException {
		Paho p = new Paho();
		while(true);
	}

}
