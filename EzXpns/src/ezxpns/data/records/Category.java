package ezxpns.data.records;

import java.util.Date;


/**
 * @author yyjhao
 *
 * An immutable class to represent the current category <br />
 * meant to be used in Record as a reference <br />
 */
public class Category{
	/**
	 * The default category for records without a category
	 */
	public static final Category undefined = new Category(0, "undefined");
	
	protected long id;
	protected String name;
	
	/**
	 * @param id a unique long
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
	
	/**
	 * @param name new name
	 */
	protected void setName(String name){this.name = name;}
	/**
	 * @return the name
	 */
	public String getName(){return name;}
	
	/**
	 * @return the id
	 */
	public long getID(){return id;}
	
	/**
	 * @return a copy of itself
	 */
	public Category copy(){
		return new Category(id, name);
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public boolean equals(Category oCat) {
		return this.name.equalsIgnoreCase(oCat.name);
	}
}
