package ezxpns.data.records;

import java.util.*;

import ezxpns.GUI.PaymentMethodHandlerInterface;

public class ExpenseRecordManager extends RecordManager<ExpenseRecord>
	implements PaymentMethodHandlerInterface{
	
	private TreeSet<PaymentMethod> payms = new TreeSet<PaymentMethod>();

	@Override
	public Vector<PaymentMethod> getAllPaymentMethod() {
		Vector<PaymentMethod> pms = new Vector<PaymentMethod>();
		for(PaymentMethod p : payms){
			pms.add(p.copy());
		}
		return pms;
	}

	@Override
	public boolean addNewPaymentMethod(PaymentMethod paymentRef) {
		if(payms.contains(paymentRef)){
			return false;
		}else{
			payms.add(paymentRef);
			return true;
		}
	}

	@Override
	public boolean removePaymentMethod(PaymentMethod paymentRef) {
		if(!payms.contains(paymentRef)){
			return false;
		}else{
			payms.remove(paymentRef);
			return true;
		}
	}

	@Override
	public boolean updatePaymentMethod(PaymentMethod paymentRef) {
		if(!payms.contains(paymentRef)){
			return false;
		}else{
			payms.remove(paymentRef);
			payms.add(paymentRef);
			return true;
		}
	}
	
}
