package paho;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Paho implements MqttCallback {
	
	private final String broker = "tcp://localhost:1883";
	private final String clientId = "Paho Analysis";
	private final int qos = 2;
	private MqttClient sampleClient = null;
	private MemoryPersistence persistence;
	
	public Paho() throws MqttException {
		persistence = new MemoryPersistence();
		sampleClient = new MqttClient(broker, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        sampleClient.connect(connOpts);
        sampleClient.setCallback(this);
        
        sampleClient.subscribe("goals/window");
        sampleClient.subscribe("goals/temperature");
	}
	
	public void send(String topic, String content) {
        try {
            System.out.println("Publishing message (" + topic + "): "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
	}
	
	@Override
	public void finalize() throws MqttException {
		sampleClient.disconnect();
        System.out.println("Disconnected");
	}

	@Override
	public void connectionLost(Throwable arg0) {

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {		
		switch(topic.split("/")[1]) {
		case "window":
			Analysis.Analysis.window_goal = "WINDOW";
			Analysis.Analysis.date_beg = new String(message.getPayload()).split("-")[0];
			Analysis.Analysis.date_end = new String(message.getPayload()).split("-")[1];
			Analysis.Analysis.windowsopen_done = false;
			Analysis.Analysis.windowsclose_done = false;
			break;
		case "temperature":
			Analysis.Analysis.threshold = Float.parseFloat(new String(message.getPayload()).split(" ")[1]);
			break;
		}
		
	}
	
}
