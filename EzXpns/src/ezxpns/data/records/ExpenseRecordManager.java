package ezxpns.data.records;

import java.util.*;


/**
 * A special record manager for expense records, since we need to store
 * all payment methods for it <br />
 * To be refactor soon
 * @author yyjhao
 */
public class ExpenseRecordManager extends RecordManager<ExpenseRecord>
	implements PayMethodHandler{
	
	private TreeMap<Long, PaymentMethod> payms = new TreeMap<Long, PaymentMethod>();
	
	public ExpenseRecordManager(){
		super();
	}
	
	@Override
	public ExpenseRecord addNewRecord(ExpenseRecord nr){
		return super.addNewRecord(nr);
	}

	@Override
	public void afterDeserialize(){
		super.afterDeserialize();
		for(ExpenseRecord r : recordsById.values()){
			r.paymentMethod = payms.get(r.paymentMethod.id);
		}
		payms.remove(PaymentMethod.undefined.id);
		payms.put(PaymentMethod.undefined.id, PaymentMethod.undefined);
	}
	
	@Override
	public Vector<PaymentMethod> getAllPaymentMethod() {
		Vector<PaymentMethod> pms = new Vector<PaymentMethod>();
		for(PaymentMethod p : payms.values()){
			if(p != PaymentMethod.undefined){
				pms.add(p);
			}
		}
		return pms;
	}

	@Override
	public boolean addNewPaymentMethod(PaymentMethod paymentRef) {
		while(payms.containsKey(paymentRef.id)){
			paymentRef.id = (new Date()).getTime() + ran.nextInt();
		}
		payms.put(paymentRef.id, paymentRef);
		return true;
	}

	@Override
	public boolean removePaymentMethod(PaymentMethod paymentRef) {
		if(!payms.containsKey(paymentRef.id)){
			return false;
		}else{
			payms.remove(paymentRef.id);
			for(ExpenseRecord r : recordsById.values()){
				if(r.paymentMethod == paymentRef){
					r.paymentMethod = PaymentMethod.undefined;
				}
			}
			return true;
		}
	}

	@Override
	public boolean updatePaymentMethod(PaymentMethod paymentRef) {
		if(!payms.containsKey(paymentRef.id)){
			return false;
		}else{
			payms.remove(paymentRef.id);
			payms.put(paymentRef.id, paymentRef);
			return true;
		}
	}
	
}
