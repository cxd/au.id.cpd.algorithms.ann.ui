/**
 * 
 */
package au.id.cpd.algorithms.ui.control;
import au.id.cpd.algorithms.data.*;
import au.id.cpd.algorithms.data.io.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import au.id.cpd.algorithms.ui.*;

import org.jdesktop.swing.utils.*;
/**
 * @author cd
 *
 */
public class DataController implements IMenuActionEventListener {
	
	// parent controller.
	private AnnUIController parent;
	/**
	 * Internal frame instance
	 */
	private ContainerFrame container;
	
	/**
	 * Percentage of data for training.
	 */
	private int trainPercent;
	/**
	 * The target column number.
	 */
	private int targetColumn;
	/**
	 * Boolean flag indicating whether to normalise.
	 */
	private boolean isNormalised;
	/**
	 * Path to file.
	 */
	private File dataFile;
	/**
	 * Internal data instance.
	 */
	private IMatrix<Double> data;
	/**
	 * Internal data set for training.
	 */
	private Matrix<Double> trainSet;
	/**
	 * Internet data set for testing.
	 */
	private Matrix<Double> testSet;
	
	/**
	 * Internal timer user to check on load operations.
	 */
	private Timer tm;
	/**
	 * Flag indicating whether data is loaded.
	 */
	private boolean isLoaded;
	/**
	 * Flag indicating whether the data loading is finished.
	 */
	private boolean isFinished;
	/**
	 * Internal status panel instance.
	 */
	private JPanel status;
	/**
	 * Internal data panel instance.
	 */
	private DataPanel dataPanel;
	/**
	 * Internal data properties panel instance.
	 */
	private DataPreferencesPanel properties;
	
	/**
	 * Default constructor.
	 *
	 */
	public DataController() {
		this.isLoaded = false;
		this.isFinished = false;
		this.isNormalised = false;
	}
	
	/* (non-Javadoc)
	 * @see au.id.cpd.algorithms.ui.IMenuActionEventListener#actionPerformed(au.id.cpd.algorithms.ui.MenuActionEvent)
	 */
	public void actionPerformed(MenuActionEvent e) {
		if (e.getActionType() == MenuActionType.LOAD_DATA) {
			this.chooseDataFile();
		} else if (e.getActionType() == MenuActionType.SAVE_DATA) {
			this.saveData();
		} else if (e.getActionType() == MenuActionType.EDIT_DATA_PROPERTIES) {
			this.displayProperties();
		} else if (e.getActionType() == MenuActionType.EDIT_DATA_PROPERTIES_OK) {
			this.processPropertiesEdit();
		} else if (e.getActionType() == MenuActionType.EDIT_DATA_PROPERTIES_CANCEL) {
			this.removeProperties();
		} else if (e.getActionType() == MenuActionType.VIEW_DATA_GRID) {
			this.displayData();
		}
	}
	/**
	 * Display the properties for the current data set.
	 *
	 */
	private void displayProperties() {
		if (!this.isLoaded) {
			JOptionPane.showMessageDialog(this.container, "Please load the data set before editing its properties.");
			return;
		}
		JPanel panel = this.container.getContainer();
		if (this.properties != null) {
			panel.remove(this.properties);
		}
		this.properties = new DataPreferencesPanel();
		this.properties.setTargetColumnCount(this.data.getSize().getCols());
		this.properties.setListener(this);
		panel.add(this.properties, BorderLayout.EAST);
		panel.validate();
	}
	/**
	 * Process the edit properties event.
	 */
	private void processPropertiesEdit() {
		this.trainPercent = Integer.parseInt(this.properties.getTrainPercentField().getText());
		this.targetColumn = (Integer)this.properties.getTargets().getSelectedItem();
		this.isNormalised = this.properties.isNormal();
		this.removeProperties();
		this.partitionData();
	}
	
	/**
	 * Remove the properties panel
	 * from the active display.
	 */
	private void removeProperties() {
		this.container.getContainer().remove(this.properties);
		this.container.validate();
	}
	
	/**
	 * Partition the data and generate training
	 * and test sets.
	 *
	 */
	private void partitionData() {
		final org.jdesktop.swing.utils.SwingWorker partitionWorker = new org.jdesktop.swing.utils.SwingWorker() {
			public Object construct() {
				return new PartitionDataTask(trainPercent);
			}
		};
		this.isFinished = false;
		status = new DataLoadStatusPanel("Partitioning Data");
		container.getStatusPanel().add(status, BorderLayout.CENTER);
		status.setVisible(true);
		container.validate();
		this.tm = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (isFinished) {
					tm.stop();
					container.setCursor(null);
					status.setVisible(false);
					status.validate();
					container.getStatusPanel().remove(status);
					if (isLoaded) {
						displayPartitionedData();
					}
					container.validate();
				}
			}
		});
		partitionWorker.start();
		tm.start();
	}
	
	
	/**
	 * Choose the data file.
	 */
	private void chooseDataFile() {
		DataFileFilter filter = new DataFileFilter();
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		int result = chooser.showOpenDialog(this.container);
		this.dataFile = chooser.getSelectedFile();
		if ((this.dataFile != null)&&(result == JFileChooser.APPROVE_OPTION)) {
			this.loadData();
		}
	}
	
	/**
	 * Load the data from file in a separate thread
	 * using an anonymous inner class.
	 *	
	 */
	private void loadData() {
		data = null;
		trainSet = null;
		testSet = null;
		final org.jdesktop.swing.utils.SwingWorker progressWorker = new org.jdesktop.swing.utils.SwingWorker() {
			public Object construct() {
				return new LoadDataTask();
			}
		};
		container.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		isFinished = false;
		isLoaded = false;
		status = new DataLoadStatusPanel();
		container.getStatusPanel().add(status, BorderLayout.CENTER);
		status.setVisible(true);
		container.validate();
		this.tm = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (isFinished) {
					tm.stop();
					container.setCursor(null);
					status.setVisible(false);
					status.validate();
					container.getStatusPanel().remove(status);
					if (isLoaded) {
						displayData();
					}
					container.validate();
				}
			}
		});
		progressWorker.start();
		tm.start();
	}
	
	/**
	 * Save the current data instance to file.
	 */
	private void saveData() {
		if (this.data == null) {
			JOptionPane.showMessageDialog(container, "Please load your data before attempting to save it.");
			return;
		}
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new DataFileFilter());
		int result = chooser.showSaveDialog(this.container);
		this.dataFile = chooser.getSelectedFile();
		if (result == JFileChooser.APPROVE_OPTION) {
			if (this.dataFile.exists()) {
				this.dataFile.delete();
			}
			final org.jdesktop.swing.utils.SwingWorker progressWorker = new org.jdesktop.swing.utils.SwingWorker() {
				public Object construct() {
					return new SaveDataTask();
				}
			};
			container.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			isFinished = false;
			isLoaded = false;
			status = new DataLoadStatusPanel("Saving Data");
			container.getStatusPanel().add(status, BorderLayout.CENTER);
			status.setVisible(true);
			container.validate();
			this.tm = new Timer(1000, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (isFinished) {
						tm.stop();
						container.setCursor(null);
						status.setVisible(false);
						status.validate();
						container.getStatusPanel().remove(status);
						container.validate();
					}
				}
			});
			progressWorker.start();
			tm.start();
		}
	}
	
	/**
	 * Display the data once it is loaded.
	 */
	private void displayData() {
		if (this.data == null) {
			JOptionPane.showMessageDialog(container, "Please load data before displaying.");
			return;
		}
		this.dataPanel = new DataPanel();
		this.dataPanel.setData(this.data);
		this.displayPartitionedData();
		this.container.setActivePanel(this.dataPanel);
	}
	/**
	 * Display the partitioned data.
	 */
	private void displayPartitionedData() {
		if ((this.trainSet != null)&&(this.testSet != null)) {
			this.dataPanel.setTrainingData(this.trainSet);
			this.dataPanel.setTestData(this.testSet);
		}
	}
	
	
	/**
	 * Inner class used to perform data loading
	 * as a swing worker process.
	 * @author cd
	 *
	 */
	public class SaveDataTask {
		public SaveDataTask() {
			this.saveData();
		}
		/**
		 * Load method of inner class.
		 *
		 */
		public void saveData() {
			try {
				FileWriter fout = new FileWriter(dataFile);
				MatrixWriter writer = new MatrixWriter(fout);
				writer.writeMatrix(data, fout);
				writer.close();
			} catch(IOException e) {
				JOptionPane.showMessageDialog(container, "Failed to write data to "+dataFile.getName());
			} finally {
				isFinished = true;
			}
		}
	}
	
	/**
	 * Inner class used to perform data loading
	 * as a swing worker process.
	 * @author cd
	 *
	 */
	public class LoadDataTask {
		public LoadDataTask() {
			this.loadData();
		}
		/**
		 * Load method of inner class.
		 *
		 */
		public void loadData() {
			try {
				FileReader fin = new FileReader(dataFile);
				MatrixReader reader = new MatrixReader(fin);
				data = reader.readMatrix();
				reader.close();
				isLoaded = true;
			} catch(IOException e) {
				isLoaded = false;
				JOptionPane.showMessageDialog(container, "Failed to load data from "+dataFile.getName());
			} finally {
				isFinished = true;
			}
		}
	}

	/**
	 * A task class used to perform data partitioning
	 * for use with SwingWorker thread.
	 */
	public class PartitionDataTask {
		
		private int trainPercent;
		
		public PartitionDataTask(int percent) {
			trainPercent = percent;
			System.err.println("Percent: " + trainPercent);
			this.partition();
		}
		
		private void partition() {
			if (data == null) {
				isFinished = true;
				return;
			}
			IMatrix<Double> shuffle = data.shuffle();
			int rows = shuffle.getSize().getRows();
			int cols = shuffle.getSize().getCols();
			int trows = (int)((trainPercent/100.0)*rows);
			System.err.println("T-Rows: " + trows);
			trainSet = new Matrix<Double>(trows, cols);
			testSet = new Matrix<Double>(rows - trows, cols);
			for(int i=0;i<rows;i++) {
				for(int j=0;j<cols;j++) {
					if (i < trows) {
						trainSet.add(shuffle.get(i,j));
					} else {
						testSet.add(shuffle.get(i, j));
					}
				}
			}
			isFinished = true;
		}
	}
	
	/**
	 * @return the data
	 */
	public IMatrix<Double> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(IMatrix<Double> data) {
		this.data = data;
	}

	/**
	 * @return the dataFile
	 */
	public File getDataFile() {
		return dataFile;
	}

	/**
	 * @param dataFile the dataFile to set
	 */
	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	/**
	 * @return the testSet
	 */
	public IMatrix<Double> getTestSet() {
		return testSet;
	}

	/**
	 * @param testSet the testSet to set
	 */
	public void setTestSet(Matrix<Double> testSet) {
		this.testSet = testSet;
	}

	/**
	 * @return the trainPercent
	 */
	public int getTrainPercent() {
		return trainPercent;
	}

	/**
	 * @param trainPercent the trainPercent to set
	 */
	public void setTrainPercent(int trainPercent) {
		this.trainPercent = trainPercent;
	}

	/**
	 * @return the trainSet
	 */
	public IMatrix<Double> getTrainSet() {
		return trainSet;
	}

	/**
	 * @param trainSet the trainSet to set
	 */
	public void setTrainSet(Matrix<Double> trainSet) {
		this.trainSet = trainSet;
	}

	/**
	 * @return the container
	 */
	public ContainerFrame getContainer() {
		return container;
	}

	/**
	 * @param container the container to set
	 */
	public void setContainer(ContainerFrame container) {
		this.container = container;
	}

	/**
	 * @return the isLoaded
	 */
	public boolean isLoaded() {
		return isLoaded;
	}

	/**
	 * @param isLoaded the isLoaded to set
	 */
	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	/**
	 * @return the targetColumn
	 */
	public int getTargetColumn() {
		return targetColumn;
	}

	/**
	 * @param targetColumn the targetColumn to set
	 */
	public void setTargetColumn(int targetColumn) {
		this.targetColumn = targetColumn;
	}

	/**
	 * @return the parent
	 */
	public AnnUIController getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(AnnUIController parent) {
		this.parent = parent;
	}

	/**
	 * @return the isNormalised
	 */
	public boolean isNormalised() {
		return isNormalised;
	}

	/**
	 * @param isNormalised the isNormalised to set
	 */
	public void setNormalised(boolean isNormalised) {
		this.isNormalised = isNormalised;
	}

}
