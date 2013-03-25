package ezxpns.GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
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
import ezxpns.data.records.Record;
import ezxpns.data.records.SearchHandler;
import ezxpns.data.records.SearchRequest;
import ezxpns.util.Pair;

/**
 * The window to handle the searching and querying needs of the user
 */
@SuppressWarnings("serial")
public class SearchFrame extends JFrame{
	
	public final int DEFAULT_WIDTH = 600;
	public final int DEFAULT_HEIGHT = 400;
	public final int SIMPLE_HEIGHT = 47;
	public final int ADVANCE_HEIGHT = 150;
	
	/** Whether Advance Options is open or not*/
	public final int OPEN = 1;
	public final int CLOSE = 0;
	private int Option_Status = 0;
	
	/** The handler object that implemented SearchHandler & CategoryHandler interface */
	private SearchHandler handler;
	private CategoryHandler inCatHandRef;
	private CategoryHandler exCatHandRef;

	
	// 2 main panels, the top (querying time frame) and the bottom (results, content)
	private SearchFormPanel panForm;
	private JPanel panBtns;
	private JScrollPane panResult;
	private RecordListView list;
	private JPanel panCtrls;
	private JButton btnAdvance;
	
	private boolean isMoreOption = false;
	
	/**
	 * To create a new Search Window
	 * @param handlerRef the reference to the SearchHandler object, catHandRef to CategoryHandler
	 */
	public SearchFrame(SearchHandler handlerRef, RecordListView li, CategoryHandler inCatHandRef, CategoryHandler exCatHandRef) {
		super();
		this.init();
		this.setLayout(new BorderLayout(25, 25));
		this.handler = handlerRef;
		
		panCtrls = new JPanel();
		panCtrls.setLayout(new BorderLayout());
		
		panForm = new SearchFormPanel(inCatHandRef, exCatHandRef);
		panCtrls.add(panForm, BorderLayout.CENTER);
		panCtrls.setPreferredSize(new Dimension(DEFAULT_WIDTH, SIMPLE_HEIGHT)); // SIMPLE SEARCH EXPERIMENTATION
		
		panBtns = new JPanel();
		
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
		list.setPreferredScrollableViewportSize(new Dimension(100, 200));
		this.panResult = new JScrollPane(list);
		this.add(this.panResult, BorderLayout.CENTER);
	}
	
	/** to initialize the components of this frame */
	private void init() {
		this.setTitle("EzXpns - Search");
		this.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT); /*x coordinate, y coordinate, width, height*/
		this.setLocationRelativeTo(null); // Set to start in the central area of the screen
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
	}

	public void performSearch(){
			// User invoked search
			
			String request;
			request = panForm.getNameField().getText().trim();
			if(!request.equals("")) {
				this.search(new SearchRequest(request.toString())); // Search by Name
				return;
			}
			
			// Name field is empty!
			Category cat = (Category) panForm.getCatField().getSelectedItem();

			if(cat != null){
				request = cat.toString();
				if(!request.equals("")){
					this.search(new SearchRequest(cat));
				}
				return;
			}	
			// No Category entered!
			
			try {
				Pair<Date, Date> dateRange = new Pair<Date, Date>(panForm.getStartDate(), panForm.getEndDate());
				this.search(new SearchRequest(dateRange)); // Search by Date range
			}
			catch(Exception err) {
				return;
			}
	}
	
	/** 
	 * Search the store data for the relevant records
	 * @param request SearchRequest object containing the query
	 */
	private void search(SearchRequest request) {
		List<Record> results = handler.search(request);
		list.show(results);
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
	private CategoryHandler inCatHandRef;
	private CategoryHandler exCatHandRef;
	
	private JLabel lblName, lblTitle, lblCat, lblDate, lblToDate;
	private JTextField txtName, txtSimpleField;
	private JComboBox txtCat;
	private JButton btnAdvance;
	//private JFormattedTextField txtStart, txtEnd;
	private JDateChooser txtStart, txtEnd;
	private final Font FORM_FONT = new Font("Segoe UI", 0, 14); // #Font
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
	
	public void switchToAdvance(){
		//lblTitle.setVisible(false);
		//txtSimpleField.setVisible(false);
		txtSimpleField.setEditable(false);
		txtSimpleField.setBackground(this.getBackground());
		lblTitle.setForeground(Color.GRAY);
		this.revalidate();
	}
	
	public void switchToSimple(){
		//lblTitle.setVisible(true);
		//txtSimpleField.setVisible(true);
		txtSimpleField.setEditable(true);
		lblTitle.setForeground(Color.BLACK);
		this.revalidate();
	}
	
	public SearchFormPanel(CategoryHandler inCatHandRef, CategoryHandler exCatHandRef) {
		this.inCatHandRef = inCatHandRef; // reference
		this.exCatHandRef = exCatHandRef; // reference
		
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
