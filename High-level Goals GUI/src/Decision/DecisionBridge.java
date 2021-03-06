package Decision;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.paho.client.mqttv3.MqttException;

import Goals.Goal;
import paho.Paho;

public class DecisionBridge {
	//Version with file:
	//private final static String file_configuration_OH = System.getProperty("user.home") + "\\Documents\\Ruscio\\OpenHAB\\high_level_goals.conf";
	
	/**
	 * Writes goals on a file, so that they can be imported into OpenHAB
	 * @param goals list of high-level goals
	 * @throws MqttException 
	 */
	public static void setGoals(List<Goal> goals) throws MqttException {
		Paho p = new Paho();
		for(Goal g : goals) {
			if(g.name() == Goal.goal_name.WINDOW) {
				p.send("goals/window", g.getInterval().date_beg() + "-" + g.getInterval().date_end());
			}
			else if(g.name() == Goal.goal_name.ELEC_MIN || g.name() == Goal.goal_name.ELEC_OPTM) {
				p.send("goals/electricity", g.name().toString());
			}
			else { //temperature goal
				p.send("goals/temperature", g.name().toString() + " " + g.getThreshold());
			}
		}
	}
	
	/** Version with file:
	 * Writes goals on a file, so that they can be imported into OpenHAB
	 * @param goals list of high-level goals
	 *\/
	public static void setGoals(List<Goal> goals) {
		List<String> lines = new LinkedList<String>();
		for(Goal g : goals) {
			lines.add(g.toString_file());
		}
		Path file = Paths.get(file_configuration_OH);
		try {
			file.toFile().createNewFile();
			Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error wrinting goals file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}*/

}
