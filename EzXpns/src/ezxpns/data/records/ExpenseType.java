package ezxpns.data.records;

public enum ExpenseType {
	NEED("Need"), 
	WANT("Want");
	
	public final String name;
	
	ExpenseType(String name) {
		this.name = name;
	}
}
