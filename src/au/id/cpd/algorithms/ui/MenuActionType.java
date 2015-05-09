/**
 * 
 */
package au.id.cpd.algorithms.ui;

/**
 * @author cd
 *
 */
public enum MenuActionType {
	LOAD_DATA(0),
	LOAD_NETWORK(1),
	SAVE_NETWORK(2),
	CLOSE(3),
	EXIT(4),
	EDIT_DATA_PROPERTIES(5),
	EDIT_NETWORK_PROPERTIES(6),
	VIEW_DATA_GRID(7),
	VIEW_NETWORK_MODEL(8),
	VIEW_GRADIENT_DESCENT(9),
	RUN_TRAIN_MODEL(10),
	RUN_TEST_MODEL(11),
	RUN_CLASSIFY_DATA(12),
	HELP_ABOUT(13),
	EDIT_DATA_PROPERTIES_OK(14),
	EDIT_DATA_PROPERTIES_CANCEL(15),
	EDIT_NETWORK_PROPERTIES_OK(16),
	EDIT_NETWORK_PROPERTIES_CANCEL(17),
	UPDATE_ERROR_LIST(18),
	RUN_KFOLD_VALIDATE(19),
	SAVE_DATA(20),
	SAVE_WEIGHTS(21),
	VIEW_NETWORK_ERROR_SURFACE(22);
	
	private int value;
	/**
	 * constructor for enum type.
	 * @param t
	 */
	private MenuActionType(int t) {
		this.value = t;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	
}
