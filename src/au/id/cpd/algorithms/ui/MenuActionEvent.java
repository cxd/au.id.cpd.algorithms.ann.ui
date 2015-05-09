/**
 * 
 */
package au.id.cpd.algorithms.ui;

import java.awt.event.*;

/**
 * @author cd
 *
 */
public class MenuActionEvent extends ActionEvent {

	private MenuActionType actionType;
	
	/**
	 * @param source
	 * @param id
	 * @param command
	 */
	public MenuActionEvent(Object source, MenuActionType id, String command) {
		super(source, id.getValue(), command);
		this.actionType = id;
	}

	/**
	 * @return the actionType
	 */
	public MenuActionType getActionType() {
		return actionType;
	}

	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(MenuActionType actionType) {
		this.actionType = actionType;
	}

}
