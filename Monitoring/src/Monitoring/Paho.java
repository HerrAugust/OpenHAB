package Monitoring;

import javax.swing.JOptionPane;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import Storage.Storage;

public class Paho implements MqttCallback {
	
	Storage s;
	String table;
	String fields; 
	String values;
	String where;
	String temp;
	boolean overwrite = false;
	
	private final String broker = "tcp://localhost:1883";
	private final String clientId = "Paho Monitoring";
	private MqttClient sampleClient = null;
	private MemoryPersistence persistence;
	
	public Paho() throws MqttException {
		System.out.println("qui");
		persistence = new MemoryPersistence();
		sampleClient = new MqttClient(broker, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        sampleClient.connect(connOpts);
        sampleClient.setCallback(this);
        
        //subscribe to topics
        sampleClient.subscribe("home/general/electricity");
		sampleClient.subscribe("home/general/humidity");
		sampleClient.subscribe("home/general/temperature");
		
		sampleClient.subscribe("home/general/lights");
		sampleClient.subscribe("home/livingroom/lights");
		sampleClient.subscribe("home/kitchen/lights");
		sampleClient.subscribe("home/sleepingroom/lights");
		sampleClient.subscribe("home/bathroom/lights");
		sampleClient.subscribe("home/livingroom/lights/1");
		sampleClient.subscribe("home/livingroom/lights/2");
		sampleClient.subscribe("home/livingroom/lights/3");
		sampleClient.subscribe("home/kitchen/lights/1");
		sampleClient.subscribe("home/kitchen/lights/2");
		sampleClient.subscribe("home/sleepingroom/lights/1");
		sampleClient.subscribe("home/sleepingroom/lights/2");
		sampleClient.subscribe("home/bathroom/lights/1");
		
		sampleClient.subscribe("home/general/windows");
		sampleClient.subscribe("home/livingroom/windows");
		sampleClient.subscribe("home/kitchen/windows");
		sampleClient.subscribe("home/sleepingroom/windows");
		sampleClient.subscribe("home/bathroom/windows");
		sampleClient.subscribe("home/livingroom/windows/1");
		sampleClient.subscribe("home/livingroom/windows/2");
		sampleClient.subscribe("home/livingroom/windows/3");
		sampleClient.subscribe("home/kitchen/windows/1");
		sampleClient.subscribe("home/kitchen/windows/2");
		sampleClient.subscribe("home/sleepingroom/windows/1");
		sampleClient.subscribe("home/bathroom/windows/1");
		
		sampleClient.subscribe("home/livingroom/people");
		sampleClient.subscribe("home/kitchen/people");
		sampleClient.subscribe("home/sleepingroom/people");
		sampleClient.subscribe("home/bathroom/people");
		System.out.println("fine subscription");
	}
	
	@Override
	public void finalize() throws MqttException {
		sampleClient.disconnect();
        System.out.println("Disconnected");
	}

	@Override
	public void connectionLost(Throwable arg0) {
		JOptionPane.showMessageDialog(null, "Connection MQTT lost", "Monitoring service lost MQTT connection. Abort", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		System.out.println("message arrived from "+ topic);
		overwrite = true;
		values = new String(msg.getPayload());;
		if(topic.endsWith("electricity")) {
			overwrite = false;
			table = "electricity";
			fields = "value";
		}
		else if(topic.endsWith("humidity")) {
			overwrite = false;
			table = "humidity";
			fields = "value";
		}
		else if(topic.endsWith("temperature")) {
			overwrite = false;
			table = "temperature";
			fields = "degrees";
		}
		else if(topic.endsWith("general/lights")) { //LightOn, overwrite
			table = "general";
			fields = "number";
			where = "WHERE subject=\"LightsOn\"";
		}
		else if(topic.endsWith("general/windows")) { //Windows_Number, overwrite
			table = "general";
			fields = "number";
			where = "WHERE subject=\"Windows_Number\"";
		}
		else if(topic.endsWith("lights")) {
			table = "general";
			fields = "number";
			if(topic.contains("livingroom"))
				temp = "LivingRoom";
			else if(topic.contains("kitchen"))
				temp = "Kitchen";
			else if(topic.contains("sleepingroom"))
				temp = "SleepingRoom";
			else if(topic.contains("bathroom"))
				temp = "Bathroom";
			where = "WHERE subject=\"Lights_" + temp + "\"";
		}
		else if(topic.endsWith("people")) {
			table = "room";
			fields = "peopleIn";
			String[] t = topic.split("/");
			where = "WHERE name=\"" + t[1] + "\"";
		}
		else { // example home/livingroom/windows/1
			fields = "state";
			
			if(topic.contains("livingroom"))
				temp = "LivingRoom_";
			else if(topic.contains("kitchen"))
				temp = "Kitchen_";
			else if(topic.contains("sleepingroom"))
				temp = "SleepingRoom_";
			else if(topic.contains("bathroom"))
				temp = "Bathroom_";
			
			if(topic.contains("win")) {
				temp += "Windows_l";
				table = "window";
			}
			else { //light
				table = "light";
				temp += "Lights_l";
			}
			temp += topic.substring(topic.length()-1);
			values = "\"" + values + "\"";
			
			where = "WHERE name=\"" + temp + "\"";
		}
		
		s = new Storage();
		if(overwrite)
			s.overwrite(table, fields, values, where);
		else
			s.write(table, fields, values);
		
	}
}
