package ezxpns.GUI; // This is a temporal storing location for compilation and testing. TO BE MOVED

import java.util.Date;
import ezxpns.util.*;
import ezxpns.data.records.*;

/**
 * Contains search query parameters
 * May support multiple queries but not handled in the master class for now.
 */
public class SearchRequest {
	public enum RecordType {
		EXPENSE, INCOME, BOTH;
	}
	
	private RecordType type = RecordType.BOTH;
	
	private String name;
	
	public RecordType getType() {
		return type;
	}

	public void setType(RecordType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Category getCategory() {
		return category;
	}

	public boolean isMultiple() {
		return multiple;
	}

	private Category category;
	
	private Pair<Date, Date> dateRange;
	
	public Pair<Date, Date> getDateRange() {
		return dateRange;
	}

	private boolean multiple = false;
	
	// no empty request allowed!
	protected SearchRequest(){}
	
	public SearchRequest(String name){
		this.name = name;
	}
	
	public SearchRequest(Pair<Date, Date> dateRange){
		this.dateRange = dateRange;
	}
	
	public SearchRequest(Category category){
		this.category = category;
	}
	
}
