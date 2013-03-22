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
	private transient TreeMap<Long, TreeSet<ExpenseRecord>> recordsByPaym;
	
	public ExpenseRecordManager(){
		super();
		recordsByPaym = new TreeMap<Long, TreeSet<ExpenseRecord>>();
	}

	@Override
	public void afterDeserialize(){
		super.afterDeserialize();
		recordsByPaym = new TreeMap<Long, TreeSet<ExpenseRecord>>();
		for(ExpenseRecord r : recordsById.values()){
			r.paymentMethod = payms.get(r.paymentMethod.id);
			TreeSet<ExpenseRecord> rs = recordsByPaym.get(r.paymentMethod.id);
			if(rs == null){
				rs = new TreeSet<ExpenseRecord>();
				recordsByPaym.put(r.paymentMethod.id, rs);
			}
			rs.add(r);
		}
		payms.remove(PaymentMethod.undefined.id);
		payms.put(PaymentMethod.undefined.id, PaymentMethod.undefined);
	}
	
	@Override
	protected void recordAdded(ExpenseRecord r){
		super.recordAdded(r);
		TreeSet<ExpenseRecord> rs = recordsByPaym.get(r.paymentMethod.id);
		if(rs == null){
			rs = new TreeSet<ExpenseRecord>();
			recordsByPaym.put(r.paymentMethod.id, rs);
		}
		rs.add(r);
	}
	
	@Override
	protected void recordRemoved(ExpenseRecord r){
		super.recordRemoved(r);
		recordsByPaym.get(r.paymentMethod.id).remove(r);
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
	public PaymentMethod addNewPaymentMethod(PaymentMethod paymentRef) {
		paymentRef = paymentRef.copy();
		while(payms.containsKey(paymentRef.id)){
			paymentRef.id = (new Date()).getTime() + ran.nextInt();
		}
		payms.put(paymentRef.id, paymentRef);
		markUpdate();
		return paymentRef;
	}

	@Override
	public boolean removePaymentMethod(long id) {
		if(!payms.containsKey(id)){
			return false;
		}else{
			payms.remove(id);
			if(recordsByPaym.get(id) != null){
				TreeSet<ExpenseRecord> rs = recordsByPaym.get(PaymentMethod.undefined.id);
				for(ExpenseRecord r : recordsByPaym.get(id)){
					r.paymentMethod = PaymentMethod.undefined;
					rs.add(r);
				}
				recordsByPaym.remove(id);
			}
			markUpdate();
			return true;
		}
	}

	@Override
	public PaymentMethod updatePaymentMethod(long id, PaymentMethod paymentRef) {
		if(!payms.containsKey(id)){
			return null;
		}else{
			PaymentMethod p = payms.get(id);
			p.name = paymentRef.name;
			markUpdate();
			return p;
		}
	}
	
}
