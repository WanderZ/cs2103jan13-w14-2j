package ezxpns.GUI;

import ezxpns.data.records.*;

import javax.swing.AbstractListModel;
import ezxpns.data.records.*;

import java.util.*;

/**
 * @author yyjhao
 * A model used to display list of category in category frame
 */
@SuppressWarnings("serial")
public class PaymentMethodModel extends AbstractListModel {
	/**
	 * @param cat the data source of category
	 * @param toadd a list item that upon selected, let the user add new category
	 */
	public PaymentMethodModel(PayMethodHandler pay, PaymentMethod toadd){
		this.pay = pay;
		pays = pay.getAllPaymentMethod();
		pays.add(toadd);
		this.toadd = toadd;
	}
	
	private PayMethodHandler pay;
	private List<PaymentMethod> pays;
	private PaymentMethod toadd;

	@Override
	public Object getElementAt(int arg0) {
		return pays.get(arg0);
	}

	@Override
	public int getSize() {
		return pays.size();
	}

	/**
	 * Refresh the whole list <br />
	 * If the size of categories gets large, this can be slow <br />
	 * But given that we are not even using a data base, who cares about this small one
	 */
	public void update(){
		int s = pays.size();
		pays.clear();
		fireIntervalRemoved(this, 0, s);
		pays = pay.getAllPaymentMethod();
		pays.add(toadd);
		fireIntervalAdded(this, 0, pays.size());
	}
}
