package ezxpns.data;

/**
 * A class to hold summary information
 * 
 * @author tingzhe
 * 
 */

public class Summary {

	private SummaryDetails allTime;
	private SummaryDetails yearly;
	private SummaryDetails monthly;
	private SummaryDetails today;

	public Summary(SummaryDetails allTime, SummaryDetails yearly,
			SummaryDetails montly, SummaryDetails today) {
		this.allTime = allTime;
		this.yearly = yearly;
		this.monthly = montly;
		this.today = today;
	}
	
	// Getter
	public SummaryDetails getAllTimeSummaryDetails(){
		return allTime;
	}
	
	public SummaryDetails getYearlySummaryDetails(){
		return yearly;
	}
	
	public SummaryDetails getMontlySummaryDetails(){
		return monthly;
	}
	
	public SummaryDetails getTodaySummaryDetails(){
		return today;
	}
	

}
