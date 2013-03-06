package ezxpns.GUI;

import ezxpns.data.records.Record;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel Class containing the inner workings of the Records' bit on the main screen
 */
@SuppressWarnings("serial")
public class RecordsDisplayPanel extends JPanel implements ActionListener {
	
	List<Record> records;
	
	public RecordsDisplayPanel() {
		// CardLayout - for toggling between expenses and income...?
		// Mouse Adapter (hover click...)		
	}
	
	/**
	 * Generate the list of most recently added expenses
	 */
	public List<Object> generateRecords() {
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
	
}

/**
 * Panel Object containing each of the records, with the functionality
 * <br />Will display in the format &#60;Amt&#62; on &#60;Name&#62;&nbsp;&#60;Category&#62;&nbsp;&#60;Day&#62;&nbsp;&#60;Time&#62;
 */
@SuppressWarnings("serial")
class RecordDisplayPanel extends JPanel {
	
	private Record record;
	private JLabel lblAmtName, lblCat, lblDate, lblTime;
	private final DecimalFormat MONEY_FORMAT = new DecimalFormat("$###,###,##0.00");
	
	public RecordDisplayPanel(Record record) {
		super();
		
		this.record = record;
		this.add(getAmtName());
		this.add(getCat());
		this.add(getDate());
		
	}
	
	public void setRecord(Record record) {this.record = record;}
	public Record getRecord() {return this.record;}
	
	public JLabel getAmtName() {
		if(lblAmtName == null) {
			String colAmtName = MONEY_FORMAT.format(record.getAmount()) + " on " + record.getName();
			lblAmtName = new JLabel(colAmtName);
		}
		return lblAmtName;
	}
	
	public JLabel getCat() {
		if(lblCat == null) {
			lblCat = new JLabel(record.getCategory().getName());
		}
		return lblCat;
	}
	
	public JLabel getDate() {
		if(lblDate == null) {
			lblDate = new JLabel("");
		}
		return lblDate;
	}
	
	public JLabel getTime() {
		if(lblTime == null) {
			lblTime = new JLabel("");
		}
		return lblTime;
	}
	
	// init. frame
	
	
	
}