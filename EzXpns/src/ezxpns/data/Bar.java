package ezxpns.data;

public class Bar implements Comparable<Bar> {
	private Target target;
	private double currentAmt;
	private BarColour colour;
	private double ratio;
	
	public Bar(Target target, double currentAmt){
		this.target = target; 
		this.currentAmt = currentAmt;
		ratio = currentAmt/target.getTargetAmt();		
		
		if(ratio>=0.8){
			colour = BarColour.HIGH;
		}
		
		if(ratio>=0.65 && ratio<0.8){
			colour = BarColour.MEDIUM;
		}
		
		if(ratio>=0.35 && ratio<0.65){
			colour = BarColour.LOW;
		}
		
		if(ratio<0.35){
			colour = BarColour.SAFE;
		}	
	}
	
	public double getRatio(){
		return ratio;
	}
	
	public BarColour getColour(){
		return colour;
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
