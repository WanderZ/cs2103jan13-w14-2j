package ezxpns.data;

import java.util.Date;

import ezxpns.data.records.Category;

/**
 * This class holds information of a Target
 * @author Suzzie
 *
 */

public class Target{
	
	private Category cat;
	private double targetAmt;

/**
 * Constructs a Target
 * @param cat
 * @param targetAmt
 */
	public Target(Category cat, double targetAmt){
		this.cat = cat;
		this.targetAmt = targetAmt;		
	}
	
/**
 * This returns a copy of the Target
 * @return a copy of the Target
 */
	public Target copy(){
		return new Target(cat, targetAmt);
	} 
	
	/**
	 * This returns the category of the target
	 * @return Category
	 */
	public Category getCategory(){
		return cat;
	}
	
	/**
	 * This returns a the target amount that is set by the user
	 * @return a double 
	 */
	public double getTargetAmt(){
		return targetAmt;
	}
		
}
