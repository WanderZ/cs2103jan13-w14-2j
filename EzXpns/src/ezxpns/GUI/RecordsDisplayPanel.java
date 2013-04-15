package ezxpns.GUI;

import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Panel Class containing the inner workings of the Records' bit on the main screen
 * @author A0097973
 * @author Yao Yujian
 */
@SuppressWarnings("serial")
public class RecordsDisplayPanel extends JPanel{
	
	/**
	 * The default maximum records to be displayed
	 */
	public static final int DEFAULT_MAX_ONSCREEN = 20;
		
	private RecordsListerPanel panContent;
	private RecordHandler recHandler;
	private RecordListView.RecordEditor editor;
	
	/**
	 * Constructor to create a RecordDisplayPanel
	 * @param recHandlerRef The RecordHandler Object reference <br />that will be used to deal with the logic of handling records
	 */
	public RecordsDisplayPanel(
			RecordHandler recHandlerRef, 
			RecordListView.RecordEditor editorRef,
			UpdateNotifyee notifyee) {
		super(new BorderLayout());
		
		recHandler = recHandlerRef; // Receive Handler
		editor = editorRef;
		
		this.setBackground(Color.WHITE);
		
		panContent = new RecordsListerPanel(recHandler, editor, notifyee);
		this.add(panContent, BorderLayout.CENTER);
		
		this.loadRecords();
	}	
	
	/**
	 * Load Records from data storage
	 */
	private void loadRecords() {
		panContent.updateContent();
	}
	
	/**
	 * Updates the records that are displayed
	 */
	public void update(){
		this.loadRecords();
	}
}

@SuppressWarnings("serial")
// Panel to display all expenses
class RecordsListerPanel extends JPanel {
	
	private JLabel lblTitle;
	
	private List<Record> records;
	private RecordHandler recHandler;
	private RecordListView list;
	
	public RecordsListerPanel(RecordHandler recHandlerRef, RecordListView.RecordEditor editorRef, UpdateNotifyee notifyee) {
		this.recHandler = recHandlerRef;
		this.setLayout(new BorderLayout());
		this.add(getTitleLabel("Recent Records"), BorderLayout.NORTH);
		
		list = new RecordListView(editorRef, recHandlerRef, notifyee);
		this.add(new JScrollPane(list), BorderLayout.CENTER);
		list.setPreferredScrollableViewportSize(new Dimension(150, 150));
		
		updateContent();
	}
	
	/**
	 * Updates the content of the list display 
	 */
	public void updateContent(){
		this.initRecords();
		list.show(records);
	}
	
	/** 
	 * Initializes records from the data store
	 */
	private void initRecords() {
		this.records = recHandler.getRecords(RecordsDisplayPanel.DEFAULT_MAX_ONSCREEN);
	}
	
	/**
	 * Gets a label for the title
	 * @param txt String object of what to be written in the title
	 * @return a JLabel object reference to the label for title
	 */
	private JLabel getTitleLabel(String txt) {
		if(lblTitle == null) {
			lblTitle = new JLabel(txt);
			lblTitle.setHorizontalAlignment(JLabel.CENTER);
			lblTitle.setFont(new Font("Segoe UI", 0, 20));
		}
		return lblTitle;
	}
	
}