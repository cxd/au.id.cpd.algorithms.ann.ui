/**
 * 
 */
package au.id.cpd.algorithms.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;
import info.monitorenter.gui.chart.*;
import info.monitorenter.gui.chart.io.*;
import info.monitorenter.gui.chart.traces.*;
import info.monitorenter.gui.chart.axis.*;
import info.monitorenter.util.*;
import info.monitorenter.gui.chart.rangepolicies.*;
import info.monitorenter.gui.chart.labelformatters.*;

/**
 * @author Chris Davey cd@cpd.id.au
 * 
 * The gradient descent dialog displays the gradient
 * descent for the current network during training.
 */
public class GradientDescentStaticDialog extends JDialog {
	
	/**
	 * Internal list for errors from the network.
	 */
	private java.util.List<Double> errors;
	/**
	 * Number of epochs to trace
	 */
	private int epoch;
	/**
	 * Internal 2d trace instance.
	 */
	private ITrace2D trace;
	/**
	 * Internal chart instance.
	 */
	private Chart2D chart;
	/**
	 * Construct the dialog with the parent frame.
	 * @param JFrame parent frame
	 * @param int number of epochs to display
	 */
	public GradientDescentStaticDialog(JFrame parent, int epochs, java.util.List<Double> errors) {
		super(parent, "Gradient Descent", false);
		this.epoch = epochs;
		this.errors = errors;
		this.init();
	}
	
	/**
	 * Initialise the component and display the dialog.
	 */
	private void init() {
		JPanel panel = new JPanel();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		panel.setBorder(border);
		panel.setLayout(new BorderLayout());
		this.chart = new Chart2D();
		this.trace = new Trace2DSimple();
		double cnt = 0;
		for(double d : this.errors) {
			this.trace.addPoint(cnt, d);
			cnt++;
		}
		this.trace.setColor(Color.BLUE);
		ARangePolicy policy = new RangePolicyMinimumViewport(new Range(0.0, 0.1));
		NumberFormat d_fmt = NumberFormat.getInstance();
		d_fmt.setMinimumFractionDigits(2);
		d_fmt.setMaximumFractionDigits(10);
		ALabelFormatter fmt = new LabelFormatterNumber(d_fmt);
		chart.getAxisY().setFormatter(fmt);
		chart.getAxisY().setRangePolicy(policy);
		chart.getAxisX().setPaintGrid(true);
		chart.getAxisY().setPaintGrid(true);
		chart.setBackground(Color.WHITE);
		chart.setGridColor(Color.gray);
		chart.addTrace(this.trace);
		panel.add(chart, BorderLayout.CENTER);
		this.setSize(640,480);
		this.getContentPane().add(panel);
	}
	
	/**
	 * @return the epoch
	 */
	public int getEpoch() {
		return epoch;
	}

	/**
	 * @param epoch the epoch to set
	 */
	public void setEpoch(int epoch) {
		this.epoch = epoch;
	}

	/**
	 * @return the errors
	 */
	public java.util.List<Double> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(java.util.List<Double> errors) {
		this.errors = errors;
	}

	/**
	 * @return the trace
	 */
	public ITrace2D getTrace() {
		return trace;
	}

	/**
	 * @param trace the trace to set
	 */
	public void setTrace(ITrace2D trace) {
		this.trace = trace;
	}
	
}
