package ezxpns.data;

/**
 * A class to hold summary information
 * 
 * @author tingzhe
 * 
 */

public class Summary {

	SummaryDetails allTime;
	SummaryDetails yearly;
	SummaryDetails monthly;
	SummaryDetails today;

	public Summary(SummaryDetails allTime, SummaryDetails yearly,
			SummaryDetails montly, SummaryDetails today) {
		this.allTime = allTime;
		this.yearly = yearly;
		this.monthly = montly;
		this.today = today;
	}

}
