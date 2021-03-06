/**
 * 
 */
package au.id.cpd.algorithms.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author cd
 * Concrete file filter class for data files.
 */
public class DataFileFilter extends FileFilter {

	public DataFileFilter() {
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
		String name = f.getName();
		String[] parts = name.split("\\.");
		if (parts.length == 0) return false;
		String ext = parts[parts.length-1];
		ext = ext.toLowerCase();
		if (ext.compareTo("txt") == 0) return true;
		if (ext.compareTo("csv") == 0) return true;
		if (ext.compareTo("data") == 0) return true;
		if (ext.compareTo("dat") == 0) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "*.txt, *.csv, *.data, *.dat";
	}

}
