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
	private transient Vector<Bar> bars;
	private TreeMap<Long,Target> mapTarget = new TreeMap<Long, Target>();	// maps category to target // maybe not necessary if max number of targets is small
	private transient boolean dataUpdated = true;
	
	public TargetManager(DataProvider data){
		this.data = data;
	}
	
	public void setDataProvider(DataProvider data){
		this.data = data;
	}

	/*Preconditions: cannot add more than one target for the same category.
	 * 				 can only set targets for the SAME month
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
	private void addTarget(Target target){
		mapTarget.put(target.getCategory().getID(),target);
		markUpdate();
	}
		
	public void removeTarget(Target target){
		mapTarget.remove(target.getCategory());
		dataUpdated = true;
		markUpdate();
	}
		
	public void modifyTarget(Target oldTarget, double targetAmt){
		removeTarget(oldTarget);
		setTarget(oldTarget.getCategory(), targetAmt);	
		dataUpdated = true;
		markUpdate();
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

	/*
	 * @returns  Vector of Bar objects that are increasing order
	 */
	public Vector<Bar> getOrderedBar(){
		if(dataUpdated){
			genBars();
			dataUpdated = false;
		}
		return bars;
	}
	
	private void genBars(){
		bars.clear();
		for(Target target: mapTarget.values()){
			Bar bar = new Bar(target, data.getMonthlyExpense(target.getCategory()));
			bars.add(bar);
		}
		Collections.sort(bars);
	}

		
	private boolean isAnAlert(Bar bar){
		if(bar.getColour().equals("RED") || bar.getColour().equals("ORANGE")) 
			return true;
		else 
			return false;
	}
	
	public void markDataUpdated(){
		dataUpdated = true;
	}
		
}
	
	
	
	
