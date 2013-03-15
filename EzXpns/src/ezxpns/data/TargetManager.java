package ezxpns.data;

import java.util.Collections;
import java.util.*;

import ezxpns.data.records.Category;
import ezxpns.data.records.ExpenseRecord;

/**
 * @author shuzhen
 * 
 * A generator that takes in targets and data and produce alert info
 * 
 */
public class TargetManager implements Storable {
	public static interface DataProvider{
		double getMonthlyTotalExpense(Category cat);
	}
	private transient boolean	updated = false, 
								alertUpdated = false;
	private transient DataProvider data;
	private transient Vector<ExpenseRecord>  expenseRecord;
	private TreeMap<Long,Target> mapTarget = new TreeMap<Long, Target>();	// maps category to target // maybe not necessary if max number of targets is small
	
	
	/**
	 * @return if the internal data store is updated (and therefore needs to be stored)
	 */
	public boolean isUpdated() {
		// TODO Auto-generated method stub
		return false;
	}
	public void saved() {
		// TODO Auto-generated method stub
		
	}
	

	/**Preconditions: cannot add more than one target for the same category.
	 * 				 can only set targets for the SAME month
	 */
	public Target setTarget(Category cat, double targetAmt){
		if(mapTarget.containsKey(cat.getID())){
			return null;
		}
		Target target = new Target(cat, targetAmt);
		addTarget(target);
		return target;
	}
		
		
	/**UPDATE METHODS
	 * after making the necessary changes
	 * call getOrdered() and getAlert() to receive latest data
	 */
	
	/** ADD/REMOVE/EDIT target
	 * to make things simple. I suggest only allowing user to make modifications within the same month
	 * All data regarding targets is archived after that month 
	 * so no modification should be made available after that
	 */
	private void addTarget(Target target){
		mapTarget.put(target.getCategory().getID(),target);
		updated = true;
	}
		
	public void removeTarget(Target target){
		mapTarget.remove(target.getCategory());
		updated = true;
	}
		
	public void modifyTarget(Target oldTarget, double targetAmt){
		removeTarget(oldTarget);
		setTarget(oldTarget.getCategory(), targetAmt);		
		updated = true;
	}
		
	/** EDITED categories
	 * if user renamed the category and decided to keep 
	 * all the old entries under the new name
	 * invoke getOrdered(); and getAlerts();
	 */

	/** REMOVED categories
	 * we will remove the target for this category;
	 */
		
	public void removeCategory(Category cat){
		if(mapTarget.containsKey(cat.getID())){
			mapTarget.remove(cat.getID());
		}
		updated=true;
	}
	
	/*
	 * @return a copy of the internal targets, alerts, or ordered targets
	 */
	public Vector<Target> getTargets(){
		Vector<Target> copy = new Vector<Target>();
		for(Target t : this.mapTarget.values()){
			copy.add(t.copy());
		}
		return copy;
	}
		
	public Vector<Bar> getAlerts(){
		Vector<Bar> ordered = getOrderedBar();
		Vector<Bar> alerts = new Vector<Bar>();
		Bar bar;
		for(int i = ordered.size()-1; i>=0; i--){
			bar = ordered.get(i);
			if (isAnAlert(bar))
				alerts.add(bar);
			else break;
		}
		return alerts;
	}

	public int getAlertNumber(){
		if(getAlerts()!=null) return getAlerts().size();
		else return 0;
	}
	
	public int getOrderedNumber(){
		if(getOrderedBar()!=null) return getOrderedBar().size();
		else return 0;
	}
	/**
	 * @returns  Vector of Bar objects that are increasing order
	 */
	public Vector<Bar> getOrderedBar(){
		Vector<Bar> ordered = new Vector<Bar>();
		for(Target target: mapTarget.values()){
			Bar bar = new Bar(target, data.getMonthlyTotalExpense(target.getCategory()));
			ordered.add(bar);
		}
		Collections.sort(ordered);
		
		return ordered;
	}

		
	private boolean isAnAlert(Bar bar){
		if(bar.getColor().equals("RED") || bar.getColor().equals("ORANGE")) 
			return true;
		else 
			return false;
	}
		
}
	
	
	
	
