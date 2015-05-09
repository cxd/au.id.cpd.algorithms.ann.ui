/**
 * 
 */
package au.id.cpd.algorithms.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
/**
 * @author cd
 *
 */
public class ContainerFrame extends JFrame implements Runnable {
	
	private MainMenuBar menu;
	private JPanel container;
	private Component activePanel;
	private JPanel statusPanel;
	/**
	 * Default constructor.
	 */
	public ContainerFrame() {
		super();
		this.init();
	}
	
	private void init() {
		this.menu = new MainMenuBar();
		this.container = new JPanel();
		this.container.setLayout(new BorderLayout());
		this.statusPanel = new JPanel();
		this.statusPanel.setLayout(new BorderLayout());
		Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		this.container.setBorder(border);
		this.statusPanel.setBorder(border);
		this.statusPanel.setPreferredSize(new Dimension(this.getWidth(), 30));
		LayoutManager layout = new BorderLayout();
		this.setLayout(layout);
		this.add(this.menu, BorderLayout.NORTH);
		this.add(this.container, BorderLayout.CENTER);
		this.add(this.statusPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Entry point for frame.
	 */
	public void run() {
		this.setVisible(true);
	}

	/**
	 * @return the menu
	 */
	public MainMenuBar getMenu() {
		return menu;
	}

	/**
	 * @param menu the menu to set
	 */
	public void setMenu(MainMenuBar menu) {
		this.menu = menu;
	}

	/**
	 * @return the statusPanel
	 */
	public JPanel getStatusPanel() {
		return statusPanel;
	}

	/**
	 * @param statusPanel the statusPanel to set
	 */
	public void setStatusPanel(JPanel statusPanel) {
		this.statusPanel = statusPanel;
	}

	/**
	 * @return the activePanel
	 */
	public Component getActivePanel() {
		return activePanel;
	}

	/**
	 * @param activePanel the activePanel to set
	 */
	public void setActivePanel(Component activePanel) {
		if (this.activePanel != null) {
			this.container.remove(this.activePanel);
		}
		this.activePanel = activePanel;
		this.container.add(this.activePanel, BorderLayout.CENTER);
		this.container.validate();
		this.update(this.getGraphics());
	}

	/**
	 * @return the container
	 */
	public JPanel getContainer() {
		return container;
	}

	/**
	 * @param container the container to set
	 */
	public void setContainer(JPanel container) {
		this.container = container;
	}
}
