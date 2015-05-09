/**
 * 
 */
package au.id.cpd.algorithms.ui;

import java.awt.*;
import javax.swing.*;
import au.id.cpd.algorithms.data.*;

/**
 * @author Chris Davey
 * The data panel contains three tabbed panels.
 * 1. The data set
 * 2. The training set
 * 3. The test set.
 */
public class DataPanel extends JTabbedPane {

	private DataTablePanel dataPanel;
	private DataTablePanel trainPanel;
	private DataTablePanel testPanel;
	/**
	 * default constructor.
	 */
	public DataPanel() {
		super();
		this.setTabPlacement(JTabbedPane.TOP);
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.dataPanel = new DataTablePanel();
		this.addTab("Data", this.dataPanel);
		this.trainPanel = new DataTablePanel();
		this.addTab("Training Data", this.trainPanel);
		this.testPanel = new DataTablePanel();
		this.addTab("Test Data", this.testPanel);
	}
	/**
	 * Define the data panel's data model.
	 * @param data
	 */
	public void setData(IMatrix<Double> data) {
		this.dataPanel.getDataTable().setModel(data);
		this.dataPanel.updateUI();
	}
	/**
	 * Define the training panel's data model.
	 * @param data
	 */
	public void setTrainingData(IMatrix<Double> data) {
		this.trainPanel.getDataTable().setModel(data);
		this.trainPanel.updateUI();
	}
	/**
	 * Define the training panel's data model.
	 * @param data
	 */
	public void setTestData(IMatrix<Double> data) {
		this.testPanel.getDataTable().setModel(data);
		this.testPanel.updateUI();
	}
	
	/**
	 * @return the dataPanel
	 */
	public DataTablePanel getDataPanel() {
		return dataPanel;
	}
	/**
	 * @param dataPanel the dataPanel to set
	 */
	public void setDataPanel(DataTablePanel dataPanel) {
		this.dataPanel = dataPanel;
	}
	/**
	 * @return the testPanel
	 */
	public DataTablePanel getTestPanel() {
		return testPanel;
	}
	/**
	 * @param testPanel the testPanel to set
	 */
	public void setTestPanel(DataTablePanel testPanel) {
		this.testPanel = testPanel;
	}
	/**
	 * @return the trainPanel
	 */
	public DataTablePanel getTrainPanel() {
		return trainPanel;
	}
	/**
	 * @param trainPanel the trainPanel to set
	 */
	public void setTrainPanel(DataTablePanel trainPanel) {
		this.trainPanel = trainPanel;
	}
	
}
