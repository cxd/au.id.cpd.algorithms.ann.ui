/**
 * 
 */
package au.id.cpd.algorithms.ui;

import javax.swing.*;
import javax.swing.table.*;
import au.id.cpd.algorithms.data.*;

/**
 * @author cd
 *
 */
public class DataTable extends JTable {

	/**
	 * default constructor
	 *
	 */
	public DataTable() {
		super();
	}
	/**
	 * Construct with supplied data.
	 * @param data
	 */
	public DataTable(IMatrix<Double> data) {
		super(data.getSize().getRows(), data.getSize().getCols());
		DataTableModel model = new DataTableModel(data);
		this.setModel(model);
	}
	
	/**
	 * Define the data table model by supplying a matrix of data.
	 * @param data
	 */
	public void setModel(IMatrix<Double> data) {
		DataTableModel model = new DataTableModel(data);
		this.setModel(model);
		((AbstractTableModel)this.getModel()).fireTableDataChanged();
		this.updateUI();
	}
	
}
