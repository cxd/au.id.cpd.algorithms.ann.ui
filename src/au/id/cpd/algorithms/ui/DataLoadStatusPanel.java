/**
 * 
 */
package au.id.cpd.algorithms.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
/**
 * @author cd
 *
 */
public class DataLoadStatusPanel extends JPanel {
	private JProgressBar progress;
	private String label;
	public DataLoadStatusPanel() {
		super();
		this.label = "Loading Data";
		this.init();
	}
	
	public DataLoadStatusPanel(String label) {
		super();
		this.label = label;
		this.init();
	}
	
	private void init() {
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		this.setBorder(border);
		this.progress = new JProgressBar();
		this.progress.setIndeterminate(true);
		this.progress.setBackground(Color.WHITE);
		this.progress.setForeground(Color.BLUE);
		this.setLayout(new FlowLayout());
		JLabel label = new JLabel(this.label);
		this.add(label);
		this.add(this.progress);
	}
}
