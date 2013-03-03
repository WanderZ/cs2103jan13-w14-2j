package ezxpns.data.records;

import java.util.Date;


public class IncomeRecord extends Record {

	public IncomeRecord(double amount, String name, String remark, Date date,
			Category category) {
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