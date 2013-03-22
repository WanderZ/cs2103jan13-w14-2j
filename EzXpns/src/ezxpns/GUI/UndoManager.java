/**
 * 
 */
package ezxpns.GUI;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.Stack;

import javax.swing.*;

/**
 * A wrapper that allows one to easily add/perform undo actions
 * @author yyjhao
 *
 */
public class UndoManager {
	private AbstractAction action;
	private Stack<AbstractAction> stack;
	
	public UndoManager(){
		action = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				performUndo();
			}
		};
		action.setEnabled(false);
	}
	
	/**
	 * @return the undo action
	 */
	public AbstractAction getAction(){
		return action;
	}
	
	/**
	 * @param a the action to perform when undoing
	 * @param name the name of the action, without "undo"
	 */
	public void add(AbstractAction a, String name){
		stack.push(a);
		action.setEnabled(true);
		action.putValue(Action.NAME, "Undo " + name);
	}
	
	private void performUndo(){
		(stack.pop()).actionPerformed(null);
		if(stack.empty()){
			action.setEnabled(false);
		}
	}
}
