/**
 * Analysis finds symptoms.
 * Reads goals and finds symptoms according to data in DB.
 * Expects WAMP and Mosquitto.exe open
 * Note: if you set goals when Analysis is running, Analysis must be reloaded!
 */
package Analysis;

import Storage.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttException;

//import Storage.File;
import paho.Paho;

public class Analysis {
    //Version with file:
	//private final static String goalsfile = System.getProperty("user.home") + "\\Documents\\Ruscio\\OpenHAB\\OpenHAB\\high_level_goals.conf"; //read only
	public static String window_goal = null;
	public static float threshold = 100;
	public static String date_beg, date_end;
	public static boolean windowsopen_done = false, windowsclose_done = false;

    public static void main(String[] args) throws MqttException, InterruptedException {
    	System.out.println("Welcome in Analysis");
        Paho p = new Paho();
        //needed not to send again and again the same symptom, allowing humans to open windows manually
        //(in fact, if analysis sends continuously "GROWING DOWN.THR ABOVE" with HEA_KEEP_WARM, humans cannot open windows for rule conflict)
        String oldTempSymptom = ""; 
        while (true) {
            //Version with file: File goal = new File(goalsfile);
            Storage s = new Storage();

            //PRUNING PHASE		
            //Check if last 2 inserted temperatures differ more than 2 degrees
            List<Object[]> temp = s.readMany("temperature", new String[]{"id", "degrees"}, "ORDER BY id DESC LIMIT 6");
            boolean cond2 = false;
            if (temp.size() > 2) {
                cond2 = (float) temp.get(0)[1] - (float) temp.get(1)[1] > 5.0f;
                if (cond2 || ((float) temp.get(0)[1] > 30 || (float) temp.get(0)[1] < -10)) {
                    s = new Storage();
                    s.delete("temperature", (int) temp.get(0)[0]);
                }
            }

            //Same for humidity (more than 1%)
            s = new Storage();
            List<Object[]> hum = s.readMany("humidity", new String[]{"id", "value"}, "ORDER BY id DESC LIMIT 5");
            if (hum.size() > 2 && ((int) hum.get(0)[1] - (int) hum.get(1)[1]) > 1) {
                s = new Storage();
                s.delete("humidity", (int) hum.get(0)[0]);
            }

            //Same for Electricity (more than 1)
            s = new Storage();
            List<Object[]> elec = s.readMany("electricity", new String[]{"id", "value"}, "ORDER BY id DESC LIMIT 5");
            if (elec.size() > 2 && ((int) elec.get(0)[1] - (int) elec.get(1)[1]) > 1) {
                s = new Storage();
                s.delete("electricity", (int) elec.get(0)[0]);
            }

            //SYMPTOMS CHECKING
            //p = new Paho();
            //getting people in all areas
            s = new Storage();
            List<Object[]> people = s.readMany("room", new String[]{"id", "name", "peopleIn"}, "");

            //if in area X there are no people => write to symptomfile. DECISION component decides: if elec.min. => turn lights off in area x; if elec.opt. => none
            for (Object[] o : people) {
                if ((int) o[2] == 0) {
                    p.send("symptoms/electricity", (String) o[1] + " VOID");
                }
                else { //else case added tonight
                	p.send("symptoms/electricity", (String) o[1] + " FULL");
                }
                Thread.sleep(1000);
            }

            //Version with file:
            //List<String> g = goal.readAll();
            
            //if time is getting to windows threshold => write to symtomfile only one time. DECISION component decides: if user has specified this goal && temperature > threshold + keep house warm => open windows; else => none
            if (window_goal != null) { //if window goal set
                //without windowsopen_done check, this symptom would be sent many times, and so user cannot open/close windows when he wants
                if (windowsopen_done == false && date_beg.equals(new SimpleDateFormat("HH:mm").format(new Date()))) {
                    p.send("symptoms/windows", "GETTING TO DATE TO OPEN WINDOWS");
                    windowsopen_done = true;
                    Thread.sleep(2000);
                    p.send("symptoms/windows", ""); //without this, there could be future rules conflicts in Decision.rules
                }

                if (windowsclose_done == false && date_end.equals(new SimpleDateFormat("HH:mm").format(new Date()))) {
                    p.send("symptoms/windows", "GETTING TO DATE TO CLOSE WINDOWS");
                    windowsclose_done = true;
                    Thread.sleep(2000);
                    p.send("symptoms/windows", ""); //without this, there could be future rules conflicts in Decision.rules
                }
            }

            //if temperature is getting to threshold, reason about last 5 temperatures to predict how temperature will grow
            //see documentation for more details. note: temp.get(0)[2] is current temperature.
            //getting avg of past temperatures and threshold
            boolean first = true; //temp variables for loop
            float avg = 0.0f; //avg of past temperatures
            if (temp.size() > 2) {
                float curtemp = (float) temp.get(0)[1];
                for (Object[] o : temp) {
                    if (first) {
                        first = false;
                        continue;
                    }
                    avg += (float) o[1];
                }
                avg = avg / 5;
                //analysis of temperature growing graph
                int growing = 2; //temperature is growing? 0 = false, 1 = true, 2 = avg==curtemp==threshold
                String temp1 = "";
                if (avg < curtemp) {
                    growing = 1;
                }
                if (avg > curtemp) {
                    growing = 0;
                }
                System.out.println("avg: " + avg + "; curtemp: " + curtemp + "; thr: " + threshold);
                if (growing == 1) {
                    temp1 = "GROWING UP.";

                    if (curtemp <= threshold) {
                        temp1 += "THR ABOVE";
                    } else {
                        temp1 += "THR DOWN";
                    }
                } else if (growing == 0) {
                    temp1 = "GROWING DOWN.";
                    if (curtemp >= threshold) {
                        temp1 += "THR DOWN";
                    } else {
                        temp1 += "THR ABOVE";
                    }
                } else {
                    temp1 = "STABILITY.";
                    if (curtemp >= threshold) {
                        temp1 += "THR DOWN";
                    } else {
                        temp1 += "THR ABOVE";
                    }
                }
                if (!temp.equals("")) //if temperature not stable
                {
                	if(threshold != 100 && oldTempSymptom.equals(temp1) == false) { //if temperature goal is set
                		p.send("symptoms/temperature", temp1);
                		oldTempSymptom = temp1;
                	}
                }
            }

            //symptoms sent. Analysis work is finished. Now Decision comes into play
            //p.finalize();
            Thread.sleep(1900);
        }
        
    }

}
