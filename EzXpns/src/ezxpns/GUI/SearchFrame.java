package ezxpns.GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import ezxpns.data.records.Category;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.PaymentHandler;
import ezxpns.data.records.PaymentMethod;
import ezxpns.data.records.Record;
import ezxpns.data.records.SearchHandler;
import ezxpns.data.records.SearchRequest;
import ezxpns.util.Pair;

/**
 * The window to handle the searching and querying needs of the user
 */
@SuppressWarnings("serial")
public class SearchFrame extends JPanel {
	
	public final int DEFAULT_WIDTH = 600;
	public final int DEFAULT_HEIGHT = 400;
	public final int SIMPLE_HEIGHT = 47;
	public final int ADVANCE_HEIGHT = 170;
	
	/** Whether Advance Options is open or not*/
	public final int OPEN = 1;
	public final int CLOSE = 0;
	private int Option_Status = 0;
	
	/** The handler object that implemented SearchHandler & CategoryHandler interface */
	private SearchHandler handler;
	private CategoryHandler<IncomeRecord> inCatHandRef;
	private CategoryHandler<ExpenseRecord> exCatHandRef;

	
	// 2 main panels, the top (querying time frame) and the bottom (results, content)
	private SearchFormPanel panForm;
	private JPanel panBtns;
	private JScrollPane panResult;
	private RecordListView list;
	private JPanel panCtrls;
	private InfoPanel panInfo;
	private JButton btnAdvance;
	
	private boolean isMoreOption = false;
	
	/**
	 * Constructs a new Search Window
	 * @param handlerRef the reference to the SearchHandler object, catHandRef to CategoryHandler
	 * @param li
	 * @param inCatHandRef
	 * @param exCatHandRef
	 * @param payHandRef
	 */
	public SearchFrame(
			SearchHandler handlerRef, 
			RecordListView li, 
			CategoryHandler<IncomeRecord> inCatHandRef, 
			CategoryHandler<ExpenseRecord> exCatHandRef, 
			PaymentHandler payHandRef) {
		// super();
/*		this.init();*/
		this.handler = handlerRef;
		
		panCtrls = new JPanel();
		panCtrls.setLayout(new BorderLayout());
		
		panForm = new SearchFormPanel(inCatHandRef, exCatHandRef, payHandRef);
		panCtrls.add(panForm, BorderLayout.CENTER);
		panCtrls.setPreferredSize(new Dimension(DEFAULT_WIDTH, SIMPLE_HEIGHT)); // SIMPLE SEARCH EXPERIMENTATION
		
		panBtns = new JPanel();
		
		panForm.addListenerToTextFields(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				performSearch();
			}
		});
		
		btnAdvance = new JButton("More Options");
		btnAdvance.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				switchMode();
			}
			
		});
		panBtns.setLayout(new BoxLayout(panBtns, BoxLayout.X_AXIS));
		
		JButton btnSearch = new JButton("Find");
		panBtns.add(btnSearch);
		panBtns.add(Box.createVerticalGlue());
		
		panBtns.add(btnAdvance);
		
		
		btnSearch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				performSearch();
			}
			
		});
		
		panCtrls.add(panBtns, BorderLayout.EAST);
		
		this.add(panCtrls, BorderLayout.NORTH);
		

		// list
		list = li;
		// list.setPreferredScrollableViewportSize(new Dimension(100, 200));
		this.panResult = new JScrollPane(list);
		this.add(this.panResult, BorderLayout.CENTER);

		// InfoPanel
		panInfo = new InfoPanel();
		this.add(panInfo, BorderLayout.SOUTH);
	}
	
//	/** to initialize the components of this frame */
//	private void init() {
//		this.setTitle("EzXpns - Search");
//		this.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT); /*x coordinate, y coordinate, width, height*/
//		this.setLocationRelativeTo(null); // Set to start in the central area of the screen
//		this.setResizable(false);
//		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
//	}

	public void performSearch(){
		if(isMoreOption){
		
			SearchRequest req = null;
			String name = panForm.getNameField().getText().trim();
			if(!name.equals("")) {
				req = new SearchRequest(name);
			}
			Category cat = null;
			try{
				cat = (Category) panForm.getCatField().getSelectedItem();
			}catch(Exception e){
				
			}

			if(cat != null){
				name = cat.toString();
				if(req == null){
					req = new SearchRequest(cat);
				}else{
					req.setCategory(cat);
				}
			}	
			
			
			Date start = panForm.getStartDate(),
				 end = panForm.getEndDate();
			if(start != null && end != null){
				Pair<Date, Date> dateRange = new Pair<Date, Date>(start, end);
				if(req == null){
					req = new SearchRequest(dateRange);
				}else{
					req.setDateRange(dateRange);
				}
				if(req != null) search(req);
			}
			
			search(req);
		}else{
			search(panForm.getSimpleQuery());
		}
	}
	
	/** 
	 * Search the store data for the relevant records
	 * @param request SearchRequest object containing the query
	 */
	private void search(SearchRequest request) {
		List<Record> results = handler.search(request);
		list.show(results);
		panInfo.setNumRec(results.size());
		panInfo.setTotalAmt(Record.sumBalance(results));
	}
	
	private void search(String prefix){
		List<Record> results = handler.search(prefix);
		list.show(results);
		panInfo.setNumRec(results.size());
		panInfo.setTotalAmt(Record.sumBalance(results));
	}
	
	private void switchMode(){
		if(!isMoreOption){
			panCtrls.setPreferredSize(new Dimension(DEFAULT_WIDTH, ADVANCE_HEIGHT));
			panCtrls.revalidate();
			btnAdvance.setText("Less options");
			isMoreOption = true;
			panForm.switchToAdvance();
		}else{
			panCtrls.setPreferredSize(new Dimension(DEFAULT_WIDTH, SIMPLE_HEIGHT));
			panCtrls.revalidate();
			btnAdvance.setText("More options");
			isMoreOption = false;
			panForm.switchToSimple();
		}
		
	}
}

@SuppressWarnings("serial")
class SearchFormPanel extends JPanel {
	
	// CategoryHandler Reference for Category JComboBox
	private CategoryHandler<IncomeRecord> inCatHandRef;
	private CategoryHandler<ExpenseRecord> exCatHandRef;
	private PaymentHandler payHandRef;
	
	private JLabel lblName, lblTitle, lblCat, lblPay, lblDate, lblToDate;
	private JTextField txtName, txtSimpleField;
	private JComboBox txtCat, txtPay;
	private JButton btnAdvance;
	private JDateChooser txtStart, txtEnd;
	private final Font FORM_FONT = new Font("Segoe UI", 0, 14); // #Font
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
	
	public void switchToAdvance(){
		txtSimpleField.setEditable(false);
		txtSimpleField.setText("");
		txtSimpleField.setBackground(this.getBackground());
		lblTitle.setForeground(Color.GRAY);
		this.getNameField().requestFocusInWindow();
		this.revalidate();
	}
	
	public void switchToSimple(){
		txtSimpleField.setEditable(true);
		lblTitle.setForeground(Color.BLACK);
		txtSimpleField.requestFocusInWindow();
		this.revalidate();
	}
	
	public void addListenerToTextFields(ActionListener listener){
		txtSimpleField.addActionListener(listener);
		getNameField().addActionListener(listener);
	}
	
	public SearchFormPanel(
			CategoryHandler<IncomeRecord> inCatHandRef, 
			CategoryHandler<ExpenseRecord> exCatHandRef, 
			PaymentHandler payHandRef) {
		this.inCatHandRef = inCatHandRef; // reference
		this.exCatHandRef = exCatHandRef; // reference
		this.payHandRef = payHandRef; 	  // reference
		
		this.setLayout(new MigLayout("insets 15", "[left]10%[]", ""));
		lblTitle = new JLabel("Search");
		this.add(lblTitle,"span 2");
		
		txtSimpleField = new JTextField("");
		txtSimpleField.setFont(FORM_FONT);
		txtSimpleField.setPreferredSize(new Dimension(230,32));
		this.add(txtSimpleField, "span 1, wrap");
		
		
		this.add(this.getNameLabel(), "span 2");
		this.add(this.getNameField(), "span, wrap");
		
		this.add(this.getCatLabel(), "span 2");
		this.add(this.getCatField(), "span, wrap");
		
		this.add(this.getPayLabel(), "span 2");
		this.add(this.getPayField(), "span, wrap");
		
		this.add(this.getDateLabel(), "span 2");
		this.add(this.getDateField(), "split 3");
		this.add(this.getToDateLabel());
		this.add(this.getToDateField());
	}
	
	private JLabel getNameLabel() {
		if(lblName == null) {
			lblName = new JLabel("Name");
			lblName.setFont(FORM_FONT); // #Font
		}
		return lblName;
	}
	
	public JTextField getNameField() {
		if(txtName == null) {
			txtName = new JTextField("");
			txtName.setFont(FORM_FONT); // #Font
			txtName.setPreferredSize(new Dimension(230, 32));
		}
		return txtName;
	}
	
	private JLabel getCatLabel() {
		if(lblCat == null) {
			lblCat = new JLabel("Category");
			lblCat.setFont(FORM_FONT); // #Font
		}
		return lblCat;
	}
	
	public JComboBox getCatField() {
		if(txtCat == null) {
			Object[] myInCatList = new Category[inCatHandRef.getAllCategories().size()];
			myInCatList = inCatHandRef.getAllCategories().toArray();
			txtCat = new JComboBox();
			txtCat.insertItemAt("", 0); // insert empty item;
			for (int i = 0; i < inCatHandRef.getAllCategories().size(); i++)
				txtCat.addItem(myInCatList[i]);
			Object[] myExCatList = new Category[exCatHandRef.getAllCategories().size()];
			myExCatList = exCatHandRef.getAllCategories().toArray();
			for (int i = 0; i < exCatHandRef.getAllCategories().size(); i++)
				txtCat.addItem(myExCatList[i]);
			txtCat.setFont(FORM_FONT); // #Font
			txtCat.setPreferredSize(new Dimension(230, 32));
		}
		return txtCat;
	}
	
	private JLabel getPayLabel(){
		if (lblPay == null){
			lblPay = new JLabel("Payment");
			lblPay.setFont(FORM_FONT);
		}
		return lblPay;
	}
	
	public JComboBox getPayField() {
		if(txtPay == null) {
			Object[] myPayList = new PaymentMethod[payHandRef.getAllPaymentMethod().size()];
			myPayList = payHandRef.getAllPaymentMethod().toArray();
			txtPay = new JComboBox();
			txtPay.insertItemAt("", 0); // insert empty item;
			for (int i = 0; i < payHandRef.getAllPaymentMethod().size(); i++)
				txtPay.addItem(myPayList[i]);
			txtPay.setFont(FORM_FONT); // #Font
			txtPay.setPreferredSize(new Dimension(230, 32));
		}
		return txtPay;
	}
	
	private JLabel getDateLabel() {
		if(lblDate == null) {
			lblDate = new JLabel("Date");
			lblDate.setFont(FORM_FONT); // #Font
		}
		return lblDate;
	}
	
	private JLabel getToDateLabel() {
		if(lblToDate == null) {
			lblToDate = new JLabel(" To ");
			lblToDate.setFont(FORM_FONT); // #Font
			
		}
		return lblToDate;
	}
	
	private JDateChooser getDateField() {
		if(txtStart == null) {
			//txtStart = new JFormattedTextField(DATE_FORMAT);
			//txtStart.setFont(FORM_FONT); // #Font
			//txtStart.setValue(new Date());
			//txtStart.setPreferredSize(new Dimension(100, 32));
			//txtStart.setHorizontalAlignment(JTextField.CENTER);
			txtStart = new JDateChooser();
			txtStart.getJCalendar().setTodayButtonVisible(true);
			txtStart.setDateFormatString("dd/MM/yyyy");
			txtStart.setMaxSelectableDate(new Date());
			PropertyChangeListener calendarChangeListener  = new PropertyChangeListener() {
		        @Override
		        public void propertyChange(PropertyChangeEvent evt) {
		            Date selectedDate = ((JCalendar)evt.getSource()).getDate();
		        }
		    };
		    txtStart.getJCalendar().addPropertyChangeListener("calendar",calendarChangeListener);
		}
		return txtStart;
	}
	
	public String getSimpleQuery(){
		return this.txtSimpleField.getText();
	}
	
	public Date getStartDate() {
		return (Date)txtStart.getDate();
	}
	
	private JDateChooser getToDateField() {
		if(txtEnd == null) {
			//txtEnd = new JFormattedTextField(DATE_FORMAT);
			//txtEnd.setFont(FORM_FONT); // #Font
			//txtEnd.setValue(new Date());
			//txtEnd.setPreferredSize(new Dimension(100, 32));
			//txtEnd.setHorizontalAlignment(JTextField.CENTER);
			txtEnd = new JDateChooser();
			txtEnd.getJCalendar().setTodayButtonVisible(true);
			txtEnd.setDateFormatString("dd/MM/yyyy");
			txtEnd.setMaxSelectableDate(new Date());
			PropertyChangeListener calendarChangeListener  = new PropertyChangeListener() {
		        @Override
		        public void propertyChange(PropertyChangeEvent evt) {
		            Date selectedDate = ((JCalendar)evt.getSource()).getDate();
		        }
		    };
		    txtEnd.getJCalendar().addPropertyChangeListener("calendar",calendarChangeListener);
			
		}
		return txtEnd;
	}
	
	public Date getEndDate() {
		return (Date) txtEnd.getDate();
	}
}

@SuppressWarnings("serial")
class InfoPanel extends JPanel{
	private JLabel lblNumRec;
	private JLabel lblTotalAmt;
	private DecimalFormat df = new DecimalFormat("#.##");
	
	public InfoPanel(){
		setLayout(new MigLayout("","1[]15[]","0[]0"));
		setPreferredSize(new Dimension(600, 25));
		this.add(getNumRecLabel());
		this.add(getTotalAmtLabel());
	}
	
	private JLabel getNumRecLabel() {
		if (lblNumRec == null){
			lblNumRec = new JLabel("No. of Records: - ");
		}
		return lblNumRec;
	}

	private JLabel getTotalAmtLabel() {
		if (lblTotalAmt == null){
			lblTotalAmt = new JLabel("Total Amount: - ");
		}
		return lblTotalAmt;
	}

	public void setNumRec(int num){
		lblNumRec.setText("No. of Records: " +  num);
	}
	
	public void setTotalAmt(double num){
		lblTotalAmt.setText("Balance: " + df.format(num)); // 2 decimal place later
	}
}
