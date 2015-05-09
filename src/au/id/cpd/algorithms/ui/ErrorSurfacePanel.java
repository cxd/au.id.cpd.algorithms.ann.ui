/**
 * 
 */
package au.id.cpd.algorithms.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import au.id.cpd.algorithms.data.*;

/**
 * Error Surface Panel.
 * Displays the error surface of the classifier
 * in three dimensions.
 * 
 * @author Chris Davey
 * @email cd@cpd.id.au
 *
 */
public class ErrorSurfacePanel extends JScrollPane implements Runnable, KeyListener, MouseMotionListener {

	/**
	 * 3 Matrices for the error surface.
	 */
	private IMatrix<Double> xMatrix;
	private IMatrix<Double> yMatrix;
	private IMatrix<Double> zMatrix;
	
	private double minZ = Double.MAX_VALUE;
	private double maxZ = Double.MIN_VALUE;
	private double minH = Double.MAX_VALUE;
	private double maxH = Double.MIN_VALUE;
	private double minX = Double.MAX_VALUE;
	private double maxX = Double.MIN_VALUE;
	private boolean isInit = false;
	private boolean active = true;
	private SurfaceRenderer r;
	private double x = Double.MAX_VALUE; 
	private double y = Double.MAX_VALUE;
	
	
	private JPanel panel;
	
	private GLCanvas canvas;
	
	/**
	 * Default constructor.
	 *
	 */
	public ErrorSurfacePanel() {
		super();
	}
	
	/**
	 * Construct with the 3 coordinate matrices.
	 * @param x
	 * @param y
	 * @param z
	 */
	public ErrorSurfacePanel(IMatrix<Double> x, IMatrix<Double> y, IMatrix<Double> z) {
		this();
		xMatrix = x;
		yMatrix = y;
		zMatrix = z;
	}

	
	/**
	 * Implementation of runnable to allow this window to execute in its own thread.
	 */
	public void run() {
		if (!isInit)
			init();
		
		while(active) {
			updateUI();
		}
	}
	
	/**
	 * Initialise the error surface display panel.
	 *
	 */
	public void init() {
		this.setSize(640, 480);
		canvas = new GLCanvas();
		canvas.setBackground(Color.BLACK);
		
		// define the branch graph.
		r = new SurfaceRenderer();
		canvas.addGLEventListener(r);
		canvas.addMouseMotionListener(this);
		// add the branch graph.
		
		canvas.requestFocus();
		canvas.display();
		this.setViewportView(canvas);
		this.updateUI();
		
	}
	
	
	
	public void update(Graphics g) {
		canvas.display();
		super.update(g);
	}
	
	/**
	 * @return the xMatrix
	 */
	public IMatrix<Double> getXMatrix() {
		return xMatrix;
	}

	/**
	 * @param matrix the xMatrix to set
	 */
	public void setXMatrix(IMatrix<Double> matrix) {
		xMatrix = matrix;
		minX = xMatrix.getMin();
		maxX = xMatrix.getMax();
	}

	/**s
	 * @return the yMatrix
	 */
	public IMatrix<Double> getYMatrix() {
		return yMatrix;
	}

	/**
	 * @param matrix the yMatrix to set
	 */
	public void setYMatrix(IMatrix<Double> matrix) {
		yMatrix = matrix;
		minH = yMatrix.getMin();
		maxH = yMatrix.getMax();
	}

	/**
	 * @return the zMatrix
	 */
	public IMatrix<Double> getZMatrix() {
		return zMatrix;
	}

	/**
	 * @param matrix the zMatrix to set
	 */
	public void setZMatrix(IMatrix<Double> matrix) {
		zMatrix = matrix;
		minZ = zMatrix.getMin();
		maxZ = zMatrix.getMax();
	}
	
	/**
	 * Callback for keylistener
	 */
	public void keyPressed(KeyEvent key) {
		switch(key.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_KP_LEFT:
			r.setRotateT(r.getRotateT() - 2.0f);
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_KP_RIGHT:
			r.setRotateT(r.getRotateT() + 2.0f);
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_KP_UP:
			r.setRotateV(r.getRotateV() - 2.0f);
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_KP_DOWN:
			r.setRotateV(r.getRotateV() + 2.0f);
			break;
		}
	}
	
	/**
	 * Callback for keylistener
	 */
	public void keyReleased(KeyEvent key) {
		
	}
	
	/**
	 * Callback for keylistener
	 */
	public void keyTyped(KeyEvent key) {
		
	}
	
	public void mouseDragged(MouseEvent m) {
		if (x == Double.MAX_VALUE) {
			x = m.getPoint().getX();
			y = m.getPoint().getY();
			return;
		}
		double d = m.getPoint().distance(x, y);
		double xd = m.getPoint().getX() - x;
		double yd = m.getPoint().getY() - y;
		float f = 2.0f;
		float v = 2.0f;
		if (xd < 0) {
			f = -1*2.0f;
		}
		if (yd < 0) {
			v = -1.0f*v;
		}
		if (xd != 0) {
			r.setRotateT(r.getRotateT() + f);
		}
		if (yd != 0) {
			r.setRotateV(r.getRotateV() + v);
		}
		
		x = m.getPoint().getX();
		y = m.getPoint().getY();
	}
	
	public void mouseMoved(MouseEvent m) {
		
	}
	
	private class SurfaceRenderer implements GLEventListener {
		
		private GLU glu = new GLU();
		private double near = 1.0;
		private double far = 1000;
		private float rotateT = 30.0f;
		private float rotateV = 5.0f;
		
		
		/**
		 * Draw into the gl render pipeline.
		 */
		public void display(GLAutoDrawable gLDrawable) {
			final GL gl = gLDrawable.getGL();
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		    if ((xMatrix == null)||
				(yMatrix == null)||
				(zMatrix == null)) 
		    	return;
		    if (xMatrix.getSize().compareTo(yMatrix.getSize()) != 0)
		    	return;
		    if (xMatrix.getSize().compareTo(zMatrix.getSize()) != 0)
		    	return;
		    gl.glLoadIdentity();
		    gl.glTranslated(0.0, 0.0, minZ - 15.0);
		    //gl.glRotatef(rotateT, 1.0f, 0.0f, 0.0f);
	        gl.glRotatef(rotateT, 0.0f, 1.0f, 0.0f);
	        gl.glRotatef(rotateV, 1.0f, 0.0f, 0.0f);
	        //gl.glRotatef(rotateT, 0.0f, 1.0f, 0.0f);
	        drawSurfaceMesh(gl, false);
	        drawSurfaceMesh(gl, true);
	        //rotateT += 2.0f;
		    gl.glFlush();
		}
		
		/**
		 * urf(X,Y,Z) creates a shaded surface using Z 
		 * for the color data as well as surface height. 
		 * X and Y are vectors or matrices defining the x and y 
		 * components of a surface. 
		 * If X and Y are matrices, dim(X) = n,m and dim(Y) = n,m, 
		 * where [m,n] = size(Z). 
		 * In this case, the vertices of the surface faces are 
		 * (X(i,j), Y(i,j), Z(i,j)) triples.
		 * 
		 * TODO: Work out how to convert the matrices into a mesh.
		 * 
		 * This function will only draw a 2d array of points in 3d space.
		 * 
		 * @param gl
		 */
		private void drawPointMesh(GL gl) {
			gl.glBegin(GL.GL_POINTS);
			Size sz = xMatrix.getSize();
			for(int i=0;i<sz.getRows();i++) {
				for (int j=0;j<sz.getCols();j++) {
					double x = (Double)xMatrix.get(i, j);
					double y = (Double)yMatrix.get(i, j);
					double z = (Double)zMatrix.get(i, j);
					gl.glColor3d(1.0, 1.0, 1.0);
					gl.glVertex3d(x, y, z);
				}
			}
			
			gl.glEnd();
		}
		
		/**
		 * Draw the surface mesh.
		 * @param gl
		 * @param outline
		 */
		private void drawSurfaceMesh(GL gl, boolean outline) {
			if (outline) {
				gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
				gl.glEnable(GL.GL_POLYGON_OFFSET_LINE);
				gl.glPolygonOffset(-1.0f, -1.0f);
				gl.glEdgeFlag(true);	    
			} else {
				gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
				gl.glEnable(GL.GL_POLYGON_OFFSET_FILL);
				gl.glPolygonOffset(1.0f, 1.0f);
			}
			gl.glBegin(GL.GL_TRIANGLE_STRIP);
			// left to right.
			Size sz = xMatrix.getSize();
			double[][] points;
			for(int i=0;i<sz.getRows();i++) {
				// first row.
				if (i == 0) {
					for(int j=0;j<sz.getCols();j++) {
						if (j == 0) {
							points = new double[3][3];
							points[0] = getPoint(i,j);
							points[1] = getPoint(i+1,j);
							points[2] = getPoint(i,j+1);
							drawFace(gl, points, outline);
						} else if (j < sz.getCols() - 1){
							points = new double[2][3];
							points[0] = getPoint(i+1,j);
							points[1] = getPoint(i, j+1);
							drawFace(gl, points, outline);
						} else {
							points = new double[1][3];
							points[0] = getPoint(i+1,j);
							drawFace(gl, points, outline);
						}
					}
				} else if (((i % 2) > 0)&&(i < sz.getRows() - 1)) {
					
					for(int j=sz.getCols() - 1; j >= 0; j--) {
						
						if (j == sz.getCols() - 1) {
							
							points = new double[3][3];
							points[0] = getPoint(i+1,j);
							points[1] = getPoint(i,j-1);
							points[2] = getPoint(i+1, j-1);
							drawFace(gl, points, outline);
							
						} else if (j > 0) {
							points = new double[2][3];
							points[0] = getPoint(i, j-1);
							points[1] = getPoint(i+1, j-1);
							drawFace(gl, points, outline);
						} else if (j == 0) {
							points = new double[1][3];
							points[0] = getPoint(i+1, j);
							drawFace(gl, points, outline);
						}
						
					}
				} else if (((i % 2) == 0) && (i < sz.getRows() - 1)) {
					
					for(int j=0;j<sz.getCols();j++) {
						if (j < sz.getCols() - 1) {
							points = new double[2][3];
							points[0] = getPoint(i+1,j);
							points[1] = getPoint(i,j+1);
							drawFace(gl, points, outline);
						} else {
							points = new double[1][3];
							points[0] = getPoint(i+1,j);
							drawFace(gl, points, outline);
						}
					}
				}
			}
			gl.glEnd();
			if (outline) {
				gl.glDisable(GL.GL_POLYGON_OFFSET_LINE);
			} else {
				gl.glDisable(GL.GL_POLYGON_OFFSET_FILL);
			}
			
		}
		
		/**
		 * Stitch the triangle mesh surface.
		 * @param i
		 * @param j
		 */
		private double[] getPoint(int i, int j) {
			Size sz = xMatrix.getSize();
			double[] vec = new double[3];
			if ((i < sz.getRows()) && (j < sz.getCols())) {
				vec[0] = (Double)xMatrix.get(i, j);
				vec[1] = (Double)yMatrix.get(i, j);
				vec[2] = (Double)zMatrix.get(i, j);
			}
			return vec;
		}
		
		/**
		 * Draw a set of points as a face.
		 * @param gl
		 * @param points
		 */
		private void drawFace(GL gl, double[][] points, boolean outline) {
			for(int m=0;m<points.length;m++) {
				if (!outline) {
					double[] col = getColor(minH, maxH, points[m][1]);
					gl.glColor3d(col[0], col[1], col[2]);
				} else {
					gl.glColor3d(0.4, 0.4, 0.4);
				}
				gl.glVertex3d(points[m][0], points[m][1], points[m][2]);
			}
		}
		
		/**
		 * Get the color for the height value.
		 * @return
		 */
		private double[] getColor(double min, double max, double value) {
			/* Set of colors.
			 * 
			 * blue 0 - 0.16, 
			 * cyan 0.12 - 0.32, 
			 * green 0.28 - 0.48,
			 * yellow 0.44 - 0.64,
			 * orange 0.60 - 0.96,
			 * red 0.90 - 1.0
			 */
			// normalise to between 0 and 1.
			double delta = max - min;
			double fact = delta/6.0;
			double overlap = 0.05 * delta;
			// a set of 6 colors
			double [] blue = new double[] { 0.0, fact };
			double [] cyan = new double[] { fact - overlap, fact*2.0 };
			double [] green = new double[] { fact*2.0 - overlap, fact*3.0 };
			double[] yellow = new double[] { fact*3.0 - overlap, fact*4.0 };
			double[] orange = new double[] { fact*4.0 - overlap, fact*5.0 };
			double[] red = new double[] { fact*5.0 - overlap, fact*6.0 };
			
			double [] mf = {
					membership(blue[0], blue[1], value),
					membership(cyan[0], cyan[1], value),
					membership(green[0], green[1], value),
					membership(yellow[0], yellow[1], value),
					membership(orange[0], orange[1], value),
					membership(red[0], red[1], value) 
					};
			
			// combine the rules togethor
			/**
			 * blue = 0,0,1
			 * cyan = 0,0.5,1
			 * green = 0,1,0
			 * yellow = 1,1,0
			 * orange = 1,0.5,0
			 * red = 1,0,0
			 */
			double [][] colors = {
					{ 0.0, 0.0, 1.0 },
					{ 0.0, 0.5, 1.0 },
					{ 0.0, 1.0, 0.0 },
					{ 1.0, 1.0, 0.0 },
					{ 1.0, 0.5, 0.0 },
					{ 1.0, 0.0, 0.0}
			};
			// just get the maximum valued membership function.
			double m = Double.MIN_VALUE;
			int idx = 0;
			for(int i=0;i<mf.length;i++) {
				if (mf[i] > m) {
					m = mf[i];
					idx = i;
				}
			}
			double [] color = colors[idx];
			return color;
		}
		
		/**
		 * Calculate the membership of a point to its corresponding 
		 * set.
		 * @param min
		 * @param max
		 * @param value
		 * @return
		 */
		private double membership(double min, double max, double value) {
			
			double delta = max - min;
			// somewhere in between.
			double [] range = new double[] { min, min + delta / 2.2, min + delta / 1.8, max };
			if ((value > min)&&(value <= range[1])) {
				return ( value - min ) / (range[1] - range[0]);
			} else if ((value > range[1]) && (value <= range[2])) {
				return 1;
			} else if ((value > range[2]) && (value <= range[3])) {
				return (value - range[2]) / (range[3] - range[2]);
			}
			return 0;
		}
		
		
		public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {
	    }
		
		/**
		 * Init the open gl library
		 */
		public void init(GLAutoDrawable gLDrawable) {
			final GL gl = gLDrawable.getGL();
			gl.glShadeModel(GL.GL_SMOOTH);
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		    gl.glClearDepth(1.0f);
		    gl.glEnable(GL.GL_DEPTH_TEST);
		    gl.glDepthFunc(GL.GL_LEQUAL);
		    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		    gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		    isInit = true;
		}
		
		/**
		 * Standard reshape method.
		 */
		public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
			final GL gl = gLDrawable.getGL();
	        if(height <= 0) {
	            height = 1;
	        }
	        final float h = (float)width / (float)height;
	        gl.glMatrixMode(GL.GL_PROJECTION);
	        gl.glLoadIdentity();
	        //glu.gluPerspective(90.0f, h, near, far);
	        
	        gl.glOrtho(minX - 5.0, maxX + 5.0, minH - 5.0, maxH + 5.0, near, far);
	        
	        gl.glMatrixMode(GL.GL_MODELVIEW);
	        gl.glLoadIdentity();
		}
		
		

		/**
		 * @return the rotateT
		 */
		public float getRotateT() {
			return rotateT;
		}

		/**
		 * @param rotateT the rotateT to set
		 */
		public void setRotateT(float rotateT) {
			this.rotateT = rotateT;
		}

		/**
		 * @return the rotateV
		 */
		public float getRotateV() {
			return rotateV;
		}

		/**
		 * @param rotateV the rotateV to set
		 */
		public void setRotateV(float rotateV) {
			this.rotateV = rotateV;
		}
		
	}


	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
