package ezxpns.data;

public class Bar implements Comparable<Bar> {
	private Target target;
	private double currentAmt;
	private double targetAmt;
	private BarColor color;
	private double ratio;
	
	/**
	 * Constructs a Bar
	 * @param target
	 * @param currentAmt
	 */
	public Bar(Target target, double currentAmt){
		this.target = target; 
		this.currentAmt = currentAmt;
		targetAmt = target.getTargetAmt();
		ratio = currentAmt/targetAmt;		
		
		if(ratio>=0.8){
			color = BarColor.HIGH;
		}
		
		if(ratio>=0.65 && ratio<0.8){
			color = BarColor.MEDIUM;
		}
		
		if(ratio>=0.35 && ratio<0.65){
			color = BarColor.LOW;
		}
		
		if(ratio<0.35) {
			color = BarColor.SAFE;
		}	
	}
	
	/**
	 * This returns the ratio of currentAmt/targetAmt
	 * @return double
	 */
	public double getRatio(){
		return ratio;
	}
	
	/**
	 * This returns the enum of the BarColor
	 * @return the BarColor of this bar
	 */
	public BarColor getBarColor(){
		return color;
	}
	
	/**
	 * This returns the Target of this bar
	 * @return the Target of this bar
	 */
	public Target getTarget(){
		return target;
	}
	
	/**
	 * This returns the current amount of monthly expenses for the target of this bar
	 * @return currentAmt
	 */
	public double getCurrentAmt(){
		return currentAmt;
	}
	
	/**
	 * This returns the target amount that the user has set for the target of this bar
	 * @return targetAmt
	 */
	public double getTargetAmt(){
		return targetAmt;
	}

	
	
	
	/**
	 * compareTo() will return an integer value that represents the comparison between two Bar objects
	 * -1 will represent that this object is lesser compared to the other object
	 * 0 will represent that both objects have equal values 
	 * 1 will represent that this object is larger compared to the other object
	 */

    public int compareTo(Bar other){
		if(this.getRatio()>other.getRatio()){
			return 1;
		}
		else if(this.getRatio()<other.getRatio()){
			return -1;
		}
		else{
			return 0;
		}
	}

}
