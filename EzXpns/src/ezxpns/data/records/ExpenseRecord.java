package ezxpns.data.records;

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
	
	protected void setExpenseType(ExpenseType t){
		this.expenseType = t;
	}
	
	public ExpenseType getExpenseType(){
		return expenseType;
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
		ExpenseRecord r = new ExpenseRecord(this.getAmount(), this.getName(), this.getRemark(), this.getDate(),
				this.getCategory(), this.getExpenseType());
		r.setId(this.getId());
		return (Record)r;
	}
}