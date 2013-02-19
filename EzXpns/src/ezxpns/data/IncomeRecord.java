package ezxpns.data;

import java.util.Date;

public class IncomeRecord extends Record {

	public IncomeRecord(double amount, String name, String remark, Date date,
			Category category) {
		super(amount, name, remark, date, category);
		// nothing new here
	}

	@Override
	public Record copy() {
		IncomeRecord r = new IncomeRecord(amount, name, remark, date,
				category);
		r.id = this.id;
		return (Record)r;
	}

}
