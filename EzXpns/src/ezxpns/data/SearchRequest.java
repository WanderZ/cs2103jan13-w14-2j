package ezxpns.data; // This is a temporal storing location for compilation and testing. TO BE MOVED

import java.util.Calendar;
import java.util.Date;
import ezxpns.util.*;
import ezxpns.data.records.*;

/**
 * Wrapper class to contains search query parameters <br />
 * To create a new request, just construct it with the required parameter, such
 * as new SearchRequest(nameToSearch)<br />
 * To add more parameters, use setters
 */
public class SearchRequest {

	public enum RecordType {
		EXPENSE, INCOME, BOTH;
	}

	private RecordType type = RecordType.BOTH;
	private String name;
	private Category category;
	private Pair<Date, Date> dateRange;
	private boolean multiple = false;

	/**
	 * Normalizes the date range to remove time information and keeping only dates
	 * @param dateRange
	 * @return a normalized date range
	 */
	public static Pair<Date, Date> normalizeDateRange(Pair<Date, Date> dateRange) {
		Date start = dateRange.getLeft();
		Date end = dateRange.getRight();

		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		start = cal.getTime();

		cal.setTime(end);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		end = cal.getTime();

		return new Pair<Date, Date>(start, end);
	}

	// no empty request allowed!
	@SuppressWarnings("unused")
	private SearchRequest() {}

	/**
	 * Creates a search request that asks for records matching the whole name
	 * @param name the name that records should contain
	 */
	public SearchRequest(String name) {
		this.name = name;
	}

	/**
	 * Constructs a Search Request for a specific date range, inclusive of start and end
	 * @param dateRange a Pair Object containing the start and end date
	 */
	public SearchRequest(Pair<Date, Date> dateRange) {
		this.dateRange = normalizeDateRange(dateRange);
	}

	/**
	 * Constructs a Search Request for a specific category
	 * @param category Category object specified. The category must be from the database
	 */
	public SearchRequest(Category category) {
		this.category = category;
	}

	/**
	 * Test if a record matches this request
	 * @param r record to be tested
	 * @return whether the record matches
	 */
	public boolean match(Record r) {
		return (name == null || r.getName().equals(name))
				&& (category == null || r.getCategory().equals(category))
				&& (dateRange == null || (!r.getDate().after(dateRange.getRight()) &&
						!r.getDate().before(dateRange.getLeft())));
	}

	/**
	 * Sets(adds) the name field of this SearchRequest
	 * @param name the string object String defining name field
	 */
	public void setName(String name) {
		if (this.name == null) {
			this.multiple = true;
		}
		this.name = name;
	}

	/**
	 * Sets(adds) the category of this SearchRequest
	 * @param category a Category Object reference to be set as the Category field
	 */
	public void setCategory(Category category) {
		if (this.category == null) {
			this.multiple = true;
		}
		this.category = category;
	}

	/**
	 * Sets(adds) the date range for this SearchRequest
	 * @param dateRange a Pair Object containing the start and end date to be set as the Date Range field.
	 */
	public void setDateRange(Pair<Date, Date> dateRange) {
		if (this.dateRange == null) {
			this.multiple = true;
		}
		this.dateRange = normalizeDateRange(dateRange);
	}

	/**
	 * Gets the RecordType of this SearchRequest
	 * @return RecordType object reference containing the RecordType, if any, otherwise returns null
	 */
	public RecordType getType() {
		return type;
	}

	/**
	 * Gets the Name field stored in this SearchRequest
	 * @return a String containing the stored name, if any, otherwise returns null
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the Date Range field of this SearchRequest
	 * @return a Pair Object containing the starting and the ending date
	 */
	public Pair<Date, Date> getDateRange() {
		return dateRange;
	}

	/**
	 * Set the type of this SearchRequest
	 * @param type a RecordType reference to the type of records to search for 
	 */
	public void setType(RecordType type) {
		this.type = type;
	}

	/**
	 * Gets the Category of stored in this SearchRequest
	 * @return the Category Object reference stored
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Check if this SearchRequest is asking for multiple fields
	 * @return true is this supports, otherwise false
	 */
	public boolean isMultiple() {
		return multiple;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
