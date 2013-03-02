package ezxpns.data;

public class Bar implements Comparable<Bar> {
	private Target target;
	private double currentAmt;
	private String colour;
	private double ratio;
	
	public Bar(Target target, double currentAmt){
		this.target = target; 
		this.currentAmt = currentAmt;
		ratio = currentAmt/target.getTargetAmt();		
		
		if(ratio>=0.8){
			colour = "RED";
		}
		
		if(ratio>=0.65 && ratio<0.8){
			colour = "ORANGE";
		}
		
		if(ratio>=0.35 && ratio<0.65){
			colour = "YELLOW";
		}
		
		if(ratio<0.35){
			colour = "GREEN";
		}	
	}
	
	public double getRatio(){
		return ratio;
	}
	
	public String getColour(){
		return colour;
	}
	
	public Target getTarget(){
		return target;
	}
	
	public double getCurrentAmt(){
		return currentAmt;
	}
	
	
	/*
	 * @returns the 1 if this.target is higher on the priority queue, -1 if lower.
	 * This is for the display of the Target Bar at Home Screen
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
