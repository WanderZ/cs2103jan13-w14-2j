package ezxpns.data.records;

import java.util.Date;

public class PaymentMethod implements Comparable<PaymentMethod> {
	/**
	 * The default category for records without a category
	 */
	public static final PaymentMethod undefined = new PaymentMethod(0, "undefined");
	
	private long id;
	private String name;
	
	/**
	 * @param id an immutable attribute that should be unique
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
	
	protected void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public long getID(){
		return id;
	}
	
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
	
	public boolean equals(PaymentMethod oMethod) {
		return this.name.equalsIgnoreCase(oMethod.name);
	}
	
	public String toString() {
		return this.id + ":" + this.name;
	}
}
