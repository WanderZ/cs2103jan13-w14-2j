package ezxpns.GUI;

import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ezxpns.data.records.Record;

@SuppressWarnings("serial")
public class MultiRecDisplay extends JPanel {

	private RecordHandlerInterface recHandler;
	private List<Record> records;
	
	public final int DEFAULT_PAGE_LIMIT = 12;
	
	/**
	 * Constructor for MultiRecordDisplay - for all records 
	 * @param recHandlerRef the handler to retrieve all the records
	 */
	public MultiRecDisplay(RecordHandlerInterface recHandlerRef) {
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

@SuppressWarnings("serial")
class RecordPanel extends JPanel {
	
	private final DecimalFormat MONEY_FORMAT = new DecimalFormat("$###,###,##0.00");
	public final int DEFAULT_FIELD_PAD = 5;
	public final Font DEFAULT_FONT = new Font("", 0, 11);
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
			String colAmtName = MONEY_FORMAT.format(record.getAmount()) + " on " + record.getName();
			lblAmtName = new JLabel(colAmtName);
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
			lblDate = new JLabel("yesterday");
			lblDate.setFont(DEFAULT_FONT);
		}
		return lblDate;
	}	
}