/**
 * Analysis finds symptoms.
 * Reads goals and finds symptoms according to data in DB.
 * Expects WAMP and Mosquitto.exe open
 */
package Analysis;

import Storage.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttException;

import Storage.File;
import paho.Paho;

public class Analysis {

	private final static String goalsfile = System.getProperty("user.home") + "\\Documents\\Ruscio\\OpenHAB\\OpenHAB\\high_level_goals.conf"; //read only
	
	public static void main(String[] args) throws MqttException {
		File goal = new File(goalsfile);
		Storage s = new Storage();
				
		//PRUNING PHASE		
		//Check if last 2 inserted temperatures differ more than 2 degrees
		List<Object[]> temp = s.readMany("temperature", new String[] {"id", "degrees"}, "ORDER BY id DESC LIMIT 10");
		int tempsize = temp.size();
		boolean cond2 = false;
		if(tempsize > 2 )
			cond2 = (float) temp.get(0)[1] - (float) temp.get(1)[1] > 2.0f;
		if( tempsize > 2 && cond2 || ((float) temp.get(0)[1] > 30 || (float) temp.get(0)[1] < -10) ) {
			s = new Storage();
			s.delete("temperature", (int)temp.get(0)[0]);
		}
		
		//Same for humidity (more than 1%)
		s = new Storage();
		List<Object[]> hum = s.readMany("humidity", new String[] {"id", "value"}, "ORDER BY id DESC LIMIT 5");
		if( hum.size() > 2 && ((int) hum.get(0)[1] - (int) hum.get(1)[1]) > 1) {
			s = new Storage();
			s.delete("humidity", (int)hum.get(0)[0]);
		}
		
		//Same for Electricity (more than 1)
		s = new Storage();
		List<Object[]> elec = s.readMany("electricity", new String[] {"id", "value"}, "ORDER BY id DESC LIMIT 5");
		if( elec.size() > 2 && ((int) elec.get(0)[1] - (int) elec.get(1)[1]) > 1) {
			s = new Storage();
			s.delete("electricity", (int)elec.get(0)[0]);
		}
		
		
		
		//SYMPTOMS CHECKING
		Paho p = new Paho();
		//getting people in all areas
		s = new Storage();
		List<Object[]> people = s.readMany("room", new String[] {"id", "name", "peopleIn"}, "");
		
		//if in area X there are no people => write to symptomfile. DECISION component decides: if elec.min. => turn lights off in area x; if elec.opt. => none
		for(Object[] o : people) {
			if((int) o[2] == 0)
				p.send("symptoms/electricity", (String) o[1] + " VOID" );
		}
		
		//if time is getting to windows threshold => write to symtomfile. DECISION component decides: if user has specified this goal && temperature > threshold + keep house warm => open windows; else => none
		List<String> g = goal.readAll();
		int index = g.indexOf("WINDOW");
		if(index != -1) {
			String timebeg = g.get(index+1);
			String timeend = g.get(index + 2);
			try {
				Date beg = new SimpleDateFormat("HH:mm").parse(timebeg);
				if(beg.equals(new SimpleDateFormat("HH:mm").format(new Date()))) {
					p.send("symptoms/windows", "GETTING TO DATE TO OPEN WINDOWS" );
				}
				
				Date end = new SimpleDateFormat("HH:mm").parse(timeend);
				if(end.equals(new SimpleDateFormat("HH:mm").format(new Date()))) {
					p.send("symptoms/windows", "GETTING TO DATE TO CLOSE WINDOWS" );
				}
			} catch (ParseException e) {
				System.err.println("error with date beg in file for high-level goals");
			}
		}
		
		//if temperature is getting to threshold, reason about last 5 temperatures to predict how temperature will grow
		//see documentation for more details. note: temp.get(0)[2] is current temperature.
			//getting avg of past temperatures and threshold
		int i;
		for(i = 0; i < g.size(); i++)
			if(g.get(i).contains("KEEP"))
				break;
		float threshold = Float.parseFloat(g.get(++i)); //temperature threshold
		boolean first = true; //temp variables for loop
		float avg = 0.0f; //avg of past temperatures
		float curtemp = (float) temp.get(0)[1];
		for(Object[] o : temp) {
			if(first) { first = false; continue; }
			avg = (float)o[1];
		}
		avg = avg / 5;
			//analysis of temperature growing graph
		int growing = 2; //temperature is growing? 0 = false, 1 = true, 2 = avg==curtemp==threshold
		String temp1 = "";
		if(avg < curtemp)
			growing = 1;
		if(avg > curtemp)
			growing = 0;
		System.out.println("avg: " + avg + "; curtemp: " +curtemp + "; thr: " +threshold);
		if(growing == 1) {
			temp1 = "GROWING UP.";
			
			if(curtemp <= threshold) {
				temp1 += "THR ABOVE";
			}
			else {
				temp1 += "THR DOWN";
			}
		}
		else if(growing == 0) {
			temp1 = "GROWING DOWN.";
			if(curtemp >= threshold) {
				temp1 += "THR DOWN";
			}
			else {
				temp1 += "THR ABOVE";
			}
		}
		if(!temp.equals("")) //if temperature not stable
			p.send("symptoms/temperature", temp1);		
		
		//symptoms sent. Analysis work is finished. Now Decision comes into play
		p.finalize();
		
	}

}
