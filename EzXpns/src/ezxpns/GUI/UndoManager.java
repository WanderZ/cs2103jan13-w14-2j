package ezxpns.GUI;

import java.awt.event.ActionEvent;
import java.util.Stack;

import javax.swing.*;

import ezxpns.util.*;

/**
 * A wrapper that allows one to easily add/perform undo actions
 * @author A0099621X
 */
public class UndoManager {
	private AbstractAction action;
	private Stack<Pair<AbstractAction, String> > stack;
	private AbstractAction postUndo;
	
	@SuppressWarnings("serial")
	public UndoManager(){
		action = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				performUndo();
			}
		};
		action.putValue(Action.NAME, "Undo");
		action.setEnabled(false);
		stack = new Stack<Pair<AbstractAction, String> >();
	}
	
	/**
	 * Get the action that pops and performs the current undo action
	 * @return the undo action
	 */
	public AbstractAction getAction(){
		return action;
	}
	
	/**
	 * Add an undo action to the current undo stack with the name
	 * @param a the action to perform when undoing
	 * @param name the name of the action, without "undo"
	 */
	public void add(AbstractAction action, String name){
		stack.push(new Pair<AbstractAction, String>(action, name));
		this.action.setEnabled(true);
		this.action.putValue(Action.NAME, "<html><center>Undo<br />" + name + "</center></html>");
	}
	
	/**
	 * Set the action that's to be carried out after an undo action is performed
	 * @param action
	 */
	public void setPostUndo(AbstractAction action){
		postUndo = action;
	}
	
	/**
	 *Perform an undo action. 
	 */
	private void performUndo(){
		(stack.pop()).getLeft().actionPerformed(null);
		if(stack.empty()){
			action.setEnabled(false);
			action.putValue(Action.NAME, "Undo");
		}else{
			action.putValue(Action.NAME, "<html><center>Undo<br />" + stack.peek().getRight() + "</center></html>");
		}
		if(postUndo != null){
			postUndo.actionPerformed(null);
		}
	}
}
