package Goals;

import java.util.List;

import Decision.DecisionBridge;
import Goals.Goal.goal_name;

public class GoalsChecking {
	
	private final static int MIN_NO = 10; //number of minutes for windows

	public static void check(List<Goal> goals) throws GoalsConflictException {
		boolean problem = false;
		String cause = null;
		
		if(goals.contains(new Goal(Goal.goal_name.ELEC_MIN)) && goals.contains(new Goal(Goal.goal_name.ELEC_OPTM))) {
			problem = true;
			cause = "cannot minimize and optimize electricity together";
		}
		
		if(goals.contains(new Goal(goal_name.HEA_KEEP_FRESH)) && goals.contains(new Goal(Goal.goal_name.HEA_KEEP_WARM))) {
			problem = true;
			cause = "cannot keep fresh and warm together";
		}
		
		if(goals.contains(new Goal(goal_name.WINDOW)) && (goals.contains(new Goal(goal_name.HEA_KEEP_WARM))) ) {
			int i = goals.indexOf(new Goal(goal_name.WINDOW));
			if(goals.get(i).getInterval().duration() > MIN_NO * 60) {
				problem = true;
				cause = "cannot open windows for more than 10 minutes while keeping house warm";
			}
		}
		
		if(!problem) {
			DecisionBridge.setGoals(goals);
			return;
		}
		
		throw new GoalsConflictException(cause);

	}
	
}

class GoalsConflictException extends Exception {
	private static final long serialVersionUID = 1L;
	public GoalsConflictException(String cause) {
		this.cause = cause;
	}
	public String cause;
}
