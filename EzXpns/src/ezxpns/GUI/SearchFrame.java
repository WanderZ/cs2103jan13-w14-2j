package ezxpns.GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import ezxpns.data.records.Category;
import ezxpns.data.records.Record;
import ezxpns.data.records.SearchHandler;
import ezxpns.data.records.SearchRequest;
import ezxpns.util.Pair;

/**
 * The window to handle the searching and querying needs of the user
 */
@SuppressWarnings("serial")
public class SearchFrame extends JFrame implements ActionListener {
	
	public final int DEFAULT_WIDTH = 600;
	public final int DEFAULT_HEIGHT = 400;
	
	/** The handler object that implemented SearchHandler interface */
	private SearchHandler handler;
	
	// 2 main panels, the top (querying time frame) and the bottom (results, content)
	private SearchFormPanel panForm;
	private SearchBtnPanel panBtns;
	private JScrollPane panResult;
	private RecordListView list;
	
	/**
	 * To create a new Search Window
	 * @param handlerRef the reference to the SearchHandler object
	 */
	public SearchFrame(SearchHandler handlerRef, RecordListView li) {
		super();
		this.init();
		this.setLayout(new BorderLayout(25, 25));
		this.handler = handlerRef;
		
		JPanel panCtrls = new JPanel();
		panCtrls.setLayout(new BorderLayout());
		
		panForm = new SearchFormPanel();
		panCtrls.add(panForm, BorderLayout.CENTER);
		
		panBtns = new SearchBtnPanel(this);
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

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == panBtns.getSearchBtn()) {
			// User invoked search
			
			StringBuilder request = new StringBuilder();
			request.append(panForm.getNameField().getText().trim());
			if(!request.equals("")) {
				this.search(new SearchRequest(request.toString())); // Search by Name
				return;
			}
			
			// Name field is empty!
			request.append(panForm.getCatField().getText().trim());
			if(!request.equals("")) {
				this.search(new SearchRequest(new Category(request.toString()))); // Search by Category
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
	}
	
	/** 
	 * Search the store data for the relevant records
	 * @param request SearchRequest object containing the query
	 */
	private void search(SearchRequest request) {
		List<Record> results = handler.search(request);
		list.show(results);
	}
}

@SuppressWarnings("serial")
class SearchFormPanel extends JPanel {
	
	private JLabel lblName, lblTitle, lblCat, lblDate, lblToDate;
	private JTextField txtName, txtCat;
	private JFormattedTextField txtStart, txtEnd;
	private final Font FORM_FONT = new Font("Segoe UI", 0, 14); // #Font
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
	
	public SearchFormPanel() {
		this.setLayout(new MigLayout("insets 15", "[left]10%[]", ""));
		this.add(this.getTitleLabel(), "span, wrap");
		
		this.add(this.getNameLabel(), "span 2");
		this.add(this.getNameField(), "span, wrap");
		
		this.add(this.getCatLabel(), "span 2");
		this.add(this.getCatField(), "span, wrap");
		
		this.add(this.getDateLabel(), "span 2");
		this.add(this.getDateField(), "split 3");
		this.add(this.getToDateLabel());
		this.add(this.getToDateField());
	}
	
	private JLabel getTitleLabel() {
		if(lblTitle == null) {
			lblTitle = new JLabel("Search");
			lblTitle.setFont(new Font("Segoe UI", 0, 24)); // #Font
		}
		return lblTitle;
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
	
	public JTextField getCatField() {
		if(txtCat == null) {
			txtCat = new JTextField("");
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
	
	private JFormattedTextField getDateField() {
		if(txtStart == null) {
			txtStart = new JFormattedTextField(DATE_FORMAT);
			txtStart.setFont(FORM_FONT); // #Font
			txtStart.setValue(new Date());
			txtStart.setPreferredSize(new Dimension(100, 32));
			txtStart.setHorizontalAlignment(JTextField.CENTER);
		}
		return txtStart;
	}
	
	public Date getStartDate() {
		return (Date)txtStart.getValue();
	}
	
	private JFormattedTextField getToDateField() {
		if(txtEnd == null) {
			txtEnd = new JFormattedTextField(DATE_FORMAT);
			txtEnd.setFont(FORM_FONT); // #Font
			txtEnd.setValue(new Date());
			txtEnd.setPreferredSize(new Dimension(100, 32));
			txtEnd.setHorizontalAlignment(JTextField.CENTER);
		}
		return txtEnd;
	}
	
	public Date getEndDate() {
		return (Date) txtEnd.getValue();
	}
}

/**
 * Panel to containing the buttons for functionality
 */
@SuppressWarnings("serial")
class SearchBtnPanel extends JPanel {
	
	private JButton btnSearch;
	private final Font BTN_FONT = new Font("Segoe UI", 0, 24);
	
	/**
	 * Constructor to get a panel for the buttons
	 * @param listener
	 */
	public SearchBtnPanel(ActionListener listener) {
		super();
		BoxLayout loBtn = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(loBtn);
		
		// Layout to make it fill the width completely, and glue to the base

		this.add(this.getSearchBtn());
		this.add(Box.createVerticalGlue());
		
		this.btnSearch.addActionListener(listener);
	}
	
	public JButton getSearchBtn() {
		if(btnSearch == null) {
			btnSearch = new JButton("Find");
			btnSearch.setFont(BTN_FONT);
		}
		return btnSearch;
	}
}
