/**
 * 
 */
package au.id.cpd.algorithms.ui.control;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.*;
import java.io.*;

import au.id.cpd.algorithms.*;
import au.id.cpd.algorithms.patterns.*;
import au.id.cpd.algorithms.classifier.*;
import au.id.cpd.algorithms.classifier.ANN.*;
import au.id.cpd.algorithms.data.*;
import au.id.cpd.algorithms.data.io.*;
import au.id.cpd.algorithms.ui.*;
import au.id.cpd.algorithms.ui.control.DataController.PartitionDataTask;

import org.jdesktop.swing.utils.*;

/**
 * @author cd
 *
 */
public class NetworkController implements IMenuActionEventListener, au.id.cpd.algorithms.patterns.Observer {

	
	private static int DEFAULT_EPOCH = 1000;
	private static double DEFAULT_LEARN_RATE = 0.01;
	private static double DEFAULT_THRESHOLD = 0.5;
	private static double DEFAULT_ERROR_THRESHOLD = 0.0001;
	
	// parent controller.
	private AnnUIController parent;
	// container frame.
	private ContainerFrame container;
	// internal properties panel.
	private NetworkPropertiesPanel netProperties;
	// network properties storing the settings
	// collected from the ui.
	private NeuralNetwork network;
	// internal flag to indicate whether network build is finished.
	private boolean isFinished;
	// internal flag indicating the network is trained.
	private boolean isTrained;
	// internal Timer for tasks.
	private Timer tm;
	// internal data status panel instance
	private DataLoadStatusPanel status;
	// internal graph panel.
	private NetworkGraph graph;
	// internal percent of misclassified samples
	// generated when testing the network.
	private double misclassified;
	// dialog to display the gradient descent.
	private JDialog descentDialog;
	// internal data set used during classification.
	private IMatrix<Double> classified;
	// internal error surface.
	private IMatrix<Double> errorSurface;
	// axis for error surface.
	private IMatrix<Double> errorAxis;
	// error surface dialog
	private ErrorSurfaceDialog errorDialog;
	/**
	 * default constructor.
	 */
	public NetworkController() {
		this.isFinished = false;
		this.isTrained = false;
	}
	
	/* (non-Javadoc)
	 * @see au.id.cpd.algorithms.ui.IMenuActionEventListener#actionPerformed(au.id.cpd.algorithms.ui.MenuActionEvent)
	 */
	public void actionPerformed(MenuActionEvent e) {
		if (e.getActionType() == MenuActionType.EDIT_NETWORK_PROPERTIES) {
			this.displayNetworkProperties();
		} else if (e.getActionType() == MenuActionType.EDIT_NETWORK_PROPERTIES_OK) {
			this.processNetworkProperties();
		} else if (e.getActionType() == MenuActionType.EDIT_NETWORK_PROPERTIES_CANCEL) {
			this.cancelNetworkProperties();
		} else if (e.getActionType() == MenuActionType.VIEW_NETWORK_MODEL) {
			this.displayNetworkGraph();
		} else if (e.getActionType() == MenuActionType.RUN_TRAIN_MODEL) {
			// train the network and display the gradient descent progressively.
			this.trainNetwork();
		} else if (e.getActionType() == MenuActionType.VIEW_GRADIENT_DESCENT) {
			// display the static view of the gradient desccent.
			this.displayGradientDescent();
		} else if (e.getActionType() == MenuActionType.RUN_TEST_MODEL) {
			// test the network.
			this.testNetwork();
		} else if (e.getActionType() == MenuActionType.RUN_CLASSIFY_DATA) {
			this.classifyData();
		} else if (e.getActionType() == MenuActionType.SAVE_NETWORK) {
			this.saveNetwork();
		} else if (e.getActionType() == MenuActionType.LOAD_NETWORK) {
			this.loadNetwork();
		} else if (e.getActionType() == MenuActionType.SAVE_WEIGHTS) {
			// save the weights as csv.
			this.saveNetworkWeights();
		} else if (e.getActionType() == MenuActionType.VIEW_NETWORK_ERROR_SURFACE) {
			// view the network error surface.
			// this might take some time.
			this.displayErrorSurface();
		}
	}

	/**
	 * Display the input properties for the neural network
	 * configuration screen.
	 */
	private void displayNetworkProperties() {
		DataController data = this.parent.getDataController();
		if (data.getData() == null) {
			JOptionPane.showMessageDialog(container, "Please define data properties\nbefore editing the network properties.");
			return;
		}
		int cols = data.getData().getSize().getCols() - 1;
		
		if (this.netProperties == null) {
			this.netProperties = new NetworkPropertiesPanel();
			this.netProperties.getInputCountField().setText(""+cols);
			this.netProperties.getOutputUnitCountField().setText("1");
			this.netProperties.getHiddenLayerCountField().setText("1");
			this.netProperties.getHiddenUnitCountField().setText(""+(cols-1));
			this.netProperties.getBiasField().setText("1.0");
			this.netProperties.getMomentumField().setText("0.0001");
			this.netProperties.getEpochField().setText(""+NetworkController.DEFAULT_EPOCH);
			this.netProperties.getErrorThresholdField().setText(""+NetworkController.DEFAULT_ERROR_THRESHOLD);
			this.netProperties.getLearnRateField().setText(""+NetworkController.DEFAULT_LEARN_RATE);
			this.netProperties.getClassThresholdField().setText(""+NetworkController.DEFAULT_THRESHOLD);
			this.netProperties.setListener(this);
		}
		this.container.setActivePanel(this.netProperties);
	}
	
	/**
	 * process the network properties settings.
	 */
	private void processNetworkProperties() {
		DataController data = this.parent.getDataController();
		if ((data.getTrainSet() == null)||(data.getTestSet() == null)) {
			JOptionPane.showMessageDialog(container, "To modify the network properties,\nthe data training set and test set must be defined.");
			return;
		}
		try {
			this.network = new NeuralNetwork();
			// add observer - via subject interface.
			this.network.addObserver(this);
			this.network.setTargetColumn(data.getTargetColumn());
			System.err.println("target Column: "+data.getTargetColumn());
			this.network.setEpoch(Integer.parseInt(this.netProperties.getEpochField().getText()));
			this.network.setLearnRate(Double.parseDouble(this.netProperties.getLearnRateField().getText()));
			this.network.setBias(Double.parseDouble(this.netProperties.getBiasField().getText()));
			this.network.setMomentum(Double.parseDouble(this.netProperties.getMomentumField().getText()));
			this.network.setErrorThreshold(Double.parseDouble(this.netProperties.getErrorThresholdField().getText()));
			this.network.setInputCount(Integer.parseInt(this.netProperties.getInputCountField().getText()));
			this.network.setHiddenLayerCount(Integer.parseInt(this.netProperties.getHiddenLayerCountField().getText()));
			this.network.setHiddenUnitCount(Integer.parseInt(this.netProperties.getHiddenUnitCountField().getText()));
			this.network.setOutputCount(Integer.parseInt(this.netProperties.getOutputUnitCountField().getText()));
			String selType = (String)this.netProperties.getActivatorTypeList().getSelectedItem();
			ActivationType actType = null;
			for(ActivationType t : ActivationType.values()) {
				if (selType.compareTo(t.getName()) == 0) {
					actType = t;
					break;
				}
			}
			this.network.setUnitType(actType);
			if (this.network.getOutputCount() > 1) {
				String[] classStr = this.netProperties.getClassInstanceListField().getText().split(",");
				if (classStr.length == 0) {
					JOptionPane.showMessageDialog(this.container, "Multiple output nodes defined.\nPlease ensure you define class instance numbers.");
					return;
				}
				List<Double> classList = new Vector<Double>();
				for(String lbl : classStr) {
					classList.add(Double.parseDouble(lbl));
				}
				this.network.setTargetsClasses(classList);
			} else {
				// determine whether the network is continuous or not.
				this.network.setIsTargetThreshold(!this.netProperties.isContinuous());
				this.network.setTargetThreshold(Double.parseDouble(this.netProperties.getClassThresholdField().getText()));
			}
			// need to define input weights.
			List<Double> weights = new Vector<Double>();
			Random rnd = new Random();
			rnd.setSeed(Calendar.getInstance().getTimeInMillis());
			for(int i=0;i<this.network.getInputCount();i++) {
				weights.add(rnd.nextDouble()/data.getTrainSet().getSize().getRows());
			}
			this.network.setInputWeights(weights);
			this.buildNetwork();
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this.container, "Incorrect number format: "+e.getMessage());
			return;
		}
	}
	/**
	 * Run the build network task
	 * invoke the view network event once complete.
	 *
	 */
	private void buildNetwork() {
		this.isFinished = false;
		final org.jdesktop.swing.utils.SwingWorker partitionWorker = new org.jdesktop.swing.utils.SwingWorker() {
			public Object construct() {
				return new NetworkBuilderTask();
			}
		};
		this.isFinished = false;
		this.isTrained = false;
		status = new DataLoadStatusPanel("Building Network");
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
					cancelNetworkProperties();
				}
			}
		});
		partitionWorker.start();
		tm.start();
	}
	
	/**
	 * cancel the network properties screen.
	 */
	private void cancelNetworkProperties() {
		if (this.netProperties != null) {
			this.container.getContainer().remove(this.netProperties);
			this.container.getContainer().validate();
			this.container.update(this.container.getGraphics());
		}
		// issue a view network model event.
		this.displayNetworkGraph();
		//this.parent.actionPerformed(new MenuActionEvent(this, MenuActionType.VIEW_NETWORK_MODEL, "View Model"));
	}
	
	/**
	 * Display the network graph.
	 */
	private void displayNetworkGraph() {
		if (this.network == null) {
			JOptionPane.showMessageDialog(this.container, "Please define load network or define network properties\nbefore viewing network.");
			return;
		}
		this.graph = new NetworkGraph(this.network);
		this.container.setActivePanel(this.graph);
	}
	
	/**
	 * Display the current gradient descent for the
	 * network. This requires a static view of the 
	 * errors list.
	 */
	private void displayGradientDescent() {
		if ((this.network == null)||(this.network.getErrors() == null)) {
			JOptionPane.showMessageDialog(this.container, "Please define load network or define network properties and train\nbefore viewing network errors.");
			return;
		}
		if (this.network.getErrors().size() == 0) {
			JOptionPane.showMessageDialog(this.container, "Please define load network or define network properties and train\nbefore viewing network errors.");
			return;
		}
		this.descentDialog = new GradientDescentStaticDialog(container, this.network.getEpoch(), this.network.getErrors());
		this.descentDialog.setVisible(true);
	}
	
	/**
	 * Check to ensure the network has the test and training
	 * data defined.
	 * @return boolean
	 */
	private boolean checkNetworkData() {
		if (network.getTrainingSet() == null) {
			DataController data = parent.getDataController();
			if (!data.isLoaded()) {
				return false;
			}
			IMatrix<Double> train;
			IMatrix<Double> test;
			if (network.getTargetsClasses().size() > 0) {
				List<Double> targets = data.getTrainSet().getColumn(network.getTargetColumn());
				train = data.getTrainSet().normalise();
				train.setColumn(network.getTargetColumn(), targets);
				targets = data.getTestSet().getColumn(network.getTargetColumn());
				test = data.getTestSet().normalise();
				test.setColumn(network.getTargetColumn(), targets);
			} else {
				train = data.getTrainSet().normalise();
				test = data.getTestSet().normalise();
			}
			network.setTrainingSet(train);
			network.setTestSet(test);
		}
		return true;
	}
	
	/**
	 * Train the network graph.
	 */
	private void trainNetwork() {
		if (this.network == null) {
			JOptionPane.showMessageDialog(this.container, "Please define load network or define network properties\nbefore training the network.");
			return;
		}
		if (!this.checkNetworkData()) {
			JOptionPane.showMessageDialog(this.container, "Please load and partition the data \nbefore training the network.");
			return;
		}
		this.descentDialog = new GradientDescentDialog(this.container, this.network.getEpoch());
		final org.jdesktop.swing.utils.SwingWorker trainWorker = new org.jdesktop.swing.utils.SwingWorker() {
			public Object construct() {
				return new TrainNetworkTask();
			}
		};
		this.isFinished = false;
		this.isTrained = false;
		status = new DataLoadStatusPanel("Training Network");
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
					((GradientDescentDialog)descentDialog).stopCollection();
					cancelNetworkProperties();
				}
			}
		});
		trainWorker.start();
		tm.start();
		((GradientDescentDialog)this.descentDialog).startCollection();
		this.descentDialog.setVisible(true);
	}
	
	/**
	 * Test the neural network and display an alert showing the percent
	 * misclassified.
	 */
	private void testNetwork() {
		if ((this.network == null)||(isTrained == false)) {
			JOptionPane.showMessageDialog(this.container, "Please define and train the network\nbefore testing.");
			return;
		}
		if (!this.checkNetworkData()) {
			JOptionPane.showMessageDialog(this.container, "Please load and partition the data \nbefore training the network.");
			return;
		}
		final org.jdesktop.swing.utils.SwingWorker testWorker = new org.jdesktop.swing.utils.SwingWorker() {
			public Object construct() {
				return new TestNetworkTask();
			}
		};
		isFinished = false;
		status = new DataLoadStatusPanel("Testing Network");
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
					cancelNetworkProperties();
					NumberFormat d_fmt = NumberFormat.getInstance();
					d_fmt.setMinimumFractionDigits(2);
					d_fmt.setMaximumFractionDigits(4);
					String pc = d_fmt.format(misclassified*100);
					JOptionPane.showMessageDialog(container, "Percent misclassified: "+pc+"%");
				}
			}
		});
		testWorker.start();
		tm.start();
	}
	
	/**
	 * Test the neural network and display an alert showing the percent
	 * misclassified.
	 */
	private void displayErrorSurface() {
		if ((this.network == null)||(isTrained == false)) {
			JOptionPane.showMessageDialog(this.container, "Please define and train the network\nbefore displaying Error Surface.");
			return;
		}
		if (!this.checkNetworkData()) {
			JOptionPane.showMessageDialog(this.container, "Please load and partition the data \nbefore displaying Error Surface.");
			return;
		}
		final org.jdesktop.swing.utils.SwingWorker worker = new org.jdesktop.swing.utils.SwingWorker() {
			public Object construct() {
				return new ErrorSurfaceTask();
			}
		};
		isFinished = false;
		status = new DataLoadStatusPanel("Computing Error Surface");
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
					cancelNetworkProperties();
					//errorAxis = errorAxis.normalise();
					errorDialog = new ErrorSurfaceDialog(container, errorAxis, errorSurface.normalise(), errorAxis.transform());
				}
			}
		});
		worker.start();
		tm.start();
	}
	
	/**
	 * Save the network model to the location
	 * chosen by the end user.
	 *
	 */
	private void saveNetwork() {
		if ((this.network == null)||(isTrained == false)) {
			JOptionPane.showMessageDialog(this.container, "Please define and train the network\nbefore saving.");
			return;
		}
		JFileChooser saveDialog = new JFileChooser();
		saveDialog.setFileFilter(new NetworkFileFilter());
		if (saveDialog.showSaveDialog(container) == JFileChooser.CANCEL_OPTION) {
			return;
		}
		File file = saveDialog.getSelectedFile();
		try {
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream os = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(this.network);
			oos.close();
		} catch(IOException e) {
			JOptionPane.showMessageDialog(this.container, "Failed to save network.\n"+e.getMessage());
		}
	}
	
	/**
	 * Save the network weights as csv to the location
	 * chosen by the end user.
	 *
	 */
	private void saveNetworkWeights() {
		if ((this.network == null)||(isTrained == false)) {
			JOptionPane.showMessageDialog(this.container, "Please define and train the network\nbefore saving.");
			return;
		}
		JFileChooser saveDialog = new JFileChooser();
		saveDialog.setFileFilter(new DataFileFilter());
		if (saveDialog.showSaveDialog(container) == JFileChooser.CANCEL_OPTION) {
			return;
		}
		File file = saveDialog.getSelectedFile();
		try {
			if (file.exists()) {
				file.delete();
			}
			IMatrix<Double> weights = this.network.getWeights();
			FileWriter f = new FileWriter(file);
			MatrixWriter wr = new MatrixWriter(f);
			wr.writeMatrix(weights, f);
			wr.close();
		} catch(IOException e) {
			JOptionPane.showMessageDialog(this.container, "Failed to save network weights.\n"+e.getMessage());
		}
	}
	
	/**
	 * Load the neural network from serialized version.
	 *
	 */
	private void loadNetwork() {
		JFileChooser dialog = new JFileChooser();
		dialog.setFileFilter(new NetworkFileFilter());
		if (dialog.showOpenDialog(container) == JFileChooser.CANCEL_OPTION) {
			return;
		}
		File file = dialog.getSelectedFile();
		try {
			FileInputStream is = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(is);
			this.network = (NeuralNetwork)ois.readObject();
			// network loaded from disc is trained.
			isTrained = true;
		} catch(ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this.container, "Failed to load network.\n"+e.getMessage());
		} catch(IOException e) {
			JOptionPane.showMessageDialog(this.container, "Failed to load network.\n"+e.getMessage());
		}
		this.cancelNetworkProperties();
	}
	
	/**
	 * Classify the data set by adding an additional column
	 * containing the classifications for the tuples in each row.
	 * Once complete display the classified data set via the data panel.
	 */
	public void classifyData() {
		if ((this.network == null)||(isTrained == false)) {
			JOptionPane.showMessageDialog(this.container, "Please define and train the network\nbefore testing.");
			return;
		}
		if (parent.getDataController().getData() == null) {
			JOptionPane.showMessageDialog(container, "Please load the data set to \nbefore classifying samples.");
			return;
		}
		// need to ensure that the data fits within the classification data set.
		// network inputs + 1 column for classification.
		if (parent.getDataController().getData().getSize().getCols() != this.network.getInputCount()) {
			JOptionPane.showMessageDialog(container, "Cannot perform classification.\nNumber of inputs does not match attributes.");
			return;
		}
		final org.jdesktop.swing.utils.SwingWorker classifyWorker = new org.jdesktop.swing.utils.SwingWorker() {
			public Object construct() {
				return new ClassificationTask();
			}
		};
		isFinished = false;
		status = new DataLoadStatusPanel("Classifying Samples");
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
					parent.getDataController().setData(classified);
					parent.actionPerformed(new MenuActionEvent(this, MenuActionType.VIEW_DATA_GRID, "View Data"));
				}
			}
		});
		classifyWorker.start();
		tm.start();
	}
	
	
	
	/**
	 * notify Observer is notified by the Subject each time the subject is
	 * updated.
	 * 
	 * In our case we are simply interested in updating
	 * a graph of mean squared error figures
	 * to show the gradient descent as the network learns.
	 * 
	 * @param sender originating Subject instance.
	 */
	public void notify(ISubject sender) {
		if (this.descentDialog == null) return;
		((GradientDescentDialog)this.descentDialog).updateTrace(this.network.getErrors());
	}

	/**
	 * notify Observer is notified by the Subject each time the subject is
	 * updated. Java copies a reference to the source reference argument.
	 * Argument should provide an interface allowing the Observer to modify its
	 * internal fields.
	 * 
	 * @param sender -
	 *            Originaiting Subject instance.
	 * @param argument
	 */
	public void notify(ISubject sender, Object argument) {
		// not implemented.
	}
	
	/**
	 * Internal task used to perform classification upon a 
	 * unknown data set in a separate thread.
	 */
	class ClassificationTask {
		private IMatrix<Double> data;
		public ClassificationTask() {
			isFinished = false;
			this.classifyData();
		}
		/**
		 * Classify each row in the data set.
		 */
		private void classifyData() {
			// copy it into the classified data set.
			IMatrix<Double> data = parent.getDataController().getData();
			classified = new Matrix<Double>(data.getSize().getRows(), data.getSize().getCols() + 1);
			for(int i=0;i<data.getSize().getRows();i++) {
				for(int j=0;j<data.getSize().getCols();j++) {
					classified.set(i, j, data.get(i,j));
				}
				// add unknown classes in this case negative infinity.
				classified.set(i,data.getSize().getCols(), Double.NEGATIVE_INFINITY);
			}
			// in this case the target column will be set to the last column.
			int col = network.getTargetColumn();
			network.setTargetColumn(classified.getSize().getCols() - 1);
			// perform classification.
			for(int i=0;i<classified.getSize().getRows();i++) {
				List<Double> row = classified.getRow(i);
				int target = row.size() - 1;
				double result = this.classify(row);
				classified.set(i, target, result);
			}
			network.setTargetColumn(col);
			isFinished = true;
		}
		/**
		 * Return the network classification for the current row.
		 * @param row
		 * @return
		 */
		private double classify(List<Double> row) {
			return network.getClassification(row);
		}
		
		
		
	}
	
	/**
	 * Internal task used to test the neural network.
	 */
	class TestNetworkTask {
		public TestNetworkTask() {
			isFinished = false;
			misclassified = 0.0;
			this.test();
		}
		private void test() {
			misclassified = network.test();
			isFinished = true;
		}
	}
	
	/**
	 * Internal task used to train the neural network.
	 */
	class TrainNetworkTask {
		public TrainNetworkTask() {
			isFinished = false;
			this.train();
		}
		private void train() {
			network.learn();
			isFinished = true;
			isTrained = true;
		}
	}
	
	/**
	 * Internal class used to build the network 
	 * within a SwingWorker thread.
	 */
	class NetworkBuilderTask {
		
		public NetworkBuilderTask() {
			this.buildNetwork();
		}
		
		public void buildNetwork() {
			try {
				DataController data = parent.getDataController();
				if (!data.isNormalised()) {
					network.setTrainingSet(data.getTrainSet().normalise(network.getTargetColumn()));
					network.setTestSet(data.getTestSet().normalise(network.getTargetColumn()));
				} else {
					network.setTrainingSet(data.getTrainSet());
					network.setTestSet(data.getTestSet());
				}
				network.constructNetwork();
			} catch(Exception e) {
				JOptionPane.showMessageDialog(container, "Failed to build network.\n"+e.getMessage());
			}
			isFinished = true;
		}
	}
	
	class ErrorSurfaceTask {
		public ErrorSurfaceTask() {
			isFinished = false;
			this.computeErrorSurface();
		}
		
		public void computeErrorSurface() {
			errorSurface = network.computeErrorSurface();
			// create matrices for the X and Z axis.
			Size sz = errorSurface.getSize();
			errorAxis = new Matrix<Double>(sz);
			double minX = sz.getWidth() / 2.0;
			double minY = sz.getHeight() / 2.0;
			double startX = -1.0 * minX;
			double startY = -1.0 * minY;
			for(int i = 0; i < sz.getRows(); i++) {
				for(int j=0;j<sz.getCols();j++) {
					errorAxis.set(i, j, startY);
					startY += 1.0;
				}
				startY = -1.0*minY;
			}
			isFinished = true;
			
			try {
				FileWriter fout = new FileWriter(new File("errsurface.csv"));
				MatrixWriter w = new MatrixWriter(fout);
				w.writeMatrix(errorSurface, fout);
				w.close();
				fout = new FileWriter(new File("erraxis.csv"));
				w = new MatrixWriter(fout);
				w.writeMatrix(errorAxis, fout);
				w.close();
			} catch(Exception e) {
				
			}
		}
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
	 * @return the classified
	 */
	public IMatrix<Double> getClassified() {
		return classified;
	}

	/**
	 * @param classified the classified to set
	 */
	public void setClassified(IMatrix<Double> classified) {
		this.classified = classified;
	}
	
}
