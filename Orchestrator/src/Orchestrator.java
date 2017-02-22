import java.io.IOException;

public class Orchestrator {
	private final static String JARfolder = System.getProperty("user.home") + "\\Documents\\Ruscio\\OpenHAB\\OpenHAB\\";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		while(true) {
			//Sensor
			Runtime.getRuntime().exec(JARfolder + "Sensor.jar");
			
			//Monitoring & Analysis
			Runtime.getRuntime().exec(JARfolder + "MonitoringAnalysis.jar");
			
			//Decision will execute automatically (OpenHAB, every minute)
			//Execution
			Runtime.getRuntime().exec(JARfolder + "Execution.jar");
			
			Thread.sleep(4000); //every 4 seconds
		}
	}
}
