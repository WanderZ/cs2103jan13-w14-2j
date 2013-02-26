package ezxpns.data;

import ezxpns.data.records.*;
import ezxpns.util.*;

import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Vector;

import ezxpns.util.*;
import ezxpns.data.*;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;

/**
 * @author shuzhen
 * 
 * A generator that takes in targets and data and produce alert info
 * 
 */
public class TargetManager implements Storable {
	public static interface DataProvider{
		Pair<Vector<ExpenseRecord>, Vector<IncomeRecord> > getDataInDateRange(Date start, Date end);
	}
	private Vector<Target> targets;
	private transient boolean	updated = false, 
								alertUpdated = false;
	private transient DataProvider data;
	
	
	private Vector<ExpenseRecord>  expenseRecord;
	public PriorityQueue pq; //for displaying of targets on home screen
	public Vector<Target> alerts;
	
	
	
	
	
	
	public void setDataProvider(DataProvider dataProvider){
		data = dataProvider;
	}
	
	/**
	 * @return if the internal data store is updated (and therefore needs to be stored)
	 */
	public boolean isUpdated(){
		if(updated==false){
			updateTargets();
			updateAlerts();
		}
		
		updated = true;
		
		return updated;
	}
	
	public void addTarget(Target target){
		updated = true;
		targets.add(target);
		pq.add(target);
		
	}
	
	public void removeTarget(Target target){
		targets.remove(target);
		pq.remove(target);
	}
	
	public void updateTargets(){
		
		
	}
	
	public void updateAlert(){
		alerts.clear();
		for(int i=0; i<targets.size(); i++){
			if(targets.get(i).getColour().equals("RED") || targets.get(i).getColour().equals("ORANGE")){
				alerts.add(targets.get(i));				
			}
		}
	}
	
	/**
	 * Creates a Target and adds it into the Vector targets and priority queue
	 */
	
	@SuppressWarnings("deprecation")
	public Target setTarget(Date start, Category cat, double targetAmt){
		expenseRecord = data.getDataInDateRange(start, start).getLeft();
		double currentAmt=0;
		for(int i=0; i<expenseRecord.size(); i++){
			currentAmt+=expenseRecord.get(i).getAmount();
		}
		
		int lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
		Date end = new Date(start.getYear(), start.getMonth(), lastDay); //last day of the month
		
		Target target = new Target(start, end, cat, targetAmt, currentAmt);
		
		addTarget(target);
		
		return target;
	}
	
	
	
	/**
	 * @return a copy of the internal targets
	 */
	public Vector<Target> getTargets(){
		
		return targets;
	}
	
	public Vector<AlertInfo> getAlerts(){
		return null;
	}
	
	public void updateAlerts(){
		
	}
	
	public boolean isAlertUpdated(){
		return alertUpdated;
	}

	public void saved() {
		updated = false;
	}
	
}
