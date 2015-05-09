/**
 * 
 */
package au.id.cpd.algorithms.ui;

import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * @author Chris Davey
 * The preferences panel allows the user
 * to partition the training and test set
 * and to define the column number (0 indexed)
 * of the target column.
 *
 */
public class DataPreferencesPanel extends JPanel implements ActionListener, ChangeListener {

	private JTextField trainPercentField;
	private JTextField testPercentField;
	private JComboBox targets;
	private IMenuActionEventListener listener;
	private JButton okBtn;
	private JButton canBtn;
	private JCheckBox normalCheck;
	private boolean isNormal;
	/**
	 * default constructor.
	 */
	public DataPreferencesPanel() {
		super();
		this.isNormal = false;
		JPanel container = new JPanel();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		container.setBorder(border);
		container.setLayout(new GridLayout(5,2));
		JLabel label = new JLabel("Percent Train:");
		container.add(label);
		this.trainPercentField = new JTextField();
		this.trainPercentField.setText("70");
		this.trainPercentField.addActionListener(this);
		container.add(this.trainPercentField);
		label = new JLabel("Percent Test:");
		container.add(label);
		this.testPercentField = new JTextField();
		this.testPercentField.setEditable(false);
		this.testPercentField.setText("30");
		container.add(this.testPercentField);
		label = new JLabel("Target Column:");
		container.add(label);
		this.targets = new JComboBox();
		container.add(this.targets);
		container.add(new JLabel("Data is Normalised:"));
		this.normalCheck = new JCheckBox();
		container.add(this.normalCheck);
		this.normalCheck.addChangeListener(this);
		this.okBtn = new JButton("OK");
		this.canBtn = new JButton("Cancel");
		this.okBtn.addActionListener(this);
		this.canBtn.addActionListener(this);
		container.add(okBtn);
		container.add(canBtn);
		container.setPreferredSize(new Dimension(200, 100));
		this.setLayout(new FlowLayout());
		this.add(container);
		this.add(new JPanel());
	}

	/**
	 * Action event handler.
	 */
	public void actionPerformed(ActionEvent e) {
		if (this.listener == null) return;
		if (e.getSource() == this.okBtn) {
			this.listener.actionPerformed(new MenuActionEvent(this, MenuActionType.EDIT_DATA_PROPERTIES_OK, "Edit Data OK"));
		} else if (e.getSource() == this.canBtn) {
			this.listener.actionPerformed(new MenuActionEvent(this, MenuActionType.EDIT_DATA_PROPERTIES_CANCEL, "Edit Data Cancel"));
		} else if (e.getSource() == this.trainPercentField) {
			String val = this.trainPercentField.getText();
			if (val.length() == 0) {
				this.trainPercentField.setText("70");
				val = "70";
			}
			double train = Double.parseDouble(val);
			double test = 100 - train;
			this.testPercentField.setText(""+test);
		}
	}
	
	/**
	 * Event handler for check box.
	 */
	public void stateChanged(ChangeEvent e) {
		this.isNormal = !this.isNormal;
	}
	
	/**
	 * Update the list selection to allow the user to choose
	 * from a set of column numbers.
	 * @param cnt
	 */
	public void setTargetColumnCount(int cnt) {
		Object[] data = new Object[cnt];
		for(int i=0;i<data.length;i++) {
			data[i] = new Integer(i);
		}
		this.targets.setModel(new DefaultComboBoxModel(data));
		this.targets.validate();
	}
	
	/**
	 * @return the targets
	 */
	public JComboBox getTargets() {
		return targets;
	}

	/**
	 * @param targets the targets to set
	 */
	public void setTargets(JComboBox targets) {
		this.targets = targets;
	}

	/**
	 * @return the testPercentField
	 */
	public JTextField getTestPercentField() {
		return testPercentField;
	}

	/**
	 * @param testPercentField the testPercentField to set
	 */
	public void setTestPercentField(JTextField testPercentField) {
		this.testPercentField = testPercentField;
	}

	/**
	 * @return the trainPercentField
	 */
	public JTextField getTrainPercentField() {
		return trainPercentField;
	}

	/**
	 * @param trainPercentField the trainPercentField to set
	 */
	public void setTrainPercentField(JTextField trainPercentField) {
		this.trainPercentField = trainPercentField;
	}

	/**
	 * @return the listener
	 */
	public IMenuActionEventListener getListener() {
		return listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(IMenuActionEventListener listener) {
		this.listener = listener;
	}

	/**
	 * @return the normalCheck
	 */
	public JCheckBox getNormalCheck() {
		return normalCheck;
	}

	/**
	 * @param normalCheck the normalCheck to set
	 */
	public void setNormalCheck(JCheckBox normalCheck) {
		this.normalCheck = normalCheck;
	}

	/**
	 * @return the isNormal
	 */
	public boolean isNormal() {
		return isNormal;
	}

	/**
	 * @param isNormal the isNormal to set
	 */
	public void setNormal(boolean isNormal) {
		this.isNormal = isNormal;
	}
	
	
	
	
	
}
