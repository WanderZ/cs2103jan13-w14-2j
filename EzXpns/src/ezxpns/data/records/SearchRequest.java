package ezxpns.data.records; // This is a temporal storing location for compilation and testing. TO BE MOVED

import java.util.Calendar;
import java.util.Date;
import ezxpns.util.*;
import ezxpns.data.records.*;

/**
 * Wrapper class to contains search query parameters <br />
 * May support multiple queries (future iterations) <br />
 * but not handled in the master class for now. <br />
 * To create a new request, just construct it with the required parameter, such
 * as new SearchRequest(nameToSearch)
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
	private SearchRequest() {
	}

	public SearchRequest(String name) {
		this.name = name;
	}

	public SearchRequest(Pair<Date, Date> dateRange) {
		this.dateRange = normalizeDateRange(dateRange);
	}

	public SearchRequest(Category category) {
		this.category = category;
	}

	public boolean match(Record r) {
		return (name == null || r.name.equals(name))
				&& (category == null || r.category.equals(category))
				&& (dateRange == null || (!r.date.after(dateRange.getRight()) && !r.date
						.before(dateRange.getLeft())));
	}

	public void setName(String name) {
		if (this.name == null) {
			this.multiple = true;
		}
		this.name = name;
	}

	public void setCategory(Category category) {
		if (this.category == null) {
			this.multiple = true;
		}
		this.category = category;
	}

	public void setDateRange(Pair<Date, Date> dateRange) {
		if (this.dateRange == null) {
			this.multiple = true;
		}
		this.dateRange = normalizeDateRange(dateRange);
	}

	public RecordType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public Pair<Date, Date> getDateRange() {
		return dateRange;
	}

	public void setType(RecordType type) {
		this.type = type;
	}

	public Category getCategory() {
		return category;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public String toString() {
		return this.name;
	}
}
