package ezxpns.data;

/**
 * A data structure representing rows for the Expense table in the report
 * 
 * @author yan
 */

public class ReportCategory implements Comparable<ReportCategory> {

	private String category;
	private int frequency = 0;
	private double amount = 0;
	private double percentage = 0;
	private double amtPerFreq = 0; // Amount/Frequency

	/**
	 * Constructor, initialise category name
	 * 
	 * @param name
	 */
	public ReportCategory(String name) {
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
	 * 
	 * @param amount
	 */
	public void incrementAmount(double amount) {
		this.amount += amount;
	}

	/**
	 * Set percentage for expense of this category
	 * 
	 * @param percentage
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	/**
	 * Calculate the amount per frequency ratio
	 */
	public void calAmtPerFreq() {
		amtPerFreq = amount / frequency;
	}

	/**
	 * Get the name of this category
	 * @return
	 */
	public String getCategory() {
		return category;
	}

	/** 
	 * Get the requency of this category
	 * @return
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * Get the total expense for this category
	 * @return
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Get the percentage of expense of this category
	 * @return
	 */
	public double getPercentage() {
		return percentage;
	}

	/**
	 * Get the amount per frequency ratio for this category
	 * @return
	 */
	public double getAmtPerFreq() {
		return amtPerFreq;
	}

	/**
	 * Comparable, for sorting Vector in descending order according to percentage
	 */
	@Override
	public int compareTo(ReportCategory anotherCategory) {
		if (this.percentage < anotherCategory.getPercentage())
			return 1;
		else if (this.percentage > anotherCategory.getPercentage())
			return -1;
		else
			return 0;
	}
}
