package ezxpns.data.records; // This is a temporal storing location for compilation and testing. TO BE MOVED

import java.util.Date;
import ezxpns.util.*;
import ezxpns.data.records.*;

/**
 * Wrapper class to contains search query parameters
 * <br />May support multiple queries (future iterations)
 * <br />but not handled in the master class for now.
 * <br />To create a new request, just construct it with the required parameter,
 * such as new SearchRequest(nameToSearch)
 */
public class SearchRequest {

	public enum RecordType {EXPENSE, INCOME, BOTH;}
	
	private RecordType type = RecordType.BOTH;
	private String name;
	private Category category;
	private Pair<Date, Date> dateRange;
	private boolean multiple = false;
	
	// no empty request allowed!
	private SearchRequest(){}
	
	public SearchRequest(String name){
		this.name = name;
	}
	
	public SearchRequest(Pair<Date, Date> dateRange){
		this.dateRange = dateRange;
	}
	
	public SearchRequest(Category category){
		this.category = category;
	}
	
	public boolean match(Record r){
		return (name == null || r.name.equals(name)) &&
				(category == null || r.category.equals(category)) &&
				(dateRange == null || 
					((r.date.before(dateRange.getRight()) || r.date.equals(dateRange.getRight())) &&
							(r.date.after(dateRange.getLeft()) || r.date.equals(dateRange.getLeft()))));
	}
	
	public void setName(String name){
		if(this.name == null){
			this.multiple = true;
		}
		this.name = name;
	}
	
	public void setCategory(Category category){
		if(this.category == null){
			this.multiple = true;
		}
		this.category = category;
	}
	
	public void setDateRange(Pair<Date, Date> dateRange){
		if(this.dateRange == null){
			this.multiple = true;
		}
		this.dateRange = dateRange;
	}
	
	public RecordType getType() {return type;}
	public String getName() {return name;}
	public Pair<Date, Date> getDateRange() {return dateRange;}
	
	public void setType(RecordType type) {this.type = type;}
	public Category getCategory() {return category;}

	public boolean isMultiple() {return multiple;}
	
	public String toString() {
		return this.name;
	}
}
