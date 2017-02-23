/**
 * Orchestrates components of MAPE-K loop.
 * Expects OpenHAB already started (Mosquitto.exe and WAMP too) 
 * Expects all package in JARfolder folder
 * 
 * Assumes that all processes (except Monitoring) close within 2 seconds
 */

import java.io.IOException;

public class Orchestrator {
	private final static String JARfolder = System.getProperty("user.home") + "\\Documents\\Ruscio\\OpenHAB\\OpenHAB\\";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Runtime.getRuntime().exec(JARfolder + "Monitoring.jar"); //open forever
		while(true) {
			//Sensor
			Runtime.getRuntime().exec(JARfolder + "Sensor.jar");
			
			//Monitoring & Analysis
			//already started
			
			//Decision
			//will execute automatically (OpenHAB). Expected it is already running
			
			//Execution
			Runtime.getRuntime().exec(JARfolder + "Execution.jar");
			
			Thread.sleep(4000); //every 4 seconds
		}
	}
}
