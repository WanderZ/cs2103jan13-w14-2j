package ezxpns.data.records;

import java.util.Date;

/**
 * A Record object that specifically stores income details
 *
 */
public class IncomeRecord extends Record {

	/**
	 * Normal constructor for an Income Record
	 * @param amount the specific amount in which this income record refers to
	 * @param name the name of this income record
	 * @param remark (s) for this income record, if any
	 * @param date the Date object in which this income record is created (not in this prog, but in user's context)
	 * @param category Category object in which this income is categorised to
	 */
	public IncomeRecord(
			double amount, 
			String name, 
			String remark, 
			Date date,
			Category category
			) {
		super(amount, name, remark, date, category);
		// nothing new here
	}

	@Override
	public Record copy() {
		IncomeRecord r = new IncomeRecord(this.getAmount(), this.getName(), this.getRemark(), this.getDate(),
				this.getCategory());
		r.setId(this.getId());
		return (Record)r;
	}

}
