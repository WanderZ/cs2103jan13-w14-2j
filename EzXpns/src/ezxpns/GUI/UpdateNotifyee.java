/**
 * 
 */
package ezxpns.GUI;

import javax.swing.AbstractAction;

/**
 * Notify the other UI components 
 * <br />Invoked when user performs an action that modifies data
 * @author yyjhao
 */
public interface UpdateNotifyee {
	
	/**
	 * Invoke upon user action
	 * <br />To alert & update all the other components 
	 */
	public void updateAll();
	
	/**
	 * To save an action done by the user into the undo stack
	 * @param action
	 */
	public void addUndoAction(AbstractAction action, String name);
}
