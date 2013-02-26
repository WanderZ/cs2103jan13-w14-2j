package ezxpns.data;

/*
 * This class holds information of a Target
 */

public class Target implements Comparable<Target>{
	
	private Date start;
	private Date end;
	private Category cat;
	private double targetAmt;
	private double currentAmt;

	public Target(Date start, Date end, Category cat, double targetAmt, double currentAmt){
		this.start = start;
		this.end = end;
		this.cat = cat;
		this.targetAmt = targetAmt;
		this.currentAmt = currentAmt;		
	}
	
	
	public Target copy(){
		
		return null;
	} 
	
	public Category getCategory(){
		return cat;
	}
	
	public double getTarget(){
		return targetAmt;
	}
	
	public double getCurrent(){
		return currentAmt;
	}
	
	public Date getStart(){
		return start;
	}
	
	public Date getEnd(){
		return end;
	}
	
	public double getRatio(){
		return currentAmt/targetAmt;
	}
	
	public String getColour(){
		double ratio = getRatio();
		if(ratio>=0.8){
			return "RED";
		}
		
		if(ratio>=0.65 && ratio<0.8){
			return "ORANGE";
		}
		
		if(ratio>=0.35 && ratio<0.65){
			return "YELLOW";
		}
		
		if(ratio<0.35){
			return "GREEN";
		}	
	}
	
	protected void updateCurrent(double currentAmt){
		this.currentAmt = currentAmt;
	}
	
	
	/*
	 * @returns the 1 if this.target is higher on the priority queue, -1 if lower.
	 */
	@Override
    public int compareTo(Target other){
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
