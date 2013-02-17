package ezxpns.data;

import java.util.Date;

/**
 * @author yyjhao
 * With one additional attribute: expenseType{NEED, WANT}
 */
public class ExpenseRecord extends Record{
	public ExpenseType expenseType;
	public ExpenseRecord(double amount, String name, String remark, Date date,
			Category category, ExpenseType expenseType) {
		super(amount, name, remark, date, category);
		this.expenseType = expenseType;
	}

	public void updateInternal(ExpenseRecord other){
		super.updateInternal(other);
		expenseType = other.expenseType;
	}
	
	public ExpenseRecord clone(){
		ExpenseRecord c = new ExpenseRecord(amount, name, remark, date, category, expenseType);
		c.id = id;
		return c;
	}
}
