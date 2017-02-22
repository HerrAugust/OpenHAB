package Goals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Goal {
	public enum goal_name { ELEC_MIN, ELEC_OPTM, HEA_KEEP_WARM, HEA_KEEP_FRESH, WINDOW };
	
	private goal_name name;
	private float threshold; //to be set only for heating
	private Interval interval; //to be set only for windows
	
	public Goal(goal_name name, Interval interval) {
		if(name == goal_name.WINDOW) {
			this.name = name;
			this.interval = interval;
		}
	}
	
	public Goal(goal_name name, float threshold) {
		if(name == goal_name.HEA_KEEP_FRESH || name == goal_name.HEA_KEEP_WARM) {
			this.name = name;
			this.threshold = threshold;
		}
	}
	
	public Goal(goal_name name) {
		this.name = name;
	}
	
	public Interval getInterval() {
		return this.interval;
	}
	
	public float getThreshold() {
		return threshold;
	}
	
	public goal_name name() {
		return name;
	}
		
	@Override
	public boolean equals(Object g) {
		Goal curgoal = (Goal) g;
		if(curgoal.name == this.name) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		String r = "";
		r = name.toString();
		if(name == goal_name.HEA_KEEP_FRESH || name == goal_name.HEA_KEEP_WARM) {
			r += " (" + this.threshold + ")";
		}
		return r;
	}
	
	/**
	 * Writes this goal as plain text.
	 * Note: specific for DecisionBridge.java!
	 * @return goal as plain text
	 */
	public String toString_file() {
		String r = "";
		switch(name) {
		case ELEC_MIN: case ELEC_OPTM:
			r = name.toString();
			break;
		case HEA_KEEP_WARM: case HEA_KEEP_FRESH:
			r = name.toString() + System.lineSeparator() + this.threshold;
			break;
		case WINDOW:
			r = name.toString() + System.lineSeparator() + this.interval.date_beg() + System.lineSeparator() + this.interval.date_end();
			break;
		default:
			break;
		}
		return r;
	}
	
}
