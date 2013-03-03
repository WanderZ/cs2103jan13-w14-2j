/**
 * 
 */
package ezxpns.data.records;

import java.util.Date;
import java.util.List;


/**
 * @author yyjhao
 * A container for some basic record attributes
 */
public abstract class Record implements Comparable<Record>{
	protected double amount;
	protected String name;
	protected String remark;
	protected Date date;
	protected transient Category category = Category.undefined;
	protected long id;
	
	public static <T extends Record> double sumAmount(List<T> rs){
		double sum = 0;
		for(Record r : rs){
			sum += r.getAmount();
		}
		return sum;
	}
	
	public Record(double amount, String name, String remark, Date date, Category category){
		this.amount = amount;
		this.name = name;
		this.remark = remark;
		this.date = new Date(date.getTime());
		this.category = category;
		this.id = (new Date()).getTime();
	}
	
	public boolean equals(Record other){
		return other.id == this.id;
	}
	
	public int compareTo(Record other){
		if(date.equals(other.date)){
			return (int)(id - other.id);
		}
		return (int)(date.getTime() - other.date.getTime());
	}
	
	public abstract Record copy();
	
	/**
	 * Copy data from another object to itself.
	 * This is to retain the reference to the object
	 * in the data manager
	 * @param other the object to copy from
	 */
	public void updateInternal(Record other){
		amount = other.amount;
		name = other.name;
		remark = other.remark;
		date = new Date(other.date.getTime());
		category = other.category;
	}

	public double getAmount() {
		return amount;
	}

	protected void setAmount(double amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	protected void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDate() {
		return new Date(date.getTime());
	}

	protected void setDate(Date date) {
		this.date = new Date(date.getTime());
	}

	public Category getCategory() {
		return category;
	}

	protected void setCategory(Category category) {
		this.category = category;
	}

	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}
}
