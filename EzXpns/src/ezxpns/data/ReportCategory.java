package ezxpns.data;

/**
 * A data structure representing rows for the Expense table
 * in the report
 * @author yan
 */

public class ReportCategory implements Comparable<ReportCategory>{
	
	private String category;
	private int frequency = 0;
	private double amount = 0;
	private double percentage = 0;
	private double amtPerFreq = 0; // Amount/Frequency
	
	/**
	 * Constructor, initialise category name
	 * @param name
	 */
	public ReportCategory(String name){
		category = name;
	}

	/**
	 * Increment frequency by 1
	 */
	public void incrementFreq() {
		frequency++;
	}

	/**
	 * Increment amount 
	 * @param amount
	 */
	public void incrementAmount(double amount) {
		this.amount += amount;
	}

	// Setter
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
	/**
	 * Calculate amtPerFreq
	 */
	public void calAmtPerFreq(){
		amtPerFreq = amount/frequency;
	}
	
	// Getters
	public String getCategory(){
		return category;
	}
	
	public int getFrequency(){
		return frequency;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public double getPercentage(){
		return percentage;
	}
	
	public double getAmtPerFreq(){
		return amtPerFreq;
	}

	/**
	 * Comparable, for sorting Vector in decending order
	 * according to percentage
	 */
	@Override
	public int compareTo(ReportCategory anotherCategory) {
		if (this.percentage > anotherCategory.getPercentage())
			return 1;
		else if (this.percentage < anotherCategory.getPercentage())
			return -1;
		else
			return 0;
	}
}
