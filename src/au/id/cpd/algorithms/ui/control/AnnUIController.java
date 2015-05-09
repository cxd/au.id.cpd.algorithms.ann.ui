/**
 * 
 */
package au.id.cpd.algorithms.ui.control;
import javax.swing.*;

import au.id.cpd.algorithms.patterns.*;
import au.id.cpd.algorithms.ui.*;
/**
 * @author cd
 *
 */
public class AnnUIController implements Runnable, IMenuActionEventListener {

	private static int DEFAULT_WIDTH=800;
	private static int DEFAULT_HEIGHT=600;
	
	private ContainerFrame activeFrame;
	private JPanel activePanel;
	private DataController dataController;
	private NetworkController networkController;
	
	public AnnUIController() {
		
	}
	
	public void run() {
		this.activeFrame = new ContainerFrame();
		this.activeFrame.setTitle("ANN GUI");
		this.activeFrame.setSize(AnnUIController.DEFAULT_WIDTH, AnnUIController.DEFAULT_HEIGHT);
		this.activeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.dataController = new DataController();
		this.dataController.setParent(this);
		this.dataController.setContainer(this.activeFrame);
		this.networkController = new NetworkController();
		this.networkController.setContainer(this.activeFrame);
		this.networkController.setParent(this);
		this.activeFrame.getMenu().setMenuListener(this);
		Thread th = new Thread(this.activeFrame);
		th.start();
	}
	/**
	 * callback for menu level events.
	 */
	public void actionPerformed(MenuActionEvent e) {
		if ((e.getActionType() == MenuActionType.LOAD_DATA)||
			(e.getActionType() == MenuActionType.SAVE_DATA)||
			(e.getActionType() == MenuActionType.EDIT_DATA_PROPERTIES)||
			(e.getActionType() == MenuActionType.EDIT_DATA_PROPERTIES_OK)||
			(e.getActionType() == MenuActionType.EDIT_DATA_PROPERTIES_CANCEL)||
			(e.getActionType() == MenuActionType.VIEW_DATA_GRID)) {
			this.dataController.actionPerformed(e);
		}
		if ((e.getActionType() == MenuActionType.LOAD_NETWORK)||
			(e.getActionType() == MenuActionType.EDIT_NETWORK_PROPERTIES)||
			(e.getActionType() == MenuActionType.VIEW_NETWORK_MODEL)||
			(e.getActionType() == MenuActionType.RUN_TRAIN_MODEL)||
			(e.getActionType() == MenuActionType.RUN_TEST_MODEL)||
			(e.getActionType() == MenuActionType.RUN_CLASSIFY_DATA)||
			(e.getActionType() == MenuActionType.VIEW_GRADIENT_DESCENT)||
			(e.getActionType() == MenuActionType.SAVE_NETWORK)||
			(e.getActionType() == MenuActionType.SAVE_WEIGHTS)||
			(e.getActionType() == MenuActionType.VIEW_NETWORK_ERROR_SURFACE)) {
			this.networkController.actionPerformed(e);
		}
		if (e.getActionType() == MenuActionType.EXIT) {
			System.exit(0);
		}
	}
	
	/**
	 * entry point to program.
	 * @param args
	 */
	public static void main(String [] args) {
		AnnUIController gui = new AnnUIController();
		gui.run();
	}

	/**
	 * @return the dataController
	 */
	public DataController getDataController() {
		return dataController;
	}

	/**
	 * @param dataController the dataController to set
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}

	/**
	 * @return the networkController
	 */
	public NetworkController getNetworkController() {
		return networkController;
	}

	/**
	 * @param networkController the networkController to set
	 */
	public void setNetworkController(NetworkController networkController) {
		this.networkController = networkController;
	}
}
