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
		double getTotalExpense(Category cat, Date start, Date end);
	}
	private Vector<Target> targets;
	private transient boolean	updated = false, 
								alertUpdated = false;
	private transient DataProvider data;
	private transient Vector<ExpenseRecord>  expenseRecord;
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
	

	/*Preconditions: cannot add more than one target for the same category.
	 * 				 can only set targets for the SAME month
	 */
		public Target setTarget(Date start, Category cat, double targetAmt){
			if(mapTarget.containsKey(cat)){
				System.out.println("You have already set a target for "+cat.getName()+".");
				return null;
			}
		Target target = new Target(start, getLastDayOfMonth(start), cat, targetAmt);
		addTarget(target);
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
			targets.add(target);
			mapTarget.put(target.getCategory(),target);
			updated = true;
		}
		
		public void removeTarget(Target target){
			targets.remove(target);
			mapTarget.remove(target.getCategory());
			updated = true;
		}
		
		public void editTarget(Target oldTarget, 
				Date start, Category cat, double targetAmt){
			removeTarget(oldTarget);
			setTarget(start, cat, targetAmt);		
			updated = true;
		}
		
		/* EDITED categories
		 * if user renamed the category and decided to keep 
		 * all the old entries under the new name
		 * invoke getOrdered(); and getAlerts();
		 */

		/* REMOVED categories
		 * we will remove the target for this category;
		 */
		
		public void removeCategory(Category cat){
			if(mapTarget.containsKey(cat)){
				targets.remove(mapTarget.get(cat));
				mapTarget.remove(cat);
			}
			updated=true;
		}
		
	
		 
		/*
		 * @return a copy of the internal targets, alerts, or ordered targets
		 */
		public Vector<Target> getTargets(){
			Vector<Target> copy = new Vector<Target>();
			copy = targets;
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
			Vector<Bar> ordered = new Vector<Bar>();
			for(Target target: targets){
			Bar bar = new Bar(target, data.getTotalExpense(target.getCategory(),
					target.getStart(), new Date()));
			ordered.add(bar);
			}
			Collections.sort(ordered);
			return ordered;
		}

		
		//helper methods
		private Date getLastDayOfMonth(Date start){
			// set last date of month
			int lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH); //last day of current month
			Calendar myCal  = new GregorianCalendar();
			myCal.setTime(start);
			Calendar calEnd = new GregorianCalendar();
			calEnd.set(myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH), lastDay);
			Date end = calEnd.getTime(); // convert Calendar object to Date object
			return end;
		}
		
		private boolean isAnAlert(Bar bar){
			if(bar.getColour().equals("RED") || bar.getColour().equals("ORANGE")) 
				return true;
			else 
				return false;
		}
		
	}
	
	
	
	
