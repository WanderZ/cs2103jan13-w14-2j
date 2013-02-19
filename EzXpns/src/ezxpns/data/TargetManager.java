package ezxpns.data;

import java.util.Date;
import java.util.Vector;

import ezxpns.util.*;
import ezxpns.data.*;

/**
 * @author yyjhao
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

	public void setDataProvider(DataProvider dataProvider){
		data = dataProvider;
	}
	
	/**
	 * @return if the internal data store is updated (and therefore needs to be stored)
	 */
	public boolean isUpdated(){
		return updated;
	}
	
	public void addTarget(Target target){
		updated = true;
	}
	
	public void removeTarget(Target target){
		
	}
	
	/**
	 * @return a copy of the internal targets
	 */
	public Vector<Target> getTargets(){
		return null;
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
