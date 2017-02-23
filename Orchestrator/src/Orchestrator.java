/**
 * Orchestrates components of MAPE-K loop.
 * Expects OpenHAB already started (Mosquitto.exe and WAMP too) 
 * Expects all package in JARfolder folder
 * 
 * Assumes that all processes run forever
 */

import java.io.IOException;

public class Orchestrator {
	private final static String JARfolder = System.getProperty("user.home") + "\\Documents\\Ruscio\\OpenHAB\\OpenHAB\\";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		//Sensor
		Runtime.getRuntime().exec(JARfolder + "Sensor.jar");
		
		//Monitoring
		Runtime.getRuntime().exec(JARfolder + "Monitoring.jar"); //open forever
		
		//Analysis
		Runtime.getRuntime().exec(JARfolder + "Analysis.jar"); //open forever
		
		//Decision
		//will execute automatically (OpenHAB). Expected it is already running
		
		//Execution
		Runtime.getRuntime().exec(JARfolder + "Execution.jar");
		
	}
}
