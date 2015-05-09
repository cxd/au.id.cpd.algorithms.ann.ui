/**
 * 
 */
package au.id.cpd.algorithms.ui;

import javax.swing.*;
import au.id.cpd.algorithms.data.*;
/**
 * @author Chris Davey
 * An individual panel s shown for each data table.
 */
public class DataTablePanel extends JScrollPane {
	
	/**
	 * Internal data table instance.
	 */
	private DataTable dataTable;
	
	/**
	 * Default constructor.
	 */
	public DataTablePanel() {
		super();
		this.dataTable = new DataTable();
		this.setViewportView(this.dataTable);
	}
	
	/**
	 * Default constructor.
	 */
	public DataTablePanel(IMatrix<Double> data) {
		super();
		this.dataTable = new DataTable(data);
		this.setViewportView(this.dataTable);
	}

	/**
	 * @return the dataTable
	 */
	public DataTable getDataTable() {
		return dataTable;
	}

	/**
	 * @param dataTable the dataTable to set
	 */
	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

}
