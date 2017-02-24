/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package execution;

import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttException;

import paho.Paho;
import storage.Storage;

/**
 *
 * @author Kelwin
 */
public class Execution {

    /**
     * @param args the command line arguments
     * @throws org.eclipse.paho.client.mqttv3.MqttException
     */
    public static void main(String[] args) throws MqttException, InterruptedException {
        String closeLightsArea = "VOID";
        String closeLight = "VOID";
        String openLight = "VOID";
        String closeWindow = "VOID";
        String openWindow = "VOID";
        String windows = "VOID";
        String temperatura = "VOID";

        for (;;) {
            //Thread.sleep(2000);
            Storage s = new Storage();
            List<Object[]> decision = s.readMany("decision", new String[]{"id", "value"}, "ORDER BY id DESC LIMIT 5");
            if (!decision.isEmpty()) {
                s = new Storage();
                s.deleteAll("decision");

                for (int j = 0; j < decision.size(); j++) {
                    String sym = (String) decision.get(j)[1];
                    String[] parts = sym.split(":");

                    switch (parts[0].trim()) {
                        case "close window":
                            switch (parts[1].trim()) {
                                case "LivingRoom_Windows_l1":
                                    closeWindow = "LivingRoom_Windows_l1";
                                    break;
                                case "LivingRoom_Windows_l2":
                                    closeWindow = "LivingRoom_Windows_l2";
                                    break;
                                case "LivingRoom_Windows_l3":
                                    closeWindow = "LivingRoom_Windows_l3";
                                    break;
                                case "Kitchen_Windows_l1":
                                    closeWindow = "Kitchen_Windows_l1";
                                    break;
                                case "Kitchen_Windows_l2":
                                    closeWindow = "Kitchen_Windows_l2";
                                    break;
                                case "SleepingRoom_Windows_l1":
                                    closeWindow = "SleepingRoom_Windows_l1";
                                    break;
                                case "Bathroom_Windows_l1":
                                    closeWindow = "Bathroom_Windows_l1";
                                    break;
                                    	
                            }
                            break;
                        case "open window":
                            switch (parts[1].trim()) {
                                case "LivingRoom_Windows_l1":
                                    openWindow = "LivingRoom_Windows_l1";
                                    break;
                                case "LivingRoom_Windows_l2":
                                    openWindow = "LivingRoom_Windows_l2";
                                    break;
                                case "LivingRoom_Windows_l3":
                                    openWindow = "LivingRoom_Windows_l3";
                                    break;
                                case "Kitchen_Windows_l1":
                                    openWindow = "Kitchen_Windows_l1";
                                    break;
                                case "Kitchen_Windows_l2":
                                    openWindow = "Kitchen_Windows_l2";
                                    break;
                                case "SleepingRoom_Windows_l1":
                                    openWindow = "SleepingRoom_Windows_l1";
                                    break;
                                case "Bathroom_Windows_l1":
                                    openWindow = "Bathroom_Windows_l1";
                                    break;
                            }
                            break;
                        case "open light":
                            switch (parts[1].trim()) {
                                case "LivingRoom_Lights_l1":
                                    openLight = "LivingRoom_Lights_l1";
                                    break;
                                case "LivingRoom_Lights_l2":
                                    openLight = "LivingRoom_Lights_l2";
                                    break;
                                case "LivingRoom_Lights_l3":
                                    openLight = "LivingRoom_Lights_l3";
                                    break;
                                case "Kitchen_Lights_l1":
                                    openLight = "Kitchen_Lights_l1";
                                    break;
                                case "Kitchen_Lights_l2":
                                    openLight = "Kitchen_Lights_l2";
                                    break;
                                case "SleepingRoom_Lights_l1":
                                    openLight = "SleepingRoom_Lights_l1";
                                    break;
                                case "SleepingRoom_Lights_l2":
                                    openLight = "SleepingRoom_Lights_l2";
                                    break;
                                case "Bathroom_Lights_l1":
                                    openLight = "Bathroom_Lights_l1";
                                    break;
                            }

                            break;
                        case "close light":

                            switch (parts[1].trim()) {
                                case "LivingRoom_Lights_l1":
                                    closeLight = "LivingRoom_Lights_l1";
                                    break;
                                case "LivingRoom_Lights_l2":
                                    closeLight = "LivingRoom_Lights_l2";
                                    break;
                                case "LivingRoom_Lights_l3":
                                    closeLight = "LivingRoom_Lights_l3";
                                    break;
                                case "Kitchen_Lights_l1":
                                    closeLight = "Kitchen_Lights_l1";
                                    break;
                                case "Kitchen_Lights_l2":
                                    closeLight = "Kitchen_Lights_l2";
                                    break;
                                case "SleepingRoom_Lights_l1":
                                    closeLight = "SleepingRoom_Lights_l1";
                                    break;
                                case "SleepingRoom_Lights_l2":
                                    closeLight = "SleepingRoom_Lights_l2";
                                    break;
                                case "Bathroom_Lights_l1":
                                    closeLight = "Bathroom_Lights_l1";
                                    break;
                            }
                            break;
                        case "temperature":
                            temperatura = parts[1].trim();
                            break;
                        case "close windows":
                            windows = "OFF";
                            break;
                        case "open windows":
                            windows = "ON";
                            break;
                        case "close lights":
                            switch (parts[1].trim()) {
                                case "livingroom":
                                    closeLightsArea = "livingroom";
                                    break;
                                case "bathroom":
                                    closeLightsArea = "bathroom";
                                    break;
                                case "kitchen":
                                    closeLightsArea = "kitchen";
                                    break;
                                case "sleepingroom":
                                    closeLightsArea = "sleepingroom";
                                    break;
                            }
                            break;
                    }
                }
                Paho p = new Paho();

                p.send("home/general/decision/openwindow", openWindow);
                p.send("home/general/decision/closewindow", closeWindow);
                p.send("home/general/decision/openlight", openLight);
                p.send("home/general/decision/closelight", closeLight);
                p.send("home/general/decision/windowsdecision", windows);
                p.send("home/general/decision/temperatura", temperatura);
                p.send("home/general/decision/closelights", closeLightsArea);
               
                
                System.out.println("\n");
                
                closeLight = "VOID";
                openLight = "VOID";
                closeWindow = "VOID";
                openWindow = "VOID";
                windows = "VOID";
                temperatura = "VOID";
                closeLightsArea = "VOID";
            }
        }
    }
}
