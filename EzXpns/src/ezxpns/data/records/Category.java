package ezxpns.data.records;

import java.util.Date;

/**
 * @author yyjhao
 * 
 * A class to represent the current category
 * meant to be used in Record as a reference
 */
public class Category{
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
	
	/**
	 * A convenient constructor that automatically creates an ID
	 * that is likely unique
	 * @param name 
	 */
	public Category(String name){
		this.id = (new Date()).getTime();
		this.name = name;
	}
	
	protected void setName(String name){this.name = name;}
	public String getName(){return name;}
	
	public long getID(){return id;}
	
	public Category copy(){
		return new Category(id, name);
	}
	
	public boolean equals(Category oCat) {
		return this.name.equalsIgnoreCase(oCat.name);
	}
	
	public String toString() {
		return this.id + ":" + this.name;
	}
}
