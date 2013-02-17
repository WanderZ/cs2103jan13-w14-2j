package ezxpns.data;

/**
 * @author yyjhao
 * 
 * A class to represent the current category
 * meant to be used in Record as a reference
 */
public class Category implements Cloneable{
	/**
	 * The default category for records without a category
	 */
	public static final Category undefined = new Category(0, "undefined");
	
	private long id;
	private String name;
	
	/**
	 * @param id an immutable attribute that should be unique
	 * @param name name of the category
	 */
	public Category(long id, String name){
		this.id = id;
		this.name = name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public long getID(){
		return id;
	}
	
	public Category clone(){
		return new Category(id, name);
	}
}
