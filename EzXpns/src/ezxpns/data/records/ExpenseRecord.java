package ezxpns.data.records;

import java.util.Date;


/**
 * @author yyjhao
 * Record with two additional attribute: expenseType{NEED, WANT} and PaymentMethod
 */
public class ExpenseRecord extends Record{
	protected ExpenseType expenseType;
	protected PaymentMethod paymentMethod;
	/**
	 * @param amount
	 * @param name
	 * @param remark
	 * @param date
	 * @param category
	 * @param expenseType
	 * @param paymentMethod
	 */
	public ExpenseRecord(
			double amount, 
			String name, 
			String remark, 
			Date date,
			Category category, 
			ExpenseType expenseType, 
			PaymentMethod paymentMethod) {
		super(amount, name, remark, date, category);
		this.expenseType = expenseType;
		this.paymentMethod = paymentMethod;
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
	
	/**
	 * @return payment method
	 */
	public PaymentMethod getPaymentMethod(){
		return paymentMethod;
	}
	
	@Override
	public Record copy() {
		ExpenseRecord r = new ExpenseRecord(this.getAmount(), this.getName(), this.getRemark(), this.getDate(),
				this.getCategory(), this.getExpenseType(), this.getPaymentMethod());
		r.setId(this.getId());
		return (Record)r;
	}
}
