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
public class TargetManager extends Storable {
	public static interface DataProvider{
		double getMonthlyExpense(Category cat);
	}
	private transient DataProvider data;
	private transient Vector<Bar> bars = new Vector<Bar>();
	private TreeMap<Long,Target> mapTarget = new TreeMap<Long, Target>();	// maps category to target // maybe not necessary if max number of targets is small
	private transient boolean dataUpdated = true;
	
	public TargetManager(DataProvider data){
		this.data = data;
		dataUpdated = true;
	}
	
	public void setDataProvider(DataProvider data){
		this.data = data;
		dataUpdated = true;
	}
	
	public void removeCategoryTarget(long identifier){
		mapTarget.remove(identifier);
		markUpdate();
		markDataUpdated();
	}

	/*Preconditions: cannot add more than one target for the same category.
	 * 				 can only set targets for the SAME month
	 */
	/**
	 * 
	 * @param cat
	 * @param targetAmt
	 * @return
	 */
	public Target setTarget(Category cat, double targetAmt){
		if(mapTarget.containsKey(cat.getID())){
			return null;
		}
		Target target = new Target(cat, targetAmt);
		addTarget(target);
		dataUpdated = true;
		return target;
	}
		
		
	/*UPDATE METHODS
	 * after making the necessary changes
	 * call getOrdered() and getAlert() to receive latest data
	 */
	
	/* ADD/REMOVE/EDIT target
	 * to make things simple. I suggest only allowing user to make modifications within the same month
	 * All data regarding targets is archived after that month 
	 * so no modification should be made available after that
	 */
	
	/**
	 * 
	 * @param target
	 */
	private void addTarget(Target target){
		mapTarget.put(target.getCategory().getID(),target);
		markUpdate();
	}
	/**
	 * Removes target and all references to it
	 * @param target
	 */
	public void removeTarget(Target target){
		mapTarget.remove(target.getCategory());
		dataUpdated = true;
		markUpdate();
	}
	
	/**
	 * Removes oldTarget and create a new one using setTarget(Category param1, double param2)
	 * @param oldTarget
	 * @param targetAmt
	 */
	public void modifyTarget(Target oldTarget, double targetAmt){
		removeTarget(oldTarget);
		setTarget(oldTarget.getCategory(), targetAmt);	
		dataUpdated = true;
		markUpdate();
	}

	
	/** 
	 * 
	 * @return a copy of the internal targets
	 */
	public Vector<Target> getTargets(){
		Vector<Target> copy = new Vector<Target>();
		for(Target t : this.mapTarget.values()){
			copy.add(t.copy());
		}
		return copy;
	}
	/**
	 * 
	 * @param cat
	 * @return target object that correspond to cat
	 */
	public Target getTarget(Category cat){
		return mapTarget.get(cat.getID());
	}
	
	/**
	 * 
	 * @return true if the vector is empty
	 */
	public boolean isEmpty(Vector<Bar> v){
		if(v == null)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return a vector of <Bar> 
	 */
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


	/**
	 * 
	 * @return a vector of <Bar> in increasing order of ratio of currentAmt/targetAmt
	 */
	public Vector<Bar> getOrderedBar(){
		if(dataUpdated){
			genBars();
			dataUpdated = false;
		}
		return bars;
	}
	
	/**
	 * Updates the bar
	 */
	private void genBars(){
		if(bars == null){
			bars = new Vector<Bar>();
		}else{
			bars.clear();
		}
		for(Target target: mapTarget.values()){
			Bar bar = new Bar(target, data.getMonthlyExpense(target.getCategory()));
			bars.add(bar);
		}
		Collections.sort(bars);
	}

	/**
	 * 
	 * @param bar
	 * @return true is the Bar qualifies as an Alert
	 */
	private boolean isAnAlert(Bar bar){
		if(bar.getColor()== BarColor.HIGH || bar.getColor()== BarColor.MEDIUM) 
			return true;
		else 
			return false;
	}
	
	/**
	 * 
	 */
	public void markDataUpdated(){
		dataUpdated = true;
	}
		
}
	
	
	
	
