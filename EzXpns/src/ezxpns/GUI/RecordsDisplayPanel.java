package ezxpns.GUI;

import ezxpns.data.records.Record;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RecordsDisplayPanel extends JPanel implements ActionListener {
	
	List<Record> records;
	
	public RecordsDisplayPanel() {
		
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
		// Mouse Event...
	}
	
}