package ezxpns.data;

import ExpenseRecord;
import IncomeRecord;
import java.util.Calendar;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Vector;

import ezxpns.util.*;
import ezxpns.data.*;

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
	public Vector<Bar> alerts;
	private Hashtable mapTarget = new Hashtable();
	
	
	
	
	
	
	public void setDataProvider(DataProvider dataProvider){
		data = dataProvider;
	}
	
	/**
	 * @return if the internal data store is updated (and therefore needs to be stored)
	 */
	public boolean isUpdated(){

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
	}
	
	
	
	public void getCurrent(Target target){
		Date now = new Date();
		//calculate current amount up to now
		if( now.before(target.getEnd())){
			expenseRecord = data.getDataInDateRange(target.getStart(), now).getLeft();
		}
		
		/*calculate current amount up to the end of target month*/
		else{
			expenseRecord = data.getDataInDateRange(target.getStart(), target.getEnd()).getLeft();			
		}
		
		return sumAmount(expenseRecord);
	}
	
	public PriorityQueue<Bar> getOrder(){
		PriorityQueue<Bar> pq = new PriorityQueue<Bar>();
		for(int i=0; i<targets.size(); i++){
			pq.add(new Bar(targets.get(i), getCurrent(targets.get(i)))); 
		}
	
		return pq;
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
			currentAmt+=expenseRecord.get(i).amount;
		}
		

	/**
	 * Creates a Target and adds it into the Vector targets and priority queue
	 */
	

	public Target setTarget(Date start, Category cat, double targetAmt){
		
		// set last date of month
		int lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar myCal  = new GregorianCalendar();
		myCal.setTime(start);
		Calendar calEnd = new GregorianCalendar();
		calEnd.set(myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH), lastDay);
		Date end = calEnd.getTime(); // convert Calendar object to Date object
		
		Target target = new Target(start, end, cat, targetAmt);
		
		addTarget(target);
		mapTarget.put(target.getCategory(), target);
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
		alerts.removeAllElements();
		PriorityQueue<Bar> pq = getOrder();
		if(pq.peek()!=null){
		for(int i=0; i<pq.size(); i++){
			if(pq.peek().getColour().equals("RED") || pq.peek().getColour().equals("ORANGE")){
				alerts.add(pq.poll());				
			}
			
			else 
				break;
		}
		}
	}
	
	public void updateTargets(){
		
	}
	
	public void makeChanges(ExpenseRecord originalRecord, ExpenseRecord newRecord, String change){
		if(change.equals("modify")){
			Target target = (Target) mapTarget.get(originalRecord.category);
			removeTarget(target);
		}
		
		else if (change.equals("remove")){
		}
		
		else if (change.equals("add")){
			
		}
		
		
	}
	
	public boolean isAlertUpdated(){
		return alertUpdated;
	}

	public void saved() {
		updated = false;
	}
	
}
