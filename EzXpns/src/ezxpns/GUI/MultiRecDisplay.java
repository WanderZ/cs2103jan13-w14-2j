package ezxpns.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;

/** MultiRecordDisplay - A GUI Component to be able to display lists of records effectively */
@SuppressWarnings("serial")
public class MultiRecDisplay extends JPanel {

	private RecordHandler recHandler;
	private List<Record> records;
	
	public final int DEFAULT_PAGE_LIMIT = 12;
	
	/**
	 * Constructor for MultiRecordDisplay - for all records 
	 * @param recHandlerRef the handler to retrieve all the records
	 */
	public MultiRecDisplay(RecordHandler recHandlerRef) {
		
		this.recHandler = recHandlerRef;
		this.records = new Vector<Record>();
		this.retrieveRecords();
		initComponents();
		
	}
	
	/**
	 * Constructor MultiRecordDisplay - for the given set of records
	 * @param records the records to display
	 */
	public MultiRecDisplay(List<Record> records) {
		this.records = records;
		initComponents();
	}
	
	/**
	 * Initializes the components of this Panel
	 */
	private void initComponents() {
		// this.setLayout();
		
		// this.add(/*Header Row here */);
		
		JPanel panList = new JPanel(new GridLayout(0, 1, 0, 0));
		this.populatePanList(panList);
		JScrollPane jspRecords = new JScrollPane(panList);
	
		jspRecords.setBorder(BorderFactory.createEmptyBorder());
		
		this.add(jspRecords);
	}
	
	/**
	 * Populates the data panel with the retrieved records
	 * @param pane Panel to populate
	 */
	private void populatePanList(JPanel pane) {
		for(Record r: this.records) {
			pane.add(new RecordPanel(r));
		}
	}
	
	/**
	 * Retrieves the records from the handler
	 */
	private void retrieveRecords() {
		List<Record> records = recHandler.getRecords(DEFAULT_PAGE_LIMIT);
		for(Record r : records) {
			this.records.add(r);
		}
	}
}

/**
 * This is the UI component of one record.
 *
 */
@SuppressWarnings("serial")
class RecordPanel extends JPanel {
	
	private final DecimalFormat MONEY_FORMAT = new DecimalFormat("$###,###,##0.00");
	public final int DEFAULT_FIELD_PAD = 5;
	public final Font DEFAULT_FONT = new Font("", 0, 14);
	private Record record;
	
	private JLabel lblAmtName, lblCategory, lblDate;
	
	/**
	 * To create a new panel to contain all the fields of a record
	 * @param record a Record Object to be displayed
	 */
	public RecordPanel(Record record) {
		this.setLayout(new GridLayout(1, 0, DEFAULT_FIELD_PAD, DEFAULT_FIELD_PAD));
		
		this.record = record;
		
		this.add(this.getAmtName());
		this.add(this.getCategory());
		this.add(this.getDate());
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent mEvent) { // Hover start
				hover();
			}
			
			@Override
			public void mousePressed(MouseEvent mEvent) {
				if(mEvent.isPopupTrigger()) {
					// Show pop up menu
				}
			}
			
			public void mouseReleased(MouseEvent mEvent) {
				if(mEvent.isPopupTrigger()) {
					// Show pop up menu
				}
			}
			
			@Override
			public void mouseExited(MouseEvent mEvent) { // Hover end
				offHover();
			}
		});
	}
	
	/** 
	 * To manage the mouse hover event effects for this UI component
	 */
	private void hover() {
		// Highlight
	}
	
	/**
	 * To return the component to its original state when not hovered
	 */
	private void offHover() {
		// 
	}
	
	/**
	 * Retrieve the displayed record
	 * @return this Record object display
	 */
	public Record getRecord() {
		return this.record;
	}
	
	/**
	 * Retrieve the JLabel object displaying the amount and the object
	 * <br />this label will display in the format $#60;Amount&#62; on $#60;Name$#62;
	 * @return the JLabel reference to the displayed text
	 */
	public JLabel getAmtName() {
		if(lblAmtName == null) {
			StringBuilder amtName = new StringBuilder(MONEY_FORMAT.format(record.getAmount()));
			amtName.append(record instanceof IncomeRecord ? " from " : " on ");
			amtName.append(record.getName());
			lblAmtName = new JLabel(amtName.toString());
			lblAmtName.setFont(DEFAULT_FONT);
		}
		return lblAmtName;
	}
	
	/**
	 * Retrieve the JLabel object displaying the category
	 * @return the JLabel reference to the displayed category
	 */
	public JLabel getCategory() {
		if(lblCategory == null) {
			lblCategory = new JLabel(record.getCategory().getName());
			lblCategory.setFont(DEFAULT_FONT);
		}
		return lblCategory;
	}
	
	/**
	 * Retrieve the JLabel object display the relative date
	 * @return the JLabel reference to the displayed date
	 */
	public JLabel getDate() {
		if(lblDate == null) {
			lblDate = new JLabel(this.getRelativeDate());
			lblDate.setFont(DEFAULT_FONT);
		}
		return lblDate;
	}	
	
	/** 
	 * To compare the relativity of the record's date to today
	 * @return a string object containing the relativity
	 */
	private String getRelativeDate() {
		System.out.println(record.getDate().compareTo(new Date()));
		return "yest.";
	}
}

