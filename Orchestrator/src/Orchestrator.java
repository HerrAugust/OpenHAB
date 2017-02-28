/**
 * Orchestrates components of MAPE-K loop.
 * Expects OpenHAB already started (Mosquitto.exe and WAMP too) 
 * Expects all package in JARfolder folder
 * 
 * Assumes that all processes run forever
 */

import java.io.IOException;

import javax.swing.JOptionPane;

public class Orchestrator {
	private final static String JARfolder = System.getProperty("user.home") + "\\Documents\\Ruscio\\OpenHAB\\OpenHAB\\";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String t = "java -jar ";
		//Sensor
		Runtime.getRuntime().exec(t + "Sensor.jar");
		
		//Monitoring
		Runtime.getRuntime().exec(t + "Monitoring.jar"); //open forever
		
		//Execution
		Runtime.getRuntime().exec(t + "Execution.jar");
		
		//Analysis
		Runtime.getRuntime().exec(t + "Analysis.jar"); //open forever
		
		//Decision
		//will execute automatically (OpenHAB). Expected it is already running
				
	}
}
