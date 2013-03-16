package ezxpns.data;

public class Bar implements Comparable<Bar> {
	private Target target;
	private double currentAmt;
	private BarColor color;
	private double ratio;
	
	public Bar(Target target, double currentAmt){
		this.target = target; 
		this.currentAmt = currentAmt;
		ratio = currentAmt/target.getTarget();		
		
		if(ratio>=0.8){
			color = BarColor.HIGH;
		}
		
		if(ratio>=0.65 && ratio<0.8){
			color = BarColor.MEDIUM;
		}
		
		if(ratio>=0.35 && ratio<0.65){
			color = BarColor.LOW;
		}
		
		if(ratio<0.35){
			color = BarColor.SAFE;
		}	
	}
	
	public double getRatio(){
		return ratio;
	}
	
	public BarColor getColor(){
		return color;
	}
	
	public Target getTarget(){
		return target;
	}
	
	public double getCurrentAmt(){
		return currentAmt;
	}
	

	
	
	
	/*
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
