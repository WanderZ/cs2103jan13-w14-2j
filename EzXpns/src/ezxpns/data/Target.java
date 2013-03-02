package ezxpns.data;

import java.util.Date;

/*
 * @author shuzhen
 * This class holds information of a Target
 */

public class Target{
	
	private Date start;
	private Date end;
	private Category cat;
	private double targetAmt;


	public Target(Date start, Date end, Category cat, double targetAmt){
		this.start = start;
		this.end = end;
		this.cat = cat;
		this.targetAmt = targetAmt;
	}
	
	
	public Target copy(){
		return new Target(start, end, cat, targetAmt);
	} 
	
	public Category getCategory(){
		return cat;
	}
	
	public double getTargetAmt(){
		return targetAmt;
	}
	
	public Date getStart(){
		return start;
	}
	
	public Date getEnd(){
		return end;
	}
	
}
