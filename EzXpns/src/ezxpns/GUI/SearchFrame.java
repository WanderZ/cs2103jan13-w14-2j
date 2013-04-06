package ezxpns.GUI;
import java.awt.*;
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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
	
	private SearchHandler handler;
	
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
		super(new BorderLayout());
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
		

		list = li;
		this.panResult = new JScrollPane(list);
		this.add(this.panResult, BorderLayout.CENTER);

		// InfoPanel
		panInfo = new InfoPanel();
		this.add(panInfo, BorderLayout.SOUTH);
	}
	
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
			if(start != null && end != null) {
				if(!start.before(end)) {
					// TODO: Error Handling for Date Range
					UINotify.createErrMsg(this, "Start is after End Date");
					return;
				}
				Pair<Date, Date> dateRange = new Pair<Date, Date>(start, end);
				if(req == null) {
					req = new SearchRequest(dateRange);
				} 
				else {
					req.setDateRange(dateRange);
				}
				if(req != null) search(req);
			}
			
			search(req);
		}
		else {
			search(panForm.getSimpleQuery());
		}
	}
	
	/** 
	 * Search the store data for the relevant records
	 * @param request SearchRequest object containing the query
	 */
	private void search(SearchRequest request) {
		if(request == null)return;
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
		if(!isMoreOption) {
			panCtrls.setPreferredSize(new Dimension(DEFAULT_WIDTH, ADVANCE_HEIGHT));
			panCtrls.revalidate();
			btnAdvance.setText("Less options");
			isMoreOption = true;
			panForm.switchToAdvance();
		}
		else{
			panCtrls.setPreferredSize(new Dimension(DEFAULT_WIDTH, SIMPLE_HEIGHT));
			panCtrls.revalidate();
			btnAdvance.setText("More options");
			isMoreOption = false;
			panForm.switchToSimple();
		}
	}
	
	/**
	 * Reloads this panel to refresh the content.
	 */
	public void reload() {
		if(!this.isVisible()){
			this.list.show(new Vector<Record>());
			panForm.clear();
		}else{
			this.list.show(new Vector<Record>());
			performSearch();
		}
		panForm.reload();
	}
}

@SuppressWarnings("serial")
class SearchFormPanel extends JPanel {
	
	// CategoryHandler Reference for Category JComboBox
	private CategoryHandler<IncomeRecord> inCatHandRef;
	private CategoryHandler<ExpenseRecord> exCatHandRef;
	
	private JLabel lblName, lblTitle, lblCat, lblDate, lblToDate;
	private JTextField txtName, txtSimpleField;
	private JComboBox txtCat;
	private JDateChooser txtStart, txtEnd;
	private final Font FORM_FONT = new Font("Segoe UI", 0, 14); // #Font
	private JPanel advancePane, normalPane;
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
	
	public void switchToAdvance(){
		txtSimpleField.setEditable(false);
		txtSimpleField.setText("");
		txtSimpleField.setBackground(this.getBackground());
		lblTitle.setForeground(Color.GRAY);
		advancePane.setVisible(true);
		this.getNameField().requestFocusInWindow();
		this.revalidate();
	}
	
	public void switchToSimple(){
		txtSimpleField.setEditable(true);
		lblTitle.setForeground(Color.BLACK);
		txtSimpleField.requestFocusInWindow();
		advancePane.setVisible(false);
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
		this.inCatHandRef = inCatHandRef;
		this.exCatHandRef = exCatHandRef;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel normalPane = new JPanel();
		normalPane.setLayout(new MigLayout("insets 5", "[left]10%[]", ""));
		
		lblTitle = new JLabel("Search");
		normalPane.add(lblTitle,"span 2");
		
		txtSimpleField = new JTextField("");
		txtSimpleField.setFont(FORM_FONT);
		txtSimpleField.setPreferredSize(new Dimension(230,32));
		normalPane.add(txtSimpleField, "span 1, wrap");
		this.add(normalPane);
		
		advancePane = new JPanel();
		advancePane.setLayout(new MigLayout("insets 5", "[left]10%[]", ""));
		advancePane.add(this.getNameLabel(), "span 2");
		advancePane.add(this.getNameField(), "span, wrap");
		
		advancePane.add(this.getCatLabel(), "span 2");
		advancePane.add(this.getCatField(), "span, wrap");
		
		advancePane.add(this.getDateLabel(), "span 2");
		advancePane.add(this.getDateField(), "split 3");
		advancePane.add(this.getToDateLabel());
		advancePane.add(this.getToDateField());
		
		this.add(advancePane);
		
		advancePane.setVisible(false);
	}
	
	public void clear(){
		this.getNameField().setText("");
		this.getCatField().setSelectedIndex(0);
		this.getDateField().setDate(null);
		this.getToDateField().setDate(null);
		txtSimpleField.setText("");
	}
	
	/**
	 * @return a JLabel Object to with Name 
	 */
	private JLabel getNameLabel() {
		if(lblName == null) {
			lblName = new JLabel("Name");
			lblName.setFont(FORM_FONT); // #Font
		}
		return lblName;
	}
	
	/**
	 * @return a JTextField Object for name
	 */
	public JTextField getNameField() {
		if(txtName == null) {
			txtName = new JTextField("");
			txtName.setFont(FORM_FONT); // #Font
			txtName.setPreferredSize(new Dimension(230, 32));
		}
		return txtName;
	}
	
	/**
	 * @return a JLabel Object for category
	 */
	private JLabel getCatLabel() {
		if(lblCat == null) {
			lblCat = new JLabel("Category");
			lblCat.setFont(FORM_FONT); // #Font
		}
		return lblCat;
	}
	
	/**
	 * @return a JComboBox for category
	 */
	public JComboBox getCatField() {
		if(txtCat == null) {
			txtCat = new JComboBox();
			txtCat.setFont(FORM_FONT); // #Font
			txtCat.setPreferredSize(new Dimension(230, 32));
			updateCats();
		}
		return txtCat;
	}
	
	private void updateCats(){
		txtCat.removeAllItems();
		Object[] myInCatList = inCatHandRef.getAllCategories().toArray();
		txtCat.insertItemAt("", 0); // insert empty item;
		for (int i = 0; i < inCatHandRef.getAllCategories().size(); i++)
			txtCat.addItem(myInCatList[i]);
		Object[] myExCatList = new Category[exCatHandRef.getAllCategories().size()];
		myExCatList = exCatHandRef.getAllCategories().toArray();
		for (int i = 0; i < exCatHandRef.getAllCategories().size(); i++)
			txtCat.addItem(myExCatList[i]);
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
	
	/**
	 * @return a String Object containing the query for Simple Search
	 */
	public String getSimpleQuery(){
		return this.txtSimpleField.getText();
	}
	
	/**
	 * @return a Date Object the starting date 
	 */
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
	
	/**
	 * @return a Date Object containing the end of the time frame
	 */
	public Date getEndDate() {
		return (Date) txtEnd.getDate();
	}
	
	/**
	 * Reloads the Form
	 */
	public void reload() {
		updateCats();
	}
}

/**
 * Pseudo Status Bar for Search
 */
@SuppressWarnings("serial")
class InfoPanel extends JPanel {
	private JLabel lblNumRec;
	private JLabel lblTotalAmt;
	private DecimalFormat df = new DecimalFormat("$###,###,##0.00");
	
	public InfoPanel(){
		setLayout(new MigLayout("","1[]15[]","0[]0"));
		setPreferredSize(new Dimension(600, 25));
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		this.add(getNumRecLabel());
		this.add(getTotalAmtLabel());
	}
	
	/**
	 * @return a JLabel object to label Sum of records
	 */
	private JLabel getNumRecLabel() {
		if (lblNumRec == null){
			lblNumRec = new JLabel("No. of Records: - ");
		}
		return lblNumRec;
	}

	/**
	 * @return a JLabel object to label Total Amount
	 */
	private JLabel getTotalAmtLabel() {
		if (lblTotalAmt == null){
			lblTotalAmt = new JLabel("Total Amount: - ");
		}
		return lblTotalAmt;
	}

	/**
	 * @param num Number to set as the sum of records
	 */
	public void setNumRec(int num){
		lblNumRec.setText("No. of Records: " +  num);
	}
	
	/**
	 * @param num Number to set as the total amount
	 */
	public void setTotalAmt(double num){
		lblTotalAmt.setText("Balance: " + df.format(num)); // 2 decimal place later
	}
}
