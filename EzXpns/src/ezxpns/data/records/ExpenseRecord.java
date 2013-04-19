package ezxpns.data.records;

import java.util.Date;


/**
 * Record with two additional attribute: expenseType{NEED, WANT} and PaymentMethod
 * @author A0099621X
 */
public class ExpenseRecord extends Record{
	protected ExpenseType expenseType;
	/**
	 * @param amount
	 * @param name
	 * @param remark
	 * @param date
	 * @param category
	 * @param expenseType
	 */
	public ExpenseRecord(
			double amount, 
			String name, 
			String remark, 
			Date date,
			Category category, 
			ExpenseType expenseType) {
		super(amount, name, remark, date, category);
		this.expenseType = expenseType;
	}
	
	/**
	 * @param t new type
	 */
	protected void setExpenseType(ExpenseType t){
		this.expenseType = t;
	}
	
	/**
	 * @return expense type
	 */
	public ExpenseType getExpenseType(){
		return expenseType;
	}
	
	@Override
	public Record copy() {
		ExpenseRecord r = new ExpenseRecord(this.getAmount(), this.getName(), this.getRemark(), this.getDate(),
				this.getCategory(), this.getExpenseType());
		r.setId(this.getId());
		return (Record)r;
	}
}
