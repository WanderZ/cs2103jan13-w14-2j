package ezxpns.data.records;

import java.util.Date;

/**
 * A category-like class that stores payment method
 * @author yyjhao
 */
public class PaymentMethod implements Comparable<PaymentMethod> {
	/**
	 * The default category for records without a payment method
	 */
	public static final PaymentMethod undefined = new PaymentMethod(0, "undefined");
	
	protected long id;
	protected String name;
	
	/**
	 * @param id a unique long
	 * @param name name of the category
	 */
	public PaymentMethod(long id, String name){
		this.id = id;
		this.name = name;
	}
	
	/**
	 * A convenient constructor that automatically creates an ID
	 * that is likely unique
	 * @param name 
	 */
	public PaymentMethod(String name){
		this.id = (new Date()).getTime();
		this.name = name;
	}
	
	/**
	 * @param name new name
	 */
	protected void setName(String name){
		this.name = name;
	}
	
	/**
	 * @return name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return id
	 */
	public long getID(){
		return id;
	}
	
	/**
	 * @return a copy of itself
	 */
	public PaymentMethod copy(){
		return new PaymentMethod(id, name);
	}

	@Override
	public int compareTo(PaymentMethod o) {
		if(o.id > this.id){
			return -1;
		}else if(o.id < this.id){
			return 1;
		}else{
			return 0;
		}
	}
	
	@Override
	public String toString(){
		return name;
	}
	
}
