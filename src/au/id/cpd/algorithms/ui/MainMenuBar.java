/**
 * 
 */
package au.id.cpd.algorithms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
/**
 * @author cd
 *
 */
public class MainMenuBar extends JMenuBar {
	/**
	 * Internal event listener for menu events.
	 */
	private IMenuActionEventListener menuListener;
	
	/**
	 * Inner menu item classes
	 *
	 */
	class LoadDataAction extends AbstractAction {
		
		public LoadDataAction() {
			super("Load Data");
		}
		
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.LOAD_DATA, "Load Data"));
			}
		}
	}
	
	class SaveDataAction extends AbstractAction {
		
		public SaveDataAction() {
			super("Save Data");
		}
		
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.SAVE_DATA, "Save Data"));
			}
		}
	}
	
	class LoadNetworkAction extends AbstractAction {
		public LoadNetworkAction() {
			super("Load Network");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.LOAD_NETWORK, "Load Network"));
			}
		}
	}
	
	class SaveNetworkAction extends AbstractAction {
		public SaveNetworkAction() {
			super("Save Network");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.SAVE_NETWORK, "Save Network"));
			}
		}
	}
	
	class SaveNetworkWeightsAction extends AbstractAction {
		public SaveNetworkWeightsAction() {
			super("Save Network Weights");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.SAVE_WEIGHTS, "Save Network Weights"));
			}
		}
	}
	
	
	class CloseNetworkAction extends AbstractAction {
		public CloseNetworkAction() {
			super("Close");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.CLOSE, "Close"));
			}
		}
	}
	
	
	class ExitAction extends AbstractAction {
		public ExitAction() {
			super("Exit");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.EXIT, "Exit"));
			}
		}
	}
	
	class EditDataProperties extends AbstractAction {
		public EditDataProperties() {
			super("Edit Data");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.EDIT_DATA_PROPERTIES, "Edit Data"));
			}
		}
	}
	
	class EditNetworkProperties extends AbstractAction {
		public EditNetworkProperties() {
			super("Edit Network");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.EDIT_NETWORK_PROPERTIES, "Edit Network"));
			}
		}
	}
	

	class ViewDataGrid extends AbstractAction {
		public ViewDataGrid() {
			super("View Data");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.VIEW_DATA_GRID, "View Data"));
			}
		}
	}
	
	class ViewNetworkModel extends AbstractAction {
		public ViewNetworkModel() {
			super("Network Model");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.VIEW_NETWORK_MODEL, "View Network Model"));
			}
		}
	}
	
	class ViewGradientDescent extends AbstractAction {
		public ViewGradientDescent() {
			super("Gradient Descent");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.VIEW_GRADIENT_DESCENT, "View Gradient Descent"));
			}
		}
	}
	
	class ViewErrorSurface extends AbstractAction {
		public ViewErrorSurface() {
			super("View Error Surface");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.VIEW_NETWORK_ERROR_SURFACE, "View Error Surface"));
			}
		}
	}
	
	class RunTrainModel extends AbstractAction {
		public RunTrainModel() {
			super("Train Model");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.RUN_TRAIN_MODEL, "Train Model"));
			}
		}
	}
	
	class RunTestModel extends AbstractAction {
		public RunTestModel() {
			super("Test Model");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.RUN_TEST_MODEL, "Test Model"));
			}
		}
	}
	
	class RunClassifyData extends AbstractAction {
		public RunClassifyData() {
			super("Classify Data");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.RUN_CLASSIFY_DATA, "Classify Model"));
			}
		}
	}
	
	class HelpAbout extends AbstractAction {
		public HelpAbout() {
			super("About");
		}
		public void actionPerformed(ActionEvent e) {
			if (menuListener != null) {
				menuListener.actionPerformed(new MenuActionEvent(this, MenuActionType.HELP_ABOUT, "Help About"));
			}
		}
	}
	
	public MainMenuBar() {
		this.init();
	}
	
	private void init() {
		JMenu menu = new JMenu("File");
		menu.setMnemonic('f');
		menu.add(new LoadDataAction());
		menu.add(new SaveDataAction());
		menu.add(new LoadNetworkAction());
		menu.add(new SaveNetworkAction());
		menu.add(new SaveNetworkWeightsAction());
		menu.add(new CloseNetworkAction());
		menu.add(new ExitAction());
		this.add(menu);
		menu = new JMenu("Edit");
		menu.setMnemonic('e');
		menu.add(new EditDataProperties());
		menu.add(new EditNetworkProperties());
		this.add(menu);
		menu = new JMenu("View");
		menu.add(new ViewDataGrid());
		menu.add(new ViewNetworkModel());
		menu.add(new ViewGradientDescent());
		menu.add(new ViewErrorSurface());
		this.add(menu);
		menu = new JMenu("Run");
		menu.add(new RunTrainModel());
		menu.add(new RunTestModel());
		menu.add(new RunClassifyData());
		this.add(menu);
		menu = new JMenu("Help");
		menu.add(new HelpAbout());
		this.add(menu);
	}

	/**
	 * @return the menuListener
	 */
	public IMenuActionEventListener getMenuListener() {
		return menuListener;
	}

	/**
	 * @param menuListener the menuListener to set
	 */
	public void setMenuListener(IMenuActionEventListener menuListener) {
		this.menuListener = menuListener;
	}
	
}
