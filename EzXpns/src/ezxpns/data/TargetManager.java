package ezxpns.data;

import java.util.Collections;
import java.util.*;

import ezxpns.data.records.Category;


/**
 * @author Suzzie
 * 
 * A generator that takes in targets and data and produce alert info
 * 
 */
public class TargetManager extends Storable {
	public static interface DataProvider{
		double getMonthlyExpense(Category cat);
		Category getCategory(long id);
	}
	private transient DataProvider data;
	private transient Vector<Bar> bars = new Vector<Bar>();
	private TreeMap<Long,Target> mapTarget = new TreeMap<Long, Target>();	// maps category to target // maybe not necessary if max number of targets is small
	private transient boolean dataUpdated = true;
	
	/**
	 * @param data
	 */
	public TargetManager(DataProvider data){
		this.data = data;
		dataUpdated = true;
	}
	
	@Override
	public void afterDeserialize(){
		for(long id : mapTarget.keySet()){
			mapTarget.put(id, new Target(data.getCategory(id), mapTarget.get(id).getTargetAmt()));
		}
	}
	
	/**
	 * 
	 * @param data
	 */
	public void setDataProvider(DataProvider data){
		this.data = data;
		dataUpdated = true;
	}
	
	/**
	 * This removes the target that has the category ID from the TreeMap
	 * This is called when a Category is deleted
	 * @param identifier
	 */
	public void removeCategoryTarget(long identifier){
		mapTarget.remove(identifier);
		markUpdate();
		markDataUpdated();
	}

	/*Preconditions: cannot add more than one target for the same category.
	 * 				 can only set targets for the SAME month
	 */
	
	/**
	 * This returns the target with the same Category and double attribute
	 * @param cat
	 * @param targetAmt
	 * @return a Target that is newly created
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
	 * adds a new target into the TreeMap
	 * @param target
	 */
	private void addTarget(Target target){
		mapTarget.put(target.getCategory().getID(),target);
		markUpdate();
	}
	/**
	 * Removes target from the tree map
	 * @param target
	 */
	public void removeTarget(Target target){
		mapTarget.remove(target.getCategory().getID());
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
	 * This returns a copy of the internal targets
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
	 * @return target object that correspond to Category
	 */
	public Target getTarget(Category cat){
		return mapTarget.get(cat.getID());
	}
	

	/**
	 * 
	 * @return a vector of bar that is classified as alerts
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
	 * This generates an ordered vector of bars in increasing order of ratio of currentAmt/targetAmt
	 * @return a vector of bar
	 */
	public Vector<Bar> getOrderedBar(){
		if(dataUpdated){
			genBars();
			dataUpdated = false;
		}
		return bars;
	}
	
	/**
	 * Updates the bars
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
	 * This method checks if a bar qualifies as an alert
	 * @param bar
	 * @return true is the Bar qualifies as an Alert
	 */
	private boolean isAnAlert(Bar bar){
		if(bar.getBarColor()== BarColor.HIGH || bar.getBarColor()== BarColor.MEDIUM) 
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
	
	
	
	
