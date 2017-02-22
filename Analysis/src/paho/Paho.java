package paho;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Paho {
	
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
}
