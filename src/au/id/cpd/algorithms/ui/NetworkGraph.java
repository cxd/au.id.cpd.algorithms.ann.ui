/**
 * 
 */
package au.id.cpd.algorithms.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import org.jgraph.*;
import org.jgraph.graph.*;
import org.jgraph.event.*;

import au.id.cpd.algorithms.classifier.*;
import au.id.cpd.algorithms.classifier.ANN.*;
import au.id.cpd.algorithms.adt.*;
import au.id.cpd.algorithms.data.*;

/**
 * @author Chris Davey cd@cpd.id.au
 * 
 * This class provides a graphical representation of the 
 * neural network through the use of the JGraph component.
 * The purpose of this display is to provide a graphical
 * view only. It does not allow graphical editing to occur.
 */
public class NetworkGraph extends JScrollPane {
	// horizontal space between layers.
	private static int H_SPACE = 200;
	// vertical space between units.
	private static int V_SPACE = 50;
	// cells are square.
	private static int CELL_SIZE = 50;
	
	private JGraph graph;
	private GraphModel model;
	private GraphLayoutCache view;
	private DefaultGraphCell[] cells;
	
	
	/**
	 * Internal neural network instance.
	 */
	private NeuralNetwork network;
	/**
	 * Default constructor.
	 *
	 */
	public NetworkGraph() {
		super();
	}
	
	/**
	 * constructor with supplied neural network.
	 * @param NeuralNetwork net
	 */
	public NetworkGraph(NeuralNetwork net) {
		super();
		this.network = net;
		this.init();
	}
	
	/**
	 * Initialise and draw the graph.
	 *
	 */
	private void init() {
		this.model = new DefaultGraphModel();
		this.view = new GraphLayoutCache(model, new DefaultCellViewFactory());
		this.graph = new JGraph(this.model,this.view);
		this.cells = new DefaultGraphCell[this.network.getNodes().size()];
		this.drawNetwork();
		this.graph.setConnectable(false);
		this.graph.setDisconnectable(false);
		this.graph.setAutoResizeGraph(true);
		this.positionVertices();
		
		this.graph.update(this.graph.getGraphics());
		this.setViewportView(this.graph);
		this.updateUI();
	}
	
	/**
	 * Initialise and draw the graph.
	 * @param NeuralNetwork net
	 */
	public void init(NeuralNetwork net) {
		this.network = net;
		this.init();
	}
	
	/**
	 * 
	 *
	 */
	private void drawNetwork() {
		// add all the cells.
		int i = 0;
		for (GraphNode<Double> node : this.network.getNodes()) {
			node.setVisited(false);
			this.cells[i] = new DefaultGraphCell(node.getData().toString());
			Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
			GraphConstants.setBorder(this.cells[i].getAttributes(), border);
			DefaultPort port = new DefaultPort();
			this.cells[i].add(port);
			port.setParent(this.cells[i]);
			i++;
		}
		this.graph.getGraphLayoutCache().insert(this.cells);
		// traverse the graph and connect the nodes togethor.
		// we will make use of the adjacency matrix to make this simpler.
		IMatrix<Integer> edges = this.network.getEdges();
		//System.out.println("Edges:\n"+edges+"\n");
		for(i=0;i<edges.getSize().getRows();i++) {
			// check for adjacent items in the row.
			for(int j=0;j<edges.getSize().getCols();j++) {
				if (edges.get(i, j).doubleValue() == 1) {
					// connect the 2 nodes in the graph.
					DefaultGraphCell from = this.cells[i];
					DefaultGraphCell to = this.cells[j];
					DefaultEdge edge = new DefaultEdge();
					edge.setSource(from.getChildAt(0));
					edge.setTarget(to.getChildAt(0));
					this.graph.getGraphLayoutCache().insertEdge(edge, from, to);
				}
			}
		}
	}
	
	/**
	 * Layout the graph vertices.
	 *
	 */
	private void positionVertices() {
		double x = NetworkGraph.H_SPACE/2;
		double y = NetworkGraph.V_SPACE;
		// layout the input layer vertically.
		for(int i=0;i<this.network.getInputCount();i++) {
			Rectangle2D r = new Rectangle2D.Double(x, y, NetworkGraph.CELL_SIZE, NetworkGraph.CELL_SIZE);
			GraphConstants.setBounds(this.cells[i].getAttributes(), r);
			y += NetworkGraph.CELL_SIZE+NetworkGraph.V_SPACE;
		}
		// layout the hidden units in each layer.
		int cnt = 1;
		x = NetworkGraph.H_SPACE/2+NetworkGraph.H_SPACE+NetworkGraph.CELL_SIZE;
		double sz = this.network.getInputCount()*(NetworkGraph.V_SPACE+NetworkGraph.CELL_SIZE);
		double offset = sz/(2*this.network.getHiddenUnitCount());
		y = offset+NetworkGraph.V_SPACE;
		for(int i=this.network.getInputCount();i<this.network.getInputCount()+(this.network.getHiddenUnitCount()*this.network.getHiddenLayerCount());i++) {
			if (cnt > this.network.getHiddenUnitCount()) {
				// move along the x axis.
				cnt = 1;
				x += NetworkGraph.H_SPACE+NetworkGraph.CELL_SIZE;
				y = offset+NetworkGraph.V_SPACE;
			}
			Rectangle2D r = new Rectangle2D.Double(x, y, NetworkGraph.CELL_SIZE, NetworkGraph.CELL_SIZE);
			GraphConstants.setBounds(this.cells[i].getAttributes(), r);
			y += NetworkGraph.CELL_SIZE+NetworkGraph.V_SPACE;
			++cnt;
		}
		// layout the output nodes.
		x = NetworkGraph.CELL_SIZE+NetworkGraph.H_SPACE+this.network.getHiddenLayerCount()*(NetworkGraph.H_SPACE+NetworkGraph.CELL_SIZE);
		// y should be around the mid point / the number of output cells.
		sz = this.network.getHiddenUnitCount()*(NetworkGraph.V_SPACE+NetworkGraph.CELL_SIZE);
		offset = sz/(2*this.network.getOutputCount());
		y = offset+NetworkGraph.V_SPACE;
		for(int i=this.network.getInputCount()+this.network.getHiddenLayerCount()*this.network.getHiddenUnitCount();i<this.network.getNodes().size();i++) {
			Rectangle2D r = new Rectangle2D.Double(x, y, NetworkGraph.CELL_SIZE, NetworkGraph.CELL_SIZE);
			GraphConstants.setBounds(this.cells[i].getAttributes(), r);
			y += NetworkGraph.CELL_SIZE+NetworkGraph.V_SPACE;
		}
		this.view.refresh(this.view.getAllViews(),false);
	}
	
	/**
	 * @return the network
	 */
	public NeuralNetwork getNetwork() {
		return network;
	}
	/**
	 * @param network the network to set
	 */
	public void setNetwork(NeuralNetwork network) {
		this.network = network;
	}
	
}
