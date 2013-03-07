package ezxpns.data;

import java.util.Calendar;

import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Vector;

import ezxpns.util.*;
import ezxpns.data.*;
import ezxpns.data.records.Category;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;

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
	
	
	private transient Vector<ExpenseRecord>  expenseRecord;
	public Vector <Bar> alerts = new Vector <Bar>(); //keeps a Vector of Bars that are requires attention 
	public Vector <Bar> ordered = new Vector <Bar>(); //keeps a ordered sequence of Bars
	private Hashtable<Category,Target> mapTarget = new Hashtable<Category, Target>();	// maps category to target // maybe not necessary if max number of targets is small
	
	
	/**
	 * @return if the internal data store is updated (and therefore needs to be stored)
	 */
	@Override
	public boolean isUpdated() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void saved() {
		// TODO Auto-generated method stub
		
	}
	
		/*
		 * creates a Target object
		 */
	
	public Target setTarget(Date start, Category cat, double targetAmt){
		Target target = new Target(start, getLastDayOfMonth(start), cat, targetAmt);
		return target;
		}
		
		
		/*
		 * assumes target is unique
		 * add to targets
		 */
		
		public void addTarget(Target target){
			
			targets.add(target);
			mapTarget.put(target.getCategory(),target);
			Bar bar = new Bar(target, getCurrent(target));
			ordered.add(bar);
			Collections.sort(ordered);
			addAlert(bar);
		}
		
		public int addAlert(Bar bar){
			
			if (isAnAlert(bar))
				alerts.add(bar);
			
			return alerts.size();
		}
		
		
		
		//getters
		 
		/*
		 * @return a copy of the internal targets, alerts, or ordered targets
		 */
		public Vector<Target> getTargets(){
			Vector<Target> copy = new Vector<Target>();
			copy = targets;
			return copy;
		}
		
		public Vector<Bar> getAlerts(){
			Vector<Bar> copy = new Vector<Bar>();
			copy = alerts;
			return copy;
		}
		
		public Vector<Bar> getOrderedTargets(){
			Vector<Bar> copy = new Vector<Bar>();
			copy = ordered;
			return copy;
		}
		
		
		
		//Modifiers
		
		/*
		 * this updates ordered and alerts when there is a change in ExpenseRecords:
		 * 1. add an expense 2.remove an expense 3. modify an expense(amount ONLY)
		 * This function works for all 3 types of change
		 */		
		public void addOrRemoveExpense(ExpenseRecord expense){

			//if the expense category has a target set on it
			if(mapTarget.containsKey(expense.getCategory())){
				Target target = mapTarget.get(expense.getCategory());
				Bar bar = new Bar(target, getCurrent(target));
				
				removeAlert(target);
				
				//modify from ordered
				for(int i=0; i<ordered.size(); i++){
					if(ordered.get(i).getTarget().equals(target))
						ordered.set(i, bar);
				}
				
				Collections.sort(ordered);
				 
				if(isAnAlert(bar))
					alerts.add(bar);		
				}
		}
		

		public void modifiedExpense(ExpenseRecord original, ExpenseRecord current){
			//if category is modified, we have to remove the original record, then add the current record
			if(!(original.getCategory().equals(current.getCategory()))){
				addOrRemoveExpense(original);
				addOrRemoveExpense(current);
			}
			
			//add current record
			else
				addOrRemoveExpense(current);			
			
		}
		

		
		//helper methods
		public double getCurrent(Target target){
			Date now = new Date();
			if( now.before(target.getEnd()))
				expenseRecord = data.getDataInDateRange(target.getStart(), now).getLeft();
			else
				expenseRecord = data.getDataInDateRange(target.getStart(), target.getEnd()).getLeft();			
						
			return Record.sumAmount(expenseRecord); //how to use sumAmount
		}
		
		public Date getLastDayOfMonth(Date start){
			// set last date of month
			int lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH); //last day of current month
			Calendar myCal  = new GregorianCalendar();
			myCal.setTime(start);
			Calendar calEnd = new GregorianCalendar();
			calEnd.set(myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH), lastDay);
			Date end = calEnd.getTime(); // convert Calendar object to Date object
			
			return end;
		}
		
		public boolean removeAlert(Target target){
			for(int i=0; i<alerts.size(); i++)
				if(alerts.get(i).getTarget().equals(target)){
					alerts.remove(i);
					return true;
				}
			
			return false;
		}
		
		public boolean isAnAlert(Bar bar){
			if(bar.getColour().equals("RED") || bar.getColour().equals("ORANGE")) 
				return true;
			
			else 
				return false;
		}
		
	}
	
	
	
	
