/**
 * 
 */
package au.id.cpd.algorithms.ui;

import au.id.cpd.algorithms.data.*;

import javax.swing.table.*;

/**
 * @author cd
 *
 */
public class DataTableModel extends AbstractTableModel {

	/**
	 * Internal data instance.
	 */
	private IMatrix<Double> data;
	
	/**
	 * 
	 */
	public DataTableModel() {
		this.data = null;
	}
	
	/**
	 * 
	 */
	public DataTableModel(IMatrix<Double> data) {
		this.data = data;
		fireTableDataChanged();
	}
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return (this.data == null) ? 0 : this.data.getSize().getCols();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		// TODO Auto-generated method stub
		return (this.data == null) ? 0 : this.data.getSize().getRows();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (this.data != null) {
			return this.data.get(rowIndex, columnIndex);
		}
		return null;
	}

	/**
	 * @return the data
	 */
	public IMatrix<Double> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(IMatrix<Double> data) {
		this.data = data;
		fireTableDataChanged();
	}

}
