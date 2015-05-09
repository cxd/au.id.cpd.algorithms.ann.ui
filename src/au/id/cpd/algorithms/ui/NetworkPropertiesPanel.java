/**
 * 
 */
package au.id.cpd.algorithms.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import au.id.cpd.algorithms.classifier.ANN.*;

/**
 * @author cd
 *
 */
public class NetworkPropertiesPanel extends JScrollPane implements ActionListener {
	
	private JTextField epochField;
	private JTextField learnRateField;
	private JTextField biasField;
	private JTextField momentumField;
	private JTextField inputCountField;
	private JTextField hiddenLayerCountField;
	private JTextField hiddenUnitCountField;
	private JTextField outputUnitCountField;
	private JTextField classInstanceListField;
	private JTextField classThresholdField;
	private JComboBox activatorTypeList;
	private JTextField errorThresholdField;
	private JCheckBox continuousField;
	private JButton okButton;
	private JButton cancelButton;
	
	private boolean continuous = false;
	
	// internal listener.
	private IMenuActionEventListener listener;
	
	/**
	 * default constructor.
	 */
	public NetworkPropertiesPanel() {
		super();
		this.init();
	}
	
	/**
	 * Initialise the properties panel.
	 */
	private void init() {
		JPanel container = new JPanel();
		container.setPreferredSize(new Dimension(400, 450));
		container.setLayout(new GridLayout(14, 2));
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		container.setBorder(border);
		
		JLabel lbl = new JLabel("Number of Input Units: ");
		container.add(lbl);
		this.inputCountField = new JTextField();
		container.add(this.inputCountField);
		
		lbl = new JLabel("Number of Output Units: ");
		container.add(lbl);
		this.outputUnitCountField = new JTextField();
		container.add(this.outputUnitCountField);
		
		lbl = new JLabel("Number of Hidden Layers: ");
		container.add(lbl);
		this.hiddenLayerCountField = new JTextField();
		container.add(this.hiddenLayerCountField);
		
		lbl = new JLabel("<html><body>Number of Hidden<br>Units per Layer: </body></html>");
		container.add(lbl);
		this.hiddenUnitCountField = new JTextField();
		container.add(this.hiddenUnitCountField);
		
		lbl = new JLabel("Learning Rate:");
		container.add(lbl);
		this.learnRateField = new JTextField();
		container.add(this.learnRateField);
		
		lbl = new JLabel("Network Bias:");
		container.add(lbl);
		this.biasField = new JTextField();
		container.add(this.biasField);
		
		lbl = new JLabel("Momentum:");
		container.add(lbl);
		this.momentumField = new JTextField();
		container.add(this.momentumField);
		
		lbl = new JLabel("Number of Epochs:");
		container.add(lbl);
		this.epochField = new JTextField();
		container.add(this.epochField);
		
		lbl = new JLabel("Error Threshold:");
		container.add(lbl);
		this.errorThresholdField = new JTextField();
		container.add(this.errorThresholdField);
		
		lbl = new JLabel("<html><body>Classification List (csv): <br><font size=\"2\" color=\"red\"><i>if no. of classes > 2</i></font><br><font size=\"2\" color=\"red\"><i>Count should equal output count.</i></font></body></html>");
		container.add(lbl);
		this.classInstanceListField = new JTextField();
		container.add(this.classInstanceListField);
		
		lbl = new JLabel("<html><body>Classification Threshold: <br><font size=\"2\" color=\"red\"><i>if no. of classes = 2</i></font></body></html>");
		container.add(lbl);
		this.classThresholdField = new JTextField();
		container.add(this.classThresholdField);
		
		lbl = new JLabel("Unit Activation Type:");
		container.add(lbl);
		String[] types = new String[ActivationType.values().length];
		int cnt = 0;
		for(ActivationType t : ActivationType.values()) {
			types[cnt] = t.getName();
			++cnt;
		}
		this.activatorTypeList = new JComboBox(types);		
		container.add(this.activatorTypeList);
		
		lbl = new JLabel("Continuous Output");
		container.add(lbl);
		continuousField = new JCheckBox();
		continuousField.addItemListener(
			new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					continuous = (e.getStateChange() == ItemEvent.SELECTED);
				}
			}
		);
		container.add(continuousField);
		
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		container.add(okButton);
		container.add(cancelButton);
		
		JPanel space = new JPanel();
		JPanel group = new JPanel();
		group.setLayout(new BorderLayout());
		group.add(container, BorderLayout.CENTER);
		group.add(space, BorderLayout.SOUTH);
		this.setViewportView(group);
	}

	/**
	 * Action listener interface.
	 */
	public void actionPerformed(ActionEvent e) {
		if (this.listener == null) return;
		if (e.getSource() == this.okButton) {
			this.listener.actionPerformed(new MenuActionEvent(this, MenuActionType.EDIT_NETWORK_PROPERTIES_OK, "OK"));
		} else if (e.getSource() == this.cancelButton) {
			this.listener.actionPerformed(new MenuActionEvent(this, MenuActionType.EDIT_NETWORK_PROPERTIES_CANCEL, "Cancel"));
		}
	}
	
	/**
	 * @return the activatorTypeList
	 */
	public JComboBox getActivatorTypeList() {
		return activatorTypeList;
	}

	/**
	 * @param activatorTypeList the activatorTypeList to set
	 */
	public void setActivatorTypeList(JComboBox activatorTypeList) {
		this.activatorTypeList = activatorTypeList;
	}

	/**
	 * @return the classInstanceListField
	 */
	public JTextField getClassInstanceListField() {
		return classInstanceListField;
	}

	/**
	 * @param classInstanceListField the classInstanceListField to set
	 */
	public void setClassInstanceListField(JTextField classInstanceListField) {
		this.classInstanceListField = classInstanceListField;
	}

	/**
	 * @return the classThresholdField
	 */
	public JTextField getClassThresholdField() {
		return classThresholdField;
	}

	/**
	 * @param classThresholdField the classThresholdField to set
	 */
	public void setClassThresholdField(JTextField classThresholdField) {
		this.classThresholdField = classThresholdField;
	}

	/**
	 * @return the epochField
	 */
	public JTextField getEpochField() {
		return epochField;
	}

	/**
	 * @param epochField the epochField to set
	 */
	public void setEpochField(JTextField epochField) {
		this.epochField = epochField;
	}

	/**
	 * @return the hiddenLayerCountField
	 */
	public JTextField getHiddenLayerCountField() {
		return hiddenLayerCountField;
	}

	/**
	 * @param hiddenLayerCountField the hiddenLayerCountField to set
	 */
	public void setHiddenLayerCountField(JTextField hiddenLayerCountField) {
		this.hiddenLayerCountField = hiddenLayerCountField;
	}

	/**
	 * @return the hiddenUnitCountField
	 */
	public JTextField getHiddenUnitCountField() {
		return hiddenUnitCountField;
	}

	/**
	 * @param hiddenUnitCountField the hiddenUnitCountField to set
	 */
	public void setHiddenUnitCountField(JTextField hiddenUnitCountField) {
		this.hiddenUnitCountField = hiddenUnitCountField;
	}

	/**
	 * @return the inputCountField
	 */
	public JTextField getInputCountField() {
		return inputCountField;
	}

	/**
	 * @param inputCountField the inputCountField to set
	 */
	public void setInputCountField(JTextField inputCountField) {
		this.inputCountField = inputCountField;
	}

	/**
	 * @return the learnRateField
	 */
	public JTextField getLearnRateField() {
		return learnRateField;
	}

	/**
	 * @param learnRateField the learnRateField to set
	 */
	public void setLearnRateField(JTextField learnRateField) {
		this.learnRateField = learnRateField;
	}

	/**
	 * @return the outputUnitCountField
	 */
	public JTextField getOutputUnitCountField() {
		return outputUnitCountField;
	}

	/**
	 * @param outputUnitCountField the outputUnitCountField to set
	 */
	public void setOutputUnitCountField(JTextField outputUnitCountField) {
		this.outputUnitCountField = outputUnitCountField;
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
	 * @return the errorThresholdField
	 */
	public JTextField getErrorThresholdField() {
		return errorThresholdField;
	}

	/**
	 * @param errorThresholdField the errorThresholdField to set
	 */
	public void setErrorThresholdField(JTextField errorThresholdField) {
		this.errorThresholdField = errorThresholdField;
	}

	/**
	 * @return the continuous
	 */
	public boolean isContinuous() {
		return continuous;
	}

	/**
	 * @param continuous the continuous to set
	 */
	public void setContinuous(boolean continuous) {
		this.continuous = continuous;
	}

	/**
	 * @return the biasField
	 */
	public JTextField getBiasField() {
		return biasField;
	}

	/**
	 * @param biasField the biasField to set
	 */
	public void setBiasField(JTextField biasField) {
		this.biasField = biasField;
	}

	/**
	 * @return the momentumField
	 */
	public JTextField getMomentumField() {
		return momentumField;
	}

	/**
	 * @param momentumField the momentumField to set
	 */
	public void setMomentumField(JTextField momentumField) {
		this.momentumField = momentumField;
	}
}
