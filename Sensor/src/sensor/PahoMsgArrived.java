package sensor;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class PahoMsgArrived implements MqttCallback {

    String contents;

    private final String broker = "tcp://localhost:1883";
    private final String clientId = "Paho sensor message arrived";
    private final int qos = 2;
    private MqttClient sampleClient = null;
    private MemoryPersistence persistence;

    public PahoMsgArrived() throws MqttException {
        persistence = new MemoryPersistence();
        sampleClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        sampleClient.connect(connOpts);
        sampleClient.setCallback(this);

        //subscribe to topics
        sampleClient.subscribe("home/general/decision/openwindow");
        sampleClient.subscribe("home/general/decision/closewindow");
        sampleClient.subscribe("home/general/decision/openlight");
        sampleClient.subscribe("home/general/decision/closelight");
        sampleClient.subscribe("home/general/decision/windowsdecision");
        sampleClient.subscribe("home/general/decision/temperatura");
        sampleClient.subscribe("home/general/decision/closelights");
        System.out.println("fine subscription");
    }

    public void send(String topic, String content) {
        try {
            System.out.println("Publishing message (" + topic + "): " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    @Override
    public void finalize() throws MqttException {
        sampleClient.disconnect();
        System.out.println("Disconnected");
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void messageArrived(String topic, MqttMessage content) throws Exception {
        System.out.println("message arrived from " + topic);
        contents = new String(content.getPayload());

        Sensor sensor = new Sensor();
        if (topic.endsWith("openwindow")) {
            switch (contents) {
                case "LivingRoom_Windows_l1":
                    if (!sensor.getWindowLR1().isSelected()) {
                        sensor.getWindowLR1().setSelected(true);

                    }
                    break;
                case "LivingRoom_Windows_l2":
                    if (!sensor.getWindowLR2().isSelected()) {
                        sensor.getWindowLR2().setSelected(true);

                    }
                    break;
                case "LivingRoom_Windows_l3":
                    if (!sensor.getWindowLR3().isSelected()) {
                        sensor.getWindowLR3().setSelected(true);

                    }
                    break;
                case "Kitchen_Windows_l1":
                    if (!sensor.getWindowKT1().isSelected()) {
                        sensor.getWindowKT1().setSelected(true);

                    }
                    break;
                case "Kitchen_Windows_l2":
                    if (!sensor.getWindowKT2().isSelected()) {
                        sensor.getWindowKT2().setSelected(true);

                    }
                    break;
                case "SleepingRoom_Windows_l1":
                    if (!sensor.getWindowSR1().isSelected()) {
                        sensor.getWindowSR1().setSelected(true);

                    }
                    break;
                case "Bathroom_Windows_l1":
                    if (!sensor.getWindowBR1().isSelected()) {
                        sensor.getWindowBR1().setSelected(true);

                    }
                    break;
                case "VOID":

                    break;
            }
        } else if (topic.endsWith("openlight")) {
            switch (contents) {
                case "LivingRoom_Lights_l1":
                    if (!sensor.getLightLR1().isSelected()) {
                        sensor.getLightLR1().setSelected(true);
                    }
                    break;
                case "LivingRoom_Lights_l2":
                    if (!sensor.getLightLR2().isSelected()) {
                        sensor.getLightLR2().setSelected(true);
                    }
                    break;

                case "LivingRoom_Lights_l3":
                    if (!sensor.getLightLR2().isSelected()) {
                        sensor.getLightLR3().setSelected(true);
                    }
                    break;
                case "Kitchen_Lights_l1":
                    if (!sensor.getLightKT1().isSelected()) {
                        sensor.getLightKT1().setSelected(true);
                    }
                    break;
                case "Kitchen_Lights_l2":
                    if (!sensor.getLightKT2().isSelected()) {
                        sensor.getLightKT2().setSelected(true);
                    }
                    break;
                case "SleepingRoom_Lights_l1":
                    if (!sensor.getLightSR1().isSelected()) {
                        sensor.getLightSR1().setSelected(true);
                    }
                    break;
                case "SleepingRoom_Lights_l2":
                    if (!sensor.getLightSR2().isSelected()) {
                        sensor.getLightSR2().setSelected(true);
                    }
                    break;
                case "BathRoom_Lights_l1":
                    if (!sensor.getLightBR1().isSelected()) {
                        sensor.getLightBR1().setSelected(true);
                    }
                    break;
                case "VOID":

                    break;

            }

        } else if (topic.endsWith("closewindow")) {
            switch (contents) {
                case "LivingRoom_Windows_l1":
                    if (sensor.getWindowLR1().isSelected()) {
                        sensor.getWindowLR1().setSelected(false);

                    }
                    break;
                case "LivingRoom_Windows_l2":
                    if (sensor.getWindowLR2().isSelected()) {
                        sensor.getWindowLR2().setSelected(false);

                    }
                    break;
                case "LivingRoom_Windows_l3":
                    if (sensor.getWindowLR3().isSelected()) {
                        sensor.getWindowLR3().setSelected(false);

                    }
                    break;
                case "Kitchen_Windows_l1":
                    if (sensor.getWindowKT1().isSelected()) {
                        sensor.getWindowKT1().setSelected(false);

                    }
                    break;
                case "Kitchen_Windows_l2":
                    if (sensor.getWindowKT2().isSelected()) {
                        sensor.getWindowKT2().setSelected(false);

                    }
                    break;
                case "SleepingRoom_Windows_l1":
                    if (sensor.getWindowSR1().isSelected()) {
                        sensor.getWindowSR1().setSelected(false);

                    }
                    break;
                case "Bathroom_Windows_l1":
                    if (sensor.getWindowBR1().isSelected()) {
                        sensor.getWindowBR1().setSelected(false);

                    }
                    break;
                case "VOID":

                    break;

            }
        } else if (topic.endsWith("closelight")) {
            switch (contents) {
                case "LivingRoom_Lights_l1":
                    if (sensor.getLightLR1().isSelected()) {
                        sensor.getLightLR1().setSelected(false);
                    }
                    break;
                case "LivingRoom_Lights_l2":
                    if (sensor.getLightLR2().isSelected()) {
                        sensor.getLightLR2().setSelected(false);
                    }
                    break;

                case "LivingRoom_Lights_l3":
                    if (sensor.getLightLR2().isSelected()) {
                        sensor.getLightLR3().setSelected(false);
                    }
                    break;

                case "Kitchen_Lights_l1":
                    if (sensor.getLightKT1().isSelected()) {
                        sensor.getLightKT1().setSelected(false);
                    }
                    break;
                case "Kitchen_Lights_l2":
                    if (sensor.getLightKT2().isSelected()) {
                        sensor.getLightKT2().setSelected(false);
                    }
                    break;
                case "SleepingRoom_Lights_l1":
                    if (sensor.getLightSR1().isSelected()) {
                        sensor.getLightSR1().setSelected(false);
                    }
                    break;
                case "SleepingRoom_Lights_l2":
                    if (sensor.getLightSR2().isSelected()) {
                        sensor.getLightSR2().setSelected(false);
                    }
                    break;
                case "BathRoom_Lights_l1":
                    if (sensor.getLightBR1().isSelected()) {
                        sensor.getLightBR1().setSelected(false);
                    }
                    break;
                case "VOID":

                    break;

            }
        } else if (topic.endsWith("temperatura")) {
            int tempe = (int) Float.parseFloat(contents);
            sensor.getSliderTemp().setValue(tempe);
            sensor.getTemp().setText("" + tempe + " °C");
        } else if (topic.endsWith("closelights")) {
            switch (contents) {
                case "sleepingroom":
                    if (sensor.getLightSR1().isSelected()) {
                        sensor.getLightSR1().setSelected(false);
                    }
                    if (sensor.getLightSR2().isSelected()) {
                        sensor.getLightSR2().setSelected(false);
                    }
                    break;
                case "bathroom":
                    if (sensor.getLightBR1().isSelected()) {
                        sensor.getLightBR1().setSelected(false);
                    }
                    break;
                case "kitchen":
                    if (sensor.getLightKT1().isSelected()) {
                        sensor.getLightKT1().setSelected(false);
                    }
                    if (sensor.getLightKT2().isSelected()) {
                        sensor.getLightKT2().setSelected(false);
                    }
                    break;
                case "livingroom":
                    if (sensor.getLightLR1().isSelected()) {
                        sensor.getLightLR1().setSelected(false);
                    }
                    if (sensor.getLightLR2().isSelected()) {
                        sensor.getLightLR2().setSelected(false);
                    }
                    if (sensor.getLightLR3().isSelected()) {
                        sensor.getLightLR3().setSelected(false);
                    }
                    break;
                case "VOID":

                    break;
            }
        } else if (topic.endsWith("windowsdecision")) {
            switch (contents) {
                case "OFF":
                    //close
                    if (sensor.getWindowLR1().isSelected()) {
                        sensor.getWindowLR1().setSelected(false);
                    }
                    if (sensor.getWindowLR2().isSelected()) {
                        sensor.getWindowLR2().setSelected(false);
                    }
                    if (sensor.getWindowLR3().isSelected()) {
                        sensor.getWindowLR3().setSelected(false);
                    }
                    if (sensor.getWindowKT1().isSelected()) {
                        sensor.getWindowKT1().setSelected(false);
                    }
                    if (sensor.getWindowKT2().isSelected()) {
                        sensor.getWindowKT2().setSelected(false);
                    }
                    if (sensor.getWindowSR1().isSelected()) {
                        sensor.getWindowSR1().setSelected(false);
                    }
                    if (sensor.getWindowBR1().isSelected()) {
                        sensor.getWindowBR1().setSelected(false);
                    }
                    break;
                case "ON":
                    //open

                    if (!sensor.getWindowLR1().isSelected()) {
                        sensor.getWindowLR1().setSelected(true);
                    }
                    if (!sensor.getWindowLR2().isSelected()) {
                        sensor.getWindowLR2().setSelected(true);
                    }
                    if (!sensor.getWindowKT1().isSelected()) {
                        sensor.getWindowKT1().setSelected(true);
                    }
                    if (!sensor.getWindowKT2().isSelected()) {
                        sensor.getWindowKT2().setSelected(true);
                    }
                    if (!sensor.getWindowSR1().isSelected()) {
                        sensor.getWindowSR1().setSelected(true);
                    }
                    if (!sensor.getWindowBR1().isSelected()) {
                        sensor.getWindowBR1().setSelected(true);
                    }

                    break;
                case "VOID":
                    break;
            }
        }
        sensor.publishData();
    }

}
