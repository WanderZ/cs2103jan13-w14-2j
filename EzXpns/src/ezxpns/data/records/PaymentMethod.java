package ezxpns.data.records;

public class PaymentMethod {
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
}
