/**
 * 
 */
package ezxpns.data;

import java.util.Date;

/**
 * @author yyjhao
 * A container for some basic record attributes
 * No getter/setter since it's purely data representation without any internal logic
 */
public abstract class Record implements Comparable<Record>{
	public double amount;
	public String name;
	public String remark;
	public Date date;
	public transient Category category = Category.undefined;
	public long id;
	
	public Record(double amount, String name, String remark, Date date, Category category){
		this.amount = amount;
		this.name = name;
		this.remark = remark;
		this.date = date;
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
		date = other.date;
		category = other.category;
	}
}
