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
	
	@Override
	public void updateInternal(Record other){
		if(other instanceof ExpenseRecord){
			super.updateInternal(other);
			expenseType = ((ExpenseRecord)other).expenseType;
		}
	}

	@Override
	public Record copy() {
		ExpenseRecord r = new ExpenseRecord(this.amount, this.name, this.remark, this.date,
				this.category, this.expenseType);
		r.id = this.id;
		return (Record)r;
	}
}
