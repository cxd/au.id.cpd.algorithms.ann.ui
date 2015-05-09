/**
 * 
 */
package au.id.cpd.algorithms.ui.test;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import au.id.cpd.algorithms.data.*;
import au.id.cpd.algorithms.data.io.*;
import au.id.cpd.algorithms.ui.*;


/**
 * @author cd
 *
 */
public class TestErrorSurfaceUI implements KeyListener {

	private IMatrix<Double> xMatrix;
	private IMatrix<Double> yMatrix;
	private IMatrix<Double> zMatrix;
	
	private String xDataFile = "/Users/cd/workspace/au.id.cpd.algorithms/resources/data/error-surface/weight0.csv";
	private String yDataFile = "/Users/cd/workspace/au.id.cpd.algorithms/resources/data/error-surface/sqrt_zvalues.csv";
	private String zDataFile = "/Users/cd/workspace/au.id.cpd.algorithms/resources/data/error-surface/weight1.csv";
	
	private boolean flag = true;
	
	private ErrorSurfacePanel  errSurface;
	private Thread t;
	
	
	public TestErrorSurfaceUI() {
		
	}
	
 
	public void run() {
		setUp();
		testErrorSurface();
	}
	
	
	/**
	 * @throws java.lang.Exception
	 */
	protected void setUp() {
		try {
			MatrixReader reader = new MatrixReader(new FileReader(xDataFile));
			xMatrix = reader.readMatrix();
			reader.close();
			reader = new MatrixReader(new FileReader(yDataFile));
			yMatrix = reader.readMatrix();
			reader.close();
			reader = new MatrixReader(new FileReader(zDataFile));
			zMatrix = reader.readMatrix();
			reader.close();
		} catch(Exception e) {
			
		}
	}
	
	
	public void testErrorSurface() {
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(640,480));
		frame.setTitle("Test Error Surface.");
		frame.setLayout(new BorderLayout());
		errSurface = new ErrorSurfacePanel();
		errSurface.setXMatrix(xMatrix);
		errSurface.setYMatrix(yMatrix);
		errSurface.setZMatrix(zMatrix);
		frame.add("Center", errSurface);
		
		frame.setVisible(true);
		
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                flag = false;
                try {
			    	flag = false;
		            errSurface.setActive(false);
		            t.join(1000);
	            } catch(Exception err) {
	            	
	            }
                System.exit(0);
            }
        });
        
        frame.addKeyListener(errSurface);
        t = new Thread(errSurface);
        t.start();
        
		
	}
	
	public void keyPressed(KeyEvent e) {
		    if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	            try {
			    	flag = false;
		            errSurface.setActive(false);
		            t.join(1000);
	            } catch(Exception err) {
	            	
	            }
	            System.exit(0);
		    }
	}

	
	public void keyReleased(KeyEvent e) {
    }
 
    public void keyTyped(KeyEvent e) {
    }
	
	public static void main(String[] args) {
		TestErrorSurfaceUI s = new TestErrorSurfaceUI();
		s.run();
	}
	
	
	
}
