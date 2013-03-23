package ezxpns.GUI;

import javax.swing.JFrame;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;

import ezxpns.data.records.*;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class PaymentMethodFrame extends JFrame {
	private JTextField textField;
	private JButton btnChange, btnRemove;
	
	private PaymentMethod curMethod, toadd;
	private PaymentMethodModel mo;
	
	private PaymentMethod curPay;
	private PayMethodHandler payhan;
	
	private JList list;
	
	public PaymentMethodFrame(PayMethodHandler payhan) {
		this.payhan = payhan;
		
		this.setSize(500, 400);
		this.setLocationRelativeTo(null);
		
		toadd = new PaymentMethod("Add a new payment method...");
		
		mo = new PaymentMethodModel(payhan, toadd);
		
		list = new JList();
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				updateDisplay((PaymentMethod) list.getSelectedValue());
			}
		});
		getContentPane().add(list, BorderLayout.CENTER);
		list.setMinimumSize(new Dimension(150, 500));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(mo);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblName = new JLabel("Name:");
		panel.add(lblName, "2, 14, right, default");
		
		textField = new JTextField();
		panel.add(textField, "4, 14, fill, default");
		textField.setColumns(10);
		
		btnChange = new JButton("Change");
		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePaymentMethod();
			}
		});
		panel.add(btnChange, "2, 16");
		
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removePaymentMethod();
			}
		});
		panel.add(btnRemove, "4, 16");
	}
	
	private void updateDisplay(PaymentMethod pm){
		if(pm == null)return;
		curPay = pm;
		if(pm == toadd){
			textField.setText("Pick a name");
			btnChange.setText("Create");
			btnRemove.setEnabled(false);
		}else{
			textField.setText(pm.getName());
			btnChange.setText("Change");
			btnRemove.setEnabled(true);
		}
	}
	
	private void changePaymentMethod(){
		String newName = textField.getText();
		if(newName.equals(curPay.getName())){
			return;
		}
		String err = payhan.validatePaymentMethodName(newName);
		if(err == null){
			PaymentMethod m;
			if(curPay == toadd){
				m = payhan.addNewPaymentMethod(new PaymentMethod(textField.getText()));
			}else{
				m = payhan.updatePaymentMethod(curPay.getID(), new PaymentMethod(textField.getText()));
			}
			mo.update();
			list.setSelectedValue(m, true);
		}else{
			JOptionPane.showMessageDialog(this, err, "Error!!!!!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void removePaymentMethod(){
		String message = "Are you sure you want to remove this payment method?\n" +
	    		"All records under this payment method will have an undefined payment method!";
		if(JOptionPane.showConfirmDialog(this, message, "what?!",
				JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
			payhan.removePaymentMethod(curPay.getID());
			mo.update();
			list.setSelectedIndex(0);
		}
	}

}
