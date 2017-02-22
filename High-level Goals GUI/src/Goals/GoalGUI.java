package Goals;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import Goals.Goal.goal_name;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.SpinnerNumberModel;
import java.text.ParseException;
import javax.swing.JCheckBox;

public class GoalGUI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	JButton btnMinimize, btnOptimizeEnergyConsumption, btnKeep, btnKeepHouseWarm, btnConfirm, btnCancel;
	JSpinner spinner, spinner_1;
	
	private final boolean DEBUG = true;
	
	private List<Goal> goals;
	private JCheckBox chckbxEnabled;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GoalGUI window = new GoalGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GoalGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.goals = new LinkedList<Goal>();
		
		frame = new JFrame("High-level goals GUI");
		frame.setBounds(100, 100, 445, 335);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblElectricity = new JLabel("Electricity");
		
		btnMinimize = new JButton("Minimize energy consumption");
		btnMinimize.setToolTipText("If there are no people at home in a given area X, then after 1 minute lights in area X are turned off");
		btnMinimize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(btnOptimizeEnergyConsumption.isEnabled()) { // if user has already selected "minimize energy consumption"
					btnOptimizeEnergyConsumption.setEnabled(false);
					btnMinimize.setBackground(Color.GREEN);
					Goal newgoal = new Goal(Goal.goal_name.ELEC_MIN);
					goals.add(newgoal);
				}
				else {
					btnOptimizeEnergyConsumption.setEnabled(true);
					btnMinimize.setBackground(new Color(-986896));;
					goals.remove(new Goal(Goal.goal_name.ELEC_MIN));
				}
			}
		});
		
		btnOptimizeEnergyConsumption = new JButton("Optimize energy consumption");
		btnOptimizeEnergyConsumption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnMinimize.isEnabled()) { // if user has already selected "optimize energy consumption"
					btnMinimize.setEnabled(false);
					btnOptimizeEnergyConsumption.setBackground(Color.GREEN);
					Goal newgoal = new Goal(Goal.goal_name.ELEC_OPTM);
					goals.add(newgoal);
				}
				else {
					btnMinimize.setEnabled(true);
					btnOptimizeEnergyConsumption.setBackground(new Color(-986896));;
					goals.remove(new Goal(Goal.goal_name.ELEC_OPTM));
				}
			}
		});
		btnOptimizeEnergyConsumption.setToolTipText("If there are no people at home in a given area X, then lights in area X can stay on");
		
		JLabel lblNewLabel = new JLabel("Heating");
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//adding windows goal if enabled
				goals.remove(new Goal(goal_name.WINDOW));
				if(chckbxEnabled.isSelected()) {
					try {
						goals.add(new Goal(Goal.goal_name.WINDOW, new Interval(textField.getText() + "-" + textField_1.getText())));
					} catch (ParseException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "title", JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
						return;
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "title", JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
						return;
					}
				}

				if(DEBUG) {
					String r = "";
					for(Goal g : goals) {
						r += g.toString() + " ";
					}
					JOptionPane.showMessageDialog(null, r, "title", JOptionPane.INFORMATION_MESSAGE);
				}
				
				
				try {
					GoalsChecking.check(goals);
				}
				catch(GoalsConflictException e1) {
					JOptionPane.showMessageDialog(null, "Some goals are in conflict: " + e1.cause, "Goals conflict", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}
		});
		btnConfirm.setBackground(Color.GREEN);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				main(null);
			}
		});
		btnCancel.setBackground(Color.RED);
		
		btnKeep = new JButton("Keep house fresh");
		btnKeep.setToolTipText("Heating off during daytime. It can be turned on if temperature is less than a certain value Y");
		btnKeep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnKeepHouseWarm.isEnabled()) { // if user has already selected "keep house fresh"
					btnKeepHouseWarm.setEnabled(false);
					spinner.setEnabled(false);
					spinner_1.setEnabled(false);
					btnKeep.setBackground(Color.GREEN);
					Goal newgoal = new Goal(Goal.goal_name.HEA_KEEP_FRESH, Float.parseFloat(spinner.getValue().toString()));
					goals.add(newgoal);
				}
				else {
					btnKeepHouseWarm.setEnabled(true);
					spinner.setEnabled(true);
					spinner_1.setEnabled(true);
					btnKeep.setBackground(new Color(-986896));;
					goals.remove(new Goal(Goal.goal_name.HEA_KEEP_FRESH));
				}
			}
		});
		
		btnKeepHouseWarm = new JButton("Keep house warm");
		btnKeepHouseWarm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnKeep.isEnabled()) { // if user has already selected "keep house warm"
					btnKeep.setEnabled(false);
					btnKeepHouseWarm.setBackground(Color.GREEN);
					Goal newgoal = new Goal(Goal.goal_name.HEA_KEEP_WARM, Float.parseFloat(spinner_1.getValue().toString()));
					spinner_1.setEnabled(false);
					spinner.setEnabled(false);
					goals.add(newgoal);
				}
				else {
					btnKeep.setEnabled(true);
					btnKeepHouseWarm.setBackground(new Color(-986896));
					spinner_1.setEnabled(true);
					spinner.setEnabled(true);
					goals.remove(new Goal(Goal.goal_name.HEA_KEEP_WARM));
				}
			}
		});
		btnKeepHouseWarm.setToolTipText("Heating on during daytime if temperature is less than a certain value Y");
		
		JLabel lblNewLabel_1 = new JLabel("Windows and balconies");
		lblNewLabel_1.setToolTipText("You can open windows and balconies during an interval of time that the user can choose");
		
		JLabel lblOpenTime = new JLabel("Open time");
		
		JLabel lblCloseTime = new JLabel("Close time");
		
		textField = new JTextField();
		textField.setText("08:00");
		textField.setToolTipText("format: [0-1][0-9]:[0-5][0-9]");
		textField.setColumns(5);
		
		textField_1 = new JTextField();
		textField_1.setText("08:30");
		textField_1.setToolTipText("format: [0-1][0-9]:[0-5][0-9]");
		textField_1.setColumns(5);
		
		JLabel lblTurnOnIf = new JLabel("Threshold");
		
		JLabel lblThreshold = new JLabel("Threshold");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Float(10), new Float(-10), new Float(30), new Float(1)));
		spinner.setToolTipText("Heating off during daytime. It can be turned on if temperature is less than a certain value Y");
		
		spinner_1 = new JSpinner();
		spinner_1.setToolTipText("Heating on during daytime if temperature is less than a certain value Y");
		spinner_1.setModel(new SpinnerNumberModel(10.0, -10.0, 30.0, 1.0));
		
		chckbxEnabled = new JCheckBox("Enabled?", true);
		chckbxEnabled.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox c = (JCheckBox) e.getSource();
		        if (c.isSelected()) {
		            textField.setEnabled(true);
		            textField_1.setEnabled(true);
		        } else {
		        	textField.setEnabled(false);
		        	textField_1.setEnabled(false);
		        }
			}	
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblElectricity)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnOptimizeEnergyConsumption)
								.addComponent(btnMinimize)))
						.addComponent(lblNewLabel)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnConfirm)
							.addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
							.addComponent(btnCancel))
						.addComponent(lblNewLabel_1)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblCloseTime)
									.addPreferredGap(ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
									.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblOpenTime)
									.addPreferredGap(ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnKeepHouseWarm, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
								.addComponent(btnKeep, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblTurnOnIf)
							.addGap(18)
							.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblThreshold)
							.addGap(18)
							.addComponent(spinner_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(chckbxEnabled))
					.addGap(111))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblElectricity)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnMinimize)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnOptimizeEnergyConsumption)
					.addGap(18)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnKeep)
						.addComponent(lblTurnOnIf)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnKeepHouseWarm)
						.addComponent(lblThreshold)
						.addComponent(spinner_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(chckbxEnabled))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOpenTime)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCloseTime)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnConfirm)
						.addComponent(btnCancel))
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
