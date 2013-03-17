package ezxpns.data.records;

import java.util.Date;
import java.util.List;

/**
 * @author yyjhao
 * A container for some basic record attributes
 */
/**
 * @author yyjhao
 *
 */
/**
 * @author yyjhao
 *
 */
public abstract class Record implements Comparable<Record>{
	protected double amount;
	protected String name;
	protected String remark;
	protected Date date;
	protected transient Category category = Category.undefined;
	protected long id;
	
	/**
	 * A helper function to calculate sum of all records
	 * @param rs list of records
	 * @return sum of all amounts of the records
	 */
	public static <T extends Record> double sumAmount(List<T> rs){
		double sum = 0;
		for(Record r : rs){
			sum += r.getAmount();
		}
		return sum;
	}

	
	/**
	 * @param amount
	 * @param name
	 * @param remark
	 * @param date
	 * @param category
	 */
	public Record(double amount, String name, String remark, Date date, Category category){
		this.amount = amount;
		this.name = name;
		this.remark = remark;
		this.date = new Date(date.getTime());
		this.category = category;
		this.id = (new Date()).getTime();
	}
	
	/**
	 * A method to check if the other Record supplied is the same as this Record object
	 * @param other A record object to check if they are the same object
	 */
	public boolean equals(Record other){
		return other.id == this.id;
	}
	
	@Override
	public int compareTo(Record other){
		if(date.equals(other.date)){
			if(this.id > other.id){
				return 1;
			}else if(this.id < other.id){
				return -1;
			}else{
				return 0;
			}
		}else{
			long a = date.getTime(),
				 b = other.date.getTime();
			if(a > b){
				return -1;
			}else if(a < b){
				return 1;
			}else{
				return 0;
			}
		}
	}
	
	
	/**
	 * @return A copy of itself
	 */
	public abstract Record copy();
	
	/**
	 * @return amount
	 */
	public double getAmount() {return amount;}
	/**
	 * @param amount new amount
	 */
	protected void setAmount(double amount) {this.amount = amount;}

	/**
	 * @return name
	 */
	public String getName() {return name;}
	/**
	 * @param name new name
	 */
	protected void setName(String name) {this.name = name;}

	/**
	 * @return remark
	 */
	public String getRemark() {return remark;}
	/**
	 * @param remark new remark
	 */
	protected void setRemark(String remark) {this.remark = remark;}

	/**
	 * @return date
	 */
	public Date getDate() {return new Date(date.getTime());}
	/**
	 * @param date new date
	 */
	protected void setDate(Date date) {this.date = new Date(date.getTime());}

	/**
	 * @return category
	 */
	public Category getCategory() {return category;}
	/**
	 * @param category new category
	 */
	protected void setCategory(Category category) {this.category = category;}

	/**
	 * @return id
	 */
	public long getId() {return id;}
	/**
	 * @param id new id
	 */
	protected void setId(long id) {this.id = id;}
}
