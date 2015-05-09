/**
 * 
 */
package au.id.cpd.algorithms.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import au.id.cpd.algorithms.data.*;
/**
 * @author cd
 *
 */
public class ErrorSurfaceDialog extends JDialog {

	private IMatrix<Double> xMat;
	private IMatrix<Double> yMat;
	private IMatrix<Double> zMat;
	private ErrorSurfacePanel errSurface;
	private Thread t;
	/**
	 * Construct the dialog with the parent frame.
	 * @param JFrame parent frame
	 * @param int number of epochs to display
	 */
	public ErrorSurfaceDialog(JFrame parent, IMatrix<Double> X, IMatrix<Double> Y, IMatrix<Double> Z) {
		super(parent, "Error Surface", false);
		xMat = X;
		yMat = Y;
		zMat = Z;
		this.init();
	}
	
	
	/**
	 * Initialize the panel used for display
	 *
	 */
	private void init() {
		setSize(new Dimension(640,480));
		setLayout(new BorderLayout());
		errSurface = new ErrorSurfacePanel();
		errSurface.setXMatrix(xMat);
		errSurface.setYMatrix(yMat);
		errSurface.setZMatrix(zMat);
		add("Center", errSurface);
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
			    	errSurface.setActive(false);
		            t.join(1000);
	            } catch(Exception err) {
	            	
	            }
            }
        });
		addKeyListener(errSurface);
		t = new Thread(errSurface);
		t.start();
		setVisible(true);
	}
	
	
}
